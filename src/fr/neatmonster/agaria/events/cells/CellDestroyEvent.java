package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellDestroyEvent extends CellEvent {
    public static enum Reason {
        RESET, INACTIVE, EATEN, MERGE, SERVER_FORCED;
    }

    public final Reason reason;

    public CellDestroyEvent(final Cell cell, final Reason reason) {
        super(cell);
        this.reason = reason;
    }
}
