package chess;

public class Square {
    public int rank;
    public ReturnPiece.PieceFile file;

    public Square(int rank, ReturnPiece.PieceFile file) {
        this.rank = rank;
        this.file = file;
    }
}
