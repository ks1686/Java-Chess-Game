package chess;

public abstract class Piece extends ReturnPiece {
  public final boolean isWhite;
  public boolean hasMoved = false; // default, nothing moved
  public boolean isCaptured = false; // default, nothing captured

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
  public char enumFileToChar(ReturnPiece.PieceFile pieceFile) {
    // 'a' is 97 in ASCII, so use ordinal to get the correct char
    return (char) (this.pieceFile.ordinal() + 97);
  }
}
