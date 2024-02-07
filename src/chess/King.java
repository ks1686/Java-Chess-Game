package chess;

public class King extends Piece {

    public King(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WK;
        } else {
            this.pieceType = PieceType.BK;
        }
    }

    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);

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
