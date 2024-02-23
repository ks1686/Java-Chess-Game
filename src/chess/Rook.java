package chess;

public class Rook extends Piece {

  public Rook(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WR;
    } else {
      this.pieceType = PieceType.BR;
    }
  }

  public boolean canMoveSpecific(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return rankChange == 0 || fileChange == 0; // can move horizontally or vertically
  }
}
