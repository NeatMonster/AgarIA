package fr.neatmonster.agaria.events.game;

import java.util.Collections;
import java.util.List;

public class LeaderboardUpdateEvent extends GameEvent {
    public final List<String> oldLeaderboard;
    public final List<String> newLeaderboard;

    public LeaderboardUpdateEvent(final List<String> oldLeaderboard, final List<String> newLeaderboard) {
        this.oldLeaderboard = Collections.unmodifiableList(oldLeaderboard);
        this.newLeaderboard = Collections.unmodifiableList(newLeaderboard);
    }
}
