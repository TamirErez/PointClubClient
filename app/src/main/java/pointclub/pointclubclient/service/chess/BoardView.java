package pointclub.pointclubclient.service.chess;

import android.content.Context;
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

public class BoardView extends TableLayout {

    private final int squareLength;
    private FrameLayout selectedPiece = null;

    public BoardView(Context context) {
        super(context);
        squareLength = getScreenWidth() / 8;
        buildBoard();
    }

    public void buildBoard() {
        setId(View.generateViewId());
        setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            addTwoRows(createRowBlackStart(rowIndex * 2), createRowWhiteStart(rowIndex * 2 + 1));
        }
    }

    private void addTwoRows(TableRow rowBlackStart, TableRow rowWhiteStart) {
        addView(rowWhiteStart, 0);
        addView(rowBlackStart, 1);
    }

    private TableRow createRowWhiteStart(int rowIndex) {
        return createRow(true, rowIndex);
    }

    private TableRow createRowBlackStart(int rowIndex) {
        return createRow(false, rowIndex);
    }

    @NonNull
    private TableRow createRow(boolean isWhite, int rowIndex) {
        TableRow row = new TableRow(getContext());
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
        return getContext().getResources().getIdentifier(id, "id", getContext().getPackageName());
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
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setId(id);
        ImageView blackSquare = new ImageView(getContext());

        blackSquare.setImageResource(drawableSquare);
        blackSquare.setLayoutParams(new FrameLayout.LayoutParams(squareLength, squareLength, Gravity.CENTER));
        frameLayout.addView(blackSquare);

        return frameLayout;
    }

    private int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public int getSquareLength() {
        return squareLength;
    }

    public View buildPieceView(PieceImage pieceImage) {
        ImageView piece = new ImageView(getContext());
        piece.setImageResource(pieceImage.getValue());
        piece.setLayoutParams(new FrameLayout.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        piece.setTag("piece");
        piece.setOnClickListener(v -> selectPiece(piece));
        return piece;
    }

    private void selectPiece(ImageView piece) {
        FrameLayout pieceParent = (FrameLayout) piece.getParent();
        if (selectedPiece != null) {
            selectedPiece.removeViewAt(1);
            if (selectedPiece.equals(pieceParent)) {
                selectedPiece = null;
                return;
            }
        }
        pieceParent.addView(buildSelectView(), 1);
        selectedPiece = pieceParent;
    }

    private View buildSelectView() {
        ImageView selectView = new ImageView(getContext());
        selectView.setImageResource(R.drawable.select_square);
        selectView.setLayoutParams(new FrameLayout.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        return selectView;
    }
}
