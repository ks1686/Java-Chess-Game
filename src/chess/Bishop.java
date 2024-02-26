package chess;

import java.util.ArrayList;

public class Bishop extends Piece {

  // constructor
  public Bishop(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WB;
    } else {
      this.pieceType = PieceType.BB;
    }
  }

  // method to check if the bishop can move to a specific square
  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {
    // change in rank and file
    int rankChange = Math.abs(rank - newRank);
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile));

    // get illegal moves out of the way
    if (rankChange != fileChange) {
      return false;
    }

    // get visible squares from location
    ArrayList<ArrayList<Square>> visibleSquares = getVisibleSquaresFromLocation(rank, file);
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

  // method to get the visible squares from the location
  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    // arraylist to store the visible squares from the location
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();
    ArrayList<Square> upperRightDiagonal = new ArrayList<>();
    ArrayList<Square> upperLeftDiagonal = new ArrayList<>();
    ArrayList<Square> lowerRightDiagonal = new ArrayList<>();
    ArrayList<Square> lowerLeftDiagonal = new ArrayList<>();

    // get the file as an int
    int fileInt = file.ordinal();

    // add all the squares in the diagonals to the arraylists
    for (int r = rank + 1, f = fileInt + 1;
        r <= Chess.MAX_RANK && f < ReturnPiece.PieceFile.values().length;
        r++, f++) {
      upperRightDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // add all the squares in the diagonals to the arraylists
    for (int r = rank + 1, f = fileInt - 1; r <= Chess.MAX_RANK && f >= 0; r++, f--) {
      upperLeftDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // add all the squares in the diagonals to the arraylists
    for (int r = rank - 1, f = fileInt + 1;
        r >= Chess.MIN_RANK && f < ReturnPiece.PieceFile.values().length;
        r--, f++) {
      lowerRightDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // add all the squares in the diagonals to the arraylists
    for (int r = rank - 1, f = fileInt - 1; r >= Chess.MIN_RANK && f >= 0; r--, f--) {
      lowerLeftDiagonal.add(new Square(r, ReturnPiece.PieceFile.values()[f]));
    }

    // add all the diagonals to the visibleSquares arraylist
    visibleSquares.add(upperRightDiagonal);
    visibleSquares.add(upperLeftDiagonal);
    visibleSquares.add(lowerRightDiagonal);
    visibleSquares.add(lowerLeftDiagonal);

    // return the visible squares
    return visibleSquares;
  }
}
