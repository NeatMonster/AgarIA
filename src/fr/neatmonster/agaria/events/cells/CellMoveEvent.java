package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellMoveEvent extends CellEvent {
    public final int oldX;
    public final int oldY;
    public final int newX;
    public final int newY;

    public CellMoveEvent(final Cell cell, final int oldX, final int oldY, final int newX, final int newY) {
        super(cell);
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
}
