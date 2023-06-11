package piece;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import androidx.annotation.NonNull;
import enums.Direction;
import move.Move;
import move.Position;
import state.GameState;
import enums.Colour;
import enums.MoveType;
import enums.PieceType;

public class Pawn extends AbstractPiece {
    @Getter
    private final Direction movingDirection;

    public Pawn(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        type = colour.equals(Colour.WHITE) ? PieceType.WHITE_PAWN : PieceType.BLACK_PAWN;
        this.value = 1;
        movingDirection = colour == Colour.WHITE ? Direction.UP : Direction.DOWN;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        movesList = new ArrayList<>();

        if (isPawnOnLastRow(gameState)) {
            return movesList;
        }

        addMoveToPosition(gameState, getPosition(gameState).transform(movingDirection), MoveType.MOVE_ONLY);

        if (isDoubleMoveLegal(gameState)) {
            addMoveToPosition(gameState, getPosition(gameState).transform(movingDirection, movingDirection), MoveType.MOVE_ONLY);
        }

        if (isCaptureRightLegal(gameState)) {
            addMoveToPosition(gameState, getPosition(gameState).transform(movingDirection, Direction.RIGHT), MoveType.CAPTURE_ONLY);
        }

        if (isCaptureLeftLegal(gameState)) {
            addMoveToPosition(gameState, getPosition(gameState).transform(movingDirection, Direction.LEFT), MoveType.CAPTURE_ONLY);
        }

        return movesList;
    }

    private boolean isPawnOnLastRow(GameState gameState) {
        return isPromote(gameState.getPositionOfPiece(this), gameState.getBoard().getRows());
    }

    private boolean isDoubleMoveLegal(GameState gameState) {
        return startingPosition.equals(gameState.getPositionOfPiece(this))
                && gameState.getPieceAtPosition(getPosition(gameState).transform(movingDirection)).getType().isNone();
    }

    private boolean isCaptureLeftLegal(GameState gameState) {
        return gameState.getPositionOfPiece(this).getColumn() > 0 &&
                !gameState.getPieceAtPosition(getPosition(gameState).transform(movingDirection, Direction.LEFT)).getType().isNone();
    }

    private boolean isCaptureRightLegal(GameState gameState) {
        return gameState.getPositionOfPiece(this).getColumn() < gameState.getBoard().getColumns() - 1 &&
                !gameState.getPieceAtPosition(getPosition(gameState).transform(movingDirection, Direction.RIGHT)).getType().isNone();
    }

    @Override
    protected boolean isPromote(Position targetPosition, int numOfRowsInBoard) {
        return getColour().equals(Colour.WHITE)
                ? targetPosition.getRow() == numOfRowsInBoard - 1
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
