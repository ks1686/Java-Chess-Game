package chess;

public class King extends Piece {

  public boolean inCheck = false; // default to false; will be set to true if in check

  public King(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WK;
    } else {
      this.pieceType = PieceType.BK;
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
    return rankChange <= 1 && fileChange <= 1; // can move one space in any direction
  }
}
