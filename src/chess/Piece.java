package chess;

/*Class to represent a generic chess piece and commonly shared methods for inheritance
* Extends ReturnPiece class to use the PieceType and PieceFile enums
*/
public abstract class Piece extends ReturnPiece {

    private boolean isWhite;
    private boolean hasMoved = false; // default, nothing moved
    private boolean isCaptured = false; // default, nothing captured

    // Constructor; sets the color of the piece
    public Piece(boolean isWhite){
        this.isWhite = isWhite;
    }

    // Getter; gets the color of the piece
    public boolean isWhite() {
        return this.isWhite;
    }

    // Setter; sets the hasMoved boolean
    public void hasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    // Getter; gets the hasMoved boolean
    public boolean hasMoved() {
        return this.hasMoved;
    }

    // Setter; sets the isCaptured boolean
    public void isCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    // Getter; gets the isCaptured boolean
    public boolean isCaptured() {
        return this.isCaptured;
    }

    // Abstract method to be implemented by the subclasses
    public abstract boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece);
    // this signature is kinda long lol maybe theres a better way to do this? not sure

    public boolean canMove(Board chessBoard, int rank, char file, int newRank, char newFile) {
        
		if (!chessBoard.areCoordsInBounds(file, rank) || !chessBoard.areCoordsInBounds(newFile, newRank)){
			return false; // if coords are out of bounds, its illegal (uses Board.areCoordsInBounds)
		} else if (file == newFile && rank == newRank) {
			return false; // if player doesn't move at all, it's illegal
		}
        
        Piece sourcePiece = Chess.ChessBoard.getPiece(rank, file);
        Piece destinationPiece = Chess.ChessBoard.getPiece(newRank, newFile);
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);

        // we still need to finish the implementation of this method for most of the piece classes
        return canMovePiece(rank, file, newRank, newFile, rankChange, fileChange, sourcePiece, destinationPiece);
    }

    // set hasMoved to true when moved
    public void movePiece(int toRank, int toFile) {
        this.hasMoved = true;
        // should also set the value at the old array location to null, and the value to the new array location to the piece.
    }

    // return the piece type
    public PieceType getPieceType() {
        return this.pieceType;
    }

    public String toString() {
        return "Piece: " + this.getPieceType().name() + " " + this.pieceFile.name() + this.pieceRank;
    }
}