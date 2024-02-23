package chess;

public class Knight extends Piece {

  public Knight(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WN;
    } else {
      this.pieceType = PieceType.BN;
    }
  }

  
  public boolean canMoveSpecific(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return (rankChange == 2 && fileChange == 1)
        || (rankChange == 1 && fileChange == 2); // can move in an L shape
  }

  public Square[][] getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file) {
    return null;
  }
}
