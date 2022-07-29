package pointclub.pointclubclient.service.chess;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.chess.enums.PieceImage;

public class ChessViewFactory {

    private final Activity containingActivity;
    private final int squareLength;

    public ChessViewFactory(Activity containingActivity) {
        this.containingActivity = containingActivity;
        squareLength = getScreenWidth() / 8;
    }

    public TableLayout buildBoard() {
        TableLayout board = new TableLayout(containingActivity);
        board.setId(View.generateViewId());
        board.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            addTwoRows(board, createRowBlackStart(rowIndex * 2), createRowWhiteStart(rowIndex * 2 + 1));
        }
        return board;
    }

    private void addTwoRows(TableLayout board, TableRow rowBlackStart, TableRow rowWhiteStart) {
        board.addView(rowWhiteStart, 0);
        board.addView(rowBlackStart, 1);
    }

    private TableRow createRowWhiteStart(int rowIndex) {
        return createRow(true, rowIndex);
    }

    private TableRow createRowBlackStart(int rowIndex) {
        return createRow(false, rowIndex);
    }

    @NonNull
    private TableRow createRow(boolean isWhite, int rowIndex) {
        TableRow row = new TableRow(containingActivity);
        row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER);
        for (int colIndex = 0; colIndex < 4; colIndex++) {
            int firstPosition = positionToId(rowIndex + 1, colIndex * 2);
            int secondPosition = positionToId(rowIndex + 1, colIndex * 2 + 1);
            View firstSquare = isWhite ? createWhiteSquare(firstPosition) : createBlackSquare(firstPosition);
            View secondSquare = isWhite ? createBlackSquare(secondPosition) : createWhiteSquare(secondPosition);
            addTwoSquares(row, firstSquare, secondSquare);
        }
        return row;
    }

    private int positionToId(int rowIndex, int colIndex) {
        String id = (char) ('a' + colIndex) + String.valueOf(rowIndex);
        return containingActivity.getResources().getIdentifier(id, "id", containingActivity.getPackageName());
    }

    private void addTwoSquares(TableRow row, View blackSquare, View whiteSquare) {
        row.addView(blackSquare);
        row.addView(whiteSquare);
    }

    @NonNull
    private View createBlackSquare(int id) {
        return createSquare(R.drawable.black_square, id);
    }

    @NonNull
    private View createWhiteSquare(int id) {
        return createSquare(R.drawable.white_square, id);
    }

    @NonNull
    private View createSquare(int drawableSquare, int id) {
        FrameLayout frameLayout = new FrameLayout(containingActivity);
        frameLayout.setId(id);
        ImageView blackSquare = new ImageView(containingActivity);

        blackSquare.setImageResource(drawableSquare);
        blackSquare.setLayoutParams(new FrameLayout.LayoutParams(squareLength, squareLength, Gravity.CENTER));
        frameLayout.addView(blackSquare);

        return frameLayout;
    }

    private int getScreenWidth() {
        return containingActivity.getWindowManager().getCurrentWindowMetrics().getBounds().width();
    }

    public int getSquareLength() {
        return squareLength;
    }

    public View buildPieceImage(PieceImage pieceImage) {
        ImageView piece = new ImageView(containingActivity);
        piece.setImageResource(pieceImage.getValue());
        piece.setLayoutParams(new FrameLayout.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        piece.setTag("piece");
        return piece;
    }
}
