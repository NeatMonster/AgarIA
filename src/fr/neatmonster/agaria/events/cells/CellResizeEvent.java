package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellResizeEvent extends CellEvent {
    public final short oldSize;
    public final short newSize;

    public CellResizeEvent(final Cell cell, final short oldSize, final short newSize) {
        super(cell);
        this.oldSize = oldSize;
        this.newSize = newSize;
    }
}
