package pointclub.pointclubclient.chess.rules;

import java.util.List;

import pointclub.pointclubclient.chess.game.state.GameState;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

interface ChessRules {
    List<Move> filterMovesForPiece(AbstractPiece piece, GameState gameState);
}
