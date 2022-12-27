package pointclub.pointclubclient.chess.game.state;

import java.util.List;

import pointclub.pointclubclient.chess.board.Square;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

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
}
