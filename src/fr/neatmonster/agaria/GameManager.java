package fr.neatmonster.agaria;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import fr.neatmonster.agaria.events.EventManager;
import fr.neatmonster.agaria.events.Listener;
import fr.neatmonster.agaria.events.cells.CellActionEvent;
import fr.neatmonster.agaria.events.cells.CellAppearEvent;
import fr.neatmonster.agaria.events.cells.CellDestroyEvent;
import fr.neatmonster.agaria.events.cells.CellDestroyEvent.Reason;
import fr.neatmonster.agaria.events.cells.CellDisappearEvent;
import fr.neatmonster.agaria.events.cells.CellEatEvent;
import fr.neatmonster.agaria.events.cells.CellMoveEvent;
import fr.neatmonster.agaria.events.cells.CellNewEvent;
import fr.neatmonster.agaria.events.cells.CellRenameEvent;
import fr.neatmonster.agaria.events.cells.CellResizeEvent;
import fr.neatmonster.agaria.events.cells.CellReskinEvent;
import fr.neatmonster.agaria.events.cells.CellUpdateEvent;
import fr.neatmonster.agaria.events.game.DrawDebugLineEvent;
import fr.neatmonster.agaria.events.game.ExperienceUpdateEvent;
import fr.neatmonster.agaria.events.game.GameEndedEvent;
import fr.neatmonster.agaria.events.game.GameResetEvent;
import fr.neatmonster.agaria.events.game.LeaderboardUpdateEvent;
import fr.neatmonster.agaria.events.game.MapSizeUpdateEvent;
import fr.neatmonster.agaria.events.game.ScoreUpdateEvent;
import fr.neatmonster.agaria.events.game.SpectateFieldUpdateEvent;
import fr.neatmonster.agaria.events.game.TeamsScoresUpdateEvent;
import fr.neatmonster.agaria.events.socket.SocketCloseEvent;
import fr.neatmonster.agaria.events.socket.SocketErrorEvent;
import fr.neatmonster.agaria.events.socket.SocketOpenEvent;
import fr.neatmonster.agaria.packets.ServerPacket;
import fr.neatmonster.agaria.packets.client.PacketEjectMass;
import fr.neatmonster.agaria.packets.client.PacketKeyQPressed;
import fr.neatmonster.agaria.packets.client.PacketKeyQReleased;
import fr.neatmonster.agaria.packets.client.PacketMouseMove;
import fr.neatmonster.agaria.packets.client.PacketResetConnection1;
import fr.neatmonster.agaria.packets.client.PacketResetConnection2;
import fr.neatmonster.agaria.packets.client.PacketSendAuthToken;
import fr.neatmonster.agaria.packets.client.PacketSendToken;
import fr.neatmonster.agaria.packets.client.PacketSetNickname;
import fr.neatmonster.agaria.packets.client.PacketSpectate;
import fr.neatmonster.agaria.packets.client.PacketSplit;
import fr.neatmonster.agaria.packets.server.PacketAddCell;
import fr.neatmonster.agaria.packets.server.PacketClearCells;
import fr.neatmonster.agaria.packets.server.PacketDrawDebugLine;
import fr.neatmonster.agaria.packets.server.PacketExperienceInfo;
import fr.neatmonster.agaria.packets.server.PacketGameEnd;
import fr.neatmonster.agaria.packets.server.PacketMessageLength;
import fr.neatmonster.agaria.packets.server.PacketSetBorder;
import fr.neatmonster.agaria.packets.server.PacketUpdateCells;
import fr.neatmonster.agaria.packets.server.PacketUpdateLeaderboard;
import fr.neatmonster.agaria.packets.server.PacketUpdateTeamsScores;
import fr.neatmonster.agaria.packets.server.PacketUpdateView;
import fr.neatmonster.agaria.utils.Pair;

public class GameManager {
    public static Logger logger = Logger.getLogger("AgarIA");

    public class Cell {
        public final int id;

        public int   x    = 0;
        public int   y    = 0;
        public short size = 0;
        public int   mass = 0;

        public String name  = null;
        public String skin  = null;
        public Color  color = null;

        public boolean virus   = false;
        public boolean mine    = false;
        public boolean dead    = false;
        public boolean visible = false;

        public long updateTick;
        public long lastUpdate;

        public Cell(final int id) {
            this.id = id;

            updateTick = 0;
            lastUpdate = System.currentTimeMillis();
        }

        public void appear() {
            if (visible)
                return;
            visible = true;

            events.callEvent(new CellAppearEvent(this));

            if (mine)
                updateScore();
        }

        public void disappear() {
            if (!visible)
                return;
            visible = false;

            events.callEvent(new CellDisappearEvent(this));
        }

        public void move(final int newX, final int newY) {
            if (newX == x && newY == y)
                return;

            final int oldX = x;
            final int oldY = y;
            x = newX;
            y = newY;

            if (oldX == 0 && oldY == 0)
                return;

            events.callEvent(new CellMoveEvent(this, oldX, oldY, x, y));
        }

        public void rename(final String newName) {
            if (newName.equals(name))
                return;

            final String oldName = name;
            name = newName;

            if (oldName == null)
                return;

            events.callEvent(new CellRenameEvent(this, oldName, name));
        }

        public void resize(final short newSize) {
            if (newSize == size)
                return;

            final short oldSize = size;
            size = newSize;
            mass = (int) Math.pow(newSize / 10D, 2D);

            if (oldSize == 0)
                return;

            events.callEvent(new CellResizeEvent(this, oldSize, size));

            if (mine)
                updateScore();
        }

        public void reskin(final String newSkin) {
            if (newSkin.equals(skin))
                return;

            final String oldSkin = skin;
            skin = newSkin;

            if (oldSkin == null)
                return;

            events.callEvent(new CellReskinEvent(this, oldSkin, skin));
        }

        public void update() {
            final long oldUpdate = lastUpdate;
            lastUpdate = System.currentTimeMillis();

            events.callEvent(new CellUpdateEvent(this, oldUpdate, lastUpdate));
        }

        public void destroy(final Reason reason) {
            dead = true;

            events.callEvent(new CellDestroyEvent(this, reason));
        }

        @Override
        public String toString() {
            if (name != null)
                return id + "(" + name + ")";
            return Integer.toString(id);
        }
    }

    private final EventManager events;
    private SocketHandler      socket;
    private final Timer        timer;

    private final String name;
    private String       token;
    private String       authToken;
    private byte         authProvider;

    private int                score       = 0;
    private final List<String> leaderboard = new ArrayList<>();
    private final List<Float>  teamsScores = new ArrayList<>();

    private int        spawnAttempt  = 0;
    private final int  spawnAttempts = 25;
    private final long spawnInterval = 200L;
    private TimerTask  spawnTask;

    private final long inactiveDestroy = 5 * 60 * 1000;
    private final long inactiveCheck   = 10 * 1000;
    private TimerTask  inactiveTask;

    private long                     ticks   = 0L;
    private final Map<Integer, Cell> cells   = new HashMap<Integer, Cell>() {
        private static final long serialVersionUID = 3284629935634095561L;

        @Override
        public Cell put(final Integer key, final Cell value) {
            if (value.dead)
                remove(key);
            else {
                if (value.mine)
                    myCells.add(value);
                return super.put(key, value);
            }
            return null;
        }

        @Override
        public Cell remove(final Object key) {
            final Cell cell = get(key);
            if (cell == null)
                return null;
            if (cell.mine)
                myCells.remove(cell);
            return super.remove(key);
        }
    };
    private final Set<Cell>          myCells = new HashSet<>();

    public GameManager(final String name) {
        this.name = name;

        events = new EventManager();
        timer = new Timer();
    }

    public void registerEvents(final Listener listener) {
        events.registerEvents(listener);
    }

    public Cell getCell(final int id) {
        return cells.get(id);
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    public Collection<Cell> getMyCells() {
        return myCells;
    }

    public void authentificate(final String authToken, final byte authProvider) {
        this.authToken = authToken;
        this.authProvider = authProvider;
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
        logger.info("disconnect() called");

        if (socket == null) {
            logger.warning("disconnect() called before connect(), ignoring this call");
            return false;
        }

        socket.close();
        return true;
    }

    public boolean eject() {
        if (socket == null || socket.isClosed()) {
            logger.warning("eject() was called when connection was not established, packet will be dropped");
            return false;
        }

        socket.sendPacket(new PacketEjectMass());

        return true;
    }

    public boolean spawn() {
        if (socket == null || socket.isClosed()) {
            logger.warning("spawn() was called when connection was not established, packet will be dropped");
            return false;
        }

        logger.fine("spawn() called, name=" + name);

        final PacketSetNickname packet = new PacketSetNickname();
        packet.name = name;
        socket.sendPacket(packet);

        if (spawnAttempt == 0 && spawnInterval > 0) {
            logger.finer("starting spawn() interval");

            spawnAttempt = 1;
            timer.schedule(spawnTask = new TimerTask() {

                @Override
                public void run() {
                    logger.finer("spawn() interval tick, attempt " + spawnAttempt + "/" + spawnAttempts);

                    if (spawnAttempt >= spawnAttempts) {
                        logger.warning("spawn() interval gave up! disconnecting from server!");

                        spawnAttempt = 0;
                        spawnTask.cancel();
                        spawnTask = null;

                        disconnect();
                        return;
                    }

                    ++spawnAttempt;
                    spawn();
                }
            }, spawnInterval, spawnInterval);
        }

        return true;
    }

    public boolean spectate() {
        if (socket == null || socket.isClosed()) {
            logger.warning("spectate() was called when connection was not established, packet will be dropped");
            return false;
        }

        socket.sendPacket(new PacketSpectate());

        return true;
    }

    public boolean spectateModeToggle() {
        if (socket == null || socket.isClosed()) {
            logger.warning(
                    "spectateModeToggle() was called when connection was not established, packet will be dropped");
            return false;
        }

        socket.sendPacket(new PacketKeyQPressed());
        socket.sendPacket(new PacketKeyQReleased());

        return true;
    }

    public boolean split() {
        if (socket == null || socket.isClosed()) {
            logger.warning("split() was called when connection was not established, packet will be dropped");
            return false;
        }

        socket.sendPacket(new PacketSplit());

        return true;
    }

    public boolean target(final int x, final int y) {
        if (socket == null || socket.isClosed()) {
            logger.warning("target() was called when connection was not established, packet will be dropped");
            return false;
        }

        final PacketMouseMove packet = new PacketMouseMove();
        packet.x = x;
        packet.y = y;
        packet.id = 0;
        socket.sendPacket(packet);

        return true;
    }

    private void reset() {
        logger.fine("reset()");

        spawnAttempt = 0;
        if (spawnTask != null) {
            spawnTask.cancel();
            spawnTask = null;
        }

        if (inactiveTask != null) {
            inactiveTask.cancel();
            inactiveTask = null;
        }

        score = 0;
        leaderboard.clear();
        teamsScores.clear();

        for (final Cell cell : cells.values())
            cell.destroy(Reason.RESET);
        cells.clear();
        myCells.clear();

        events.callEvent(new GameResetEvent());
    }

    private void updateScore() {
        int totalSize = 0;
        for (final Cell cell : myCells)
            totalSize += cell.size * cell.size;

        final int oldScore = score;
        score = Math.max(score, totalSize / 100);

        if (score == oldScore)
            return;

        events.callEvent(new ScoreUpdateEvent(oldScore, score));

        logger.fine("score: " + score);
    }

    boolean handlePacket(final ServerPacket packet) {
        if (packet instanceof PacketUpdateCells) {
            final PacketUpdateCells packetUC = (PacketUpdateCells) packet;

            ++ticks;

            for (final Pair<Integer, Integer> eat : packetUC.eats) {
                final int eaterId = eat.fst;
                final int eatenId = eat.snd;

                Cell eater = cells.get(eaterId);
                if (eater == null)
                    eater = new Cell(eaterId);
                eater.updateTick = ticks;
                eater.update();
                cells.put(eaterId, eater);

                Cell eaten = cells.get(eatenId);
                if (eaten == null)
                    eaten = new Cell(eatenId);
                eaten.destroy(Reason.EATEN);
                cells.put(eatenId, eaten);

                events.callEvent(new CellEatEvent(eater, eaten));

                logger.finer(eaterId + " ate " + eatenId + "(" + cells.get(eaterId) + ">" + cells.get(eatenId) + ")");
            }

            for (final PacketUpdateCells.Update update : packetUC.updates) {
                Cell cell = cells.get(update.id);
                if (cell == null)
                    cell = new Cell(update.id);

                final boolean isVirus = (update.flags & 1) > 0;
                cell.virus = isVirus;
                cell.color = new Color(0xff00000 | update.r << 16 | update.g << 8 | update.b);

                cell.move(update.x, update.y);
                cell.resize(update.size);

                if (update.name != null)
                    cell.rename(update.name);
                if (update.skin != null)
                    cell.reskin(update.skin);
                cell.appear();

                cell.updateTick = ticks;
                cell.update();

                cells.put(update.id, cell);

                events.callEvent(
                        new CellActionEvent(cell, update.x, update.y, update.size, isVirus, update.name, update.skin));

                logger.finest("action: id=" + update.id + " x=" + update.x + " y=" + update.y + " size=" + update.size
                        + " is_virus=" + isVirus + " name=" + update.name + " skin=" + update.skin);
            }

            for (final Integer removalId : packetUC.removals) {
                Cell cell = cells.get(removalId);
                if (cell == null)
                    cell = new Cell(removalId);

                cell.updateTick = ticks;
                cell.update();

                if (cell.mine)
                    cell.destroy(Reason.MERGE);
                else
                    cell.disappear();

                cells.put(removalId, cell);

                logger.finer(removalId + "(" + cell + ") disappeared");
            }
        }

        else if (packet instanceof PacketUpdateView) {
            final PacketUpdateView packetUV = (PacketUpdateView) packet;

            events.callEvent(new SpectateFieldUpdateEvent(packetUV.x, packetUV.y, packetUV.zoom));

            logger.finer("spectate FOV update: x=" + packetUV.x + " y=" + packetUV.y + " zoom=" + packetUV.zoom);
        }

        else if (packet instanceof PacketClearCells) {
            for (final Cell cell : cells.values())
                cell.destroy(Reason.SERVER_FORCED);
            cells.clear();
            myCells.clear();
        }

        else if (packet instanceof PacketDrawDebugLine) {
            final PacketDrawDebugLine packetDL = (PacketDrawDebugLine) packet;

            events.callEvent(new DrawDebugLineEvent(packetDL.x, packetDL.y));

            logger.finer("debug line drawn to x=" + packetDL.x + " y=" + packetDL.y);
        }

        else if (packet instanceof PacketAddCell) {
            final PacketAddCell packetAC = (PacketAddCell) packet;

            final int cellId = packetAC.id;
            Cell cell = cells.get(cellId);
            if (cell == null)
                cell = new Cell(cellId);

            cell.mine = true;
            if (myCells.isEmpty())
                score = 0;

            if (spawnTask != null) {
                logger.finer("detected new cell, disabling spawn() interval");

                spawnAttempt = 0;
                spawnTask.cancel();
                spawnTask = null;
            }

            cells.put(cellId, cell);

            events.callEvent(new CellNewEvent(cell));

            logger.fine("my new cell: " + cellId);
        }

        else if (packet instanceof PacketUpdateLeaderboard) {
            final PacketUpdateLeaderboard packetUL = (PacketUpdateLeaderboard) packet;

            final List<String> newLeaderboard = new ArrayList<>();
            for (final Pair<Integer, String> leader : packetUL.leaderboard)
                newLeaderboard.add(leader.snd);

            if (!leaderboard.equals(newLeaderboard)) {
                final List<String> oldLeaderboard = new ArrayList<>(newLeaderboard);
                leaderboard.clear();
                leaderboard.addAll(newLeaderboard);

                events.callEvent(new LeaderboardUpdateEvent(oldLeaderboard, leaderboard));

                logger.fine("leaderboard update: " + Arrays.toString(leaderboard.toArray()));
            }
        }

        else if (packet instanceof PacketUpdateTeamsScores) {
            final PacketUpdateTeamsScores packetUTS = (PacketUpdateTeamsScores) packet;

            final List<Float> newTeamsScores = new ArrayList<>();
            for (final Float score : packetUTS.scores)
                newTeamsScores.add(score);

            if (!teamsScores.equals(newTeamsScores)) {
                final List<Float> oldTeamsScores = new ArrayList<>(teamsScores);
                teamsScores.clear();
                teamsScores.addAll(newTeamsScores);

                events.callEvent(new TeamsScoresUpdateEvent(oldTeamsScores, teamsScores));

                logger.fine("teams scores update: " + Arrays.toString(teamsScores.toArray()));
            }
        }

        else if (packet instanceof PacketSetBorder) {
            final PacketSetBorder packetSB = (PacketSetBorder) packet;

            events.callEvent(new MapSizeUpdateEvent(packetSB.minX, packetSB.minY, packetSB.maxX, packetSB.maxY));

            logger.fine(
                    "map size: " + packetSB.minX + ", " + packetSB.minY + ", " + packetSB.maxX + ", " + packetSB.maxY);
        }

        else if (packet instanceof PacketExperienceInfo) {
            final PacketExperienceInfo packetEI = (PacketExperienceInfo) packet;

            events.callEvent(new ExperienceUpdateEvent(packetEI.level, packetEI.curExp, packetEI.nxtExp));

            logger.fine("experience update: " + packetEI.level + ", " + packetEI.curExp + "/" + packetEI.nxtExp);
        }

        else if (packet instanceof PacketMessageLength) {
            final PacketMessageLength packetML = (PacketMessageLength) packet;

            if (!handlePacket(packetML.packet))
                GameManager.logger.warning("unhandled packet " + packetML.packet.getClass().getSimpleName());
        }

        else if (packet instanceof PacketGameEnd) {
            events.callEvent(new GameEndedEvent(leaderboard.get(0)));

            logger.info(leaderboard.get(0) + " WON THE GAME! Server going for restart");
        }

        else
            return false;
        return true;
    }

    void socketOpened() {
        logger.info("connected to server");

        timer.schedule(inactiveTask = new TimerTask() {

            @Override
            public void run() {
                logger.fine("destroying inactive cells");

                final long time = System.currentTimeMillis();
                for (final Cell cell : new ArrayList<>(cells.values())) {
                    if (cell.visible || time - cell.lastUpdate < inactiveDestroy)
                        continue;

                    cell.destroy(Reason.INACTIVE);
                    cells.remove(cell);

                    logger.fine("destroying inactive " + cell.id);
                }
            }
        }, inactiveCheck, inactiveCheck);

        socket.sendPacket(new PacketResetConnection1());
        socket.sendPacket(new PacketResetConnection2());

        if (token != null) {
            final PacketSendToken packet = new PacketSendToken();
            packet.token = token;
            socket.sendPacket(packet);
        }

        if (authToken != null) {
            final PacketSendAuthToken packet = new PacketSendAuthToken();
            packet.provider = authProvider;
            packet.token = authToken;
            socket.sendPacket(packet);
        }

        events.callEvent(new SocketOpenEvent());
    }

    void socketClosed() {
        logger.info("disconnected");

        events.callEvent(new SocketCloseEvent());

        reset();
    }

    void socketErrored(final Throwable cause) {
        logger.info("connection error: " + cause.getMessage());

        events.callEvent(new SocketErrorEvent(cause));

        reset();
    }
}
