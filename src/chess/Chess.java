package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    if (!(other instanceof ReturnPiece otherPiece)) {
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

  // static variable for a captured piece
  static Piece capturedPiece;

  // static variable for a successful move (see Piece.movePiece())
  static boolean successfulMove = false;

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

  // more global castleFile variable
  static ReturnPiece.PieceFile castleFile;

  // static boolean for castling
  static boolean castleAttempt = false;

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

    // reset castleFile
    castleFile = null;

    // move input formatting

    ReturnPiece.PieceFile fromFile = Piece.charToEnumFile(move.toLowerCase().charAt(0));
    int fromRank = Character.getNumericValue(move.charAt(1));
    ReturnPiece.PieceFile toFile = Piece.charToEnumFile(move.toLowerCase().charAt(3));
    int toRank = Character.getNumericValue(move.charAt(4));

    // find the piece to move in the piecesOnBoard list
    Piece pieceToMove = getPiece(fromRank, fromFile);

    // check piece to move exists
    if (pieceToMove == null) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // check if the move is castling
    if (pieceToMove.getPieceType() == ReturnPiece.PieceType.WK
        || pieceToMove.getPieceType() == ReturnPiece.PieceType.BK) {
      if (fromFile == ReturnPiece.PieceFile.e
          && fromRank == 1
          && toFile == ReturnPiece.PieceFile.g) {
        castleFile = ReturnPiece.PieceFile.h;
        castleAttempt = true;
      } else if (fromFile == ReturnPiece.PieceFile.e
          && fromRank == 1
          && toFile == ReturnPiece.PieceFile.c) {
        castleFile = ReturnPiece.PieceFile.a;
        castleAttempt = true;
      } else if (fromFile == ReturnPiece.PieceFile.e
          && fromRank == 8
          && toFile == ReturnPiece.PieceFile.g) {
        castleFile = ReturnPiece.PieceFile.h;
        castleAttempt = true;
      } else if (fromFile == ReturnPiece.PieceFile.e
          && fromRank == 8
          && toFile == ReturnPiece.PieceFile.c) {
        castleFile = ReturnPiece.PieceFile.a;
        castleAttempt = true;
      }
    }

    // check if the piece to move is the same color as the current player
    if (currentPlayer == Player.white && !pieceToMove.isWhite) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    } else if (currentPlayer == Player.black && pieceToMove.isWhite) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // check if the move is legal
    if (!pieceToMove.canMove(toRank, toFile)) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      return play;
    }

    // move the piece to the new spot
    pieceToMove.movePiece(toRank, toFile);

    // check for possible check after move for current player
    if (Piece.inCheck(Chess.getKing(Chess.currentPlayer))) {
      // move the piece back to its original spot
      pieceToMove.pieceRank = fromRank;
      pieceToMove.pieceFile = fromFile;
      // add the chess class variable capturedPiece back to the board
      Piece otherPiece = Chess.getPiece(toRank, toFile);
      boolean isNewSpotEmpty =
          otherPiece == null; // check if the new spot is empty (otherPiece is null)
      if (!isNewSpotEmpty) {
        play.piecesOnBoard.add(Chess.capturedPiece);
      }
      // set successfulMove to false
      Chess.successfulMove = false;
    }

    // check if successful move is true
    if (!successfulMove) {
      play.message = ReturnPlay.Message.ILLEGAL_MOVE;
      // remove all null pieces from the piecesOnBoard arraylist
      play.piecesOnBoard.removeIf(Objects::isNull);
      return play;
    }

    // reset successfulMove
    successfulMove = false;

    // reset castleAttempt
    castleAttempt = false;

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
        if (promotion != 'N' && promotion != 'B' && promotion != 'R' && promotion != 'Q') {
          play.message = ReturnPlay.Message.ILLEGAL_MOVE;
          return play;
        }

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

      // switch the current player
      if (currentPlayer == Player.white) {
        currentPlayer = Player.black;
      } else {
        currentPlayer = Player.white;
      }

      // check if move results in check for the other player
      if (Piece.inCheck(getKing(currentPlayer))) { // player here is inversed
        play.message = ReturnPlay.Message.CHECK;

        // check if the move results in checkmate
        if (isInCheckmate(currentPlayer)) {
          if (currentPlayer == Player.white) {
            play.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
          } else {
            play.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
          }
        }
      }
    }
    // reset capturedPiece
    capturedPiece = null;

    // remove all null pieces from the piecesOnBoard arraylist
    play.piecesOnBoard.removeIf(Objects::isNull);

    return play; // return the current state of the game
  }

  // method to get a piece from the piecesOnBoard arraylist
  public static Piece getPiece(int rank, ReturnPiece.PieceFile file) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece.pieceFile == file && piece.pieceRank == rank) {
        return (Piece) piece;
      }
    }
    return null;
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

  // method to get the piecesOnBoard arraylist
  public static ArrayList<ReturnPiece> getPiecesOnBoard() {
    return play.piecesOnBoard;
  }

  // method to check if a player is in checkmate
  public static boolean isInCheckmate(Player player) {
    // if player not in check, return false
    if (!Piece.inCheck(Chess.getKing(player))) {
      return false;
    }

    // create copy of piecesOnBoard to avoid concurrent modification
    ArrayList<Piece> piecesOnBoardCopy = new ArrayList<>();
    for (ReturnPiece piece : play.piecesOnBoard) {
      if (piece != null) {
        piecesOnBoardCopy.add((Piece) piece);
      }
    }

    // iterate through all pieces on the board
    List<Piece> piecesToAddBack = new ArrayList<>();
    // set inCheckmate to true
    boolean inCheckmate = true;
    for (ReturnPiece tempPiece :
        new ArrayList<>(play.piecesOnBoard)) { // Create a copy for safe iteration
      // ignore null pieces and pieces of the same color
      // cast piece into a Piece object
      Piece piece = (Piece) tempPiece;
      // filter possible null pieces and pieces of the same color
      if (piece != null && piece.isWhite == (player == Player.white)) {
        // iterate through all possible moves for each piece
        for (ReturnPiece.PieceFile toFile : ReturnPiece.PieceFile.values()) {
          for (int toRank = MIN_RANK; toRank <= MAX_RANK; toRank++) {
            // save the piece's position to move back if needed
            int fromRank = piece.pieceRank;
            ReturnPiece.PieceFile fromFile = piece.pieceFile;

            // use entire previous play logic to check if the whole play is/isn't valid
            // if it is, then it's not checkmate (return false)
            // if it isn't, then it's checkmate (return false)

            // check if in coordinates are in bounds; break if not
            if (isSquareOnBoard(toRank, toFile)) {
              continue;
            }

            // check if the piece to move is the same color as the current player
            if (currentPlayer == Player.white && !piece.isWhite) {
              continue;
            } else if (currentPlayer == Player.black && piece.isWhite) {
              continue;
            }

            // check if the move is legal
            if (!piece.canMove(toRank, toFile)) {
              continue;
            }

            // move the piece to the new spot
            piece.movePiece(toRank, toFile);

            // check for possible check after move for current player
            if (Piece.inCheck(Chess.getKing(Chess.currentPlayer))) {
              // move the piece back to its original spot
              piece.pieceRank = fromRank;
              piece.pieceFile = fromFile;
              // add the chess class variable capturedPiece back to the board
              Piece otherPiece = Chess.getPiece(toRank, toFile);
              boolean isNewSpotEmpty =
                  otherPiece == null; // check if the new spot is empty (otherPiece is null)
              if (!isNewSpotEmpty) {
                play.piecesOnBoard.add(Chess.capturedPiece);
              }
              // set successfulMove to false if the move results in check
              Chess.successfulMove = false;
            }

            // if we were able to successfully move a piece, then it's not checkmate
            if (successfulMove) {
              inCheckmate = false;

              // move the piece back to its original spot
              piece.pieceRank = fromRank;
              piece.pieceFile = fromFile;

              // add back the capturedPiece to the board
              if (capturedPiece != null) {
                piecesToAddBack.add(capturedPiece);
              }

              // reset successfulMove
              successfulMove = false;
              break;
            }
            // reset successfulMove
            successfulMove = false;
          }
        }
      }
    }

    return inCheckmate;
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

  // method to capture a piece of opposite color
  public static void capturePiece(Piece piece) {
    // remove the piece from the piecesOnBoard arraylist
    capturedPiece = piece;
    play.piecesOnBoard.remove(piece);
  }

  // method to reset the hasJustAdvancedTwice variable for all pawns of a specific player
  private static void resetPawnHasJustAdvancedTwice(Player player) {
    for (ReturnPiece piece : play.piecesOnBoard) {
      // reset hasJustAdvancedTwice for all pawns of the opposite color
      // filter possible null pieces
      if (piece != null) {
        if (piece.pieceType == ReturnPiece.PieceType.WP && player == Player.black) {
          ((Pawn) piece).hasJustAdvancedTwice = false;
        } else if (piece.pieceType == ReturnPiece.PieceType.BP && player == Player.white) {
          ((Pawn) piece).hasJustAdvancedTwice = false;
        }
      }
    }
  }
}
