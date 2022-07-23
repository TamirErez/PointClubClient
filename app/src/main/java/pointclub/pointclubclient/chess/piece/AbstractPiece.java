package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Getter;
import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public abstract class AbstractPiece {
    @Getter
    PieceType pieceType;
    @Getter
    Position startingPosition;
    int value;
    Colour colour;
    List<Move> movesList = new ArrayList<>();

    public AbstractPiece(Colour colour, Position startingPosition) {
        this.startingPosition = startingPosition;
        this.colour = colour;
    }

    public abstract List<Move> getPossibleMoves();

    public abstract String getAsciiName();

    protected Position getPosition(GameState gameState) {
        return gameState.getPositionOfPiece(this);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", colour, pieceType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPiece piece = (AbstractPiece) o;
        return startingPosition.equals(piece.startingPosition);
    }
}
