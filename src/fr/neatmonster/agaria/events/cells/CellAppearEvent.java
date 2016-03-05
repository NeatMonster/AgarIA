package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellAppearEvent extends CellEvent {

    public CellAppearEvent(final Cell cell) {
        super(cell);
    }
}
