package pointclub.pointclubclient.chess.board;

import androidx.annotation.NonNull;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.piece.AbstractPiece;
import pointclub.pointclubclient.chess.piece.EmptyPiece;

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
        return piece.getPieceType().equals(PieceType.NONE) ? getAsciiName() : piece.getAsciiName();
    }

    public String getAsciiName() {
        return colour == Colour.BLACK ? "⬜" : "⏹";
    }
}