package chess;

public class Bishop extends Piece {

  public Bishop(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WB;
    } else {
      this.pieceType = PieceType.BB;
    }
  }

  public boolean canMove(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile,
      boolean isNewSpotEmpty) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return rankChange == fileChange; // can move diagonally
  }
}
