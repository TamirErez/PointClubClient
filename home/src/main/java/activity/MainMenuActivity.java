package activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import enums.MenuOptions;
import pointclub.pointclubclient.R;
import pointclub.shared.enums.RegisterOption;
import pointclub.shared.model.User;
import pointclub.shared.rest.RestController;
import pointclub.shared.service.RegisterService;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class MainMenuActivity extends AppCompatActivity {
    private int dp;
    private int sp;
    private RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        registerService = new RegisterService(this);
        initUnitFields();
        createCards();
        setupServer();
        registerUserIfNotExist();
    }


    private void registerUserIfNotExist() {
        //TODO: fix the crash when first opening the app
        if (!isUserExist()) {
            registerService.register(RegisterOption.USER, result -> {
                LogService.info(LogTag.REGISTER, "New User id: " + result.getId());
                getToken();
            });
        }
    }

    private boolean isUserExist() {
        List<User> users = User.listAll(User.class);
        if (users.size() > 0) {
            LogService.info(LogTag.QUERY_USER, users.get(0).toString());
            return true;
        }
        return false;
    }

    private void setupServer() {
        checkServerConnection();
        if (isUserExist()) {
            getToken();
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        LogService.warn(LogTag.TOKEN, task.getException());
                        return;
                    }

                    String token = task.getResult();
                    LogService.info(LogTag.TOKEN, token);
                    RestController.getInstance().updateToken(token,
                            response -> LogService.info(LogTag.TOKEN, "Updated Token In Server"));
                });
    }


    private void checkServerConnection() {
        RestController.getInstance().isAlive(booleanResponse -> {
            if (booleanResponse == null) {
                LogService.warn(LogTag.SERVER_STATUS, "Failed to Call Server, trying again");
                checkServerConnection();
            } else if (booleanResponse.isSuccessful()) {
                LogService.info(LogTag.SERVER_STATUS, "Alive");
            } else {
                LogService.warn(LogTag.SERVER_STATUS, "Bad Request");
            }
        });
    }

    private void createCards() {
        GridLayout gridLayout = findViewById(R.id.main_menu_grid);
        Arrays.stream(MenuOptions.values()).forEach((menuOption) -> gridLayout.addView(
                createCard(
                        gridLayout.getChildCount() / 2,
                        gridLayout.getChildCount() % 2,
                        menuOption.name())));
    }

    private MaterialCardView createCard(int row, int column, String cardText) {
        MaterialCardView cardView = new MaterialCardView(this);
        cardView.setLayoutParams(createCardLayoutParams(row, column));
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, pointclub.chess.R.color.purple_500));
        cardView.setClickable(true);
        cardView.setOnClickListener(this::startCardActivity);
        cardView.addView(createCardText(cardText));
        return cardView;
    }

    private void startCardActivity(View v) {
        TextView child = (TextView) ((CardView) v).getChildAt(0);
        MenuOptions menuOptions = MenuOptions.valueOf(child.getText().toString());
        startActivity(new Intent(this, menuOptions.getActivityClass()));
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
