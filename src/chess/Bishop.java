package chess;

public class Bishop extends Piece {

    // check color to set PieceType
    public Bishop(boolean isWhite) {
        super(isWhite);
        if (this.isWhite()) {
            this.pieceType = PieceType.WB;
        } else {
            this.pieceType = PieceType.BB;
        }
    }

    // check if the move is valid for a queen
    public boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece) {
        if (rank == newRank || file == newFile || rankChange == fileChange) {
            // perfect diagonal
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }
}
