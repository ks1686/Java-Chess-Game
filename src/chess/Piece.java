package chess;

import chess.Chess.Player;
import java.util.ArrayList;

public abstract class Piece extends ReturnPiece {
  public boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved

  // Constructor; sets the color of the piece
  public Piece(boolean isWhite) {
    this.isWhite = isWhite;
  }

  public abstract ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(
      int rank, ReturnPiece.PieceFile file);

  public abstract boolean canMoveSpecific(
      int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile);

  public boolean canMove(int newRank, ReturnPiece.PieceFile newFile) {
    int rank = this.pieceRank;
    ReturnPiece.PieceFile file = this.pieceFile;
    char fileChar = file.name().charAt(0);
    char newFileChar = newFile.name().charAt(0);

    // check if the piece to move is the correct color
    if ((Chess.currentPlayer == Player.white && pieceType.name().charAt(0) == 'B')
        || (Chess.currentPlayer == Player.black && pieceType.name().charAt(0) == 'W')) {
      return false; // can't move a piece of the opposite color
    } else if (file == newFile && rank == newRank) {
      return false; // can't move to the same square
    } else if (fileChar < Chess.MIN_FILE
        || fileChar > Chess.MAX_FILE
        || newFileChar < Chess.MIN_FILE
        || newFileChar > Chess.MAX_FILE
        || rank < Chess.MIN_RANK
        || rank > Chess.MAX_RANK
        || newRank < Chess.MIN_RANK
        || newRank > Chess.MAX_RANK) {
      return false; // check if square is within bounds of the board
    }

    Piece otherPiece = Chess.getPiece(newRank, newFile);

    // check if the new square is occupied by a piece of the same team
    if ((this.pieceType != ReturnPiece.PieceType.WK && this.pieceType != ReturnPiece.PieceType.BK)
        && otherPiece != null) {
      if (!this.isEnemy(otherPiece)) {
        return false; // can't move to a square occupied by a piece of the same team
      }
    }

    // get the king of the current player and check if the move puts the king in check
    Piece king = Chess.getKing(Chess.currentPlayer);
    int oldRank = this.pieceRank;
    ReturnPiece.PieceFile oldFile = this.pieceFile;
    // temporarily move the piece to the new spot to check if it puts the king in check
    this.pieceRank = newRank;
    this.pieceFile = newFile;

    // * check if the king is in check
    assert king != null;
    // kicks in before canMoveSpecific; checks if after moving the piece the king is in check
    boolean inCheck = Chess.isKingInCheck(king);

    // move the piece back
    this.pieceRank = oldRank;
    this.pieceFile = oldFile;
    if (inCheck) {
      return false; // can't move if it puts the king into check
    }

    // check if the piece can move to the new square after checking for obstacles
    return canMoveSpecific(rank, file, newRank, newFile);
  }

  // move the piece to a new spot
  public void movePiece(int newRank, Piece.PieceFile newFile) {
    boolean isNewSpotEmpty;

    // piece can move, check if the new spot is empty
    Piece otherPiece = Chess.getPiece(newRank, newFile);
    isNewSpotEmpty = otherPiece == null;
    if (!isNewSpotEmpty) {
      Chess.capturePiece(otherPiece);
    }

    // move the piece to the new spot
    this.pieceRank = newRank;
    this.pieceFile = newFile;

    // set hasMoved to true
    this.hasMoved = true;
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
    return file.name().charAt(0); // return the first character of the enum constant
  }

  // method that returns a PieceFile for the piece's file
  public static ReturnPiece.PieceFile charToEnumFile(char file) {
    for (ReturnPiece.PieceFile fileEnum : ReturnPiece.PieceFile.values()) {
      if (Character.toLowerCase(fileEnum.name().charAt(0)) == Character.toLowerCase(file)) {
        return fileEnum;
      }
    }

    throw new IllegalArgumentException(file + " is not a valid enum constant.");
  }

  // method that returns a PieceFile for the piece's file
  public boolean isEnemy(Piece other) {
    // returns true if they are opposite teams
    return (other != null) && (this.isWhite ^ other.isWhite);
  }
}
