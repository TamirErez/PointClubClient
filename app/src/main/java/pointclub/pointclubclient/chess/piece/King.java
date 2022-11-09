package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Setter;
import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class King extends AbstractPiece {
    @Setter
    private boolean hasMoved = false;

    public King(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        pieceType = colour.equals(Colour.WHITE) ? PieceType.WHITE_KING : PieceType.BLACK_KING;
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
        clone.hasMoved = hasMoved;
        return clone;
    }
}
