package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.chess.game.state.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class Bishop extends AbstractPiece {
    public Bishop(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_BISHOP : PieceType.BLACK_BISHOP;
        this.value = 3;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        movesList = new ArrayList<>();

        Position downRightPosition = getPosition(gameState).transform(Direction.DOWN, Direction.RIGHT);
        Position downLeftPosition = getPosition(gameState).transform(Direction.DOWN, Direction.LEFT);
        Position upRightPosition = getPosition(gameState).transform(Direction.UP, Direction.RIGHT);
        Position upLeftPosition = getPosition(gameState).transform(Direction.UP, Direction.LEFT);

        for (int i = 0; i < gameState.getBoard().getRows(); i++) {
            downRightPosition = addMoveToPosition(gameState, downRightPosition, Direction.DOWN, Direction.RIGHT);
            downLeftPosition = addMoveToPosition(gameState, downLeftPosition, Direction.DOWN, Direction.LEFT);
            upRightPosition = addMoveToPosition(gameState, upRightPosition, Direction.UP, Direction.RIGHT);
            upLeftPosition = addMoveToPosition(gameState, upLeftPosition, Direction.UP, Direction.LEFT);
        }

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♗" : "♝";
    }

    @NonNull
    @Override
    public AbstractPiece clone() {
        return new Bishop(colour, startingPosition);
    }

    @Override
    public String getNotationName() {
        return "B";
    }
}
