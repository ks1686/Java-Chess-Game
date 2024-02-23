package chess;

public class Pawn extends Piece {

  private final boolean exposedToEnPassant = false; // true if pawn makes 2 square move at start

  public Pawn(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WP;
    } else {
      this.pieceType = PieceType.BP;
    }
  }

  public boolean canMoveSpecific(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    boolean isAllowed = false;

    // check if pawn is moving up or down (white or black)
    if (isWhite) {
      if (rankChange == 1 && fileChange == 0 && isNewSpotEmpty) {
        isAllowed = true;
      } else if (rankChange == 2 && fileChange == 0 && isNewSpotEmpty && rank == 2) {
        isAllowed = true;
      } else if (rankChange == 1 && fileChange == 1 && !isNewSpotEmpty) {
        isAllowed = true;
      }
    } else {
      if (rankChange == 1 && fileChange == 0 && isNewSpotEmpty) {
        isAllowed = true;
      } else if (rankChange == 2 && fileChange == 0 && isNewSpotEmpty && rank == 7) {
        isAllowed = true;
      } else if (rankChange == 1 && fileChange == 1 && !isNewSpotEmpty) {
        isAllowed = true;
      }
    }
    return false; // can't move any other way
  }
}
