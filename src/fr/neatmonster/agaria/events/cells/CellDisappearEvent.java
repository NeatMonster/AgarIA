package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellDisappearEvent extends CellEvent {

    public CellDisappearEvent(final Cell cell) {
        super(cell);
    }
}
