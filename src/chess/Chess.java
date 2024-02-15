package chess;

import java.util.ArrayList;

// !Cannot modify this class
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

	/*
	! IntelliJ crying that we override this method with Piece toString() method
	! Since we can't edit this class, keep an eye out when testing and using toString()
	! This method needs to work for their tester to work, so we just gotta be careful.
	*/
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

	static ReturnPlay play; // the current state of the game
	public static ArrayList<String> moves = new ArrayList<String>(); // list of moves played

	// static final variables for the board (file, rank), which correspond to (column, row) respectively
	static final int MIN_RANK = 1;
	static final int MAX_RANK = 8;
	static final char MIN_FILE = 'a';
	static final char MAX_FILE = 'h';
	public static Board ChessBoard = new Board(MIN_FILE, MAX_FILE, MIN_RANK, MAX_RANK); // the board; will be used to place pieces and check for piece locations
	private static String[] pieceOrder = {"R", "N", "B", "Q", "K", "B", "N", "R"}; // order of pieces on the back rank

	enum Player {
		white, black
	};

	private static Player currentPlayer = Player.white; // the current player (white starts)

	public static ReturnPlay play(String move) {

		move = move.trim(); // remove leading/trailing whitespace

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
		char fromFile = move.toLowerCase().charAt(0);
		int fromRank = Character.getNumericValue(move.charAt(1));
		char toFile = move.toLowerCase().charAt(3);
		int toRank = Character.getNumericValue(move.charAt(4));

		// regular input and a draw; looks like e4 e5 draw?
		if (move.length() >= 7) {
			if (move.substring(5).equals("draw?")) {
				play.message = ReturnPlay.Message.DRAW;
				return play;
			}
		}
		
		// if coords are out of bounds, its illegal (uses Board.areCoordsInBounds)
		if (!ChessBoard.areCoordsInBounds(fromFile, fromRank) || !ChessBoard.areCoordsInBounds(toFile, toRank)){
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		// if player doesn't move at all, it's illegal
		if (fromFile == toFile && fromRank == toRank) {
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		//TODO: must implement logic to handle pawn promotion
		//moves can either look like "e4 e5" or "a7 a8 R"; queen is the default promotion if not specified
		Character pawnPromotion = null;
		//acceptable promotion rows are 1 and 8
		//logic: if move is 5 characters and pieceRank is at 1 or 8, then it's a promotion to queen
		if (move.length() == 5 && (toRank == 1 || toRank == 8)) {
			pawnPromotion = 'Q';
		} else if (move.length() == 7) {
			pawnPromotion = move.charAt(6);
		}


		// must implement logic to play the move
		// 1. find the piece on (fromFile, fromRank). if there's no piece, it's ILLEGAL_MOVE. 
		// 2. check if piece.canMove() to the location. if false, it's ILLEGAL_MOVE
		// 3. if true, piece.movePiece(file, rank, Piece[][] board). 
		// then after that we can handle pawn promotion and draw (they might happen at the same time too)
		// does this sound good?
		// we should also handle check and checkmate after playing the move.

		// play the move (WIP)
		Piece piece = ChessBoard.getPiece(fromRank, fromFile);

		if (piece == null) {
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		//TODO: most implement logic for canMove and movePiece
		if (piece.canMove(fromRank, fromFile, toRank, toFile)){
			piece.movePiece(toRank, toFile);
		}

		// must implement logic to handle draw. (we should handle draw after playing the move).
		if (move.endsWith("draw?")) {
			// handle the move first per assignment requirements
			play.message = ReturnPlay.Message.DRAW;
			return play;
		}

		
		// all moves are legal, so make sure we don't output an illegal move message
		play.message = null;
		//set the current player to the other player
		if (currentPlayer == chess.Chess.Player.white) {
			currentPlayer = chess.Chess.Player.black;
		} else {
			currentPlayer = chess.Chess.Player.white;
		}
		return play;
	}

	// creates a new piece based on the piece type
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

	// adds a piece to the board
	private static void addPieceToBoard(ReturnPlay play, String pieceType, ReturnPiece.PieceFile file, int rank, boolean isWhite) {
		ReturnPiece newPiece = createNewPiece(pieceType, isWhite);
		newPiece.pieceFile = file; //map to file enum
		newPiece.pieceRank = rank; //map to rank enum
		play.piecesOnBoard.add(newPiece);
		ChessBoard.placePiece(rank, file.name().charAt(0), (Piece) newPiece);
		// ? for the above line, we need to cast to Piece because the board is a 2D array of Pieces, not ReturnPieces. Just so we can remain consistent with our thought process.
	}	

	// sets up the specified team
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

		// Set white player to start the game (useful for resets/draws)
		currentPlayer = chess.Chess.Player.white;

		// list of pieces on the board
		play.piecesOnBoard = new ArrayList<ReturnPiece>();
		ChessBoard.clear();

		// Clear the list of moves and reset message
		moves.clear();
		play.message = null;

		setupTeam(play, pieceOrder, true); // setup white's pawns + back rank
		setupTeam(play, pieceOrder, false); // setup black's pawns + back rank

	}
}
