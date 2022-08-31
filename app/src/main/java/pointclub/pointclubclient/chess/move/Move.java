package pointclub.pointclubclient.chess.move;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

@Data
@AllArgsConstructor
public class Move {
    Position start;
    Position end;
    AbstractPiece movingPiece;
    MoveType moveType;

    @NonNull
    @Override
    public String toString() {
        return movingPiece.toString() + start + "->" + end;
    }
}
