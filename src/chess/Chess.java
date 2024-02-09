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
		String fromFile = move.substring(0, 1);
		int fromRank = Integer.parseInt(move.substring(1, 2));
		String toFile = move.substring(3, 4);
		int toRank = Integer.parseInt(move.substring(4, 5));
		
		// must implement logic to handle pawn promotion
		if (move.length() >= 7) { // move looks like "e4 e5 N"
			String pawnPromotion = move.substring(7,8);
		} else {
			String pawnPromotion = null;
		}

		// must implement logic to handle draw
		boolean draw = false;
		if (move.endsWith("draw?")) draw = true;

		



		// Check if player is picking a valid position on the board (not out of bounds)
		if (fromFile.charAt(0) < 'a' || fromFile.charAt(0) > 'h' || toFile.charAt(0) < 'a' || toFile.charAt(0) > 'h'
				|| fromRank < 1 || fromRank > 8 || toRank < 1 || toRank > 8) {
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
		ReturnPiece.PieceFile[] fileOrder = ReturnPiece.PieceFile.values();
		int backrank;
		int pawnRank;

		if (isWhite) { 
			backrank = 1;
			pawnRank = 2;
		} else { 
			backrank = 8; 
			pawnRank = 7;
		}
		
		// setup pawns
		for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
			addPieceToBoard(play, "P", file, pawnRank, isWhite);
		}

		// setup back rank
		for (int i = 0; i < pieceOrder.length; i++) {
			addPieceToBoard(play, pieceOrder[i], ReturnPiece.PieceFile.values()[i], backrank, isWhite);
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
