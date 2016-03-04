package fr.neatmonster.agaria.events.cell;

public class CellActionEvent extends CellEvent {
    public final int     x;
    public final int     y;
    public final short   size;
    public final boolean virus;
    public final String  name;
    public final String  skin;

    public CellActionEvent(final int id, final int x, final int y, final short size, final boolean virus,
            final String name, final String skin) {
        super(id);
        this.x = x;
        this.y = y;
        this.size = size;
        this.virus = virus;
        this.name = name;
        this.skin = skin;
    }
}
