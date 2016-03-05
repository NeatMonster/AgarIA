package fr.neatmonster.agaria.agents;

import java.util.Timer;
import java.util.TimerTask;

import fr.neatmonster.agaria.GameManager;
import fr.neatmonster.agaria.ServerConnector;
import fr.neatmonster.agaria.events.EventHandler;
import fr.neatmonster.agaria.events.Listener;
import fr.neatmonster.agaria.events.cells.CellDestroyEvent;
import fr.neatmonster.agaria.events.cells.CellEatEvent;
import fr.neatmonster.agaria.events.cells.CellNewEvent;
import fr.neatmonster.agaria.events.game.ExperienceUpdateEvent;
import fr.neatmonster.agaria.events.game.GameResetEvent;
import fr.neatmonster.agaria.events.game.LeaderboardUpdateEvent;
import fr.neatmonster.agaria.events.socket.SocketErrorEvent;
import fr.neatmonster.agaria.events.socket.SocketOpenEvent;

public class Basic implements Listener {
    private static final String REGION = "EU-London";

    public static void main(final String[] args) {
        new Basic();
    }

    private final GameManager game;
    private final String[]    server;

    private final Timer timer;
    private TimerTask   targetTask;

    public Basic() {
        timer = new Timer();

        game = new GameManager("AgarIA");
        game.registerEvents(this);

        server = ServerConnector.getFFAServer(REGION);
        GameManager.logger.info("connecting to " + server[0] + " with key " + server[1]);
        game.connect("ws://" + server[0], server[1]);
    }

    @EventHandler
    public void onSocketOpen(final SocketOpenEvent event) {
        GameManager.logger.info("spawning");

        game.spawn();

        timer.schedule(targetTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
            }
        }, 100L, 100L);
    }

    @EventHandler
    public void onSocketError(final SocketErrorEvent event) {
        GameManager.logger.warning("connection failed with reason: " + event.cause.getMessage());
        GameManager.logger.warning(
                "server address set to: " + server[0] + " please check if this is correct and working address");
    }

    @EventHandler
    public void onCellNew(final CellNewEvent event) {
        GameManager.logger.info("My new ball " + event.id + ", total " + game.getMyCells().size());
    }

    @EventHandler
    public void onCellEat(final CellEatEvent event) {
        if (event.isMine())
            GameManager.logger.info("I ate " + event.eatenId + ", my new size is " + game.getCell(event.id).size);
    }

    @EventHandler
    public void onLeaderboardUpdate(final LeaderboardUpdateEvent event) {
        GameManager.logger.info("Leaders on server: " + String.join(", ", event.newLeaderboard));
    }

    @EventHandler
    public void onGameReset(final GameResetEvent event) {
        if (targetTask != null) {
            targetTask.cancel();
            targetTask = null;
        }
    }

    @EventHandler
    public void onCellDestroy(final CellDestroyEvent event) {
        if (event.isMine()) {
            GameManager.logger.info("I lost my ball " + event.id + " because " + event.reason.name().toLowerCase());

            if (game.getMyCells().isEmpty()) {
                GameManager.logger.info("Lost all my balls, respawning");
                game.spawn();
            }
        }
    }

    @EventHandler
    public void onExperienceUpdate(final ExperienceUpdateEvent event) {
        GameManager.logger.info("Experience update: current level is " + event.level + " and experience is "
                + event.curExp + "/" + event.nxtExp);
    }
}
