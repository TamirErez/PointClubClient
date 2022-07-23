package pointclub.pointclubclient.chess;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Data;
import pointclub.pointclubclient.chess.board.Board;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

@Data
public class GameState {
    private Board board;
    private Colour currentPlayer;
    List<Move> moves;

    public GameState() {
        this.currentPlayer = Colour.WHITE;
        this.moves = new ArrayList<>();
        setupClassicBoard();
    }

    private void setupClassicBoard() {
        board = new Board(Board.DEFAULT_BOARD_SIZE, Board.DEFAULT_BOARD_SIZE);
    }

    @NonNull
    @Override
    public String toString() {
        return board.toString();
    }

    public boolean isPositionLegal(Position position) {
        return board.isPositionLegal(position);
    }

    public AbstractPiece getPieceByPosition(Position position) {
        return board.getPieceByPosition(position);
    }

    public Position getPositionOfPiece(AbstractPiece piece) {
        return board.getPositionOfPiece(piece);
    }
}