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
    // change in rank and file
    int rankChange = Math.abs(rank - newRank);
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile));

    // get illegal moves out of the way
    if (rankChange > 0 && fileChange > 0) {
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

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();
    ArrayList<Square> upperColumn = new ArrayList<>();
    ArrayList<Square> lowerColumn = new ArrayList<>();
    ArrayList<Square> rightRow = new ArrayList<>();
    ArrayList<Square> leftRow = new ArrayList<>();

    for (int r = rank + 1; r <= Chess.MAX_RANK; r++) {
      upperColumn.add(new Square(r, file));
    }

    for (int r = rank - 1; r >= Chess.MIN_RANK; r--) {
      lowerColumn.add(new Square(r, file));
    }

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
