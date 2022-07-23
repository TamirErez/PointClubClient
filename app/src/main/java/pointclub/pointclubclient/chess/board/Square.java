package pointclub.pointclubclient.chess.board;

import lombok.Data;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.piece.AbstractPiece;
import pointclub.pointclubclient.chess.piece.EmptyPiece;

@Data
public class Square {
    private AbstractPiece piece;
    private Colour colour;

    public Square(Colour colour) {
        this(new EmptyPiece(colour), colour);
    }

    public Square(AbstractPiece piece, Colour colour) {
        this.piece = piece;
        this.colour = colour;
    }
}