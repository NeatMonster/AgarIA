package fr.neatmonster.agaria.events.game;

public class DrawDebugLineEvent extends GameEvent {
    public short x;
    public short y;

    public DrawDebugLineEvent(final short x, final short y) {
        this.x = x;
        this.y = y;
    }
}
