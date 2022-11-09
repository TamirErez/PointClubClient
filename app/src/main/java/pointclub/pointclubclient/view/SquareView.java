package pointclub.pointclubclient.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.chess.enums.PieceType;

public class SquareView extends FrameLayout {
    public SquareView(@NonNull Context context) {
        super(context);
    }

    public ImageView getPiece() {
        List<View> children = getChildren();
        for (View child : children) {
            if (child.getTag() instanceof PieceType) {
                return (ImageView) child;
            }
        }
        return null;
    }

    private List<View> getChildren() {
        List<View> children = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            children.add(getChildAt(i));
        }
        return children;
    }

    public ImageView removePiece() {
        List<View> children = getChildren();
        for (View child : children) {
            if (child.getTag() instanceof PieceType) {
                removeView(child);
                return (ImageView) child;
            }
        }
        return null;
    }

    public void addPiece(ImageView promotionImage) {
        addView(promotionImage);
    }
}

