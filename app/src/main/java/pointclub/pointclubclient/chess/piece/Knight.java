package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class Knight extends AbstractPiece {
    public Knight(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        pieceType = PieceType.KNIGHT;
        this.value = 3;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        movesList = new ArrayList<>();

        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.UP, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.RIGHT, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.RIGHT, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.DOWN, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.DOWN, Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.LEFT, Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.LEFT, Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.UP, Direction.LEFT));

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♘" : "♞";
    }
}
