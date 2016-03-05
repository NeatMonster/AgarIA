package fr.neatmonster.agaria.events.game;

public class MapSizeUpdateEvent extends GameEvent {
    public final double minX;
    public final double minY;
    public final double maxX;
    public final double maxY;

    public MapSizeUpdateEvent(final double minX, final double minY, final double maxX, final double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
}
