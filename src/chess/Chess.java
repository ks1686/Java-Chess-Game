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

  private static Piece getPiece(char file, int rank) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceFile.name().charAt(0) == file && piece.pieceRank == rank) {
        return (Piece) piece;
      }
    }
    return null;
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
    char fromFile = move.toLowerCase().charAt(0);
    int fromRank = Character.getNumericValue(move.charAt(1));
    char toFile = move.toLowerCase().charAt(3);
    int toRank = Character.getNumericValue(move.charAt(4));

    // find the piece to move in the piecesOnBoard list
    Piece pieceToMove = getPiece(fromFile, fromRank); // the piece to move
    
    // check if the piece to move exists
    if (pieceToMove == null) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // TODO: implement pawn promotion logic
    // move is valid, check for pawn promotion (queen is default for unspecified promotion)
    char promotion = '0'; // 0 for no promotion
    if (pieceToMove instanceof Pawn && (toRank == 1 || toRank == 8)) {
      switch (move.length()) {
        case 5:
          promotion = 'Q'; // handle future logic
          break;
        case 7:
          promotion = move.charAt(6);
          break;
      }
    }

    // TODO: finish unique move logic, obstacle checking, and check/checkmate logic
    if (!pieceToMove.canMove(fromRank, Piece.charToEnumFile(fromFile), toRank, Piece.charToEnumFile(toFile))) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

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
  private static void movePiece(ReturnPiece specificPiece, int newRank, Piece.PieceFile newFile) {
    // cast a piece to a specific type to call canMove method
    Piece piece = (Piece) specificPiece;

    // call canMove method to see if the move is valid
    if (piece.canMove(piece.pieceRank, piece.pieceFile, newRank, newFile, true)) {
      // piece can move, check if the new spot is empty
      boolean isNewSpotEmpty = true;
      for (ReturnPiece otherPiece : play.piecesOnBoard) {
        // check if piece is at the new spot
        if (otherPiece.pieceFile == newFile && otherPiece.pieceRank == newRank) {
          isNewSpotEmpty = false;
          break;
        }
      }

      // TODO: obstacle checking for other pieces before capturing

      // if spot is not empty, attempt to capture the piece
      if (!isNewSpotEmpty) {
        if (capturePiece(piece, newRank, newFile)) {
          piece.pieceRank = newRank; // move piece to new spot
          piece.pieceFile = newFile;
        }
      } else {
        // spot is empty, move piece to new spot
        piece.pieceRank = newRank;
        piece.pieceFile = newFile;
      }
    }
    // set hasMoved to true
    piece.hasMoved = true;
  }

  // move king (has additional conditionals for castling logic)
  private static void moveKing(ReturnPiece specificPiece, int newRank, Piece.PieceFile newFile) {
    // cast a piece to a specific type to call canMove method
    Piece piece = (Piece) specificPiece;

    // castling logic: king/rook has not moved, no pieces between king/rook
    if (!piece.hasMoved) {
      // castling to the right
      if (newFile == Piece.PieceFile.g && newRank == piece.pieceRank) {
        // check if rook has not moved
        for (ReturnPiece otherPiece : play.piecesOnBoard) {
          if (otherPiece.pieceFile == Piece.PieceFile.h
              && otherPiece.pieceRank == piece.pieceRank) {
            // cast otherPiece to a specific type to call hasMoved
            Piece tempOtherPiece = (Piece) otherPiece;
            if (!tempOtherPiece.hasMoved) {
              // check if there are pieces between king and rook
              boolean isPathClear = true;
              for (ReturnPiece pathPiece : play.piecesOnBoard) {
                if (pathPiece.pieceRank == piece.pieceRank
                    && pathPiece.pieceFile.name().charAt(0) > piece.pieceFile.name().charAt(0)
                    && pathPiece.pieceFile.name().charAt(0) < newFile.name().charAt(0)) {
                  isPathClear = false;
                  break;
                }
              }
              if (isPathClear) {
                // move king and rook
                piece.pieceFile = newFile;
                otherPiece.pieceFile = Piece.PieceFile.f;
              }
            }
          }
        }
      }
    } else if (newFile == Piece.PieceFile.c && newRank == piece.pieceRank) {
      // castling to the left
      // check if rook has not moved
      for (ReturnPiece otherPiece : play.piecesOnBoard) {
        if (otherPiece.pieceFile == Piece.PieceFile.a && otherPiece.pieceRank == piece.pieceRank) {
          // cast otherPiece to a specific type to call hasMoved
          Piece tempOtherPiece = (Piece) otherPiece;
          if (!tempOtherPiece.hasMoved) {
            // check if there are pieces between king and rook
            boolean isPathClear = true;
            for (ReturnPiece pathPiece : play.piecesOnBoard) {
              if (pathPiece.pieceRank == piece.pieceRank
                  && pathPiece.pieceFile.name().charAt(0) < piece.pieceFile.name().charAt(0)
                  && pathPiece.pieceFile.name().charAt(0) > newFile.name().charAt(0)) {
                isPathClear = false;
                break;
              }
            }
            if (isPathClear) {
              // move king and rook
              piece.pieceFile = newFile;
              otherPiece.pieceFile = Piece.PieceFile.d;
            }
          }
        }
      }
    } else {
      // move king normally
      movePiece(piece, newRank, newFile);
    }

    // set hasMoved to true
    piece.hasMoved = true;
  }

  // move a knight given specific piece and new rank and file (requires no obstacle checking)
  private static void moveKnight(ReturnPiece specificPiece, int newRank, Piece.PieceFile newFile) {
    // cast a piece to a specific type to call canMove method
    Piece piece = (Piece) specificPiece;

    // call canMove method to see if the move is valid
    if (piece.canMove(piece.pieceRank, piece.pieceFile, newRank, newFile, true)) {
      // piece can move, check if the new spot is empty
      boolean isNewSpotEmpty = true;
      for (ReturnPiece otherPiece : play.piecesOnBoard) {
        // check if piece is at the new spot
        if (otherPiece.pieceFile == newFile && otherPiece.pieceRank == newRank) {
          isNewSpotEmpty = false;
          break;
        }
      }

      // if spot is not empty, attempt to capture the piece
      if (!isNewSpotEmpty) {
        if (capturePiece(piece, newRank, newFile)) {
          piece.pieceRank = newRank; // move piece to new spot
          piece.pieceFile = newFile;
        }
      } else {
        // spot is empty, move piece to new spot
        piece.pieceRank = newRank;
        piece.pieceFile = newFile;
      }
    }

    // set hasMoved to true
    piece.hasMoved = true;
  }

  // method to capture a piece (must be different color)
  private static boolean capturePiece(Piece piece, int rank, Piece.PieceFile file) {
    for (ReturnPiece otherPiece : play.piecesOnBoard) {
      // check if piece is at the new spot
      if (otherPiece.pieceFile == file && otherPiece.pieceRank == rank) {
        // check if piece is different color
        if (piece.pieceType.name().charAt(0) != otherPiece.pieceType.name().charAt(0)) {
          play.piecesOnBoard.remove(otherPiece); // remove captured piece
          return true; // piece captured
        }
      }
    }
    return false; // same color; no capture
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
