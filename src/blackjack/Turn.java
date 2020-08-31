package blackjack;

import java.io.Serializable;

class Turn implements Serializable {
private int turn;
private String message;
    public Turn(int turn, String message) {
        this.turn = turn;
        this.message = message;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
