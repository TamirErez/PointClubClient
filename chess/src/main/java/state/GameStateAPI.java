package state;

import java.util.List;

import board.Square;
import enums.Colour;
import enums.PieceType;
import move.Move;
import move.Position;
import piece.AbstractPiece;

interface GameStateAPI {

    GameState simulateMove(Move move);

    List<Move> getPieceThreateningMoves(AbstractPiece piece);

    void move(Move move);

    void move(Move move, PieceType promotedPiece);

    List<Move> getLegalMovesOfPiece(AbstractPiece piece);

    boolean isPositionLegal(Position position);

    Square getSquareByPosition(Position position);

    Position getPositionOfPiece(AbstractPiece piece);

    AbstractPiece getPieceAtPosition(Position position);

    boolean isCheck(Colour player);

    void switchPlayer();
}
