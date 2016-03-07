package fr.neatmonster.agaria.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.neatmonster.agaria.GameManager;
import fr.neatmonster.agaria.GameManager.Cell;
import fr.neatmonster.agaria.events.EventHandler;
import fr.neatmonster.agaria.events.Listener;
import fr.neatmonster.agaria.events.game.MapSizeUpdateEvent;
import fr.neatmonster.agaria.utils.Pair;
import fr.neatmonster.agaria.utils.StringUtils;

public class GameWindow extends JFrame implements Runnable {
    private static final Dimension size = new Dimension(1280, 720);

    public class GameCanvas extends JPanel implements Listener {
        private final Font          FONT = new Font("Ubuntu", Font.BOLD, 18);
        private final BufferedImage image;

        public GameCanvas() {
            image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void paint(final Graphics g) {
            final Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(242, 251, 255));
            g2d.fillRect(0, 0, size.width, size.height);

            synchronized (game) {
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
                    final double ratio = Math.max(size.width / 1920.0, size.height / 1080.0);
                    final double scale = scaleBasis * ratio;

                    g2d.translate(size.width / 2.0, size.height / 2.0);
                    g2d.scale(scale, scale);
                    g2d.translate(-centerX, -centerY);

                    g2d.setColor(new Color(219, 227, 230));
                    g2d.setStroke(new BasicStroke(3));

                    final double x0 = centerX - size.width / 2.0 / scale;
                    final double y0 = centerY - size.height / 2.0 / scale;
                    final double x1 = centerX + size.width / 2.0 / scale;
                    final double y1 = centerY + size.height / 2.0 / scale;
                    double x2 = (centerX - minX) % 50.0;
                    x2 = 50.0 - (x2 < 0.0 ? x2 + 50.0 : x2);
                    double y2 = (centerY - (int) minY) % 50.0;
                    y2 = 50.0 - (y2 < 0.0 ? y2 + 50.0 : y2);
                    for (double x = x0; x <= x1; x += 50.0)
                        g2d.draw(new Line2D.Double(x + x2, y0, x + x2, y1));
                    for (double y = y0; y <= y1; y += 50.0)
                        g2d.draw(new Line2D.Double(x0, y + y2, x1, y + y2));

                    final List<Cell> cells = new ArrayList<>(game.getCells());
                    cells.sort(new Comparator<Cell>() {

                        @Override
                        public int compare(final Cell o1, final Cell o2) {
                            return (int) (o1.sizeRender - o2.sizeRender);
                        }
                    });
                    for (final Cell cell : cells) {
                        if (cell.dead || !cell.visible)
                            continue;

                        g2d.setColor(cell.color);
                        g2d.setStroke(new BasicStroke(10));

                        final double radius = cell.sizeRender + 5.0;
                        if (cell.virus) {
                            final Path2D.Double virus = new Path2D.Double();
                            final int vertices = (int) cell.sizeRender;
                            for (int i = 0; i < vertices; ++i) {
                                final double spike = i % 2 == 0 ? cell.sizeRender / 10.0 : 0.0;
                                final double x = cell.xRender
                                        + (radius + spike) * Math.cos(2.0 * Math.PI * i / vertices);
                                final double y = cell.yRender
                                        + (radius + spike) * Math.sin(2.0 * Math.PI * i / vertices);
                                if (i == 0)
                                    virus.moveTo(x, y);
                                else
                                    virus.lineTo(x, y);
                            }
                            virus.closePath();
                            g2d.fill(virus);
                            g2d.setColor(g2d.getColor().darker());
                            g2d.draw(virus);
                        } else if (cell.mass < 5) {
                            final Path2D.Double food = new Path2D.Double();
                            final int vertices = 7 + cell.size - 10;
                            for (int i = 0; i < vertices; ++i) {
                                final double x = cell.xRender + radius * Math.cos(2.0 * Math.PI * i / vertices);
                                final double y = cell.yRender + radius * Math.sin(2.0 * Math.PI * i / vertices);
                                if (i == 0)
                                    food.moveTo(x, y);
                                else
                                    food.lineTo(x, y);
                            }
                            food.closePath();
                            g2d.fill(food);
                        } else {
                            final double xCell = cell.xRender - radius;
                            final double yCell = cell.yRender - radius;
                            g2d.fill(new Ellipse2D.Double(xCell, yCell, 2.0 * radius, 2.0 * radius));
                            g2d.setColor(g2d.getColor().darker());
                            g2d.draw(new Ellipse2D.Double(xCell, yCell, 2.0 * radius, 2.0 * radius));

                            if (cell.name != null) {
                                final Pair<Double, Double> bounds = StringUtils.getBounds(g2d, FONT, cell.name);
                                final float xString = (float) (cell.xRender - bounds.fst / 2f);
                                final float yString = (float) (cell.yRender + bounds.snd / 2f);
                                g2d.setFont(FONT);
                                g2d.setColor(Color.BLACK);
                                g2d.drawString(cell.name, xString - 1f, yString);
                                g2d.drawString(cell.name, xString + 1f, yString);
                                g2d.drawString(cell.name, xString, yString - 1f);
                                g2d.drawString(cell.name, xString, yString + 1f);
                                g2d.setColor(Color.WHITE);
                                g2d.drawString(cell.name, xString, yString);
                            }
                        }
                    }
                }
            }

            g2d.dispose();
            g.drawImage(image, 0, 0, null);
        }

        private double minX = 0;
        private double minY = 0;

        @EventHandler
        public void onMapSizeUpdate(final MapSizeUpdateEvent event) {
            if (minX == 0) {
                minX = event.minX;
                minY = event.minY;
            }
        }
    }

    private final GameManager game;
    private final GameCanvas  canvas;

    public GameWindow(final GameManager game) {
        this.game = game;
        game.registerEvents(canvas = new GameCanvas());

        setTitle("AgarIA");
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(canvas);
        pack();
        setVisible(true);

        new Thread(this).start();
    }

    private static final int TICKS_PER_SECOND = 60;
    private static final int SKIP_TICKS       = 1000 / TICKS_PER_SECOND;
    private static final int MAX_FRAMESKIP    = 10;

    @Override
    public void run() {
        long nextGameTick = System.currentTimeMillis();

        int loops;
        while (true) {
            loops = 0;

            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
                game.tick();

                nextGameTick += SKIP_TICKS;
                ++loops;
            }

            repaint();
        }
    }
}
