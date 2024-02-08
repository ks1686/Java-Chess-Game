package chess;

import java.util.ArrayList;

// wait why do we need these imports here, isn't ReturnPiece in this class
import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

class ReturnPiece {
	// im thinking we can change this to 
	/* 
	 * 
	static enum PieceType {
		R, N, B, Q, K, B, N, R;
	};

	because we already have the isWhite flag, so specificing WP, WR or BP BR is redundant
	it would also make setting up the pieces easier, since we can just iterate through 
	PieceType.values() for each team

	but im unsure whether or not we might need the original PieceType below for something else?
	 */
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

class ReturnPlay {
	enum Message {
		ILLEGAL_MOVE, DRAW,
		RESIGN_BLACK_WINS, RESIGN_WHITE_WINS,
		CHECK, CHECKMATE_BLACK_WINS, CHECKMATE_WHITE_WINS,
		STALEMATE
	};

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
	}

	private static Player currentPlayer = chess.Chess.Player.white; // current player

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */

		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */

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
		move = move.trim();
		String fromFile = move.substring(0, 1);
		int fromRank = Integer.parseInt(move.substring(1, 2));
		String toFile = move.substring(3, 4);
		int toRank = Integer.parseInt(move.substring(4, 5));

		// Check if player even made a move
		if (fromFile.equals(toFile) && fromRank == toRank) {
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		// Check if player is picking a valid position on the board (not out of bounds)
		if (fromFile.charAt(0) < 'a' || fromFile.charAt(0) > 'h' || toFile.charAt(0) < 'a' || toFile.charAt(0) > 'h'
				|| fromRank < 1 || fromRank > 8 || toRank < 1 || toRank > 8) {
			play.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return play;
		}

		play = null;
		return play;
	}

	/* if we change enum PieceType, we can use this to simplify creating pieces
	 * private ReturnPiece createNewPiece(boolean isWhite, PieceType pieceType){
		switch (pieceType) {
			case ReturnPiece.PieceType.R: return new Rook(isWhite);
			case ReturnPiece.PieceType.N: return new Knight(isWhite);
			case ReturnPiece.PieceType.B: return new Bishop(isWhite);
			case ReturnPiece.PieceType.Q: return new Queen(isWhite);
			case ReturnPiece.PieceType.K: return new King(isWhite);
		}
	}
	 */

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

		/*
		 * Add all pieces to the board
		 * Some pieces have repeats, numbers go from left to right (follow the file
		 * logic)
		 * I'm going to be honest, there's gotta be a better way to do this but I can't
		 * figure it out.
		 * Loops maybe?
		 */
		

		/*if we change PieceType
			ReturnPiece.PieceType[] piece_order = ReturnPiece.PieceType.values();
			PieceFile[] piece_files = ReturnPiece.PieceFile.values();
			then for PieceType in piece_order, createNewPiece(), set piecerank and file, 
			then play.PiecesOnBoard.add(piece)
		*/
		
		// ? -- White --
		// A1
		ReturnPiece WR1 = new Rook(true);
		WR1.pieceFile = ReturnPiece.PieceFile.a;
		WR1.pieceRank = 1;
		play.piecesOnBoard.add(WR1);
		// B1
		ReturnPiece WN1 = new Knight(true);
		WN1.pieceFile = ReturnPiece.PieceFile.b;
		WN1.pieceRank = 1;
		play.piecesOnBoard.add(WN1);
		// C1
		ReturnPiece WB1 = new Bishop(true);
		WB1.pieceFile = ReturnPiece.PieceFile.c;
		WB1.pieceRank = 1;
		play.piecesOnBoard.add(WB1);
		// D1
		ReturnPiece WQ = new Queen(true);
		WQ.pieceFile = ReturnPiece.PieceFile.d;
		WQ.pieceRank = 1;
		play.piecesOnBoard.add(WQ);
		// E1
		ReturnPiece WK = new King(true);
		WK.pieceFile = ReturnPiece.PieceFile.e;
		WK.pieceRank = 1;
		play.piecesOnBoard.add(WK);
		// F1
		ReturnPiece WB2 = new Bishop(true);
		WB2.pieceFile = ReturnPiece.PieceFile.f;
		WB2.pieceRank = 1;
		play.piecesOnBoard.add(WB2);
		// G1
		ReturnPiece WN2 = new Knight(true);
		WN2.pieceFile = ReturnPiece.PieceFile.g;
		WN2.pieceRank = 1;
		play.piecesOnBoard.add(WN2);
		// H1
		ReturnPiece WR2 = new Rook(true);
		WR2.pieceFile = ReturnPiece.PieceFile.h;
		WR2.pieceRank = 1;
		play.piecesOnBoard.add(WR2);
		
		// white pawns
		for (PieceFile file : PieceFile.values()) {
			ReturnPiece bp = new Pawn(false);
			bp.pieceFile = file;
			bp.pieceRank = 2;
			play.piecesOnBoard.add(bp);
		}
		
		// ? -- Black --
		// A8
		ReturnPiece BR1 = new Rook(false);
		BR1.pieceFile = ReturnPiece.PieceFile.a;
		BR1.pieceRank = 8;
		play.piecesOnBoard.add(BR1);
		// B8
		ReturnPiece BN1 = new Knight(false);
		BN1.pieceFile = ReturnPiece.PieceFile.b;
		BN1.pieceRank = 8;
		play.piecesOnBoard.add(BN1);
		// C8
		ReturnPiece BB1 = new Bishop(false);
		BB1.pieceFile = ReturnPiece.PieceFile.c;
		BB1.pieceRank = 8;
		play.piecesOnBoard.add(BB1);
		// D8
		ReturnPiece BQ = new Queen(false);
		BQ.pieceFile = ReturnPiece.PieceFile.d;
		BQ.pieceRank = 8;
		play.piecesOnBoard.add(BQ);
		// E8
		ReturnPiece BK = new King(false);
		BK.pieceFile = ReturnPiece.PieceFile.e;
		BK.pieceRank = 8;
		play.piecesOnBoard.add(BK);
		// F8
		ReturnPiece BB2 = new Bishop(false);
		BB2.pieceFile = ReturnPiece.PieceFile.f;
		BB2.pieceRank = 8;
		play.piecesOnBoard.add(BB2);
		// G8
		ReturnPiece BN2 = new Knight(false);
		BN2.pieceFile = ReturnPiece.PieceFile.g;
		BN2.pieceRank = 8;
		play.piecesOnBoard.add(BN2);
		// H8
		ReturnPiece BR2 = new Rook(false);
		BR2.pieceFile = ReturnPiece.PieceFile.h;
		BR2.pieceRank = 8;
		play.piecesOnBoard.add(BR2);

		// black pawns
		for (PieceFile file : PieceFile.values()) {
			ReturnPiece bp = new Pawn(false);
			bp.pieceFile = file;
			bp.pieceRank = 7;
			play.piecesOnBoard.add(bp);
		}

	}
}
