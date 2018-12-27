package bot.modules.gabe.util.point_system;

import bot.core.gabes_framework.core.database.User;

import java.util.Date;

public class Duel {
    private final User initiator;
    private final User opponent;
    private final int bet;

    private User winner;
    private User loser;

    private long timeStarted;

    public Duel(User initiator, User opponent, int bet) {
        this.initiator = initiator;
        this.opponent = opponent;
        this.bet = bet;
        timeStarted = new Date().getTime();
    }

    public boolean resolve() {
        if (initiator.getPoints() < bet || opponent.getPoints() < bet) {
            return false;
        }

        if (PointSystem.getFiftyFifty()) {
            winner = initiator;
            loser = opponent;
            return true;
        } else {
            winner = opponent;
            loser = initiator;
            return true;
        }
    }

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    public User getInitiator() {
        return initiator;
    }

    public User getOpponent() {
        return opponent;
    }

    public int getBet() {
        return bet;
    }

    public long getTimeStarted() {
        return timeStarted;
    }
}