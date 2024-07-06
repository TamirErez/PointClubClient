package rules;

import java.util.List;

import move.Move;
import state.GameState;
import piece.AbstractPiece;

interface ChessRules {
    List<Move> filterMovesForPiece(AbstractPiece piece, GameState gameState);
}
