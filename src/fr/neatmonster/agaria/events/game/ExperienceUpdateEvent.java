package fr.neatmonster.agaria.events.game;

public class ExperienceUpdateEvent extends GameEvent {
    public final int level;
    public final int curExp;
    public final int nxtExp;

    public ExperienceUpdateEvent(final int level, final int curExp, final int nxtExp) {
        this.level = level;
        this.curExp = curExp;
        this.nxtExp = nxtExp;
    }
}
