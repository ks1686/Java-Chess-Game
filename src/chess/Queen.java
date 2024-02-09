package chess;

public class Queen extends Piece {

    // check color to set PieceType
    public Queen(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WQ;
        } else {
            this.pieceType = PieceType.BQ;
        }
    }

    // check if the move is valid for a queen
    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);

        /*
         * Karim's idea:
         * 1. Check if the move is even valid for a queen (diagonals, horizontals, etc.)
         * 2. Loop to check if there are pieces in the way (will need to make the board an import into such a method)
         * 3. Edge cases for if there is/isn't a piece, if it's a friendly or not, etc.
         * 
         * (Jude) i like this lets do this
         */
 
        if (rank == newRank && file == newFile) {
            // can't move in place
            return false;
        } else if (rank == newRank || file == newFile || rankChange == fileChange) {
            // perfect diagonal
            return true;
        } else if (rankChange == 0 && fileChange != 0) {
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
