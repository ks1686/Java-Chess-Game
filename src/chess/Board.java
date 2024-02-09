package chess;

//Class to represent the chess board
public class Board {
    final private int minFile;
    final private int maxFile;
    final private int minRank;
    final private int maxRank;

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
    }

    // Getter for minFile
    public int getMinFile() {
        return minFile;
    }

    // Getter for maxFile
    public int getMaxFile() {
        return maxFile;
    }

    // Getter for minRank
    public int getMinRank() {
        return minRank;
    }

    // Getter for maxRank
    public int getMaxRank() {
        return maxRank;
    }

    public boolean areCoordsInBounds(char file, int rank) {
        return (rank >= this.getMinRank() && rank <= this.getMaxRank() && 
            file >= this.getMinRank() || file <= this.getMaxRank());
    }
}
