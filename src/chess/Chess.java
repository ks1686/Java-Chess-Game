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
  private static final String[] pieceOrder = {
    "R", "N", "B", "Q", "K", "B", "N", "R"
  }; // order of pieces on the back rank

  static Player currentPlayer; // current player (white goes first)

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

  public static ReturnPlay play(String move) {
    move = move.trim(); // remove leading and trailing whitespace

    // resign kills game immediately
    if (move.equals("resign")) {
      if (currentPlayer == Player.white) {
        play.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
      } else {
        play.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
      }
      return play;
    }

    // move input formatting
    ReturnPiece.PieceFile fromFile = Piece.charToEnumFile(move.toLowerCase().charAt(0));
    int fromRank = Character.getNumericValue(move.charAt(1));
    ReturnPiece.PieceFile toFile = Piece.charToEnumFile(move.toLowerCase().charAt(3));
    int toRank = Character.getNumericValue(move.charAt(4));

    // find the piece to move in the piecesOnBoard list
    Piece pieceToMove = getPiece(fromRank, fromFile); // the piece to move

    // check if the piece to move exists
    if (pieceToMove == null) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // TODO: finish unique move logic, obstacle checking, and check/checkmate logic
    if (!pieceToMove.canMove(toRank, toFile)) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }
    // the square is a valid piece to move. actually move the piece
    pieceToMove.movePiece(toRank, toFile);

    // move is valid and we'ved moved the piece. check for pawn promotion (queen is default for
    // unspecified promotion)
    if (pieceToMove instanceof Pawn) {
      char promotion;
      if (move.length() == 5) {
        promotion = 'Q';
      } else if (move.length() == 7) { // move looks like 'g7 g8 Q' (or N, B, R))
        promotion = move.charAt(6);
      } else {
        promotion = 'Q'; // default promotion
      }

      int lastRank;
      if (pieceToMove.isWhite) {
        lastRank = 8;
      } else {
        lastRank = 1;
      }
      if (pieceToMove.pieceRank == lastRank) {
        int currentRank = pieceToMove.pieceRank;
        ReturnPiece.PieceFile currentFile = pieceToMove.pieceFile;
        Piece newPiece;

        switch (promotion) {
          case 'N':
            newPiece = new Knight(pieceToMove.isWhite);
            break;
          case 'B':
            newPiece = new Bishop(pieceToMove.isWhite);
            break;
          case 'R':
            newPiece = new Rook(pieceToMove.isWhite);
            break;
          default:
            newPiece = new Queen(pieceToMove.isWhite);
            break;
        }
        play.piecesOnBoard.remove(pieceToMove);
        newPiece.pieceRank = currentRank;
        newPiece.pieceFile = currentFile;
        play.piecesOnBoard.add(newPiece);
      }
    }

    // move is valid; check if a draw is requested. else, just perform move
    if (move.endsWith("draw?") && move.length() == 11) {
      play.message = ReturnPlay.Message.DRAW;
    } else {
      play.message = null; // no message

      resetPawnHasJustAdvancedTwice(
          currentPlayer); // reset pawn hasJustAdvancedTwice of the opposite team to false

      // change player
      if (currentPlayer == Player.white) {
        currentPlayer = Player.black;
      } else {
        currentPlayer = Player.white;
      }
    }

    return play; // return the current state of the game
  }

  public static boolean isSquareVisibleByEnemy(
      int rank, ReturnPiece.PieceFile file, boolean isWhite) {
    // find the team of the piece. if isWhite is true, check if the piece is black. if isWhite is
    // false, check if the piece is white.
    // first  check if square is in bounds
    Square s;
    try {
      s = new Square(rank, file); // throws exception if square is out of bounds
    } catch (IllegalArgumentException e) {
      return false;
    }

    for (ReturnPiece p : play.piecesOnBoard) {
      Piece piece = (Piece) p;
      if (isWhite != piece.isWhite) {
        ArrayList<ArrayList<Square>> visibleSquares =
            piece.getVisibleSquaresFromLocation(piece.pieceRank, piece.pieceFile);
        if (Square.isSquareInNestedList(visibleSquares, s)) {
          return true; // there is a piece that can see the specified square
        }
      }
    }

    return false;
  }

  public static Piece getKing(Player player) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceType == ReturnPiece.PieceType.WK && player == Player.white) {
        return (Piece) piece;
      } else if (piece.pieceType == ReturnPiece.PieceType.BK && player == Player.black) {
        return (Piece) piece;
      }
    }
    return null;
  }

  private static void resetPawnHasJustAdvancedTwice(Player player) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceType == ReturnPiece.PieceType.WP && player == Player.black) {
        ((Pawn) piece).hasJustAdvancedTwice = false;
      } else if (piece.pieceType == ReturnPiece.PieceType.BP && player == Player.white) {
        ((Pawn) piece).hasJustAdvancedTwice = false;
      }
    }
  }

  // method to check if a king is in check
  public static boolean isInCheck(Piece king) {
    // get color of the king
    boolean isWhite = king.isWhite;

    // get the rank and file of the king
    int kingRank = king.pieceRank;
    ReturnPiece.PieceFile kingFile = king.pieceFile;

    // call canMove() for all pieces of the opposite color to see if they can move to the king's
    // square

    for (ReturnPiece piece : play.piecesOnBoard) {
      // cast a piece to a specific type to call canMove method
      Piece otherPiece = (Piece) piece;
      if (otherPiece.isEnemy(king)) {
        if (otherPiece.canMove(kingRank, kingFile)) {
          return true;
        }
      }
    }

    return false;
  }

  // method to capture a piece (must be different color)
  public static void capturePiece(Piece piece) {
    play.piecesOnBoard.remove(piece);
    piece = null;
  }

  public static boolean isSquareOnBoard(int rank, ReturnPiece.PieceFile file) {
    int a = rank;
    char b = Piece.enumFileToChar(file);
    char c = MIN_FILE;
    char d = MAX_FILE;
    boolean isRankOnBoard = a >= MIN_RANK && a <= MAX_RANK;
    boolean isFileOnBoard = b >= MIN_FILE && b <= MAX_FILE;

    return rank >= MIN_RANK
        && rank <= MAX_RANK
        && Piece.enumFileToChar(file) >= MIN_FILE
        && Piece.enumFileToChar(file) <= MAX_FILE;
  }

  // method to create a new piece given a piece type and color
  private static ReturnPiece createNewPiece(String pieceType, boolean isWhite) {
    switch (pieceType) {
      case "P":
        return new Pawn(isWhite);
      case "R":
        return new Rook(isWhite);
      case "N":
        return new Knight(isWhite);
      case "B":
        return new Bishop(isWhite);
      case "Q":
        return new Queen(isWhite);
      case "K":
        return new King(isWhite);
      default:
        throw new IllegalArgumentException("Invalid piece type: " + pieceType);
    }
  }

  // method to add a piece to the board
  private static void addPieceToBoard(
      ReturnPlay play, String pieceType, Piece.PieceFile file, int rank, boolean isWhite) {
    ReturnPiece piece = createNewPiece(pieceType, isWhite);
    piece.pieceFile = file;
    piece.pieceRank = rank;
    play.piecesOnBoard.add(piece);
  }

  // method to set up pieces on the board
  private static void setupPieces(ReturnPlay play, String[] pieceOrder, boolean isWhite) {
    int backRank;
    int pawnRank;

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
    for (int i = 0; i < pieceOrder.length; i++) {
      addPieceToBoard(play, pieceOrder[i], ReturnPiece.PieceFile.values()[i], backRank, isWhite);
    }
  }

  // method to start a new game
  public static void start() {
    play = new ReturnPlay(); // create new ReturnPlay object
    currentPlayer = Player.white; // white goes first (needed for resets/draws)
    play.piecesOnBoard = new ArrayList<ReturnPiece>(); // create new ArrayList for pieces
    play.message = null; // no message

    // set up team pieces
    setupPieces(play, pieceOrder, true); // white
    setupPieces(play, pieceOrder, false); // black
  }
}
