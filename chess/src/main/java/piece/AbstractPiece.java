package piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Getter;
import enums.Direction;
import move.Move;
import move.Position;
import state.GameState;
import enums.Colour;
import enums.MoveType;
import enums.PieceType;

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
        boolean isCheck = gameState.isCheck(Colour.getOppositeColor(getColour()));
        AbstractPiece pieceOnTargetPosition = gameState.getSquareByPosition(targetPosition).getPiece();
        if (pieceOnTargetPosition.type != PieceType.NONE) {
            if (!moveType.equals(MoveType.MOVE_ONLY)) {
                addCaptureMove(gameState, targetPosition, pieceOnTargetPosition, moveType, isCheck);
            }
            return Position.EMPTY_POSITION;
        } else {
            movesList.add(Move.createMove(getPosition(gameState), targetPosition, this, moveType,
                    this.isPromote(targetPosition, gameState.getBoard().getRows()), false));
            targetPosition = targetPosition.transform(movingDirection);
            return targetPosition;
        }
    }

    protected boolean isPromote(Position targetPosition, int numOfRows) {
        return false;
    }

    private void addCaptureMove(GameState gameState, Position targetPosition, AbstractPiece pieceAtEnd, MoveType moveType, boolean isCheck) {
        if (pieceAtEnd.colour != colour) {
            movesList.add(Move.createMove(getPosition(gameState), targetPosition, this, moveType,
                    this.isPromote(targetPosition, gameState.getBoard().getRows()), true));
        }
    }

    public String getNotationName() {
        return type.getNotationName();
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
