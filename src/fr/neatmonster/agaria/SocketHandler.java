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
import fr.neatmonster.agaria.packets.client.PacketResetConnection1;
import fr.neatmonster.agaria.packets.client.PacketResetConnection2;
import fr.neatmonster.agaria.packets.client.PacketSendAuthToken;
import fr.neatmonster.agaria.packets.client.PacketSendToken;

@WebSocket
public class SocketHandler {
    private Session              session;
    private final CountDownLatch latch;

    public String token;
    public String authToken;

    public SocketHandler() {
        latch = new CountDownLatch(1);
    }

    public boolean awaitClose(final int duration, final TimeUnit unit)
            throws InterruptedException {
        return latch.await(duration, unit);
    }

    @OnWebSocketConnect
    public void onWebSocketConnect(final Session session) {
        this.session = session;

        sendPacket(new PacketResetConnection1());
        sendPacket(new PacketResetConnection2());

        if (token != null) {
            final PacketSendToken packet = new PacketSendToken();
            packet.token = token;
            sendPacket(packet);
        }

        if (authToken != null) {
            final PacketSendAuthToken packet = new PacketSendAuthToken();
            packet.token = authToken;
            sendPacket(packet);
        }

        // TODO Notify the client.
    }

    @OnWebSocketMessage
    public void onWebSocketMessage(final Session session, final byte[] bufArray,
            final int offset, final int length) {
        if (length == 0)
            return;

        final ByteBuffer buf = ByteBuffer.wrap(bufArray, offset, length);
        buf.order(ByteOrder.LITTLE_ENDIAN);

        final byte packetId = buf.get();
        final ServerPacket packet = PacketFactory.createPacket(packetId);

        // TODO Notify the client.
    }

    @OnWebSocketClose
    public void onWebSocketClose(final Session session, final int closeCode,
            final String closeReason) {
        latch.countDown();

        // TODO Notify the client.
    }

    @OnWebSocketError
    public void onWebSocketError(final Session session, final Throwable cause) {
        // TODO Notify the client.
    }

    public void sendPacket(final ClientPacket packet) {
        final ByteBuffer buf = ByteBuffer.allocate(1 + packet.getLength());
        buf.put(packet.getPacketId());
        packet.write(buf);
        try {
            session.getRemote().sendBytes(buf);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
