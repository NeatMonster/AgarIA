package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellRenameEvent extends CellEvent {
    public final String oldName;
    public final String newName;

    public CellRenameEvent(final Cell cell, final String oldName, final String newName) {
        super(cell);
        this.oldName = oldName;
        this.newName = newName;
    }
}
