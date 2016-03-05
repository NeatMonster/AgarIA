package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellNewEvent extends CellEvent {

    public CellNewEvent(final Cell cell) {
        super(cell);
    }
}
