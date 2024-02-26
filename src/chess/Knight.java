package chess;

import java.util.ArrayList;

public class Knight extends Piece {

  // constructor
  public Knight(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WN;
    } else {
      this.pieceType = PieceType.BN;
    }
  }

  // method to check if the piece can move to a new square
  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {
    // change in rank and file
    int rankChange = Math.abs(rank - newRank);
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile));

    // get illegal moves out of the way; knights move in an L shape
    return (rankChange == 2 && fileChange == 1) || (rankChange == 1 && fileChange == 2);
  }

  // method to get the visible squares from the location
  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file) {
    // arraylist to store the visible squares from the location
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();
    ArrayList<Square> knightSquares = new ArrayList<>();

    // array of possible knight moves
    int[][] knightMoves = {
      {+2, +1}, {+2, -1}, {-2, +1}, {-2, -1},
      {+1, +2}, {+1, -2}, {-1, +2}, {-1, -2}
    };

    // convert the file to an int to use in the knightMoves array
    int fileInt = file.ordinal();

    // loop through the knightMoves array and add the squares to the knightSquares array
    for (int[] move : knightMoves) {
      int newRank = rank + move[0];
      int newFileInt = fileInt + move[1];

      // check if the new rank and file are valid
      if (newRank >= Chess.MIN_RANK
          && newRank <= Chess.MAX_RANK
          && newFileInt >= 0
          && newFileInt < ReturnPiece.PieceFile.values().length) {
        knightSquares.add(new Square(newRank, ReturnPiece.PieceFile.values()[newFileInt]));
      }
    }

    // add the knightSquares array to the visibleSquares arraylist
    visibleSquares.add(knightSquares);
    return visibleSquares;
  }
}
