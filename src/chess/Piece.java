package chess;

public abstract class Piece extends ReturnPiece {
  public boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved

  // Constructor; sets the color of the piece
  public Piece(boolean isWhite) {
    this.isWhite = isWhite;
  }

  // abstract method; check if move is valid for specific type of piece (use PieceRank and PieceFile
  // from ReturnPiece class
  public abstract boolean canMove(
      int rank,
      ReturnPiece.PieceFile file,
      int newRank,
      ReturnPiece.PieceFile newFile,
      boolean isNewSpotEmpty);

  // return pieceType
  public ReturnPiece.PieceType getPieceType() {
    return this.pieceType;
  }

  // override toString
  public String toString() {
    return "Piece: " + this.getPieceType().name() + " " + this.pieceFile.name() + this.pieceRank;
  }

  // method that returns a char for the piece's file
  public char enumFileToChar(ReturnPiece.PieceFile file) {
    return (char) (file.name().charAt(0) + 32); // convert to lowercase
  }
}
