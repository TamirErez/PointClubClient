package piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.Setter;
import enums.Direction;
import move.Move;
import move.Position;
import enums.Colour;
import enums.PieceType;
import state.GameState;

public class King extends AbstractPiece {
    @Getter
    @Setter
    private boolean isMoved = false;

    public King(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_KING : PieceType.BLACK_KING;
        this.value = 0;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
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

    @NonNull
    @Override
    public AbstractPiece clone() {
        King clone = new King(colour, startingPosition);
        clone.isMoved = isMoved;
        return clone;
    }

    @Override
    public String getNotationName() {
        return "K";
    }
}
