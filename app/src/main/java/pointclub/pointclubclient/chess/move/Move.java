package pointclub.pointclubclient.chess.move;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pointclub.pointclubclient.chess.piece.Piece;

@Data
@AllArgsConstructor
public class Move {
    Position start;
    Position end;
    Piece movingPiece;

    @NonNull
    @Override
    public String toString() {
        return movingPiece.toString() + start + "->" + end;
    }
}
