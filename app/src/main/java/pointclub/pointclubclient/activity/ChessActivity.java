package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.service.ConstraintsService;
import pointclub.pointclubclient.service.chess.ViewFactoryService;

public class ChessActivity extends AppCompatActivity {

    ViewFactoryService chessViewFactory;
    private ConstraintLayout chessLayout;
    private TableLayout board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        initParams();
        addBoardToCenter();
    }

    private void initParams() {
        chessViewFactory = new ViewFactoryService(this);
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
}