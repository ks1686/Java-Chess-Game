package chess;

import java.util.ArrayList;

// ! Do not change

class ReturnPiece {
  enum PieceType {
    WP,
    WR,
    WN,
    WB,
    WQ,
    WK,
    BP,
    BR,
    BN,
    BB,
    BK,
    BQ
  }

  enum PieceFile {
    a,
    b,
    c,
    d,
    e,
    f,
    g,
    h
  }

  PieceType pieceType;
  PieceFile pieceFile;
  int pieceRank; // 1..8

  public String toString() {
    return "" + pieceFile + pieceRank + ":" + pieceType;
  }

  public boolean equals(Object other) {
    if (other == null || !(other instanceof ReturnPiece otherPiece)) {
      return false;
    }
    return pieceType == otherPiece.pieceType
        && pieceFile == otherPiece.pieceFile
        && pieceRank == otherPiece.pieceRank;
  }
}

// ! Do not change
class ReturnPlay {
  enum Message {
    ILLEGAL_MOVE,
    DRAW,
    RESIGN_BLACK_WINS,
    RESIGN_WHITE_WINS,
    CHECK,
    CHECKMATE_BLACK_WINS,
    CHECKMATE_WHITE_WINS,
    STALEMATE
  }

  ArrayList<ReturnPiece> piecesOnBoard; // all pieces on the board
  Message message;
}

public class Chess {
  enum Player {
    white,
    black
  }

  static ReturnPlay play; // the current state of the game, meaning the board and the message

  // static final variables for board dimensions
  static final int MIN_RANK = 1;
  static final int MAX_RANK = 8;
  static final char MIN_FILE = 'a';
  static final char MAX_FILE = 'h';
  // static final variables for piece order
  private static final String[] pieceOrder = {"R", "N", "B", "Q", "K", "B", "N", "R"};

  // static variable for the current player
  static Player currentPlayer;

  // method to get a piece from the piecesOnBoard arraylist
  public static Piece getPiece(int rank, ReturnPiece.PieceFile file) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceFile == file && piece.pieceRank == rank) {
        return (Piece) piece;
      }
    }
    return null;
  }

  // method to get the piecesOnBoard arraylist
  public static ArrayList<ReturnPiece> getPiecesOnBoard() {
    return play.piecesOnBoard;
  }

  // method to get the current state of the game, updated with String move
  public static ReturnPlay play(String move) {
    move = move.trim(); // remove leading/trailing whitespace

    // resign kills game immediately
    if (move.equals("resign")) {
      if (currentPlayer == Player.white) {
        play.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
      } else {
        play.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
      }
      return play;
    }

    // move input formatting
    ReturnPiece.PieceFile fromFile = Piece.charToEnumFile(move.toLowerCase().charAt(0));
    int fromRank = Character.getNumericValue(move.charAt(1));
    ReturnPiece.PieceFile toFile = Piece.charToEnumFile(move.toLowerCase().charAt(3));
    int toRank = Character.getNumericValue(move.charAt(4));

    // find the piece to move in the piecesOnBoard list
    Piece pieceToMove = getPiece(fromRank, fromFile);

    // check if the piece to move exists
    if (pieceToMove == null) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // TODO: Check/Checkmate Logic
    if (!pieceToMove.canMove(toRank, toFile)) { // check if the move is legal
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    pieceToMove.movePiece(toRank, toFile); // move the piece

    // TODO: recheck for check/checkmate?

    // check if move results in pawn promotion
    if (pieceToMove instanceof Pawn) {
      // check if the move results in pawn promotion
      char promotion = 'Q'; // default promotion is queen
      if (move.length() == 7) promotion = move.charAt(6);

      int lastRank;
      if (pieceToMove.isWhite) { // white pawn
        lastRank = 8;
      } else { // black pawn
        lastRank = 1;
      }

      // check if the pawn has reached the last rank
      if (pieceToMove.pieceRank == lastRank) {
        int currentRank = pieceToMove.pieceRank;
        ReturnPiece.PieceFile currentFile = pieceToMove.pieceFile;
        // create a new piece of the specified promotion type
        Piece newPiece =
            switch (promotion) {
              case 'N' -> new Knight(pieceToMove.isWhite);
              case 'B' -> new Bishop(pieceToMove.isWhite);
              case 'R' -> new Rook(pieceToMove.isWhite);
              default -> new Queen(pieceToMove.isWhite);
            };

        // remove the old piece and add the new piece
        play.piecesOnBoard.remove(pieceToMove);
        newPiece.pieceRank = currentRank;
        newPiece.pieceFile = currentFile;
        play.piecesOnBoard.add(newPiece);
      }
    }

    // move performed, check if player requests a draw
    if (move.endsWith("draw?") && move.length() == 11) {
      play.message = ReturnPlay.Message.DRAW;
    } else {
      play.message = null; // no message

      // reset pawn hasJustAdvancedTwice
      resetPawnHasJustAdvancedTwice(currentPlayer);

      // change player
      if (currentPlayer == Player.white) {
        currentPlayer = Player.black;
      } else {
        currentPlayer = Player.white;
      }
    }

    return play; // return the current state of the game
  }

  // TODO: method never implemented; remove or implement
  public static boolean isSquareVisibleByEnemy(
      int rank, ReturnPiece.PieceFile file, boolean isWhite) {

    // check if the square is on the board
    Square s;
    try {
      s = new Square(rank, file); // throws exception if square is out of bounds
    } catch (IllegalArgumentException e) {
      return false;
    }

    // check if any piece of the opposite color can see the square
    for (ReturnPiece p : play.piecesOnBoard) {
      Piece piece = (Piece) p;
      if (isWhite != piece.isWhite) {
        ArrayList<ArrayList<Square>> visibleSquares =
            piece.getVisibleSquaresFromLocation(piece.pieceRank, piece.pieceFile);
        // check if the square is in the visibleSquares arraylist
        if (Square.isSquareInNestedList(visibleSquares, s)) {
          return true;
        }
      }
    }

    return false;
  }

  // method to get the king of a specific player
  public static Piece getKing(Player player) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceType == ReturnPiece.PieceType.WK && player == Player.white) {
        return (Piece) piece;
      } else if (piece.pieceType == ReturnPiece.PieceType.BK && player == Player.black) {
        return (Piece) piece;
      }
    }
    return null; // should never happen
  }

  // method to reset the hasJustAdvancedTwice variable for all pawns of a specific player
  private static void resetPawnHasJustAdvancedTwice(Player player) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      // reset hasJustAdvancedTwice for all pawns of the opposite color
      if (piece.pieceType == ReturnPiece.PieceType.WP && player == Player.black) {
        ((Pawn) piece).hasJustAdvancedTwice = false;
      } else if (piece.pieceType == ReturnPiece.PieceType.BP && player == Player.white) {
        ((Pawn) piece).hasJustAdvancedTwice = false;
      }
    }
  }

  // method to check if a king is in check
  public static boolean isInCheck(Piece king) {

    // get the rank and file of the king
    int kingRank = king.pieceRank;
    ReturnPiece.PieceFile kingFile = king.pieceFile;

    // check if any piece of the opposite color can move to the king's square
    for (ReturnPiece piece : play.piecesOnBoard) {
      // cast a piece to a specific type to call canMove method
      Piece otherPiece = (Piece) piece;
      if (otherPiece.isEnemy(king)) {
        if (otherPiece.canMove(kingRank, kingFile)) {
          return true;
        }
      }
    }

    // king is not in check
    return false;
  }

  // method to capture a piece of opposite color
  public static void capturePiece(Piece piece) {
    play.piecesOnBoard.remove(piece);
  }

  // method to check if a square is on the board
  public static boolean isSquareOnBoard(int rank, ReturnPiece.PieceFile file) {
    // return true if the square is on the board
    return rank >= MIN_RANK
        && rank <= MAX_RANK
        && Piece.enumFileToChar(file) >= MIN_FILE
        && Piece.enumFileToChar(file) <= MAX_FILE;
  }

  // method to create a new piece given a piece type and color
  private static ReturnPiece createNewPiece(String pieceType, boolean isWhite) {
    return switch (pieceType) {
      case "P" -> new Pawn(isWhite);
      case "R" -> new Rook(isWhite);
      case "N" -> new Knight(isWhite);
      case "B" -> new Bishop(isWhite);
      case "Q" -> new Queen(isWhite);
      case "K" -> new King(isWhite);
      default -> throw new IllegalArgumentException("Invalid piece type: " + pieceType);
    };
  }

  // method to add a piece to the board
  private static void addPieceToBoard(
      ReturnPlay play, String pieceType, Piece.PieceFile file, int rank, boolean isWhite) {
    // create a new piece and add it to the piecesOnBoard arraylist
    ReturnPiece piece = createNewPiece(pieceType, isWhite);
    piece.pieceFile = file;
    piece.pieceRank = rank;
    play.piecesOnBoard.add(piece);
  }

  // method to set up pieces on the board
  private static void setupPieces(ReturnPlay play, boolean isWhite) {
    int backRank;
    int pawnRank;

    // set up back rank and pawn rank
    if (isWhite) {
      backRank = 1;
      pawnRank = 2;
    } else {
      backRank = 8;
      pawnRank = 7;
    }

    // set up Pawn pieces
    for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
      addPieceToBoard(play, "P", file, pawnRank, isWhite);
    }

    // set up back rank pieces
    for (int i = 0; i < Chess.pieceOrder.length; i++) {
      addPieceToBoard(
          play, Chess.pieceOrder[i], ReturnPiece.PieceFile.values()[i], backRank, isWhite);
    }
  }

  // method to start a new game
  public static void start() {
    play = new ReturnPlay(); // create new ReturnPlay object
    currentPlayer = Player.white; // white goes first (needed for resets/draws)
    play.piecesOnBoard = new ArrayList<>(); // create new ArrayList for pieces
    play.message = null; // no message

    // set up team pieces
    setupPieces(play, true); // white
    setupPieces(play, false); // black
  }
}
