package chess;

public class Queen extends Piece {

  public Queen(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WQ;
    } else {
      this.pieceType = PieceType.BQ;
    }
  }

  // TODO: need to implement checking for pieces in the way
  public boolean canMove(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile,
      boolean isNewSpotEmpty) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    if (rankChange == 0 && fileChange != 0) {
      // perfect vertical
      return true;
    } else // perfect diagonal
    if (fileChange == 0 && rankChange != 0) {
      // perfect horizontal
      return true;
    } else return rank == newRank || file == newFile || rankChange == fileChange;
    // invalid move not accounting for pieces in the way
  }
}
