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
    return rankChange == 0 || fileChange == 0; // can move horizontally or vertically
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
