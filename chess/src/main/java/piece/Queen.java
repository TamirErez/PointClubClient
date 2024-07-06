package piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import enums.Direction;
import move.Move;
import move.Position;
import state.GameState;
import enums.Colour;
import enums.PieceType;

public class Queen extends AbstractPiece {
    public Queen(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_QUEEN : PieceType.BLACK_QUEEN;
        this.value = 9;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
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

    @NonNull
    @Override
    public AbstractPiece clone() {
        return new Queen(colour, startingPosition);
    }

    @Override
    public String getNotationName() {
        return "Q";
    }
}
