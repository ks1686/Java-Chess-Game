package chess;

import java.util.ArrayList;

public class Knight extends Piece {

  public Knight(boolean isWhite) {
    super(isWhite);
    if (isWhite) {
      this.pieceType = PieceType.WN;
    } else {
      this.pieceType = PieceType.BN;
    }
  }

  
  public boolean canMoveSpecific(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return (rankChange == 2 && fileChange == 1)
        || (rankChange == 1 && fileChange == 2); // can move in an L shape
  }

  public ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file) {
    ArrayList<ArrayList<Square>> visibleSquares = new ArrayList<>();
    ArrayList<Square> knightSquares = new ArrayList<>();
    int[][] knightMoves = {
        {+2, +1}, {+2, -1}, {-2, +1}, {-2, -1},
        {+1, +2}, {+1, -2}, {-1, +2}, {-1, -2}
    };
    
    int fileInt = file.ordinal();
    
    for (int[] move : knightMoves) {
        int newRank = rank + move[0];
        int newFileInt = fileInt + move[1];
        
        // Check if the new position is within the bounds of the board
        if (newRank >= Chess.MIN_RANK && newRank <= Chess.MAX_RANK &&
            newFileInt >= 0 && newFileInt < ReturnPiece.PieceFile.values().length) {
            knightSquares.add(new Square(newRank, ReturnPiece.PieceFile.values()[newFileInt]));
        }
    }
    visibleSquares.add(knightSquares);
    return visibleSquares;
  }
}
