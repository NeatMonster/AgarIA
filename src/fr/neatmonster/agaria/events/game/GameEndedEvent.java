package fr.neatmonster.agaria.events.game;

public class GameEndedEvent extends GameEvent {
    public final String winner;

    public GameEndedEvent(final String winner) {
        this.winner = winner;
    }
}
