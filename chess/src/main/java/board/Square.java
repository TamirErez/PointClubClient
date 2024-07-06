package board;

import androidx.annotation.NonNull;
import lombok.Data;
import piece.EmptyPiece;
import enums.Colour;
import enums.PieceType;
import piece.AbstractPiece;

@Data
public class Square {
    private AbstractPiece piece;
    private Colour colour;

    public Square(Colour colour) {
        this(EmptyPiece.getInstance(), colour);
    }

    public Square(AbstractPiece piece, Colour colour) {
        this.piece = piece;
        this.colour = colour;
    }

    protected AbstractPiece removePiece() {
        AbstractPiece currentPiece = this.piece;
        piece = EmptyPiece.getInstance();
        return currentPiece;
    }

    @NonNull
    @Override
    public String toString() {
        return piece.getType().equals(PieceType.NONE) ? getAsciiName() : piece.getAsciiName();
    }

    public String getAsciiName() {
        return colour == Colour.BLACK ? "⬜" : "⏹";
    }
}