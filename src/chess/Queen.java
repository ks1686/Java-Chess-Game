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
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {

    // arraylist to store the visible squares from the location
    ArrayList<ArrayList<Square>> visibleSquares = getVisibleSquaresFromLocation(rank, file);

    // arraylist of visible squares from location
    ArrayList<Square> visibleSquaresFromLocation = null;
    for (ArrayList<Square> squaresList : visibleSquares) {
      if (squaresList.contains(new Square(newRank, newFile))) {
        visibleSquaresFromLocation = squaresList;
        break;
      }
    }

    // if required for the piece, make sure there are no pieces in the way.
    if (visibleSquaresFromLocation == null) {
      return false;
    }
    for (Square s : visibleSquaresFromLocation) {
      if (s.rank != newRank || s.file != newFile) {
        if (Chess.getPiece(s.rank, s.file) != null) {
          return false;
        }
      } else {
        break;
      }
    }

    // make sure the new square is not occupied by a piece of the same team.
    Piece otherPiece = Chess.getPiece(newRank, newFile);
    if (otherPiece != null) {
      return otherPiece.isWhite != this.isWhite;
    }

    // return true if all conditions are met
    return true;
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();

    // combine logic we used for rook and bishop
    ArrayList<Square> upperColumn = new ArrayList<>();
    ArrayList<Square> lowerColumn = new ArrayList<>();
    ArrayList<Square> rightRow = new ArrayList<>();
    ArrayList<Square> leftRow = new ArrayList<>();
    ArrayList<Square> upperRightDiagonal = new ArrayList<>();
    ArrayList<Square> upperLeftDiagonal = new ArrayList<>();
    ArrayList<Square> lowerRightDiagonal = new ArrayList<>();
    ArrayList<Square> lowerLeftDiagonal = new ArrayList<>();

    // upper column
    for (int r = rank + 1; r <= Chess.MAX_RANK; r++) {
      upperColumn.add(new Square(r, file));
    }

    // lower column
    for (int r = rank - 1; r >= Chess.MIN_RANK; r--) {
      lowerColumn.add(new Square(r, file));
    }

    int fileInt = file.ordinal(); // get the file as an int

    // right row
    for (int f = fileInt + 1; f < ReturnPiece.PieceFile.values().length; f++) {
      rightRow.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
    }

    // left row
    for (int f = fileInt - 1; f >= 0; f--) {
      leftRow.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
    }

    // Upper Right Diagonal
    for (int r = rank + 1, f = fileInt + 1;
        r <= Chess.MAX_RANK && f < ReturnPiece.PieceFile.values().length;
        r++, f++) {
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

    // add all the arrays to the visibleSquares arraylist
    visibleSquares.add(upperColumn);
    visibleSquares.add(lowerColumn);
    visibleSquares.add(rightRow);
    visibleSquares.add(leftRow);
    visibleSquares.add(upperRightDiagonal);
    visibleSquares.add(upperLeftDiagonal);
    visibleSquares.add(lowerRightDiagonal);
    visibleSquares.add(lowerLeftDiagonal);

    // return the arraylist
    return visibleSquares;
  }
}
