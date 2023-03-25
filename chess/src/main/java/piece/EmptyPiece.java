package piece;

import java.util.List;

import androidx.annotation.NonNull;
import state.GameState;
import enums.Colour;
import enums.PieceType;
import move.Move;
import move.Position;

public class EmptyPiece extends AbstractPiece {
    private static EmptyPiece instance;

    public static EmptyPiece getInstance() {
        if (instance == null) {
            instance = new EmptyPiece(Colour.WHITE);
        }
        return instance;
    }

    private EmptyPiece(Colour colour) {
        super(colour, Position.EMPTY_POSITION);
        this.type = PieceType.NONE;
        this.value = 0;
    }

    @Override
    public List<Move> getPossibleMoves(GameState gameState) {
        return List.of();
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.BLACK ? "⬜" : "⏹";
    }

    @NonNull
    @Override
    public AbstractPiece clone() {
        return this;
    }

    @Override
    public String getNotationName() {
        return null;
    }
}