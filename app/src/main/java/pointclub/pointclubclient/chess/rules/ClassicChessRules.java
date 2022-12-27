package pointclub.pointclubclient.chess.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.Direction;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.game.state.GameState;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.chess.piece.AbstractPiece;
import pointclub.pointclubclient.chess.piece.King;
import pointclub.pointclubclient.chess.piece.Pawn;
import pointclub.pointclubclient.chess.piece.Rook;

public class ClassicChessRules implements ChessRules {

    @Override
    public List<Move> filterMovesForPiece(AbstractPiece piece, GameState gameState) {
        List<Move> legalMoves = piece.getPossibleMoves(gameState);

        if (piece.getType().isPawn()) {
            legalMoves = checkEnPassantMoves((Pawn) piece, gameState);
        } else if (piece.getType().isKing()) {
            legalMoves = checkCastleMoves((King) piece, gameState);
        }
        return legalMoves.stream()
                .filter(move -> !gameState.simulateMove(move).isCheck(piece.getColour()))
                .collect(Collectors.toList());
    }

    private List<Move> checkCastleMoves(King king, GameState gameState) {
        List<Move> kingPossibleMoves = king.getPossibleMoves(gameState);
        if (king.isMoved() || gameState.isCheck(king.getColour())) return kingPossibleMoves;

        PieceType rookType = king.getColour() == Colour.WHITE ? PieceType.WHITE_ROOK : PieceType.BLACK_ROOK;
        kingPossibleMoves.addAll(getCastleMoves(king, gameState, rookType));
        return kingPossibleMoves;
    }

    @NonNull
    private List<Move> getCastleMoves(King king, GameState gameState, PieceType rookType) {
        Position kingPosition = gameState.getPositionOfPiece(king);

        return gameState.getBoard()
                .getPiecesOfType(rookType).stream()
                .filter(rook -> !((Rook) rook).isMoved())
                .filter(rook -> isClearLineBetweenPositions(gameState.getPositionOfPiece(rook), kingPosition, gameState))
                .filter(rook -> !isKingMovesThreatened(kingPosition, gameState.getPositionOfPiece(rook), gameState, king.getColour()))
                .map(gameState::getPositionOfPiece)
                .map(rookPosition -> Move.castleMove(kingPosition, gameState.getBoard().getPositionTowardsTarget(kingPosition, rookPosition, 2), king, rookPosition))
                .collect(Collectors.toList());
    }

    private boolean isKingMovesThreatened(Position kingPosition, Position targetPosition, GameState gameState, Colour kingColour) {
        List<Position> threatenedPositions = (kingColour == Colour.WHITE ? gameState.getBlackThreats() : gameState.getWhiteThreats())
                .stream().map(Move::getEnd).collect(Collectors.toList());

        return getLineBetweenPositions(kingPosition,
                gameState.getBoard().getPositionTowardsTarget(kingPosition, targetPosition, 2))
                .stream().anyMatch(threatenedPositions::contains);
    }

    private List<Position> getLineBetweenPositions(Position firstPosition, Position secondPosition) {
        List<Position> line = new ArrayList<>();
        int firstPositionColumn = firstPosition.getColumn(), firstPositionRow = firstPosition.getRow(),
                secondPositionColumn = secondPosition.getColumn(), secondPositionRow = secondPosition.getRow();

        if (firstPositionRow == secondPositionRow) {
            for (int col = Math.min(firstPositionColumn, secondPositionColumn) + 1;
                 col < Math.max(firstPositionColumn, secondPositionColumn);
                 col++) {
                line.add(new Position(firstPositionRow, col));
            }
        } else if (firstPositionColumn == secondPositionColumn) {
            for (int row = Math.min(firstPositionRow, secondPositionRow) + 1;
                 row < Math.max(firstPositionRow, secondPositionRow);
                 row++) {
                line.add(new Position(row, firstPositionColumn));
            }
        }
        return line;
    }

    private boolean isClearLineBetweenPositions(Position firstPosition, Position secondPosition, GameState gameState) {
        return getLineBetweenPositions(firstPosition, secondPosition).stream()
                .noneMatch(position -> doesPositionHavePiece(gameState, position));
    }

    private boolean doesPositionHavePiece(GameState gameState, Position position) {
        return !gameState.getPieceAtPosition(position).getType().isNone();
    }

    private List<Move> checkEnPassantMoves(Pawn pawn, GameState gameState) {
        List<Move> pawnMoves = pawn.getPossibleMoves(gameState);
        PieceType oppositePawn = pawn.getColour() == Colour.WHITE ? PieceType.BLACK_PAWN : PieceType.WHITE_PAWN;
        Position pawnPosition = gameState.getPositionOfPiece(pawn);
        Move lastMove = gameState.getMoves().peekLast();
        if(lastMove == null) return pawnMoves;

        if (isLastMoveOpponentPawn(oppositePawn, pawnPosition, lastMove)) {
            if (pawnPosition.transform(Direction.LEFT).equals(lastMove.getEnd())) {
                pawnMoves.add(Move.enPassantMove(pawnPosition, pawnPosition.transform(pawn.getMovingDirection(), Direction.LEFT), pawn));
            }
            if (pawnPosition.transform(Direction.RIGHT).equals(lastMove.getEnd())) {
                pawnMoves.add(Move.enPassantMove(pawnPosition, pawnPosition.transform(pawn.getMovingDirection(), Direction.RIGHT), pawn));
            }
        }
        return pawnMoves;
    }

    private boolean isLastMoveOpponentPawn(PieceType oppositePawn, Position pawnPosition, Move lastMove) {
        return lastMove.getMovingPiece().getType() == oppositePawn
                && getLineBetweenPositions(lastMove.getStart(), lastMove.getEnd()).size() == 1
                && pawnPosition.getRow() == lastMove.getEnd().getRow();
    }
}
