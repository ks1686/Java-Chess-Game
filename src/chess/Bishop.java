package chess;

public class Bishop extends Piece {

    // check color to set PieceType
    public Bishop(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WB;
        } else {
            this.pieceType = PieceType.BB;
        }
    }

    // check if the move is valid for a queen
    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);
        if (rank == newRank || file == newFile || rankChange == fileChange) {
            // perfect diagonal
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }
}
