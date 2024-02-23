package chess;

import java.util.ArrayList;

public class Pawn extends Piece {

  /* Rules for en passant (from wikipedia)
  1. the enemy pawn advanced two squares on the previous turn;
  2. the capturing pawn attacks the square that the enemy pawn passed over.

  so the pawn class needs a flag that indiciates whether or not it has advanced two squares on the previous turn
  and then whenever a player makes any move, the flag of all of the opposing player's pawns must be set to false
  since it would no longer be the case that any of their pawns have advanced two squares on the previous turn 

 */
   
  public boolean hasJustAdvancedTwice = false; // true if pawn makes 2 square move in the previous turn

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
    return isAllowed; // can't move any other way
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquaresOuter = new ArrayList<>();
    ArrayList<Square> visibleSquares = new ArrayList<>();


    // for pawn, if !hasMoved, then the pawn can see two squares ahead. otherwise, only 1 square ahead
    int rankMultiplier;
    if (Chess.currentPlayer == Chess.Player.white) { rankMultiplier = 1; } else { rankMultiplier = -1;}

    if (!hasMoved) {
      // can move two squares forward if they're in bounds
      if (rank + rankMultiplier <= Chess.MAX_RANK && rank + rankMultiplier >= Chess.MIN_RANK)
        visibleSquares.add(new Square(rank + rankMultiplier, file));
        if (rank + 2 * rankMultiplier <= Chess.MAX_RANK && rank + 2 * rankMultiplier >= Chess.MIN_RANK) {
          visibleSquares.add(new Square(rank + 2 * rankMultiplier, file));
        }
    } else {
      // Pawn has moved, can only move one square forward
      if (rank + rankMultiplier <= Chess.MAX_RANK && rank + rankMultiplier >= Chess.MIN_RANK) {
        visibleSquares.add(new Square(rank + rankMultiplier, file));
        }
    }

    // if there is an enemy piece directly 1 diagonal square to the left or right of the pawn, then the pawn can see that piece also (since it would be able to move there)
    int fileInt = file.ordinal();
    Square rightDiag = new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[fileInt + 1]);
    Square leftDiag = new Square(rank+rankMultiplier, ReturnPiece.PieceFile.values()[fileInt + 1]);
    Piece rightDiagPiece = Chess.getPiece(rightDiag.rank, rightDiag.file);
    Piece leftDiagPiece = Chess.getPiece(leftDiag.rank, leftDiag.file);
    if (isEnemy(rightDiagPiece)) {
      visibleSquares.add(rightDiag);
    }
    if (isEnemy(leftDiagPiece)) {
      visibleSquares.add(leftDiag);
    }
    

    // also, if the pawn can en passant, it can see there as well.
    Square leftSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt - 1]);
    Square rightSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt + 1]);
    Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
    Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);
    // if the left or right pieces are pawns and are enemies and have just advanced twice, then the pawn can see them
    if (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP) {
      if (leftPiece != null && isEnemy(leftPiece) && ((Pawn)leftPiece).hasJustAdvancedTwice) {
        visibleSquares.add(leftSquare);
      }
    }

    if (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP) {
      if (rightPiece != null && isEnemy(rightPiece) && ((Pawn)rightPiece).hasJustAdvancedTwice) {
        visibleSquares.add(rightSquare);
      }
    }

    visibleSquaresOuter.add(visibleSquares);
    return visibleSquaresOuter;
  }
}
