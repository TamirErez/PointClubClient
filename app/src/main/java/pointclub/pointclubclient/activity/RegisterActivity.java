package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.Objects;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register New User");
        setKeyListenerOnNameField();
    }

    private void setKeyListenerOnNameField() {
        findViewById(R.id.newUserName).setOnKeyListener((v, keyCode, event) -> {
            if (isEnterPressed(keyCode, event)) {
                RestController.getInstance().registerUser(
                        getNameValueFromField(),
                        extractResponseFromServer()
                );
                return true;
            }
            return false;
        });
    }

    private boolean isEnterPressed(int keyCode, KeyEvent event) {
        return (event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER);
    }

    @NonNull
    private String getNameValueFromField() {
        return ((EditText) findViewById(R.id.newUserName)).getText().toString();
    }

    @NonNull
    private Consumer<Response<Integer>> extractResponseFromServer() {
        return response -> {
            if (response.isSuccessful() && response.body() != null) {
                finishActivity(response.body());
            }
        };
    }

    public void finishActivity(int userId) {
        new User(userId, getNameValueFromField()).save();
        Intent i = new Intent();
        i.putExtra("id", userId);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}