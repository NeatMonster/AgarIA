package fr.neatmonster.agaria.agents;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Set;

import fr.neatmonster.agaria.GameManager;
import fr.neatmonster.agaria.GameManager.Cell;
import fr.neatmonster.agaria.GameWindow;
import fr.neatmonster.agaria.ServerConnector;
import fr.neatmonster.agaria.events.EventHandler;
import fr.neatmonster.agaria.events.Listener;
import fr.neatmonster.agaria.events.cells.CellDestroyEvent;
import fr.neatmonster.agaria.events.socket.SocketOpenEvent;

public class HumanAgent implements Listener {
    private static final String REGION = "EU-London";

    public static void main(final String[] args) {
        new HumanAgent();
    }

    private final GameManager game;

    public HumanAgent() {
        game = new GameManager("AgarIA");
        game.registerEvents(this);

        final GameWindow window = new GameWindow(game);
        window.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(final MouseEvent e) {
                final Set<Cell> myCells = game.getMyCells();
                if (!myCells.isEmpty()) {
                    double centerX = 0.0;
                    double centerY = 0.0;
                    int totalSize = 0;

                    for (final Cell cell : myCells) {
                        centerX += cell.xRender;
                        centerY += cell.yRender;
                        totalSize += cell.sizeRender;
                    }

                    centerX /= myCells.size();
                    centerY /= myCells.size();

                    final double scaleBasis = Math.pow(Math.min(64.0 / totalSize, 1.0), 0.4);
                    final double ratio = Math.max(window.getWidth() / 1920.0, window.getHeight() / 1080.0);
                    final double scale = scaleBasis * ratio;

                    final double targetX = centerX + (e.getX() - window.getWidth() / 2.0) / scale;
                    final double targetY = centerY + (e.getY() - window.getHeight() / 2.0) / scale;

                    game.target((int) targetX, (int) targetY);
                }
            }
        });
        window.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
                    game.split();
                else if (e.getKeyCode() == KeyEvent.VK_W)
                    game.eject();
            }
        });

        final String[] server = ServerConnector.getFFAServer(REGION);
        game.connect("ws://" + server[0], server[1]);
    }

    @EventHandler
    public void onSocketOpen(final SocketOpenEvent event) {
        game.spawn();
    }

    @EventHandler
    public void onCellDestroy(final CellDestroyEvent event) {
        if (event.isMine() && game.getMyCells().isEmpty())
            game.spawn();
    }
}
