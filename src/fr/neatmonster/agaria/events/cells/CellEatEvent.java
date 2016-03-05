package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;

public class CellEatEvent extends CellEvent {
    public final int eatenId;

    public CellEatEvent(final Cell eater, final Cell eaten) {
        super(eater);
        eatenId = eaten.id;
    }
}
