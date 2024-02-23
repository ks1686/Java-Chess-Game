package chess;

import java.util.ArrayList;

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
  public boolean canMoveSpecific(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
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

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file) {
    // combination of rook, bishop, and king. 
    // repeating squares should be fine 
    // (for example, the square left of the queen would be shared w/ the king array as well as the leftRow array from the rook implementation)
    // these squares repeat but it shouldn't cause problems if they do
    return null;
  }

}
