package pointclub.pointclubclient.activity;

import android.os.Bundle;

import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.chess.GameState;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.MoveType;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.service.ConstraintsService;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;
import pointclub.pointclubclient.view.BoardView;
import pointclub.pointclubclient.view.SquareView;

public class ChessActivity extends AppCompatActivity {

    private BoardView board;
    private ConstraintLayout chessLayout;
    private GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        Colour player = (Colour) getIntent().getSerializableExtra("player");
        initFields(player);
        addBoardToCenter();
        setupBoard();
    }

    private void initFields(Colour player) {
        chessLayout = findViewById(R.id.chess_layout);
        board = new BoardView(this, player);
        gameState = new GameState();
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
        gameState.getBoard().getPieces().forEach(abstractPiece ->
                addPieceToBoard(abstractPiece.getPieceType(), abstractPiece.getStartingPosition().toString()));
    }

    public List<String> getPossibleMoves(String currentPosition) {
        List<Move> possibleMoves = gameState.getSquareByPosition(new Position(currentPosition)).getPiece().getPossibleMoves(gameState);
        return possibleMoves.stream()
                .map(move -> move.getEnd().toString())
                .collect(Collectors.toList());

    }

    public void movePiece(String startPosition, String endPosition, boolean isCapture) {
        Position piecePosition = new Position(startPosition);
        gameState.move(new Move(piecePosition, new Position(endPosition),
                gameState.getPieceAtPosition(piecePosition),
                isCapture ? MoveType.CAPTURE_ONLY : MoveType.MOVE_ONLY));
    }
}