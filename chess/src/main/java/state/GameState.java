package state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import lombok.Data;
import board.Board;
import board.Square;
import enums.Direction;
import enums.MoveType;
import move.Move;
import move.Position;
import piece.AbstractPiece;
import piece.Bishop;
import piece.EmptyPiece;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
import rules.ClassicChessRules;
import enums.Colour;
import enums.PieceType;

@Data
public class GameState implements GameStateAPI {
    private Board board;
    private Colour currentPlayer;
    LinkedList<Move> moves;
    Set<Move> blackThreats;
    Set<Move> whiteThreats;
    List<AbstractPiece> capturedPieces;

    public GameState() {
        this.currentPlayer = Colour.WHITE;
        this.moves = new LinkedList<>();
        this.capturedPieces = new ArrayList<>();
        this.blackThreats = new HashSet<>();
        this.whiteThreats = new HashSet<>();
        setupClassicBoard();
        updateThreats();
    }

    public GameState(GameState gameState) {
        this.currentPlayer = gameState.currentPlayer;
        this.moves = new LinkedList<>(gameState.moves);
        this.capturedPieces = new ArrayList<>(gameState.capturedPieces);
        this.blackThreats = new HashSet<>(gameState.blackThreats);
        this.whiteThreats = new HashSet<>(gameState.whiteThreats);
        this.board = new Board(gameState.board);
    }

    public GameState simulateMove(Move move) {
        GameState state = new GameState(this);
        state.move(move.cloneWithPiece(state.getPieceAtPosition(getPositionOfPiece(move.getMovingPiece()))));
        return state;
    }

    @NonNull
    public List<Move> getPieceThreateningMoves(AbstractPiece piece) {
        return piece.getPossibleMoves(this).stream().filter(this::isCapturingMove).collect(Collectors.toList());
    }

    public void move(Move move) {
        if (move.getCastleTarget() != null) {
            castle(move);
        }
        if (move.isEnPassant()) {
            enPassant(move);
        }
        capturePiece(move.getEnd());
        board.movePieceToPosition(board.removePieceFromPosition(move.getStart()), move.getEnd());
        setHasPieceMoved(move.getMovingPiece());
        updateThreats();
        moves.add(move);
        move.setCheck(isCheck(Colour.getOppositeColor(currentPlayer)));
        if (move.isPromotion()) {
            board.removePieceFromPosition(move.getEnd());
            board.addNewPiece(promotePiece(move, move.getPromotedPieceType()));
        }
    }

    private void castle(Move move) {
        setHasPieceMoved(getPieceAtPosition(move.getCastleTarget()));
        board.movePieceToPosition(
                board.removePieceFromPosition(move.getCastleTarget()),
                board.getPositionTowardsTarget(move.getEnd(), move.getStart(), 1));
    }

    private void enPassant(Move move) {
        board.removePieceFromPosition(move.getEnd()
                .transform(Direction.getOppositeDirection(((Pawn) move.getMovingPiece()).getMovingDirection())));
    }

    public List<Move> getLegalMovesOfPiece(AbstractPiece piece) {
        return new ClassicChessRules().filterMovesForPiece(piece, this);
    }

    public boolean isPositionLegal(Position position) {
        return board.isPositionLegal(position);
    }

    public Square getSquareByPosition(Position position) {
        return board.getSquareByPosition(position);
    }

    public Position getPositionOfPiece(AbstractPiece piece) {
        return board.getPositionOfPiece(piece);
    }

    public AbstractPiece getPieceAtPosition(Position position) {
        return board.getSquareByPosition(position).getPiece();
    }

    @Override
    public boolean isCheck(Colour player) {
        Set<Move> movesToCheck = player == Colour.WHITE ? blackThreats : whiteThreats;
        PieceType kingType = player == Colour.WHITE ? PieceType.WHITE_KING : PieceType.BLACK_KING;

        return movesToCheck.stream()
                .anyMatch(move -> board.getSquareByPosition(move.getEnd()).getPiece().getType().equals(kingType));
    }

    @Override
    public void switchPlayer() {
        currentPlayer = Colour.getOppositeColor(currentPlayer);
    }

    private AbstractPiece promotePiece(Move move, PieceType promotedPiece) {
        Colour pieceColour = move.getMovingPiece().getColour();
        switch (promotedPiece) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                Bishop bishop = new Bishop(pieceColour, move.getEnd());
                move.setPromotedPieceType(promotedPiece);
                return bishop;
            case BLACK_ROOK:
            case WHITE_ROOK:
                Rook rook = new Rook(pieceColour, move.getEnd());
                move.setPromotedPieceType(promotedPiece);
                return rook;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                Knight knight = new Knight(pieceColour, move.getEnd());
                move.setPromotedPieceType(promotedPiece);
                return knight;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                Queen queen = new Queen(pieceColour, move.getEnd());
                move.setPromotedPieceType(promotedPiece);
                return queen;
        }
        return EmptyPiece.getInstance();
    }

    private void setHasPieceMoved(AbstractPiece piece) {
        if (piece.getType().isRook()) {
            ((Rook) piece).setMoved(true);
        } else if (piece.getType().isKing()) {
            ((King) piece).setMoved(true);
        }
    }

    private void capturePiece(Position endPosition) {
        AbstractPiece removedPiece = board.removePieceFromPosition(endPosition);
        if (!removedPiece.getType().equals(PieceType.NONE)) {
            capturedPieces.add(removedPiece);
        }
    }

    private void updateThreats() {
        blackThreats.clear();
        whiteThreats.clear();
        board.getPieces().forEach(piece -> {
            switch (piece.getColour()) {
                case BLACK:
                    blackThreats.addAll(getPieceThreateningMoves(piece));
                    break;
                case WHITE:
                    whiteThreats.addAll(getPieceThreateningMoves(piece));
                    break;
            }
        });
    }

    private boolean isCapturingMove(Move move) {
        return move.getMoveType().equals(MoveType.MOVE_AND_CAPTURE) || move.getMoveType().equals(MoveType.CAPTURE_ONLY);
    }

    private void setupClassicBoard() {
        board = new Board(Board.DEFAULT_BOARD_SIZE, Board.DEFAULT_BOARD_SIZE);
        createWhitePieces();
        createBlackPieces();
    }

    @NonNull
    @Override
    public String toString() {
        return board.toString();
    }

    private void createWhitePieces() {
        for (int column = 0; column < Board.DEFAULT_BOARD_SIZE; column++) {
            board.addNewPiece(new Pawn(Colour.WHITE, new Position(1, column)));
        }
        board.addNewPiece(new Rook(Colour.WHITE, new Position(0, 0)));
        board.addNewPiece(new Knight(Colour.WHITE, new Position(0, 1)));
        board.addNewPiece(new Bishop(Colour.WHITE, new Position(0, 2)));
        board.addNewPiece(new Queen(Colour.WHITE, new Position(0, 3)));
        board.addNewPiece(new King(Colour.WHITE, new Position(0, 4)));
        board.addNewPiece(new Bishop(Colour.WHITE, new Position(0, 5)));
        board.addNewPiece(new Knight(Colour.WHITE, new Position(0, 6)));
        board.addNewPiece(new Rook(Colour.WHITE, new Position(0, 7)));
    }

    private void createBlackPieces() {
        for (int column = 0; column < Board.DEFAULT_BOARD_SIZE; column++) {
            board.addNewPiece(new Pawn(Colour.BLACK, new Position(6, column)));
        }
        board.addNewPiece(new Rook(Colour.BLACK, new Position(7, 0)));
        board.addNewPiece(new Knight(Colour.BLACK, new Position(7, 1)));
        board.addNewPiece(new Bishop(Colour.BLACK, new Position(7, 2)));
        board.addNewPiece(new Queen(Colour.BLACK, new Position(7, 3)));
        board.addNewPiece(new King(Colour.BLACK, new Position(7, 4)));
        board.addNewPiece(new Bishop(Colour.BLACK, new Position(7, 5)));
        board.addNewPiece(new Knight(Colour.BLACK, new Position(7, 6)));
        board.addNewPiece(new Rook(Colour.BLACK, new Position(7, 7)));
    }
}