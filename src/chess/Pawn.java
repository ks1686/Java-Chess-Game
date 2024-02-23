package chess;

import java.util.ArrayList;

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
    Piece newSpotPiece = Chess.getPiece(newRank, newFile);
    boolean isNewSpotEmpty;
    if (newSpotPiece == null) { isNewSpotEmpty = true; } else { isNewSpotEmpty = false; }



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

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file) {
    // for pawn, if !hasMoved, then the pawn can see two squares ahead. otherwise, only 1 square ahead
    // also, if there is an enemy piece directly 1 diagonal square to the left or right of the pawn, then the pawn can see that piece also (since it would be able to move there)
    // also, if the pawn can en passant, it can see there as well.
    return null; 
  }
}
