package pointclub.pointclubclient.chess.piece;

import java.util.List;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class EmptyPiece extends AbstractPiece {
    public EmptyPiece(Colour colour) {
        super(colour, Position.EMPTY_POSITION);
        this.pieceType = PieceType.NONE;
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
}
