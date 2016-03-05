package fr.neatmonster.agaria.events.game;

public class SpectateFieldUpdateEvent extends GameEvent {
    public final double x;
    public final double y;
    public final double zoom;

    public SpectateFieldUpdateEvent(final double x, final double y, final double zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
}