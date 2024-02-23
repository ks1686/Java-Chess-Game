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

    // castling logic: see if there's a rook of the same team in the new square. if so, make sure neither the king nor the
    // rook hasMoved.
    // if so, make sure there are no obstacles in between the king and rook.
    // if so, make sure no piece can see any squares between the king and rook.
    // if so, castle.

    // king can't move into check
    if (Chess.isSquareVisibleByEnemy(newRank, newFile, isWhite)) {
      return false;
    }

    // castling logic implemented here
    Piece otherPiece = Chess.getPiece(newRank, newFile);
    if (otherPiece != null) {
      if ((isWhite && otherPiece.getPieceType() == PieceType.WR) || (!isWhite && otherPiece.getPieceType() == PieceType.BR)) {
        if (!otherPiece.hasMoved && !this.hasMoved) {
          // get all the squares in between the king and the rook. they haven't moved, so it will be just a horizontal line.
          ArrayList<Square> squares = new ArrayList<>();
          int fileInt = file.ordinal();
          int newFileInt = newFile.ordinal();
          if (fileInt < newFileInt) {
            for (int f = fileInt + 1; f < newFileInt; f++) { // castling to the right
              squares.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
            }
          } else {
            for (int f = newFileInt + 1; f < fileInt; f++) { // castling to the left
              squares.add(new Square(rank, ReturnPiece.PieceFile.values()[f]));
            }
          }
          // check if any of the squares have pieces on them
          for (Square s : squares) {
            if (Chess.getPiece(s.rank, s.file) != null) {
              return false; // can't castle if there are pieces in between
            }
          }

          // check if any of the squares are visible to the other team
          for (Square s : squares) {
            // go through piecesOnBoard arraylist, if the piece is of the opposite team, get its visible squares
            // then iterate through its visible squares and see if any of them are in the squares arraylist
            if (Chess.isSquareVisibleByEnemy(s.rank, s.file, isWhite)) {
              return false; // can't castle if any of the squares are visible to the other team
            }
          }
          // otherwise, we can castle
          return true;

        }
      }
    }

    

    // king can move one square in any direction provided 
    int rankChange = Math.abs(rank - newRank); // change in rank
    int fileChange = Math.abs(enumFileToChar(file) - enumFileToChar(newFile)); // change in file
    return rankChange <= 1 && fileChange <= 1; // can move one square in any direction
    
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
      squares.add(new Square(rank, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1 - 'a']));
    }
    if (enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // left
      squares.add(new Square(rank, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1  - 'a']));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // up-right
      squares.add(new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1 - 'a']));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // up-left
      squares.add(new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1 - 'a']));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // down-right
      squares.add(new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1 - 'a']));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // down-left
      squares.add(new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1 - 'a']));
    }

    visibleSquares.add(squares);
    return visibleSquares;
  }
}
