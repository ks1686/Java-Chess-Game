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
         * TODO:
         * if new spot not empty:
         *  1. if the piece occupying new spot is the same team as the piece, then ILLEGAL_MOVE
         *  2. if the piece occupying new spot is the opposite team, then return true
         *      - would also need to implement capture logic here, but maybe not in this method
         * 
         * if new spot empty:
         *  1. check whether the piece can move there normally
         *  2. if it can move there, for all squares between the current spot and new spot,
         *      check if there is a piece in between blocking the path. if there is, return false.
         *      - must do this for diagonals (queen and bishop), verticals (queen and rook)
         *      - and first pawn move (if pawn moves two squares up)
         *      - no need to do this for king or knight moves.
         *      - if nothing is in between, return true
         * 
         * Karim's idea:
         * 1. Check if the move is even valid for a queen (diagonals, horizontals, etc.)
         * 2. Loop to check if there are pieces in the way (will need to make the board an import into such a method)
         * 3. Edge cases for if there is/isn't a piece, if it's a friendly or not, etc.
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
