package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellUpdateEvent extends CellEvent {
    public final long oldTime;
    public final long newTime;

    public CellUpdateEvent(final Cell cell, final long oldTime, final long newTime) {
        super(cell);
        this.oldTime = oldTime;
        this.newTime = newTime;
    }
}
