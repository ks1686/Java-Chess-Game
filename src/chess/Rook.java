package chess;

public class Rook extends Piece {


    public Rook(boolean isWhite) {
        super(isWhite);
        if (this.isWhite()) {
            this.pieceType = PieceType.WR;
        } else {
            this.pieceType = PieceType.BR;
        }
    }

    // check if the move is valid for a queen
    public boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece) {
        if (rankChange == 0 && fileChange != 0) {
            // perfect vertical
            return true;
        } else if (fileChange == 0 && rankChange != 0) {
            // perfect horizontal
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }
}
