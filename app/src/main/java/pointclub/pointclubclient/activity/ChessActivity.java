package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.service.ConstraintsService;
import pointclub.pointclubclient.view.BoardView;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;

public class ChessActivity extends AppCompatActivity {

    private BoardView board;
    private ConstraintLayout chessLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        initFields();
        addBoardToCenter();
        setupClassicBoard();
    }

    private void initFields() {
        chessLayout = findViewById(R.id.chess_layout);
        board = new BoardView(this);
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
        FrameLayout square = board.getSquareAtPosition(position);
        square.addView(board.buildPieceView(pieceType));
    }

    private boolean isPositionLegal(String position) {
        return position.charAt(0) >= 'a' && position.charAt(0) <= 'h' &&
                position.charAt(1) >= '1' && position.charAt(1) <= '8';
    }

    private void setupClassicBoard() {
        addPieceToBoard(PieceType.WHITE_ROOK, "a1");
        addPieceToBoard(PieceType.WHITE_KNIGHT, "b1");
        addPieceToBoard(PieceType.WHITE_BISHOP, "c1");
        addPieceToBoard(PieceType.WHITE_QUEEN, "d1");
        addPieceToBoard(PieceType.WHITE_KING, "e1");
        addPieceToBoard(PieceType.WHITE_BISHOP, "f1");
        addPieceToBoard(PieceType.WHITE_KNIGHT, "g1");
        addPieceToBoard(PieceType.WHITE_ROOK, "h1");
        addPieceToBoard(PieceType.WHITE_PAWN, "a2");
        addPieceToBoard(PieceType.WHITE_PAWN, "b2");
        addPieceToBoard(PieceType.WHITE_PAWN, "c2");
        addPieceToBoard(PieceType.WHITE_PAWN, "d2");
        addPieceToBoard(PieceType.WHITE_PAWN, "e2");
        addPieceToBoard(PieceType.WHITE_PAWN, "f2");
        addPieceToBoard(PieceType.WHITE_PAWN, "g2");
        addPieceToBoard(PieceType.WHITE_PAWN, "h2");

        addPieceToBoard(PieceType.BLACK_ROOK, "a8");
        addPieceToBoard(PieceType.BLACK_KNIGHT, "b8");
        addPieceToBoard(PieceType.BLACK_BISHOP, "c8");
        addPieceToBoard(PieceType.BLACK_QUEEN, "d8");
        addPieceToBoard(PieceType.BLACK_KING, "e8");
        addPieceToBoard(PieceType.BLACK_BISHOP, "f8");
        addPieceToBoard(PieceType.BLACK_KNIGHT, "g8");
        addPieceToBoard(PieceType.BLACK_ROOK, "h8");
        addPieceToBoard(PieceType.BLACK_PAWN, "a7");
        addPieceToBoard(PieceType.BLACK_PAWN, "b7");
        addPieceToBoard(PieceType.BLACK_PAWN, "c7");
        addPieceToBoard(PieceType.BLACK_PAWN, "d7");
        addPieceToBoard(PieceType.BLACK_PAWN, "e7");
        addPieceToBoard(PieceType.BLACK_PAWN, "f7");
        addPieceToBoard(PieceType.BLACK_PAWN, "g7");
        addPieceToBoard(PieceType.BLACK_PAWN, "h7");
    }

    public List<String> getPossibleMoves(String currentPosition) {
        int row = new Random().nextInt(8) + 1;
        String pos = "d" + row;
        return List.of(pos, "c3", "c7");
    }
}