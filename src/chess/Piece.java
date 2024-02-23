package chess;

import java.util.ArrayList;

import chess.Chess.Player;

public abstract class Piece extends ReturnPiece {
  public boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved

  // Constructor; sets the color of the piece
  public Piece(boolean isWhite) {
    this.isWhite = isWhite;
  }



  /* Create an abstract method to generate a list of potential move squares for a chess piece, excluding obstacle considerations. This method should return arrays representing the piece's movement options:

  For bishops, an array of four arrays, each detailing a diagonal's squares.
  For rooks, an array of four arrays, each listing a row or column's squares.
  For pawns, a single array with two squares ahead, adaptable for initial moves.
  For kings and knights, a single array each, detailing their respective surrounding move squares.
  For queens, combine the movement patterns of kings, rooks, and bishops.
  Then, in a separate method, we can evaluate these paths for obstacles to determine actual moves.
  
   */
  
  public abstract ArrayList<ArrayList<Square>> getVisibleSquaresFromLocation(int rank, ReturnPiece.PieceFile file);

  public abstract boolean canMoveSpecific(int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile);

  public boolean canMove(int newRank, ReturnPiece.PieceFile newFile) {
      int rank = this.pieceRank;
      ReturnPiece.PieceFile file = this.pieceFile;
      char fileChar = file.name().charAt(0);
      char newFileChar = newFile.name().charAt(0);
      if ((Chess.currentPlayer == Player.white && pieceType.name().charAt(0) == 'B') || 
          (Chess.currentPlayer == Player.black && pieceType.name().charAt(0) == 'W')) {
          return false; // check if the piece to move is the correct color
      } else if (file == newFile && rank == newRank) {
          return false; // check if piece didn't change squares
      } else if (fileChar < Chess.MIN_FILE || fileChar > Chess.MAX_FILE ||
                newFileChar < Chess.MIN_FILE || newFileChar > Chess.MAX_FILE ||
                  rank < Chess.MIN_RANK || rank > Chess.MAX_RANK ||
                  newRank < Chess.MIN_RANK || newRank > Chess.MAX_RANK) {
          return false; // check if square is within bounds of the board
      }

      // TODO: obstacle checking for other pieces before capturing

      return canMoveSpecific(rank, file, newRank, newFile); // find out whether the specific piece can move to the new square

      }


  // method to move a piece given specific piece and new rank and file after checking for obstacles and checking if the piece canMove() to the spot
  public void movePiece(int newRank, Piece.PieceFile newFile) {
    boolean isNewSpotEmpty;
  
    // piece can move, check if the new spot is empty
    Piece otherPiece = Chess.getPiece(newRank, newFile);
    if (otherPiece == null) { isNewSpotEmpty = true; } else { isNewSpotEmpty = false; }

    // at this point, we should've already established that the piece can move to the new spot. so, if there's a piece there, just capture it.
    if (!isNewSpotEmpty) { Chess.capturePiece(otherPiece); }

    this.pieceRank = newRank; // move piece to new spot
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

  public boolean isEnemy(Piece other) {
    // returns true if they are opposite teams
    return (other != null) && (this.isWhite ^ other.isWhite);
  }

}
