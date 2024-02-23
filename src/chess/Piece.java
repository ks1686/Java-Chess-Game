package chess;

import chess.Chess.Player;

public abstract class Piece extends ReturnPiece {
  public boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved

  // Constructor; sets the color of the piece
  public Piece(boolean isWhite) {
    this.isWhite = isWhite;
  }

  // abstract method; check if move is valid for specific type of piece (use PieceRank and PieceFile
  // from ReturnPiece class

  public abstract boolean canMoveSpecific(int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile);

  public boolean canMove(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile) {
        if ((Chess.currentPlayer == Player.white && pieceType.name().charAt(0) == 'B') || 
            (Chess.currentPlayer == Player.black && pieceType.name().charAt(0) == 'W')) {
            return false; // check if the piece to move is the correct color
        } else if (file == newFile && rank == newRank) {
            return false; // check if piece didn't change squares
        } else if (file.name().charAt(0) < Chess.MIN_FILE || file.name().charAt(0) > Chess.MAX_FILE ||
                    newFile.name().charAt(0) < Chess.MIN_FILE || newFile.name().charAt(0) > Chess.MAX_FILE ||
                    rank < Chess.MIN_RANK || rank > Chess.MAX_RANK ||
                    newRank < Chess.MIN_RANK || newRank > Chess.MAX_RANK) {
            return false; // check if square is within bounds of the board
        }


        return canMoveSpecific(rank, file, newRank, newFile);

      }

  // return pieceType
  public ReturnPiece.PieceType getPieceType() {
    return this.pieceType;
  }

  // override toString
  public String toString() {
    return "Piece: " + this.getPieceType().name() + " " + this.pieceFile.name() + this.pieceRank;
  }

  // method that returns a char for the piece's file
  public static char enumFileToChar(ReturnPiece.PieceFile file) {
    return (char) (file.name().charAt(0) + 32); // convert to lowercase
  }

  public static ReturnPiece.PieceFile charToEnumFile(char file) {
    for (ReturnPiece.PieceFile fileEnum : ReturnPiece.PieceFile.values()) {
      if (Character.toLowerCase(fileEnum.name().charAt(0)) == Character.toLowerCase(file)) {
          return fileEnum;
      }
    }

    throw new IllegalArgumentException(file + " is not a vaid enum constant.");
  }

}
