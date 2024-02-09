package chess;

/*Class to represent a generic chess piece and commonly shared methods for inheritance
* Extends ReturnPiece class to use the PieceType and PieceFile enums
*/
public abstract class Piece extends ReturnPiece {

    private boolean isWhite;
    private boolean hasMoved = false; // default, nothing moved

    public Piece(boolean isWhite){
        this.isWhite = isWhite;
    }

    // Setter; sets the color of the piece
    public void setWhite(boolean isWhite) {
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

    // Abstract method to be implemented by the subclasses
    public abstract boolean canMovePiece(int rank, char file, int newRank, char newFile, int rankChange, int fileChange, Piece sourcePiece, Piece destinationPiece);
    // this signature is kinda long lol maybe theres a better way to do this? not sure

    public boolean canMove(int rank, char file, int newRank, char newFile) {
        // all canMove functions should (imo) first check if coordinates are in bounds of the board, then if there is a piece at (newRank, newFile)
        // then get rank and file changes, etc.
        // so we can group common behavior together

        // this method still isn't finished
        Piece sourcePiece = Chess.ChessBoard.getPiece(rank, file);
        Piece destinationPiece = Chess.ChessBoard.getPiece(newRank, newFile);
        int rankChange = Math.abs(rank - newRank);
        int fileChange = Math.abs(file - newFile);

        return canMovePiece(rank, file, newRank, newFile, rankChange, fileChange, sourcePiece, destinationPiece);
    }

    // set hasMoved to true when moved
    public void movePiece(int toRank, int toFile) {
        this.hasMoved = true;
    }

    // return the piece type
    public PieceType getPieceType() {
        return this.pieceType;
    }
}