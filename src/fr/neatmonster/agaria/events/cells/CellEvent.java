package fr.neatmonster.agaria.events.cells;

import fr.neatmonster.agaria.GameManager.Cell;
import fr.neatmonster.agaria.events.Event;

public abstract class CellEvent extends Event {
    public final int id;

    public CellEvent(final Cell cell) {
        id = cell.id;
        mine = cell.mine;
    }

    private final boolean mine;

    public boolean isMine() {
        return mine;
    }
}
