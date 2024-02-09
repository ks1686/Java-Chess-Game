package chess;

//Class to represent the chess board
public class Board {
    final private int minFile;
    final private int maxFile;
    final private int minRank;
    final private int maxRank;

    // Idea for board class: have a 2D array boardGrid that contains pieces. on Chess.start(), clear() the boardGrid and add these pieces in the correct spots.
    // on piece capture, set the value at the array location corresponding to that coordinate to null
    // on piece move, set the value at the old array location to null, and the value to the new array location to the piece.
    // this lets us check what piece is at a given location easily (the alternative being linearly search through play.piecesOnBoard until you find the location you're looking for)
    private Piece[][] boardGrid;

    public Board(char minFile, char maxFile, int minRank, int maxRank) {
        if (minFile < 'a' || maxFile > 'h' || minFile > maxFile) {
            throw new IllegalArgumentException("File inputs are out of range or improperly ordered.");
        }

        if (minRank < 1 || maxRank > 8 || minRank > maxRank) {
            throw new IllegalArgumentException("Rank inputs are out of range or improperly ordered.");
        }

        this.minFile = minFile;
        this.maxFile = maxFile;
        this.minRank = minRank;
        this.maxRank = maxRank;

        this.boardGrid = new Piece[8][8];

    }

    public void clear() {
        this.boardGrid = new Piece[8][8];
    }

    public void placePiece(int rank, char file, Piece pieceToPlace) {
        this.boardGrid[rank - 1][file - 'a'] = pieceToPlace;
    }

    public Piece getPiece(int rank, char file) {
        return this.boardGrid[rank - 1][file - 'a'];
    }

    public int getMinFile() {
        return minFile;
    }

    public int getMaxFile() {
        return maxFile;
    }

    public int getMinRank() {
        return minRank;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public boolean areCoordsInBounds(char file, int rank) {
        return (rank >= this.getMinRank() && rank <= this.getMaxRank() && 
            file >= this.getMinRank() || file <= this.getMaxRank());
    }
}
