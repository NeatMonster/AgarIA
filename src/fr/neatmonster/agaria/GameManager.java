package fr.neatmonster.agaria;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import fr.neatmonster.agaria.events.EventManager;
import fr.neatmonster.agaria.events.Listener;
import fr.neatmonster.agaria.events.socket.SocketCloseEvent;
import fr.neatmonster.agaria.events.socket.SocketErrorEvent;
import fr.neatmonster.agaria.events.socket.SocketOpenEvent;
import fr.neatmonster.agaria.packets.ServerPacket;
import fr.neatmonster.agaria.packets.client.PacketResetConnection1;
import fr.neatmonster.agaria.packets.client.PacketResetConnection2;
import fr.neatmonster.agaria.packets.client.PacketSendAuthToken;
import fr.neatmonster.agaria.packets.client.PacketSendToken;

public class GameManager {
    public static Logger logger = Logger.getLogger("AgarIA");

    private final EventManager events;
    private SocketHandler      socket;

    private final String name;
    private String       token;
    private final String authToken;

    public GameManager(final String name) {
        this(name, null);
    }

    public GameManager(final String name, final String authToken) {
        this.name = name;
        this.authToken = authToken;

        events = new EventManager();
    }

    public void connect(final String server, final String token) {
        this.token = token;

        logger.info("connecting...");

        try {
            socket = new SocketHandler(this);
            final WebSocketClient client = new WebSocketClient();
            client.start();
            final ClientUpgradeRequest request = new ClientUpgradeRequest();
            request.setHeader("Origin", "http://agar.io");
            client.connect(socket, new URI(server), request);
            socket.awaitClose(7, TimeUnit.DAYS);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public boolean disconnect() {
        logger.info("disconntect() called");

        if (socket == null) {
            logger.warning("disconnect() called before connect(), ignoring this call");
            return false;
        }

        socket.close();
        return true;
    }

    public void eject() {
        // TODO Auto-generated method stub
    }

    public void reset() {
        // TODO Auto-generated method stub
    }

    public void spawn() {
        // TODO Auto-generated method stub
    }

    public void spectate() {
        // TODO Auto-generated method stub
    }

    public void split() {
        // TODO Auto-generated method stub
    }

    public void target(final int x, final int y) {
        // TODO Auto-generated method stub
    }

    public void handlePacket(final ServerPacket packet_) {
        // TODO Auto-generated method stub
    }

    public void onSocketOpen() {
        logger.info("connected to server");

        socket.sendPacket(new PacketResetConnection1());
        socket.sendPacket(new PacketResetConnection2());

        if (token != null) {
            final PacketSendToken packet = new PacketSendToken();
            packet.token = token;
            socket.sendPacket(packet);
        }

        if (authToken != null) {
            final PacketSendAuthToken packet = new PacketSendAuthToken();
            packet.token = authToken;
            socket.sendPacket(packet);
        }

        events.callEvent(new SocketOpenEvent());
    }

    public void onSocketClose() {
        logger.info("disconnected");

        events.callEvent(new SocketCloseEvent());

        reset();
    }

    public void onSocketError(final Throwable cause) {
        logger.info("connection error: " + cause.getMessage());

        events.callEvent(new SocketErrorEvent(cause));

        reset();
    }

    public void registerEvents(final Listener listener) {
        events.registerEvents(listener);
    }
}
