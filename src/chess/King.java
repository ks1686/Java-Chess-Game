package chess;

import java.util.ArrayList;

public class King extends Piece {

  public boolean inCheck = false; // default to false; will be set to true if in check

  public King(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WK;
    } else {
      this.pieceType = PieceType.BK;
    }
  }

  // TODO: need to implement checking for pieces in the way
  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {

    // need to handle castling logic here
    // logic: see if there's a rook in the new square. if so, make sure neither the king nor the
    // rook hasMoved.
    // if so, make sure there are no obstacles in between the king and rook.
    // if so, make sure no piece can see any squares between the king and rook.
    // if so, castle.

    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return rankChange <= 1 && fileChange <= 1; // can move one space in any direction
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();

    // just add one array of squares for the king's 8 possible moves
    ArrayList<Square> squares = new ArrayList<>();

    // add all 8 possible moves for the king, keeping in mind the board boundaries
    if (rank + 1 <= Chess.MAX_RANK) { // up
      squares.add(new Square(rank + 1, file));
    }
    if (rank - 1 >= Chess.MIN_RANK) { // down
      squares.add(new Square(rank - 1, file));
    }
    if (enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // right
      squares.add(new Square(rank, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1]));
    }
    if (enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // left
      squares.add(new Square(rank, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1]));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // up-right
      squares.add(new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1]));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // up-left
      squares.add(new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1]));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // down-right
      squares.add(new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1]));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // down-left
      squares.add(new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1]));
    }

    visibleSquares.add(squares);
    return visibleSquares;
  }
}
