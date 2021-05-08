package boardgame.model;

public class CurrentPlayer {

    private PlayerNumber playerNumber;

    public CurrentPlayer() {
        this.playerNumber = PlayerNumber.FIRST_PLAYER;
    }

    private enum PlayerNumber {
        FIRST_PLAYER,
        SECOND_PLAYER
    }

    public void nextPlayer() {
        if (playerNumber == PlayerNumber.FIRST_PLAYER) {
            this.playerNumber = PlayerNumber.SECOND_PLAYER;
        } else {
            this.playerNumber = PlayerNumber.FIRST_PLAYER;
        }
    }

    @Override
    public String toString() {
        return playerNumber.toString();
    }
}
