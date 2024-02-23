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
    Piece newSpotPiece = Chess.getPiece(newRank, newFile);
    boolean isNewSpotEmpty;
    if (newSpotPiece == null) { isNewSpotEmpty = true; } else { isNewSpotEmpty = false; }

    if (rankChange > 2 || fileChange > 1) {
      return false; // pawns can never move like this
    }

    ArrayList<ArrayList<Square>> visibleSquares = getVisibleSquaresFromLocation(rank, file);

    // the only time we have to check for an obstacle is if the pawn is moving two squares ahead. we have to check that there's no piece in between the pawn and the new spot
    if (rankChange == 2) {
      if (isWhite) {
        if (Chess.getPiece(rank + 1, file) != null) {
          return false;
        }
      } else {
        if (Chess.getPiece(rank - 1, file) != null) {
          return false;
        }
      }
    }

    // otherwise, if the new spot is empty, the pawn can move there
    if (isNewSpotEmpty) {
      if (Square.isSquareInNestedList(visibleSquares, new Square(newRank, newFile))) {
        return true;
      }
    } else {
      // if the new spot is not empty, the pawn can only move there if it's capturing an enemy piece (diagonally forward 1 square or en passant)
      // need to get the current file as an int so we can get the file to the left and right of it
      int pieceFileIndex = enumFileToChar(file) - 'a';
      Square rightDiag = new Square(rank + 1, ReturnPiece.PieceFile.values()[pieceFileIndex + 1]);
      Square leftDiag = new Square(rank - 1, ReturnPiece.PieceFile.values()[pieceFileIndex - 1]);
      Square leftSquare = new Square(rank, ReturnPiece.PieceFile.values()[pieceFileIndex - 1]);
      Square rightSquare = new Square(rank, ReturnPiece.PieceFile.values()[pieceFileIndex + 1]);
      Piece rightDiagPiece = Chess.getPiece(rightDiag.rank, rightDiag.file);
      Piece leftDiagPiece = Chess.getPiece(leftDiag.rank, leftDiag.file);
      Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
      Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);

      // check if square is visible to pawn. then check if the piece there is an enemy piece. if so, the pawn can move there
      if (Square.isSquareInNestedList(visibleSquares, rightDiag) && isEnemy(rightDiagPiece) && rightDiag.rank == newRank && rightDiag.file == newFile) {
        return true;
      }
      if (Square.isSquareInNestedList(visibleSquares, leftDiag) && isEnemy(leftDiagPiece) && leftDiag.rank == newRank && leftDiag.file == newFile) {
        return true;
      }
      if (Square.isSquareInNestedList(visibleSquares, rightSquare) && isEnemy(rightPiece) && rightSquare.rank == newRank && rightSquare.file == newFile) {
        // this is for en passant. in order to capture en passant, the other piece must be a pawn and must have just advanced twice
        if (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP) {
          if (((Pawn)rightPiece).hasJustAdvancedTwice) {
            return true;
          }
        }
      }
      if (Square.isSquareInNestedList(visibleSquares, leftSquare) && isEnemy(leftPiece) && leftSquare.rank == newRank && leftSquare.file == newFile) {
        // this is for en passant. in order to capture en passant, the other piece must be a pawn and must have just advanced twice
        if (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP) {
          if (((Pawn)leftPiece).hasJustAdvancedTwice) {
            return true;
          }
        }
      }
    }

    return false; // can't move any other way
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

    // checlk to make sure that file is not 'h'
    if (fileInt != 7) {
      Square rightDiag = new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[fileInt + 1]);
      Piece rightDiagPiece = Chess.getPiece(rightDiag.rank, rightDiag.file);
      if (isEnemy(rightDiagPiece)) {
        visibleSquares.add(rightDiag);
      }
      Square rightSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt + 1]);
      if (Chess.isSquareOnBoard(rightSquare.rank, rightSquare.file)) {
        Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);
        if (rightPiece != null && (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP)) {
          if (rightPiece != null && isEnemy(rightPiece) && ((Pawn)rightPiece).hasJustAdvancedTwice) {
            visibleSquares.add(rightSquare);
          }
        }
      }
    }

    // check to make sure that file is not 'a'
    if (fileInt != 0) {
      Square leftDiag = new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[fileInt - 1]);
      Piece leftDiagPiece = Chess.getPiece(leftDiag.rank, leftDiag.file);
      if (isEnemy(leftDiagPiece)) {
        visibleSquares.add(leftDiag);
      }

      // also, if the pawn can en passant, it can see there as well.
      Square leftSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt - 1]);
      if (Chess.isSquareOnBoard(leftSquare.rank, leftSquare.file)) {
        Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
        if (leftPiece != null && (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP)) {
          if (leftPiece != null && isEnemy(leftPiece) && ((Pawn)leftPiece).hasJustAdvancedTwice) {
            visibleSquares.add(leftSquare);
          }
        }
      }

    }
    

    

    

    visibleSquaresOuter.add(visibleSquares);
    return visibleSquaresOuter;
  }
}
