package activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import pointclub.pointclubclient.R;

public class MainMenuActivity extends Activity {
    private int dp;
    private int sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initUnitFields();
        createCards();
    }

    private void createCards() {
        GridLayout gridLayout = findViewById(R.id.main_menu_grid);
        gridLayout.addView(createCard(0, 0, "Chat"));
        gridLayout.addView(createCard(0, 1, "Chess"));
        gridLayout.addView(createCard(1, 0, "Settings"));
    }

    private MaterialCardView createCard(int row, int column, String cardText) {
        MaterialCardView cardView = new MaterialCardView(this);
        cardView.setLayoutParams(createCardLayoutParams(row, column));
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, pointclub.chess.R.color.purple_500));
        cardView.setClickable(true);
        cardView.addView(createCardText(cardText));
        return cardView;
    }

    @NonNull
    private GridLayout.LayoutParams createCardLayoutParams(int row, int column) {
        GridLayout.Spec rowSpan = GridLayout.spec(row);
        GridLayout.Spec colSpan = GridLayout.spec(column, 1, 1);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpan, colSpan);

        layoutParams.width = 150 * dp;
        layoutParams.height = 150 * dp;
        layoutParams.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutParams.topMargin = 10 * dp;
        return layoutParams;
    }

    @NonNull
    private TextView createCardText(String text) {
        TextView cardText = new TextView(this);
        ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //TODO: center the text normally. Gravity.CENTER doesn't work
        cardText.setPadding(20 * dp, 55 * dp, 0, 0);
        cardText.setTextSize(10 * sp);
        cardText.setText(text);
        cardText.setTextColor(Color.WHITE);
        cardText.setLayoutParams(textParams);
        return cardText;
    }

    private void initUnitFields() {
        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                this.getResources().getDisplayMetrics());

        sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1,
                this.getResources().getDisplayMetrics());
    }
}
