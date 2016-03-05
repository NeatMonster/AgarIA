package fr.neatmonster.agaria.events.game;

import java.util.List;

public class TeamsScoresUpdateEvent extends GameEvent {
    public final List<Float> oldTeamsScores;
    public final List<Float> newTeamsScores;

    public TeamsScoresUpdateEvent(final List<Float> oldTeamsScores, final List<Float> newTeamsScores) {
        this.oldTeamsScores = oldTeamsScores;
        this.newTeamsScores = newTeamsScores;
    }
}
