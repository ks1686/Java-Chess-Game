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

  public void movePiece(int newRank, Piece.PieceFile newFile) {
    // check if king is castling
    // variable to store file change for castling king
    ReturnPiece.PieceFile kingCastleFile = null;
    if (Math.abs(this.pieceFile.ordinal() - newFile.ordinal()) > 1) {
      // if the king is castling, move the rook as well
      if (newFile.ordinal() > this.pieceFile.ordinal()) { // castling to the right
        Piece rook = Chess.getPiece(this.pieceRank, ReturnPiece.PieceFile.h);
        rook.movePiece(this.pieceRank, ReturnPiece.PieceFile.f);
        rook.hasMoved = true;
        // set kingCastleFile to g
        kingCastleFile = ReturnPiece.PieceFile.g;
      } else { // castling to the left
        Piece rook = Chess.getPiece(this.pieceRank, ReturnPiece.PieceFile.a);
        rook.movePiece(this.pieceRank, ReturnPiece.PieceFile.d);
        rook.hasMoved = true;
        // set the kingCastleFile to c
        kingCastleFile = ReturnPiece.PieceFile.c;
      }
      // now move the king
      this.pieceRank = newRank; // move piece to new spot
      this.pieceFile = kingCastleFile;
      this.hasMoved = true;
      return;
    }

    // otherwise, super.movePiece()
    super.movePiece(newRank, newFile);
  }

  // TODO: need to implement checking for pieces in the way
  public boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile) {

    // castling logic: see if there's a rook of the same team in the new square. if so, make sure
    // neither the king nor the
    // rook hasMoved.
    // if so, make sure there are no obstacles in between the king and rook.
    // if so, make sure no piece can see any squares between the king and rook.
    // if so, castle.

    // king can't move into check
    boolean inCheck = Chess.isInCheck(this);
    if (inCheck) {
      return false; // can't move if it puts the king into check
    }

    // castling logic implemented here
    Piece otherPiece = Chess.getPiece(newRank, newFile);
    if (otherPiece != null) {
      if ((isWhite && otherPiece.getPieceType() == PieceType.WR)
          || (!isWhite && otherPiece.getPieceType() == PieceType.BR)) {
        if (!otherPiece.hasMoved && !this.hasMoved) {
          // get all the squares in between the king and the rook. they haven't moved, so it will be
          // just a horizontal line.
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

          // check if any of the enemy pieces canMove() to any of the squares
          for (Square s : squares) {
            for (ReturnPiece p : Chess.getPiecesOnBoard()) {
              // cast to Piece
              Piece piece = (Piece) p;
              if (piece.isWhite != this.isWhite) {
                // see if piece canMove() to any squares between the king and rook
                if (piece.canMove(s.rank, s.file)) {
                  return false; // can't castle if any of enemy pieces canMove() to any of the
                  // squares
                }
              }
            }
          }
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
      squares.add(new Square(rank, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1 - 'a']));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // up-right
      squares.add(
          new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1 - 'a']));
    }
    if (rank + 1 <= Chess.MAX_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // up-left
      squares.add(
          new Square(rank + 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1 - 'a']));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) + 1 <= Chess.MAX_FILE) { // down-right
      squares.add(
          new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) + 1 - 'a']));
    }
    if (rank - 1 >= Chess.MIN_RANK && enumFileToChar(file) - 1 >= Chess.MIN_FILE) { // down-left
      squares.add(
          new Square(rank - 1, ReturnPiece.PieceFile.values()[enumFileToChar(file) - 1 - 'a']));
    }

    visibleSquares.add(squares);
    return visibleSquares;
  }
}
