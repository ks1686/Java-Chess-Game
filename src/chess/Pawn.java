package chess;

public class Pawn extends Piece {


    public Pawn(boolean isWhite) {
        super(isWhite);
        if (this.isWhite()) {
            this.pieceType = PieceType.WP;
        } else {
            this.pieceType = PieceType.BP;
        }
    }

    public boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece) {
        boolean isAllowed = false; // default to false for legal move

        // check if the pawn is moving up or down
        if (this.isWhite()) {
            // white pawn, moves up
            rankChange = newRank - rank;
        } else {
            // black pawn, moves down
            rankChange = -(newRank - rank);
        }

        boolean isNewSpotEmpty = false;
        if (destinationPiece == null) isNewSpotEmpty = false;

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
