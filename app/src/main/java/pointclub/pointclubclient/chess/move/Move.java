package pointclub.pointclubclient.chess.move;

import androidx.annotation.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.piece.AbstractPiece;
import pointclub.pointclubclient.chess.piece.King;
import pointclub.pointclubclient.chess.piece.Pawn;

@Data
@AllArgsConstructor
public class Move {
    Position start;
    Position end;
    AbstractPiece movingPiece;
    MoveType moveType;
    boolean isPromotion;
    AbstractPiece promotedPiece;
    Position castleTarget;
    boolean isEnPassant;

    public static Move promotionMove(Position start, Position end, AbstractPiece movingPiece, MoveType moveType, boolean isPromotion) {
        return new Move(start, end, movingPiece, moveType, isPromotion, null, null, false);
    }

    public static Move castleMove(Position start, Position end, King king, Position castleTarget) {
        return new Move(start, end, king, MoveType.MOVE_ONLY, false, null, castleTarget, false);
    }

    public static Move enPassantMove(Position start, Position end, Pawn pawn) {
        return new Move(start, end, pawn, MoveType.CAPTURE_ONLY, false, null, null, true);
    }

    @NonNull
    @Override
    public String toString() {
        return movingPiece.toString() + start + "->" + end;
    }

    public Move cloneWithPiece(AbstractPiece movingPiece) {
        return new Move(start, end, movingPiece, moveType, isPromotion, promotedPiece, castleTarget, isEnPassant);
    }
}
