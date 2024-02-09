package chess;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
        if (this.isWhite()) {
            this.pieceType = PieceType.WN;
        } else {
            this.pieceType = PieceType.BN;
        }
    }

    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);
        if ((rankChange == 2 && fileChange == 1) || (rankChange == 1 && fileChange == 2)) {
            // perfect L
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }
}
