package pointclub.pointclubclient.chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import lombok.Data;
import pointclub.pointclubclient.chess.board.Board;
import pointclub.pointclubclient.chess.board.Square;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.chess.piece.AbstractPiece;
import pointclub.pointclubclient.chess.piece.Bishop;
import pointclub.pointclubclient.chess.piece.EmptyPiece;
import pointclub.pointclubclient.chess.piece.King;
import pointclub.pointclubclient.chess.piece.Knight;
import pointclub.pointclubclient.chess.piece.Pawn;
import pointclub.pointclubclient.chess.piece.Queen;
import pointclub.pointclubclient.chess.piece.Rook;

@Data
public class GameState {
    private Board board;
    private Colour currentPlayer;
    List<Move> moves;
    Set<Move> whiteThreats;
    Set<Move> blackThreats;
    List<AbstractPiece> capturedPieces;

    public GameState() {
        this.currentPlayer = Colour.WHITE;
        this.moves = new ArrayList<>();
        this.capturedPieces = new ArrayList<>();
        this.whiteThreats = new HashSet<>();
        this.blackThreats = new HashSet<>();
        setupClassicBoard();
        updateThreats();
    }

    public GameState(GameState gameState) {
        this.currentPlayer = gameState.currentPlayer;
        this.moves = new ArrayList<>(gameState.moves);
        this.capturedPieces = new ArrayList<>(gameState.capturedPieces);
        this.whiteThreats = new HashSet<>(gameState.whiteThreats);
        this.blackThreats = new HashSet<>(gameState.blackThreats);
        this.board = new Board(board);
    }

    public void move(Move move) {
        capturePiece(move.getEnd());
        board.movePieceToPosition(board.removePieceFromPosition(move.getStart()), move.getEnd());
        setHasPieceMoved(move);
        updateThreats();
    }

    public void move(Move move, PieceType promotedPiece) {
        move(move);
        board.removePieceFromPosition(move.getEnd());
        board.addNewPiece(promotePiece(move, promotedPiece));
        moves.add(move);
    }

    private AbstractPiece promotePiece(Move move, PieceType promotedPiece) {
        switch (promotedPiece) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                Bishop bishop = new Bishop(currentPlayer, move.getEnd());
                move.setPromotedPiece(bishop);
                return bishop;
            case BLACK_ROOK:
            case WHITE_ROOK:
                Rook rook = new Rook(currentPlayer, move.getEnd());
                move.setPromotedPiece(rook);
                return rook;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                Knight knight = new Knight(currentPlayer, move.getEnd());
                move.setPromotedPiece(knight);
                return knight;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                Queen queen = new Queen(currentPlayer, move.getEnd());
                move.setPromotedPiece(queen);
                return queen;
        }
        return EmptyPiece.getInstance();
    }

    private void setHasPieceMoved(Move move) {
        if (move.getMovingPiece().getPieceType().isRook()) {
            ((Rook) move.getMovingPiece()).setHasMoved(true);
        } else if (move.getMovingPiece().getPieceType().isKing()) {
            ((King) move.getMovingPiece()).setHasMoved(true);
        }
    }

    private void capturePiece(Position endPosition) {
        AbstractPiece removedPiece = board.removePieceFromPosition(endPosition);
        if (!removedPiece.getPieceType().equals(PieceType.NONE)) {
            capturedPieces.add(removedPiece);
        }
    }

    private void updateThreats() {
        whiteThreats.clear();
        blackThreats.clear();
        board.getPieces().forEach(piece -> {
            switch (piece.getColour()) {
                case BLACK:
                    whiteThreats.addAll(getPieceThreateningMoves(piece));
                    break;
                case WHITE:
                    blackThreats.addAll(getPieceThreateningMoves(piece));
                    break;
            }
        });
    }

    @NonNull
    public List<Move> getPieceThreateningMoves(AbstractPiece piece) {
        return piece.getPossibleMoves(this).stream().filter(this::isCapturingMove).collect(Collectors.toList());
    }

    private boolean isCapturingMove(Move move) {
        return move.getMoveType().equals(MoveType.MOVE_AND_CAPTURE) || move.getMoveType().equals(MoveType.CAPTURE_ONLY);
    }

    //TODO: create a filter function that takes all the possible moves for a piece and applies the chess rules on them
    public List<Move> getLegalMovesOfPiece(AbstractPiece piece) {
        return new ArrayList<>();
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

    private GameState simulateMove(Move move) {
        GameState state = new GameState(this);
        state.move(move);
        return state;
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