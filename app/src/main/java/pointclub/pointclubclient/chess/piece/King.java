package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class King extends AbstractPiece {
    private boolean hasMoved = false;

    public King(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        this.pieceType = PieceType.KING;
        this.value = 0;
    }

    @Override
    public List<Move> getThreateningMoves(GameState gameState) {
        movesList = new ArrayList<>();

        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN, Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.RIGHT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.DOWN));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.LEFT));
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.RIGHT));

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♔" : "♚";
    }
}
