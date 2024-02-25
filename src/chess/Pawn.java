package chess;

import java.util.ArrayList;

public class Pawn extends Piece {

  // if the pawn has just advanced twice, then it can be captured en passant
  public boolean hasJustAdvancedTwice = false;

  public Pawn(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WP;
    } else {
      this.pieceType = PieceType.BP;
    }
  }

  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {
    // change in rank and file
    int rankChange = Math.abs(rank - newRank);
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile));

    // get the piece at the new spot
    Piece newSpotPiece = Chess.getPiece(newRank, newFile);

    // if the new spot is empty, then we can move there
    boolean isNewSpotEmpty;
    isNewSpotEmpty = newSpotPiece == null;

    // see if we have a valid move
    if (rankChange > 2 || fileChange > 1) {
      return false;
    }

    // get visible squares from location
    ArrayList<ArrayList<Square>> visibleSquares = getVisibleSquaresFromLocation(rank, file);

    // if the new spot is empty, then we can move there
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

    // if the new spot is empty, then we can move there
    if (isNewSpotEmpty) {
      return Square.isSquareInNestedList(visibleSquares, new Square(newRank, newFile));
    }

    // if the new spot is not empty, then we can only move there if we are capturing a piece
    int pieceFileIndex = enumFileToChar(file) - 'a';
    int rankMultiplier;
    if (this.isWhite) {
      rankMultiplier = 1;
    } else {
      rankMultiplier = -1;
    }

    // booleans for if the pawn is moving right or left
    boolean isPawnMovingRight = newFile.ordinal() == file.ordinal() + 1;
    boolean isPawnMovingLeft = newFile.ordinal() == file.ordinal() - 1;

    // if the pawn is not moving right or left, then it can't move there
    if (!isPawnMovingRight && !isPawnMovingLeft) return false;

    // if the pawn is moving right or left, then it can only move there if it is capturing a piece
    int fileDirection;
    if (isPawnMovingRight) fileDirection = 1;
    else fileDirection = -1;

    // if the pawn is moving right or left, then it can only move there if it is capturing a piece
    try {
      // get the piece at the new spot
      Square frontDiag =
          new Square(
              rank + rankMultiplier,
              ReturnPiece.PieceFile.values()[pieceFileIndex + fileDirection]);
      Piece frontDiagPiece = Chess.getPiece(frontDiag.rank, frontDiag.file);

      // handle diagonal capture
      if (Square.isSquareInNestedList(visibleSquares, frontDiag)
          && isEnemy(frontDiagPiece)
          && frontDiag.rank == newRank
          && frontDiag.file == newFile) {
        return true;
      }

      // handle en passant
      Square horizontalSquare =
          new Square(rank, ReturnPiece.PieceFile.values()[pieceFileIndex + fileDirection]);
      Piece horizontalPiece = Chess.getPiece(horizontalSquare.rank, horizontalSquare.file);

      // if the pawn is moving right or left, then it can only move there if it is capturing a piece
      if (horizontalPiece != null
          && Square.isSquareInNestedList(visibleSquares, frontDiag)
          && isEnemy(horizontalPiece)
          && frontDiag.rank == newRank
          && frontDiag.file == newFile
          && isEnemy(horizontalPiece)
          && (horizontalPiece.pieceType == PieceType.WP
              || horizontalPiece.pieceType == PieceType.BP)
          && ((Pawn) horizontalPiece).hasJustAdvancedTwice) {
        return true; // can move there if it is capturing a piece
      }

    } catch (IllegalArgumentException e) { // if the square is not on the board
      return false;
    }
    // if the new spot is not empty, then we can only move there if we are capturing a piece
    return false;
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    // arraylist to store the visible squares from the location
    ArrayList<ArrayList<Square>> visibleSquaresOuter = new ArrayList<>();
    ArrayList<Square> visibleSquares = new ArrayList<>();

    // get the rank multiplier
    int rankMultiplier;
    if (Chess.currentPlayer == Chess.Player.white) {
      rankMultiplier = 1;
    } else {
      rankMultiplier = -1;
    }

    // add all the squares in the diagonals to the arraylists
    boolean bounds =
        rank + rankMultiplier <= Chess.MAX_RANK && rank + rankMultiplier >= Chess.MIN_RANK;
    if (!hasMoved) {
      // can move two squares forward if they're in bounds
      if (bounds) visibleSquares.add(new Square(rank + rankMultiplier, file));
      if (rank + 2 * rankMultiplier <= Chess.MAX_RANK
          && rank + 2 * rankMultiplier >= Chess.MIN_RANK) {
        visibleSquares.add(new Square(rank + 2 * rankMultiplier, file));
      }
    } else {
      // Pawn has moved, can only move one square forward
      if (bounds) visibleSquares.add(new Square(rank + rankMultiplier, file));
    }

    // get the file as an int
    int fileInt = file.ordinal();

    // check to make sure that file is not 'h'
    if (fileInt != 7) {
      // add all the squares in the diagonals to the arraylists
      Square rightDiag =
          new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[fileInt + 1]);
      Piece rightDiagPiece = Chess.getPiece(rightDiag.rank, rightDiag.file);
      // if the square is occupied by an enemy piece, then add it to the list of visible squares
      if (isEnemy(rightDiagPiece)) visibleSquares.add(rightDiag);

      // also, if the pawn can en passant, it can see there as well.
      Square rightSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt + 1]);
      if (Chess.isSquareOnBoard(rightSquare.rank, rightSquare.file)) {
        Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);
        if (rightPiece != null
            && (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP)) {
          if (isEnemy(rightPiece) && ((Pawn) rightPiece).hasJustAdvancedTwice) { //
            visibleSquares.add(rightDiag);
          }
        }
      }
    }

    // same but left side; check to make sure that file is not 'a'
    if (fileInt != 0) {
      Square leftDiag =
          new Square(rank + rankMultiplier, ReturnPiece.PieceFile.values()[fileInt - 1]);
      Piece leftDiagPiece = Chess.getPiece(leftDiag.rank, leftDiag.file);
      if (isEnemy(leftDiagPiece)) {
        visibleSquares.add(leftDiag);
      }

      // also, if the pawn can en passant, it can see there as well.
      Square leftSquare = new Square(rank, ReturnPiece.PieceFile.values()[fileInt - 1]);
      if (Chess.isSquareOnBoard(leftSquare.rank, leftSquare.file)) {
        Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
        if (leftPiece != null
            && (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP)) {
          if (isEnemy(leftPiece) && ((Pawn) leftPiece).hasJustAdvancedTwice) {
            visibleSquares.add(leftDiag);
          }
        }
      }
    }

    // add all the squares to the visibleSquares arraylist
    visibleSquaresOuter.add(visibleSquares);
    return visibleSquaresOuter;
  }

  // move the piece
  public void movePiece(int newRank, Piece.PieceFile newFile) {

    // if the pawn has just advanced twice, then it can be captured en passant
    if (this.pieceType == PieceType.WP || this.pieceType == PieceType.BP) {
      if (Math.abs(this.pieceRank - newRank) == 2) {
        this.hasJustAdvancedTwice = true;
      }
    }

    // rankMultiplier for moving the pawn
    int rankMultiplier;
    if (this.isWhite) {
      rankMultiplier = 1;
    } else {
      rankMultiplier = -1;
    }
    if (this.pieceFile.ordinal() != 0) {
      Square leftSquare =
          new Square(
              this.pieceRank,
              ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' - 1]);
      Piece leftPiece = Chess.getPiece(leftSquare.rank, leftSquare.file);
      Square leftDiag =
          new Square(
              this.pieceRank + rankMultiplier,
              ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' - 1]);

      // if the pawn can en passant, it can see there as well.
      if (leftPiece != null
          && (leftPiece.pieceType == PieceType.WP || leftPiece.pieceType == PieceType.BP)
          && isEnemy(leftPiece)) {
        if (((Pawn) leftPiece).hasJustAdvancedTwice) {
          if (leftDiag.rank == newRank && leftDiag.file == newFile) {
            // capture the piece
            Chess.capturedPiece = leftPiece;
            Chess.capturePiece(leftPiece);
          }
        }
      }
    }

    // do the same thing but for the right side
    if (this.pieceFile.ordinal() != 7) {
      Square rightSquare =
          new Square(
              this.pieceRank,
              ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' + 1]);
      Piece rightPiece = Chess.getPiece(rightSquare.rank, rightSquare.file);
      Square rightDiag =
          new Square(
              this.pieceRank + rankMultiplier,
              ReturnPiece.PieceFile.values()[enumFileToChar(this.pieceFile) - 'a' + 1]);
      if (rightPiece != null
          && (rightPiece.pieceType == PieceType.WP || rightPiece.pieceType == PieceType.BP)
          && isEnemy(rightPiece)) {
        if (((Pawn) rightPiece).hasJustAdvancedTwice) {
          if (rightDiag.rank == newRank && rightDiag.file == newFile) {
            // capture the piece
            Chess.capturedPiece = rightPiece;
            Chess.capturePiece(rightPiece);
          }
        }
      }
    }
    // move the piece
    super.movePiece(newRank, newFile);
  }
}
