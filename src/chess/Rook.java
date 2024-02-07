package chess;

public class Rook extends Piece {

    // check color to set PieceType
    public Rook(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WR;
        } else {
            this.pieceType = PieceType.BR;
        }
    }

    // check if the move is valid for a queen
    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);
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
