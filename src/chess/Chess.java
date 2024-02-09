package chess;

import java.util.ArrayList;


//!Cannot modify this class
class ReturnPiece {
	static enum PieceType {
		WP, WR, WN, WB, WQ, WK,
		BP, BR, BN, BB, BK, BQ
	};

	static enum PieceFile {
		a, b, c, d, e, f, g, h
	};

	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank; // 1..8

	public String toString() {
		return "" + pieceFile + pieceRank + ":" + pieceType;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece) other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

// !Cannot modify this class
class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW,
		RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
		CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS,
		STALEMATE};

	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {

	static ReturnPlay play; // the result of the last play
	public static ArrayList<String> moves = new ArrayList<String>(); // list of moves

	static final int MIN_RANK = 1;
	static final int MAX_RANK = 8;
	static final char MIN_FILE = 'a';
	static final char MAX_FILE = 'h';
	public static Board ChessBoard = new Board(MIN_FILE, MAX_FILE, MIN_RANK, MAX_RANK);
	
	// TODO: May move these to King class
	static boolean white_check; // true if white is in check
	static boolean black_check; // true if black is in check

	enum Player {
		white, black
	};

	private static Player currentPlayer = chess.Chess.Player.white; // current player

	public static ReturnPlay play(String move) {

		move = move.trim(); // remove trailing and leading whitespace

		// resign resets entire game
		if (move.equals("resign")) {
			if (currentPlayer == chess.Chess.Player.white) {
				play.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
				return play;
			} else if (currentPlayer == chess.Chess.Player.black) {
				play.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
				return play;
			}
		}

		// move input formatting
		char fromFile = move.charAt(0);
		int fromRank = Character.getNumericValue(move.charAt(1));
		char toFile = move.charAt(3);
		int toRank = Character.getNumericValue(move.charAt(4));
		
		// must implement logic to handle pawn promotion
		Character pawnPromotion = null;
		if (move.length() >= 7) { pawnPromotion = move.charAt(6); } // move looks like "e4 e5 Q"

		// must implement logic to play the move

		// must implement logic to handle draw. (we should handle draw after playing the move).
		boolean draw = false;
		if (move.endsWith("draw?")) draw = true;

		// Check if player is picking a valid position on the board (not out of bounds)
		// idea: maybe instead of hardcoding the min and max ranks/files, we can put them in the board class.
		// and then maybe we can have a method in the board class, that takes a given coordinate and returns true if it's within the bounds of the board
		if (!ChessBoard.areCoordsInBounds(fromFile, fromRank) || !ChessBoard.areCoordsInBounds(toFile, toRank)){
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		return play;
	}

	private static ReturnPiece createNewPiece(String pieceType, boolean isWhite){
		switch (pieceType) {
			case "P": return new Pawn(isWhite);
			case "R": return new Rook(isWhite);
			case "N": return new Knight(isWhite);
			case "B": return new Bishop(isWhite);
			case "Q": return new Queen(isWhite);
			case "K": return new King(isWhite);
			default: throw new IllegalArgumentException("Invalid piece type: " + pieceType);

		}
	}

	private static void addPieceToBoard(ReturnPlay play, String pieceType, ReturnPiece.PieceFile file, int rank, boolean isWhite) {
		ReturnPiece newPiece = createNewPiece(pieceType, isWhite);
		newPiece.pieceFile = file;
		newPiece.pieceRank = rank;
		play.piecesOnBoard.add(newPiece);
	}	

	private static void setupTeam(ReturnPlay play, String[] pieceOrder, boolean isWhite) {
		int backRank;
		int pawnRank;
		if (isWhite) { 
			backRank = 1;
			pawnRank = 2;
		} else { 
			backRank = 8; 
			pawnRank = 7;
		}
		
		// setup pawns
		for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
			addPieceToBoard(play, "P", file, pawnRank, isWhite);
		}

		// setup back rank
		for (int i = 0; i < pieceOrder.length; i++) {
			addPieceToBoard(play, pieceOrder[i], ReturnPiece.PieceFile.values()[i], backRank, isWhite);
		}
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {

		// Create a new ReturnPlay object
		play = new ReturnPlay();

		// Set white player to start
		currentPlayer = chess.Chess.Player.white;

		// May remove this later; maybe move check to King class?
		white_check = false; // white is not in check at start
		black_check = false; // black is not in check at start

		// list of pieces on the board
		play.piecesOnBoard = new ArrayList<ReturnPiece>();

		// Clear the list of moves and reset message
		moves.clear();
		play.message = null;

		String[] pieceOrder = {"R", "N", "B", "Q", "K", "B", "N", "R"}; // we can probably put this somewhere else 
		setupTeam(play, pieceOrder, true); // setup white's pawns + back rank
		setupTeam(play, pieceOrder, false); // setup black's pawns + back rank

	}
}
