package chess;

public class King extends Piece {

    // in check
    private boolean inCheck = false; // default to false because we didn't even get to play yet

    public King(boolean isWhite) {
        super(isWhite);
        if (this.isWhite()) {
            this.pieceType = PieceType.WK;
        } else {
            this.pieceType = PieceType.BK;
        }
    }

    public boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece) {
        if (rankChange == fileChange && rankChange == 1 && fileChange == 1) {
            // perfect diagonal
            return true;
        } else if (rankChange == 1 && fileChange == 0) {
            // perfect vertical
            return true;
        } else if (fileChange == 1 && rankChange == 0) {
            // perfect horizontal
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }
}
