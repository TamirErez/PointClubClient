package activity;

import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import enums.Colour;
import enums.PieceType;
import move.Move;
import move.Position;
import pointclub.chess.R;
import pointclub.shared.service.ConstraintsService;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import state.GameState;
import view.BoardView;
import view.SquareView;

public class ChessActivity extends AppCompatActivity {

    private BoardView board;
    private ConstraintLayout chessLayout;
    private GameState gameState;
    private boolean isLocalGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        isLocalGame = true;
        Colour player = (Colour) getIntent().getSerializableExtra("player");
        player = player == null ? Colour.WHITE : player;
        initFields(player);
        addBoardToCenter();
        setupBoard();
    }

    private void initFields(Colour player) {
        chessLayout = findViewById(R.id.chess_layout);
        board = new BoardView(this, player);
        gameState = new GameState();
    }

    private void switchPlayer() {
        gameState.switchPlayer();
        board.switchPlayer();
    }

    private void addBoardToCenter() {
        chessLayout.addView(board);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(chessLayout);
        ConstraintsService.addCenterVerticallyConstraint(constraintSet, board.getId(), chessLayout.getId());
        constraintSet.applyTo(chessLayout);
    }

    private void addPieceToBoard(PieceType pieceType, String position) {
        if (!isPositionLegal(position)) {
            LogService.error(LogTag.CHESS, "Add Piece got Illegal Position: " + position);
            return;
        }
        SquareView square = board.getSquareAtPosition(position);
        square.addPiece(board.buildPieceView(pieceType));
    }

    private boolean isPositionLegal(String position) {
        return position.charAt(0) >= 'a' && position.charAt(0) <= 'h' &&
                position.charAt(1) >= '1' && position.charAt(1) <= '8';
    }

    public void setupBoard() {
        board.clearBoard();
        gameState.getBoard().getPieces().forEach(piece ->
                addPieceToBoard(piece.getType(), gameState.getPositionOfPiece(piece).toString()));
    }

    public List<Move> getPossibleMoves(String currentPosition) {
        return gameState.getLegalMovesOfPiece(gameState.getSquareByPosition(new Position(currentPosition)).getPiece());
    }

    public void movePiece(Move move) {
        gameState.move(move);
        setupBoard();
        if (isLocalGame) {
            switchPlayer();
        }
    }

    public void movePiece(Move move, PieceType promotedPiece) {
        gameState.move(move, promotedPiece);
        setupBoard();
        if (isLocalGame) {
            switchPlayer();
        }
    }
}