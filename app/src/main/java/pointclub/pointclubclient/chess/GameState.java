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
import pointclub.pointclubclient.chess.piece.King;
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
}