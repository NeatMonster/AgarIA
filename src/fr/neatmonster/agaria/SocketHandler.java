package fr.neatmonster.agaria;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import fr.neatmonster.agaria.packets.ClientPacket;
import fr.neatmonster.agaria.packets.PacketFactory;
import fr.neatmonster.agaria.packets.ServerPacket;

@WebSocket
public class SocketHandler {
    public static String toString(final ByteBuffer buf) {
        String out = "";
        for (int i = 0; i < buf.limit(); ++i) {
            if (!out.isEmpty())
                out += " ";
            final String chr = Integer.toHexString(buf.get(i));
            if (chr.length() == 1)
                out += "0";
            out += chr;
        }
        return out;
    }

    private final GameManager manager;

    private final CountDownLatch latch;
    private Session              session;

    public SocketHandler(final GameManager manager) {
        this.manager = manager;
        latch = new CountDownLatch(1);
    }

    public boolean awaitClose(final int duration, final TimeUnit unit) throws InterruptedException {
        return latch.await(duration, unit);
    }

    @OnWebSocketConnect
    public void onWebSocketConnect(final Session session) {
        this.session = session;

        manager.socketOpened();
    }

    @OnWebSocketMessage
    public void onWebSocketMessage(final Session session, final byte[] bufArray, final int offset, final int length) {
        if (length == 0) {
            GameManager.logger.warning("empty packet received");
            return;
        }

        final ByteBuffer buf = ByteBuffer.wrap(bufArray, offset, length);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        final byte packetId = buf.get();

        GameManager.logger.finer("RECV packet ID=" + packetId + " LEN=" + length);
        GameManager.logger.finest("dump: " + toString(buf));

        final ServerPacket packet = PacketFactory.createPacket(packetId);
        if (packet == null) {
            GameManager.logger.warning("unknown packet ID(" + packetId + ")");
            return;
        }
        packet.read(buf);

        if (!manager.handlePacket(packet))
            GameManager.logger.warning("unhandled packet " + packet.getClass().getSimpleName());
    }

    @OnWebSocketClose
    public void onWebSocketClose(final Session session, final int closeCode, final String closeReason) {
        latch.countDown();

        manager.socketClosed();
    }

    @OnWebSocketError
    public void onWebSocketError(final Session session, final Throwable cause) {
        manager.socketErrored(cause);
    }

    public void sendPacket(final ClientPacket packet) {
        final ByteBuffer buf = ByteBuffer.allocate(1 + packet.getLength());
        buf.put(packet.getPacketId());
        packet.write(buf);

        GameManager.logger.finer("SEND packet ID=" + packet.getPacketId() + " LEN=" + buf.limit());
        GameManager.logger.finest("dump: " + toString(buf));

        try {
            session.getRemote().sendBytes(buf);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        session.close();
    }

    public boolean isClosed() {
        return !session.isOpen();
    }
}
