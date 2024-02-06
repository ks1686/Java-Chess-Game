package chess;

//Class to represent a generic chess piece and commonly shared methods for inheritance

public abstract class Piece {
    // Boolean to track if the piece is white, 1 for white, 0 for black
    public boolean isWhite;

    // Set the color of the piece
    public void setColor(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // return if piece is white (1) or black (0)
    public boolean isWhite() {
        return isWhite;
    }

    // boolean to track if this is piece's first move (pawns, rooks, kings)
    public boolean firstMove;

    // set if this is piece's first move (1) or not (0)
    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    // return if this is piece's first move (1) or not (0)
    public boolean isFirstMove() {
        return firstMove;
    }

    // check if other piece is an enemy piece
    public boolean isEnemy(Piece other) {
        return this.isWhite != other.isWhite;
    }

    // check if other piece is an ally piece
    public boolean isAlly(Piece other) {
        return this.isWhite == other.isWhite;
    }

    // boolean to track if piece is still on the board
    public boolean isCaptured;

    // set if piece is captured (1) or not (0)
    public void setCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    // return if piece is captured (1) or not (0)
    public boolean isCaptured() {
        return isCaptured;
    }

}
