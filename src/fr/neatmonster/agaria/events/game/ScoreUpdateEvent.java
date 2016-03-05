package fr.neatmonster.agaria.events.game;

public class ScoreUpdateEvent extends GameEvent {
    public final int oldScore;
    public final int newScore;

    public ScoreUpdateEvent(final int oldScore, final int newScore) {
        this.oldScore = oldScore;
        this.newScore = newScore;
    }
}
