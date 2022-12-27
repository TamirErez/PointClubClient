package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Getter;
import pointclub.pointclubclient.chess.game.state.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public abstract class AbstractPiece {
    @Getter
    PieceType type;
    @Getter
    Position startingPosition;
    int value;
    @Getter
    Colour colour;
    List<Move> movesList = new ArrayList<>();

    public AbstractPiece(Colour colour, Position startingPosition) {
        this.startingPosition = startingPosition;
        this.colour = colour;
    }

    public abstract List<Move> getPossibleMoves(GameState gameState);

    public abstract String getAsciiName();

    @NonNull
    @Override
    public abstract AbstractPiece clone();

    protected Position getPosition(GameState gameState) {
        return gameState.getPositionOfPiece(this);
    }

    /**
     * @return Position that continues the movement in the given direction
     */
    Position addMoveToPosition(GameState gameState, Position targetPosition, Direction... movingDirection) {
        return addMoveToPosition(gameState, targetPosition, MoveType.MOVE_AND_CAPTURE, movingDirection);
    }

    /**
     * @return Position that continues the movement in the given direction
     */
    Position addMoveToPosition(GameState gameState, Position targetPosition, MoveType moveType, Direction... movingDirection) {
        if (targetPosition == null || !gameState.isPositionLegal(targetPosition)) {
            return Position.EMPTY_POSITION;
        }

        AbstractPiece pieceOnTargetPosition = gameState.getSquareByPosition(targetPosition).getPiece();
        if (pieceOnTargetPosition.type != PieceType.NONE) {
            return addCaptureMove(gameState, targetPosition, pieceOnTargetPosition, moveType);
        } else {
            movesList.add(new Move(getPosition(gameState), targetPosition, this, moveType,
                    this.isPromote(targetPosition, gameState.getBoard().getRows())
            ));
            targetPosition = targetPosition.transform(movingDirection);
            return targetPosition;
        }
    }

    protected boolean isPromote(Position targetPosition, int numOfRows) {
        return false;
    }

    private Position addCaptureMove(GameState gameState, Position targetPosition, AbstractPiece piece, MoveType moveType) {
        if (piece.colour != colour) {
            movesList.add(new Move(getPosition(gameState), targetPosition, this, moveType,
                    this.isPromote(targetPosition, gameState.getBoard().getRows())));
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", colour, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPiece piece = (AbstractPiece) o;
        return startingPosition.equals(piece.startingPosition);
    }
}
