package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.chess.enums.PieceImage;
import pointclub.pointclubclient.service.ConstraintsService;
import pointclub.pointclubclient.service.chess.ChessViewFactory;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;

public class ChessActivity extends AppCompatActivity {

    ChessViewFactory chessViewFactory;
    private ConstraintLayout chessLayout;
    private TableLayout board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        initFields();
        addBoardToCenter();
        setupClassicBoard();
    }

    private void initFields() {
        chessViewFactory = new ChessViewFactory(this);
        chessLayout = findViewById(R.id.chess_layout);
        board = chessViewFactory.buildBoard();
    }

    private void addBoardToCenter() {
        chessLayout.addView(board);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(chessLayout);
        ConstraintsService.addCenterVerticallyConstraint(constraintSet, board.getId(), chessLayout.getId());
        constraintSet.applyTo(chessLayout);
    }

    private void addPieceToBoard(PieceImage pieceImage, String position) {
        if (!isPositionLegal(position)) {
            LogService.error(LogTag.CHESS, "Add Piece got Illegal Position: " + position);
            return;
        }
        FrameLayout square = getSquareAtPosition(position);
        square.addView(chessViewFactory.buildPieceImage(pieceImage));
    }

    private FrameLayout getSquareAtPosition(String position) {
        return board.findViewById(getResources().getIdentifier(position, "id", getPackageName()));
    }

    private boolean isPositionLegal(String position) {
        return position.charAt(0) >= 'a' && position.charAt(0) <= 'h' &&
                position.charAt(1) >= '1' && position.charAt(1) <= '8';
    }

    private void setupClassicBoard() {
        addPieceToBoard(PieceImage.WHITE_ROOK, "a1");
        addPieceToBoard(PieceImage.WHITE_KNIGHT, "b1");
        addPieceToBoard(PieceImage.WHITE_BISHOP, "c1");
        addPieceToBoard(PieceImage.WHITE_QUEEN, "d1");
        addPieceToBoard(PieceImage.WHITE_KING, "e1");
        addPieceToBoard(PieceImage.WHITE_BISHOP, "f1");
        addPieceToBoard(PieceImage.WHITE_KNIGHT, "g1");
        addPieceToBoard(PieceImage.WHITE_ROOK, "h1");
        addPieceToBoard(PieceImage.WHITE_PAWN, "a2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "b2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "c2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "d2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "e2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "f2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "g2");
        addPieceToBoard(PieceImage.WHITE_PAWN, "h2");

        addPieceToBoard(PieceImage.BLACK_ROOK, "a8");
        addPieceToBoard(PieceImage.BLACK_KNIGHT, "b8");
        addPieceToBoard(PieceImage.BLACK_BISHOP, "c8");
        addPieceToBoard(PieceImage.BLACK_QUEEN, "d8");
        addPieceToBoard(PieceImage.BLACK_KING, "e8");
        addPieceToBoard(PieceImage.BLACK_BISHOP, "f8");
        addPieceToBoard(PieceImage.BLACK_KNIGHT, "g8");
        addPieceToBoard(PieceImage.BLACK_ROOK, "h8");
        addPieceToBoard(PieceImage.BLACK_PAWN, "a7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "b7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "c7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "d7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "e7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "f7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "g7");
        addPieceToBoard(PieceImage.BLACK_PAWN, "h7");
    }
}