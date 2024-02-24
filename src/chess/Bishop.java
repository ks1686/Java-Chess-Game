package chess;

import java.util.ArrayList;

public class Bishop extends Piece {

  public Bishop(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WB;
    } else {
      this.pieceType = PieceType.BB;
    }
  }

  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    /* CANMOVESPECIFIC() CHECKLIST
       * 0. Get any prelimary illegal moves out of the way
       * 1. Get visible squares from location
       * 2. Make sure the new square is in in the visible squares list.
       * 3. If required for the piece, make sure there are no pieces in the way (not including the new square, which is checked in step 5)
       * 4. Make sure the new square is not occupied by a piece of the same team.
       * 5. Return true if all conditions are met.
       * (i think this is everything)
       */


    // 0. Get any prelimary illegal moves out of the way. bishop can only move diagonally
    if (rankChange != fileChange) {
      return false;
    }

    ArrayList<ArrayList<Square>> visibleSquares =
        getVisibleSquaresFromLocation(
            rank, file); // 1. Get visible squares from location
    
    // 2. Make sure the new square is in in the visible squares list.
    // find the arraylist in visibleSquares that contains the new square. this is the diagonal the bishop is moving along
    ArrayList<Square> visibleSquaresFromLocation = null;
    for (ArrayList<Square> squaresList : visibleSquares) {
      if (squaresList.contains(new Square(newRank, newFile))) {
        visibleSquaresFromLocation = squaresList;
        break;
      }
    }

    if (visibleSquaresFromLocation == null) {
      return false; // the new square is not in any of the diagonals the bishop can move along
    }

    // 3. If required for the piece, make sure there are no pieces in the way.
    for (Square s : visibleSquaresFromLocation) {
      if (s.rank != newRank || s.file != newFile) {
        if (Chess.getPiece(s.rank, s.file) != null) {
          return false;
        }
      }
    }

    // 4. Make sure the new square is not occupied by a piece of the same team.
    if (Chess.getPiece(newRank, newFile) != null) {
      if (Chess.getPiece(newRank, newFile).isWhite == this.isWhite) {
        return false;
      }
    }

    return true; // 5. Return true if all conditions are met.
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();
    ArrayList<Square> upperRightDiagonal = new ArrayList<>();
    ArrayList<Square> upperLeftDiagonal = new ArrayList<>();
    ArrayList<Square> lowerRightDiagonal = new ArrayList<>();
    ArrayList<Square> lowerLeftDiagonal = new ArrayList<>();

    int fileInt = file.ordinal();

    // Upper Right Diagonal
    for (int r = rank + 1, f = fileInt + 1;
        r <= Chess.MAX_RANK && f < ReturnPiece.PieceFile.values().length;
        r++, f++) {
      // add to the arraylist until you reach the end of the board (MAXRANK)
      upperRightDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // Upper Left Diagonal
    for (int r = rank + 1, f = fileInt - 1; r <= Chess.MAX_RANK && f >= 0; r++, f--) {
      upperLeftDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // Lower Right Diagonal
    for (int r = rank - 1, f = fileInt + 1;
        r >= Chess.MIN_RANK && f < ReturnPiece.PieceFile.values().length;
        r--, f++) {
      lowerRightDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // Lower Left Diagonal
    for (int r = rank - 1, f = fileInt - 1; r >= Chess.MIN_RANK && f >= 0; r--, f--) {
      lowerLeftDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // add all the diagonals to the visibleSquares arraylist
    visibleSquares.add(upperRightDiagonal);
    visibleSquares.add(upperLeftDiagonal);
    visibleSquares.add(lowerRightDiagonal);
    visibleSquares.add(lowerLeftDiagonal);

    return visibleSquares;
  }
}
