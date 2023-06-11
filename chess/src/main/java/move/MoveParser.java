package move;

import java.util.List;
import java.util.stream.Collectors;

import enums.MoveType;
import enums.PieceType;
import exception.InvalidChessNotationException;
import piece.AbstractPiece;
import piece.King;
import piece.Pawn;
import piece.Rook;
import state.GameState;

public class MoveParser {
    public static Move parseMove(String moveNotation, GameState gameState) {
        boolean isCheck = false, isPromotion = false, isCapture = false, isEnPassant = false;
        MoveType moveType = MoveType.MOVE_AND_CAPTURE;
        Move move = null;
        moveNotation = moveNotation.toLowerCase().trim();
        if (moveNotation.contains("+")) {
            isCheck = true;
        }
        if (moveNotation.contains("0-0")) {
            move = parseCastleNotation(moveNotation, gameState);
            move.setMoveType(MoveType.MOVE_ONLY);
            move.setCheck(isCheck);
            return move;
        } else if (moveNotation.contains("e.p.")) {
            isEnPassant = true;
        }
        PieceType promotedPieceType = null;
        if (moveNotation.contains("=")) {
            isPromotion = true;
            promotedPieceType = PieceType.valueOf(gameState.getCurrentPlayer().name() + "_" +
                    getPieceNameFromNotation(moveNotation));
        }
        if (moveNotation.contains("x")) {
            isCapture = true;
        }
        Position startingPosition = extractStartingPosition(moveNotation);
        AbstractPiece movingPiece = gameState.getPieceAtPosition(startingPosition);
        Position endPosition = extractEndPosition(moveNotation, startingPosition.toString());
        if (movingPiece.getType().isPawn()) {
            moveType = isCapture ? MoveType.CAPTURE_ONLY : MoveType.MOVE_ONLY;
        }
        if (isEnPassant) {
            move = Move.createEnPassantMove(startingPosition, endPosition, (Pawn) movingPiece);
        } else if (move == null) {
            move = Move.createMove(startingPosition, endPosition, movingPiece, moveType, isPromotion, isCapture);
        }
        move.setCheck(isCheck);
        move.setPromotedPieceType(promotedPieceType);
        return move;
    }

    private static Position extractEndPosition(String moveNotation, String startingPosition) {
        String moveNotationWithoutStart = moveNotation.substring(moveNotation.indexOf(startingPosition) + 2);
        if (moveNotationWithoutStart.charAt(0) == 'x') {
            return new Position(moveNotationWithoutStart.substring(1, 3));
        }
        return new Position(moveNotationWithoutStart.substring(0, 2));
    }

    private static Move parseCastleNotation(String moveNotation, GameState gameState) {
        int castleDistance = (int) moveNotation.chars().filter(value -> value == '0').count();
        Position castleTarget = getCastleTargetFromNotation(moveNotation, gameState, castleDistance);
        King king = getKingFromGamestate(gameState);
        Position endPosition = gameState.getBoard().getPositionTowardsTarget(king.getStartingPosition(), castleTarget, 2);

        return Move.createCastleMove(king.getStartingPosition(), endPosition, king, castleTarget);
    }

    private static Position getCastleTargetFromNotation(String notation, GameState gameState, int castleDistance) {
        King king = getKingFromGamestate(gameState);
        Position kingPosition = king.getStartingPosition();

        if (king.isMoved()) {
            throw new InvalidChessNotationException(notation, "Can't castle when king has already moved!");
        }

        List<Position> rookPosition = kingPosition.transformDistance(castleDistance + 1).stream()
                .filter(position -> {
                    if (!gameState.isPositionLegal(position)) return false;
                    AbstractPiece piece = gameState.getPieceAtPosition(position);
                    return piece.getType().isRook()
                            && piece.getColour() == gameState.getCurrentPlayer()
                            && !((Rook) piece).isMoved();
                }).collect(Collectors.toList());

        if (rookPosition.isEmpty()) {
            throw new InvalidChessNotationException(notation, "Can't find valid rook for castle");
        }
        return rookPosition.get(0);
    }

    private static King getKingFromGamestate(GameState gameState) {
        return (King) gameState.getBoard()
                .getPiecesOfType(PieceType.valueOf(gameState.getCurrentPlayer() + "_" + "KING"))
                .get(0);
    }

    private static String getPieceNameFromNotation(String notation) {
        char symbol = getPromotedPieceCharFromNotation(notation);
        switch (symbol) {
            case 'q':
                return "QUEEN";
            case 'n':
                return "KNIGHT";
            case 'b':
                return "BISHOP";
            case 'r':
                return "ROOK";
            default:
                throw new InvalidChessNotationException(notation);
        }
    }

    private static char getPromotedPieceCharFromNotation(String moveNotation) {
        return moveNotation.charAt(moveNotation.indexOf('=') + 1);
    }

    private static Position extractStartingPosition(String moveNotation) {
        if (Character.isAlphabetic(moveNotation.charAt(0)) && Character.isDigit(moveNotation.charAt(1))) {
            return new Position(moveNotation.substring(0, 2));
        }
        if (Character.isAlphabetic(moveNotation.charAt(1)) && Character.isDigit(moveNotation.charAt(2))) {
            return new Position(moveNotation.substring(1, 3));
        }
        throw new InvalidChessNotationException(moveNotation, "Invalid starting position");
    }
}
