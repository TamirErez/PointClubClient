package pointclub.pointclubclient.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.activity.ChessActivity;
import pointclub.pointclubclient.chess.enums.PieceType;

public class BoardView extends TableLayout {

    public static final String SELECT_TAG = "select";
    public static final String MOVE_TAG = "move";
    public static final String PIECE_TAG = "piece";
    private final int squareLength;
    private SquareView selectedSquare = null;
    private List<SquareView> possibleMoves = new ArrayList<>();

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
        String squareName = (char) ('a' + colIndex) + String.valueOf(rowIndex);
        return findIdByName(squareName);
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
        SquareView squareView = new SquareView(getContext());
        squareView.setId(id);
        ImageView square = new ImageView(getContext());
        square.setImageResource(drawableSquare);
        square.setLayoutParams(new SquareView.LayoutParams(squareLength, squareLength, Gravity.CENTER));
        squareView.addView(square);

        return squareView;
    }

    private int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public int getSquareLength() {
        return squareLength;
    }

    public ImageView buildPieceView(PieceType pieceType) {
        ImageView piece = new ImageView(getContext());
        piece.setImageResource(pieceType.getValue());
        piece.setLayoutParams(new SquareView.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        piece.setTag(pieceType);
        piece.setOnClickListener(v -> {
            selectPiece(piece);
            if (selectedSquare != null) {
                drawPossibleMoves(piece);
            }
        });
        return piece;
    }

    private void selectPiece(ImageView piece) {
        SquareView containingSquare = (SquareView) piece.getParent();
        if (selectedSquare != null) {
            SquareView selectedPiece = this.selectedSquare;
            clearSelection();
            if (selectedPiece.equals(containingSquare)) {
                return;
            }
        }
        containingSquare.addView(buildSelectView(), 1);
        selectedSquare = containingSquare;
    }

    private void clearSelection() {
        selectedSquare.removeView(selectedSquare.findViewWithTag(SELECT_TAG));
        possibleMoves.forEach(squareView -> squareView.removeView(squareView.findViewWithTag(MOVE_TAG)));
        possibleMoves.clear();
        selectedSquare = null;
    }

    private View buildSelectView() {
        ImageView selectView = new ImageView(getContext());
        selectView.setImageResource(R.drawable.select_square);
        selectView.setLayoutParams(new SquareView.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        selectView.setTag(SELECT_TAG);
        return selectView;
    }

    private void drawPossibleMoves(ImageView piece) {
        String currentPosition = getIdNameOfView((SquareView) piece.getParent());
        ((ChessActivity) getContext()).getPossibleMoves(currentPosition)
                .forEach(possibleMove -> drawMove(currentPosition, possibleMove));
    }

    private void drawMove(String currentPosition, String possibleMove) {
        SquareView targetSquare = getSquareAtPosition(possibleMove);
        ImageView possibleMoveImage = buildPossibleMoveView(isPieceExistAtPosition(possibleMove));
        possibleMoveImage.setOnClickListener(v -> {
            movePiece(currentPosition, possibleMove);
            clearSelection();
        });
        targetSquare.addView(possibleMoveImage);
        possibleMoves.add(targetSquare);
    }

    private boolean isPieceExistAtPosition(String position) {
        return getSquareAtPosition(position).getPiece() != null;
    }

    @NonNull
    private ImageView buildPossibleMoveView(boolean isPieceExistAtPosition) {
        ImageView possibleMoveImage = new ImageView(getContext());
        possibleMoveImage.setImageResource(isPieceExistAtPosition ? R.drawable.select_piece : R.drawable.select);
        possibleMoveImage.setLayoutParams(new SquareView.LayoutParams(getSquareLength(), getSquareLength(), Gravity.CENTER));
        possibleMoveImage.setTag(MOVE_TAG);
        return possibleMoveImage;
    }

    private void movePiece(String startPosition, String endPosition) {
        SquareView startSquare = getSquareAtPosition(startPosition);
        SquareView endSquare = getSquareAtPosition(endPosition);

        ImageView movingPiece = startSquare.removePiece();
        endSquare.removePiece();
        endSquare.addPiece(movingPiece);
    }

    public SquareView getSquareAtPosition(String position) {
        return findViewById(findIdByName(position));
    }

    private int findIdByName(String position) {
        return getResources().getIdentifier(position, "id", getContext().getPackageName());
    }

    private String getIdNameOfView(View view) {
        return getContext().getResources().getResourceEntryName(view.getId());
    }

    public void showPromotionDialog(ImageView promotingPiece) {
        SquareView containingSquare = (SquareView) promotingPiece.getParent();
        PromotionDialog.showPromotionDialog(getContext(), promotingPiece, pieceType -> {
            containingSquare.removePiece();
            containingSquare.addPiece(buildPieceView(pieceType));
        });
    }
}
