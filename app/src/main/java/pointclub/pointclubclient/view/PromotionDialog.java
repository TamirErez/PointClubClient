package pointclub.pointclubclient.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.chess.enums.PieceType;

public class PromotionDialog {
    public static void showPromotionDialog(Context containingContext, ImageView promotingPiece, Callable callback) {
        View promotionDialog = createPopupView(containingContext);

        ImageView queenPromotion = addTagToTimage(promotionDialog, R.id.promotion_queen, PieceType.WHITE_QUEEN);
        ImageView knightPromotion = addTagToTimage(promotionDialog, R.id.promotion_knight, PieceType.WHITE_KNIGHT);
        ImageView bishopPromotion = addTagToTimage(promotionDialog, R.id.promotion_bishop, PieceType.WHITE_BISHOP);
        ImageView rookPromotion = addTagToTimage(promotionDialog, R.id.promotion_rook, PieceType.WHITE_ROOK);

        setPromotePieceColor(promotingPiece, queenPromotion, knightPromotion, bishopPromotion, rookPromotion);
        PopupWindow popupWindow = buildPopupWindow(promotionDialog);
        popupWindow.showAsDropDown((View) promotingPiece.getParent());

        setPromotionAction((SquareView) promotingPiece.getParent(), promotionDialog, popupWindow, callback);
    }

    private static void setPromotePieceColor(ImageView promotingPiece, ImageView queen, ImageView knight, ImageView bishop, ImageView rook) {
        if (isPieceBlack(promotingPiece)) {
            queen.setImageResource(R.drawable.black_queen);
            queen.setTag(PieceType.BLACK_QUEEN);
            bishop.setImageResource(R.drawable.black_bishop);
            bishop.setTag(PieceType.BLACK_BISHOP);
            knight.setImageResource(R.drawable.black_knight);
            knight.setTag(PieceType.BLACK_KNIGHT);
            rook.setImageResource(R.drawable.black_rook);
            rook.setTag(PieceType.BLACK_ROOK);

        }
    }

    private static ImageView addTagToTimage(View containingView, int id, PieceType tag) {
        ImageView image = containingView.findViewById(id);
        image.setTag(tag);
        return image;
    }

    private static boolean isPieceBlack(ImageView promotingPiece) {
        return ((PieceType) promotingPiece.getTag()).name().contains("BLACK");
    }

    private static void setPromotionAction(SquareView containingSquare, View promotionDialog, PopupWindow popupWindow, Callable callback) {
        setClosePopupButton(promotionDialog, popupWindow);
        promotionDialog.findViewById(R.id.promotion_queen).setOnClickListener(v -> promoteToPiece((ImageView) v, popupWindow, callback));
        promotionDialog.findViewById(R.id.promotion_knight).setOnClickListener(v -> promoteToPiece((ImageView) v, popupWindow, callback));
        promotionDialog.findViewById(R.id.promotion_bishop).setOnClickListener(v -> promoteToPiece((ImageView) v, popupWindow, callback));
        promotionDialog.findViewById(R.id.promotion_rook).setOnClickListener(v -> promoteToPiece((ImageView) v, popupWindow, callback));
    }

    private static void promoteToPiece(ImageView promotionImage, PopupWindow popupWindow, Callable callback) {
        callback.call((PieceType) promotionImage.getTag());
        popupWindow.dismiss();
    }

    private static void setClosePopupButton(View customView, PopupWindow popupWindow) {
        customView.findViewById(R.id.closePopupBtn).setOnClickListener(v -> popupWindow.dismiss());
    }

    private static View createPopupView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.chess_promotion, null);
        return customView;
    }

    @NonNull
    private static PopupWindow buildPopupWindow(View customView) {
        PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        return popupWindow;
    }

    public interface Callable {
        void call(PieceType pieceType);
    }
}
