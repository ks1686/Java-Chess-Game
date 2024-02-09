package chess;

public class Pawn extends Piece {

    private boolean firstMove;

    public Pawn(boolean isWhite, boolean firstMove) {
        super(isWhite);
        this.firstMove = firstMove;
        if (this.isWhite()) {
            this.pieceType = PieceType.WP;
        } else {
            this.pieceType = PieceType.BP;
        }
    }

    public Pawn(boolean isWhite) {
        this(isWhite, true);
    }

    public boolean getFirstMove() {
        return this.firstMove;
    }

    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange;
        int fileChange;
        boolean isAllowed = false; // default to false for legal move

        // check if the pawn is moving up or down
        if (this.isWhite()) {
            // white pawn, moves up
            rankChange = newRank - rank;
        } else {
            // black pawn, moves down
            rankChange = -(newRank - rank);
        }

        fileChange = Math.abs(file - newFile);
        // check if the move is valid for a pawn
        if (rankChange == 1 && fileChange == 0 && isNewSpotEmpty) {
            // pawn can move forward one spot
            isAllowed = true;
        } else if (this.hasMoved() == false && rankChange == 2 && fileChange == 0 && isNewSpotEmpty) {
            // pawn can move forward two spots
            isAllowed = true;
        } else if (rankChange == 1 && fileChange == 0 && isNewSpotEmpty == false) {
            // pawn can capture, en passant
            isAllowed = true;
        }
        return isAllowed;
    }

}
