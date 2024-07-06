package piece;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import androidx.annotation.NonNull;
import state.GameState;
import lombok.Setter;
import enums.Colour;
import enums.Direction;
import enums.PieceType;
import move.Move;
import move.Position;

public class Rook extends AbstractPiece {
    @Getter
    @Setter
    private boolean isMoved = false;

    public Rook(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK;
        this.value = 5;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        movesList = new ArrayList<>();

        Position upPosition = getPosition(gameState).transform(Direction.UP);
        Position downPosition = getPosition(gameState).transform(Direction.DOWN);
        Position leftPosition = getPosition(gameState).transform(Direction.LEFT);
        Position rightPosition = getPosition(gameState).transform(Direction.RIGHT);

        for (int i = 0; i < gameState.getBoard().getRows(); i++) {
            upPosition = addMoveToPosition(gameState, upPosition, Direction.UP);
            downPosition = addMoveToPosition(gameState, downPosition, Direction.DOWN);
            leftPosition = addMoveToPosition(gameState, leftPosition, Direction.LEFT);
            rightPosition = addMoveToPosition(gameState, rightPosition, Direction.RIGHT);
        }

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♖" : "♜";
    }

    @NonNull
    @Override
    public AbstractPiece clone() {
        Rook clone = new Rook(colour, startingPosition);
        clone.isMoved = isMoved;
        return clone;
    }

    @Override
    public String getNotationName() {
        return "R";
    }
}
