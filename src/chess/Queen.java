package chess;

public class Queen extends Piece {

    private PieceType pieceType;

    // check color to set PieceType
    public Queen(boolean isWhite) {
        this.isWhite = isWhite;
        if (isWhite) {
            this.pieceType = PieceType.WQ;
        } else {
            this.pieceType = PieceType.BQ;
        }
    }

}
