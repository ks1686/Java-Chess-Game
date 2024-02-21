package chess;

import java.util.ArrayList;

// ! Do not chang

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
    char fromFile = move.toLowerCase().charAt(0);
    int fromRank = Character.getNumericValue(move.charAt(1));
    char toFile = move.toLowerCase().charAt(3);
    int toRank = Character.getNumericValue(move.charAt(4));

    // check if the move is inside the board
    if (fromFile < MIN_FILE
        || fromFile > MAX_FILE
        || toFile < MIN_FILE
        || toFile > MAX_FILE
        || fromRank < MIN_RANK
        || fromRank > MAX_RANK
        || toRank < MIN_RANK
        || toRank > MAX_RANK) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // find the piece to move in the piecesOnBoard list
    ReturnPiece pieceToMove = null; // the piece to move
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceFile.name().charAt(0) == fromFile && piece.pieceRank == fromRank) {
        pieceToMove = piece;
        break;
      }
    }

    // check if the piece to move exists
    if (pieceToMove == null) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // check if the piece to move is the correct color
    if (currentPlayer == Player.white && pieceToMove.pieceType.name().charAt(0) == 'B') {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    } else if (currentPlayer == Player.black && pieceToMove.pieceType.name().charAt(0) == 'W') {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // check if we even move the piece at all
    if (fromFile == toFile && fromRank == toRank) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // TODO: implement pawn promotion logic
    // move is valid, check for pawn promotion (queen is default for unspecified promotion)
    char promotion = 'Q';
    if (toRank == 1 || toRank == 8) {
      switch (move.length()) {
        case 5:
          // promotion = 'Q'; handle future logic
          break;
        case 7:
          promotion = move.charAt(6);
          break;
      }
    }

    // TODO: move is valid; begin checking for unique moves (castling, en passant, etc.)

    // TODO: move is valid; check if move is legal for the piece

    // TODO: move is valid; check if move puts the player in check

    // TODO: move is valid; check if move puts the opponent in check

    // TODO: move is valid; check if move puts the player in checkmate

    // TODO: move is valid; check if move puts the opponent in checkmate

    // move is valid; check if a draw is requested. else, just perform move
    if (move.endsWith("draw?") && move.length() == 11) {
      play.message = ReturnPlay.Message.DRAW;
    } else {
      play.message = null; // no message

      // change player
      if (currentPlayer == Player.white) {
        currentPlayer = Player.black;
      } else {
        currentPlayer = Player.white;
      }
    }

    return play; // return the current state of the game
  }

  // method to move a piece given specific piece and new rank and file after checking for obstacles
  // (other pieces)
  private static void movePiece(ReturnPiece piece, int newRank, ReturnPiece.PieceFile newFile) {}

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
      ReturnPlay play, String pieceType, ReturnPiece.PieceFile file, int rank, boolean isWhite) {
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
