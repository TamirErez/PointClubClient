package pointclub.pointclubclient.chess.piece;

import java.util.ArrayList;
import java.util.List;

import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;

public class Rook extends AbstractPiece {
    public Rook(Colour colour, Position startingPosition) {
        super(colour, startingPosition);
        this.pieceType = PieceType.ROOK;
        this.value = 5;
    }

    @Override
    public List<Move> getPossibleMoves() {
        movesList = new ArrayList<>();

        return movesList;
    }

    @Override
    public String getAsciiName() {
        return colour == Colour.WHITE ? "♖" : "♜";
    }
}
