package chess;

import chess.Chess.Player;

public abstract class Piece extends ReturnPiece {
  public boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved

  // Constructor; sets the color of the piece
  public Piece(boolean isWhite) {
    this.isWhite = isWhite;
  }


  // TODO: make an abstract method that returns a list of visible squares for the piece (squares that the piece can MOVE to, accounting for obstacles)
  // TODO: make an abstract method that returns a list of capturable squares for the piece (squares that the piece can CAPTURE on, if there was an enemy piece on it)

  public abstract boolean canMoveSpecific(int rank, ReturnPiece.PieceFile file, int newRank, ReturnPiece.PieceFile newFile);

  public boolean canMove(int newRank, ReturnPiece.PieceFile newFile) {
      int rank = this.pieceRank;
      ReturnPiece.PieceFile file = this.pieceFile;

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

        // TODO: obstacle checking for other pieces before capturing

        return canMoveSpecific(rank, file, newRank, newFile); // find out whether the specific piece can move to the new square

      }


  // method to move a piece given specific piece and new rank and file after checking for obstacles and checking if the piece canMove() to the spot
  private void movePiece(int newRank, Piece.PieceFile newFile) {
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

}
