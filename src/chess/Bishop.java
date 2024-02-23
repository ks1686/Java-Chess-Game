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
    return rankChange == fileChange; // can move diagonally
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
