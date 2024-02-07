package chess;

public class Bishop extends Piece {

    private PieceType pieceType;

    // check color to set PieceType
    public Bishop(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WQ;
        } else {
            this.pieceType = PieceType.BQ;
        }
    }

    // check if the move is valid for a queen
    public boolean canMove(int rank, int file, int newRank, int newFile, boolean isNewSpotEmpty) {
        int diagX = Math.abs(rank - newRank);
        int diagY = Math.abs(file - newFile);
        if (rank == newRank || file == newFile || diagX == diagY) {
            // perfect diagonal
            return true;
        }
        // invalid move not accounting for pieces in the way
        return false;
    }

    public void movePiece() {
        this.hasMoved = true;
    }

}
