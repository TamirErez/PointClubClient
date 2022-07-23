package pointclub.pointclubclient.chess.move;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

@Data
@AllArgsConstructor
public class Move {
    Position start;
    Position end;
    AbstractPiece movingPiece;

    @NonNull
    @Override
    public String toString() {
        return movingPiece.toString() + start + "->" + end;
    }
}
