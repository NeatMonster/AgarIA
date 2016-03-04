package fr.neatmonster.agaria.events.cell;

public class CellDestroyEvent extends CellEvent {
    public static enum Reason {
        RESET, INACTIVE, EATEN, MERGE, SERVER_FORCED;
    }

    public final Reason reason;

    public CellDestroyEvent(final int id, final Reason reason) {
        super(id);
        this.reason = reason;
    }
}
