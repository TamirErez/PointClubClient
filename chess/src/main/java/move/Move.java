package move;

import androidx.annotation.NonNull;
import enums.MoveType;
import enums.PieceType;
import lombok.Data;
import piece.AbstractPiece;
import piece.King;
import piece.Pawn;

@Data
public class Move {
    Position start;
    Position end;
    AbstractPiece movingPiece;
    MoveType moveType;
    boolean isCapture;
    boolean isPromotion;
    PieceType promotedPieceType;
    Position castleTarget;
    boolean isEnPassant;
    boolean isCheck;

    //TODO: Refactor this class to builder
    public static Move createMove(Position start, Position end, AbstractPiece movingPiece, MoveType moveType, boolean isPromotion, boolean isCapture) {
        return new Move(start, end, movingPiece, moveType, isCapture, isPromotion, null, null, false);
    }

    public static Move createCastleMove(Position start, Position end, King king, Position castleTarget) {
        return new Move(start, end, king, MoveType.MOVE_ONLY, false, false, null, castleTarget, false);
    }

    public static Move createEnPassantMove(Position start, Position end, Pawn pawn) {
        return new Move(start, end, pawn, MoveType.CAPTURE_ONLY, true, false, null, null, true);
    }

    @NonNull
    @Override
    public String toString() {
        return movingPiece.toString() + start + "->" + end;
    }

    public Move cloneWithPiece(AbstractPiece movingPiece) {
        return new Move(start, end, movingPiece, moveType, isCapture, isPromotion, promotedPieceType, castleTarget, isEnPassant);
    }

    public String getChessNotation() {
        String notation = "";
        if (!this.movingPiece.getType().isPawn()) {
            notation += this.movingPiece.getNotationName();
        }

        notation += this.start.toString();

        if (this.isCapture) {
            notation += 'x';
        }

        notation += this.end.toString();

        if (this.isPromotion) {
            notation += "=" + this.promotedPieceType.getNotationName();
        }

        if (this.isCheck) {
            notation += "+";
        }

        if (this.isEnPassant) {
            notation += " e.p.";
        }

        if (castleTarget != null) {
            notation = getCastleNotation();
        }

        return notation;
    }

    private String getCastleNotation() {
        int distance = castleTarget.getDistanceToPosition(start);
        return "O" + "-O".repeat(distance - 2);
    }

    private Move(Position start, Position end, AbstractPiece movingPiece, MoveType moveType, boolean isCapture, boolean isPromotion, PieceType promotedPieceType, Position castleTarget, boolean isEnPassant) {
        this.start = start;
        this.end = end;
        this.movingPiece = movingPiece;
        this.moveType = moveType;
        this.isCapture = isCapture;
        this.isPromotion = isPromotion;
        this.promotedPieceType = promotedPieceType;
        this.castleTarget = castleTarget;
        this.isEnPassant = isEnPassant;
    }
}
