package chess;

import java.util.ArrayList;

public class Rook extends Piece {

  public Rook(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WR;
    } else {
      this.pieceType = PieceType.BR;
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

    // r0. Get any prelimary illegal moves out of the way. rook can only move in a straight line
    if (rankChange > 0 && fileChange > 0) {
      return false;
    }

    ArrayList<ArrayList<Square>> visibleSquares =
    getVisibleSquaresFromLocation(
        rank, file); // 1. Get visible squares from location

    // 2. Make sure the new square is in in the visible squares list.
    // find the arraylist in visibleSquares that contains the new square. this is the row or column the rook is moving along
    ArrayList<Square> visibleSquaresFromLocation = null;
    for (ArrayList<Square> squaresList : visibleSquares) {
      if (squaresList.contains(new Square(newRank, newFile))) {
        visibleSquaresFromLocation = squaresList;
        break;
      }
    }

    if (visibleSquaresFromLocation == null) {
      return false; // the new square is not in any of the rows or columns the rook can move along
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
    ArrayList<Square> upperColumn = new ArrayList<>();
    ArrayList<Square> lowerColumn = new ArrayList<>();
    ArrayList<Square> rightRow = new ArrayList<>();
    ArrayList<Square> leftRow = new ArrayList<>();

    // to get the upper column, start at the rank, go up one, and then add to the arraylist until
    // you reach the end of the board (MAXRANK)
    for (int r = rank + 1; r <= Chess.MAX_RANK; r++) {
      upperColumn.add(new Square(r, file));
    }

    // etc for lower column, right row, left row
    for (int r = rank - 1; r >= Chess.MIN_RANK; r--) {
      lowerColumn.add(new Square(r, file));
    }

    // get the file as an int
    int fileInt = file.ordinal();

    for (int f = fileInt + 1; f < ReturnPiece.PieceFile.values().length; f++) {
      rightRow.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
    }

    for (int f = fileInt - 1; f >= 0; f--) {
      leftRow.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
    }

    visibleSquares.add(upperColumn);
    visibleSquares.add(lowerColumn);
    visibleSquares.add(rightRow);
    visibleSquares.add(leftRow);

    return visibleSquares;
  }
}
