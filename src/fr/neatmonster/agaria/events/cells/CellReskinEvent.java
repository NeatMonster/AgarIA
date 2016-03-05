package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellReskinEvent extends CellEvent {
    public final String oldSkin;
    public final String newSkin;

    public CellReskinEvent(final Cell cell, final String oldSkin, final String newSkin) {
        super(cell);
        this.oldSkin = oldSkin;
        this.newSkin = newSkin;
    }
}
