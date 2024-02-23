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
   
  public boolean hasJustAdvancedTwice = false; // true if pawn advanced 2 squares in the previous turn

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
    Piece newSpotPiece = Chess.getPiece(newRank, newFile); // piece at new spot
    boolean isNewSpotEmpty;
    if (newSpotPiece == null) { isNewSpotEmpty = true; } else { isNewSpotEmpty = false; }

    if (rankChange > 2 || fileChange > 1) {
      return false; // pawns can never move like this
    }

    ArrayList<ArrayList<Square>> visibleSquares = getVisibleSquaresFromLocation(rank, file); // list of squares the pawn can see (not including obstacles)

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

    if (isNewSpotEmpty) {
      if (Square.isSquareInNestedList(visibleSquares, new Square(newRank, newFile))) {
        return true; // if the new spot is empty and the pawn can see it, then it can move there
      } else {
        return false; // if the new spot is empty and the pawn can't see it, then it can't move there
      }
    }
    
    // now we know that the new spot is not empty
    // if the new spot is not empty, the pawn can only move there if it's capturing an enemy piece (diagonally forward 1 square or en passant)
    // need to get the current file as an int so we can get the file to the left and right of it
    int pieceFileIndex = enumFileToChar(file) - 'a';
    int rankMultiplier;
    if (this.isWhite) { rankMultiplier = 1; } else { rankMultiplier = -1; }

    boolean isPawnMovingRight = newFile.ordinal() == file.ordinal() + 1;
    boolean isPawnMovingLeft = newFile.ordinal() == file.ordinal() - 1;

    if (!isPawnMovingRight && !isPawnMovingLeft) {
      return false; // if a pawn is capturing a piece (which it is since the new spot is not empty)
    }

    int fileDirection;
    if (isPawnMovingRight) { fileDirection = 1; } else { fileDirection = -1; }
    try {
      Square frontDiag = new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[pieceFileIndex + fileDirection]);
      Piece frontDiagPiece = Chess.getPiece(frontDiag.rank, frontDiag.file);

      // handle diagonal capture
      if (Square.isSquareInNestedList(visibleSquares, frontDiag) && isEnemy(frontDiagPiece) && frontDiag.rank == newRank && frontDiag.file == newFile) {
        return true;
      }

      // handle en passant
      Square horizontalSquare = new Square(rank, ReturnPiece.PieceFile.values()[pieceFileIndex + fileDirection]);
      Piece horizontalPiece = Chess.getPiece(horizontalSquare.rank, horizontalSquare.file);

      if (Square.isSquareInNestedList(visibleSquares, frontDiag) && isEnemy(horizontalPiece) && frontDiag.rank == newRank && frontDiag.file == newFile
      && isEnemy(horizontalPiece) && (horizontalPiece.pieceType == PieceType.WP || horizontalPiece.pieceType == PieceType.BP)  && ((Pawn)horizontalPiece).hasJustAdvancedTwice) {
        return true;
      }

    } catch (IllegalArgumentException e) {
      // if the square is out of bounds, then we don't need to check if the pawn can move there
      return false;
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

    // check to make sure that file is not 'h'
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
            visibleSquares.add(rightDiag);
          }
        }
      }
    }

    // do the same thing but for the left side
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
            visibleSquares.add(leftDiag);
          }
        }
      }

    }

    visibleSquaresOuter.add(visibleSquares);
    return visibleSquaresOuter;
  }

  // need to override movePiece for pawn since it has some unique stuff w/ en passant
  public void movePiece(int newRank, Piece.PieceFile newFile) {

    if (this.pieceType == PieceType.WP || this.pieceType == PieceType.BP) {
      if (Math.abs(this.pieceRank - newRank) == 2) {
        ((Pawn) this).hasJustAdvancedTwice = true;
      }
    }

    // if the pawn is on the last rank, then throw an exception
    if (newRank == Chess.MAX_RANK || newRank == Chess.MIN_RANK) {
      throw new IllegalArgumentException("Pawn is on the last rank and should've been promoted already.");
    }

    // check for en passant. if the pawn is moving diagonally and the new spot is empty and the other piece is a pawn and has just advanced twice, then capture it
    // the piece to be captured will either be to the left or right. so we need to check both and capture if it is a pawn and has advanced twice
    // make sure we're not on the first or last file
    int rankMultiplier;
    if (this.isWhite) { rankMultiplier = 1; } else { rankMultiplier = -1; }
    if (this.pieceFile.ordinal() != 0) {
      Square leftSquare = new Square(this.pieceRank, ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' - 1]);
      Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
      Square leftDiag = new Square(this.pieceRank + rankMultiplier, ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' - 1]);

      if (leftPiece != null && (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP) && isEnemy(leftPiece)) {
        if (((Pawn)leftPiece).hasJustAdvancedTwice) {
          if (leftDiag.rank == newRank && leftDiag.file == newFile) {
            Chess.capturePiece(leftPiece);
          }
        }
      }
    }

    // do the same thing but for the right side
    if (this.pieceFile.ordinal() != 7) {
      Square rightSquare = new Square(this.pieceRank, ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' + 1]);
      Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);
      Square rightDiag = new Square(this.pieceRank + rankMultiplier, ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' + 1]);
      if (rightPiece != null && (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP) && isEnemy(rightPiece)) {
        if (((Pawn)rightPiece).hasJustAdvancedTwice) {
          if (rightDiag.rank == newRank && rightDiag.file == newFile) {
            Chess.capturePiece(rightPiece);
          }
        }
      }
    }
    super.movePiece(newRank, newFile);
  }

}
