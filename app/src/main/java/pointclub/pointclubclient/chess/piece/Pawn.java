package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.chess.game.state.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class Pawn extends AbstractPiece {
    public Pawn(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN;
        this.value = 1;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        movesList = new ArrayList<>();

        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP), MoveType.MOVE_ONLY);
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.RIGHT), MoveType.CAPTURE_ONLY);
        addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.LEFT), MoveType.CAPTURE_ONLY);
        if (startingPosition.equals(gameState.getPositionOfPiece(this))) {
            addMoveToPosition(gameState, getPosition(gameState).transform(Direction.UP, Direction.UP), MoveType.MOVE_ONLY);
        }

        return movesList;
    }

    @Override
    protected boolean isPromote(Position targetPosition, int numOfRows) {
        return getColour().equals(Colour.WHITE)
                ? targetPosition.getRow() == numOfRows - 1
                : targetPosition.getRow() == 0;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♙" : "♟";
    }

    @NonNull
    @Override
    public AbstractPiece clone() {
        return new Pawn(this.colour, startingPosition);
    }
}
