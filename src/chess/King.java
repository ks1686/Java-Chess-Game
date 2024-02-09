package chess;

public class King extends Piece {

    private boolean firstMove;

    public King(boolean isWhite) {
        this(isWhite, true);
    }

    public King(boolean isWhite, boolean firstMove) {
        super(isWhite);
        this.firstMove = firstMove;
        if (this.isWhite()) {
            this.pieceType = PieceType.WK;
        } else {
            this.pieceType = PieceType.BK;
        }
    }

    public boolean getFirstMove() {
        return this.firstMove;
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
