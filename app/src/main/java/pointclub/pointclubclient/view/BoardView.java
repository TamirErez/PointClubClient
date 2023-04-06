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
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import pointclub.pointclubclient.R;
import pointclub.pointclubclient.activity.ChessActivity;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.enums.PieceType;
import pointclub.pointclubclient.chess.move.Move;

import pointclub.pointclubclient.view.PromotionDialog.Callable;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class BoardView extends TableLayout {

    private static final String SELECT_TAG = "select";
    private static final String MOVE_TAG = "move";
    private final int squareLength;
    private SquareView selectedSquare = null;
    private final List<SquareView> possibleMoves = new ArrayList<>();
    private Colour playerColour;

    public BoardView(Context context) {
        this(context, Colour.WHITE);
    }

    public BoardView(Context context, Colour playerColour) {
        super(context);
        this.playerColour = playerColour;
        squareLength = getScreenWidth() / 8;
        createLayoutParams();
        buildBoard();
    }

    private void createLayoutParams() {
        setId(View.generateViewId());
        setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void buildBoard() {
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            addTwoRows(createRowBlackStart(rowIndex * 2), createRowWhiteStart(rowIndex * 2 + 1));
        }
        if (playerColour.equals(Colour.BLACK)) {
            flipBoard();
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
        row.setId(findIdByName("row" + (rowIndex + 1)));
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
        setPieceMovement(piece);
        return piece;
    }

    private void setPieceMovement(ImageView piece) {
        piece.setOnClickListener(v -> {
            if (!Objects.equals(getPieceColour(piece), playerColour)) return;
            selectPiece(piece);
            if (selectedSquare != null) {
                drawPossibleMoves(piece);
            }
        });
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
                .forEach(this::drawMove);
    }

    private void drawMove(Move move) {
        SquareView targetSquare = getSquareAtPosition(move.getEnd().toString());
        boolean isCapture = isPieceExistAtPosition(move.getEnd().toString());
        ImageView possibleMoveImage = buildPossibleMoveView(isCapture);
        possibleMoveImage.setOnClickListener(v -> {
            if (move.isPromotion()) {
                showPromotionDialog(selectedSquare.getPiece(),
                        pieceType -> ((ChessActivity) getContext()).movePiece(move, pieceType));
            } else {
                ((ChessActivity) getContext()).movePiece(move);
            }
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

    public SquareView getSquareAtPosition(String position) {
        return findViewByName(position);
    }

    private int findIdByName(String name) {
        return getResources().getIdentifier(name, "id", getContext().getPackageName());
    }

    private String getIdNameOfView(View view) {
        return getContext().getResources().getResourceEntryName(view.getId());
    }

    public void switchPlayer() {
        playerColour = Colour.getOppositeColor(playerColour);
        flipBoard();
    }

    public void flipBoard() {
        for (int i = 1; i <= 8; i++) {
            TableRow row = findViewByName("row" + (playerColour.equals(Colour.BLACK) ? i : 9 - i));
            removeView(row);
            addView(row);
        }
    }

    public void showPromotionDialog(ImageView promotingPiece, Callable callback) {
        SquareView containingSquare = (SquareView) promotingPiece.getParent();
        PromotionDialog.showPromotionDialog(getContext(), promotingPiece, pieceType -> {
            containingSquare.removePiece();
            containingSquare.addPiece(buildPieceView(pieceType));
            callback.call(pieceType);
        });
    }

    private <T extends View> T findViewByName(String name) {
        return findViewById(findIdByName(name));
    }

    public static Colour getPieceColour(ImageView piece) {
        if (((PieceType) piece.getTag()).name().contains("BLACK"))
            return Colour.BLACK;
        if (((PieceType) piece.getTag()).name().contains("WHITE"))
            return Colour.WHITE;
        LogService.error(LogTag.CHESS, "Couldn't find colour for piece");
        return null;
    }

    public static boolean isPieceBlack(ImageView piece) {
        return Objects.equals(getPieceColour(piece), Colour.BLACK);
    }

    public void clearBoard() {
        removeAllViews();
        buildBoard();
    }
}
