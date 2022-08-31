package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class Queen extends AbstractPiece {
    public Queen(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        pieceType = PieceType.QUEEN;
        this.value = 9;
    }

    @Override
    public List<Move> getThreateningMoves(GameState gameState) {
        movesList = new ArrayList<>();

        Position downRightPosition = getPosition(gameState).transform(Direction.DOWN, Direction.RIGHT);
        Position downLeftPosition = getPosition(gameState).transform(Direction.DOWN, Direction.LEFT);
        Position upRightPosition = getPosition(gameState).transform(Direction.UP, Direction.RIGHT);
        Position upLeftPosition = getPosition(gameState).transform(Direction.UP, Direction.LEFT);

        Position upPosition = getPosition(gameState).transform(Direction.UP);
        Position downPosition = getPosition(gameState).transform(Direction.DOWN);
        Position leftPosition = getPosition(gameState).transform(Direction.LEFT);
        Position rightPosition = getPosition(gameState).transform(Direction.RIGHT);

        for (int i = 0; i < gameState.getBoard().getRows(); i++) {
            downRightPosition = addMoveToPosition(gameState, downRightPosition, Direction.DOWN, Direction.RIGHT);
            downLeftPosition = addMoveToPosition(gameState, downLeftPosition, Direction.DOWN, Direction.LEFT);
            upRightPosition = addMoveToPosition(gameState, upRightPosition, Direction.UP, Direction.RIGHT);
            upLeftPosition = addMoveToPosition(gameState, upLeftPosition, Direction.UP, Direction.LEFT);

            upPosition = addMoveToPosition(gameState, upPosition, Direction.UP);
            downPosition = addMoveToPosition(gameState, downPosition, Direction.DOWN);
            leftPosition = addMoveToPosition(gameState, leftPosition, Direction.LEFT);
            rightPosition = addMoveToPosition(gameState, rightPosition, Direction.RIGHT);
        }

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♕" : "♛";
    }
}
