package fr.neatmonster.agaria.events.cell;

import fr.neatmonster.agaria.events.Event;

public abstract class CellEvent extends Event {
    public final int id;

    public CellEvent(final int id) {
        this.id = id;
    }
}
