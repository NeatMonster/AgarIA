package fr.neatmonster.agaria.events.cell;

public class CellMoveEvent extends CellEvent {
    public final int oldX;
    public final int oldY;
    public final int newX;
    public final int newY;

    public CellMoveEvent(final int id, final int oldX, final int oldY, final int newX, final int newY) {
        super(id);
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
}
