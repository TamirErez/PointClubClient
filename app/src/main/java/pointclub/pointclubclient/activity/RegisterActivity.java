package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.util.function.Consumer;

import androidx.annotation.NonNull;

import pointclub.pointclubclient.R;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;
import retrofit2.Response;

public class RegisterActivity extends Activity {

    private REGISTER_OPTION registerOption;

    public enum REGISTER_OPTION {
        user, room
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle();
        setKeyListenerOnNameField();
        setRegisterButtonAction();
        findViewById(R.id.registerInputField).requestFocus();
        registerOption = (REGISTER_OPTION) getIntent().getExtras().get("option");
    }

    private void setTitle() {
        ((TextView) findViewById(R.id.registerTitle))
                .setText(getIntent().getExtras().getString("title"));
    }

    private void setKeyListenerOnNameField() {
        findViewById(R.id.registerInputField).setOnKeyListener((v, keyCode, event) -> {
            if (isEnterPressed(keyCode, event)) {
                register();
                return true;
            }
            return false;
        });
    }

    private void setRegisterButtonAction() {
        findViewById(R.id.registerButton).setOnClickListener(v -> register());
    }

    private void register() {
        switch (registerOption) {
            case user:
                registerUser();
                break;
            case room:
                registerRoom();
                break;
        }
    }

    private void registerUser() {
        RestController.getInstance().registerUser(
                getNameValueFromField(),
                extractResponseFromServer()
        );
    }

    private void registerRoom() {
        finish();
    }


    private boolean isEnterPressed(int keyCode, KeyEvent event) {
        return (event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER);
    }

    @NonNull
    private String getNameValueFromField() {
        return ((EditText) findViewById(R.id.registerInputField)).getText().toString();
    }

    @NonNull
    private Consumer<Response<Integer>> extractResponseFromServer() {
        return response -> {
            if (response.isSuccessful() && response.body() != null) {
                saveUser(response.body());
            }
        };
    }

    public void saveUser(int userId) {
        new User(userId, getNameValueFromField()).save();
        Intent i = new Intent();
        i.putExtra("id", userId);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}