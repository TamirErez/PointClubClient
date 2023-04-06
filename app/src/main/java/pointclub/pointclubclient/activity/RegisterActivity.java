package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.model.ChatUser;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.rest.ChatRestController;
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

    private boolean isEnterPressed(int keyCode, KeyEvent event) {
        return (event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER);
    }

    private void setRegisterButtonAction() {
        findViewById(R.id.registerButton).setOnClickListener(v -> register());
    }

    private void register() {
        if (getRegisterValue().length() == 0) return;
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
        ChatRestController.getInstance().registerUser(
                getRegisterValue(),
                this::saveUserWithServerId
        );
    }

    private void registerRoom() {
        ChatRestController.getInstance().registerRoom(
                getRegisterValue(),
                this::saveRoomWithServerId
        );
    }

    private void saveRoomWithServerId(Response<Integer> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            saveRoom(response.body());
        } else {
            Toast.makeText(this, "Failed to save room", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRoom(int roomId) {
        String roomName = getRegisterValue();
        new Room(roomId, roomName).save();
        Intent i = new Intent();
        i.putExtra("id", roomId);
        i.putExtra("name", roomName);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @NonNull
    private String getRegisterValue() {
        return ((EditText) findViewById(R.id.registerInputField)).getText().toString().trim();
    }

    private void saveUserWithServerId(Response<Integer> response) {
        if (response != null && response.isSuccessful() && response.body() != null) {
            saveUser(response.body());
        } else {
            Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUser(int userId) {
        new ChatUser(userId, getRegisterValue()).save();
        Intent i = new Intent();
        i.putExtra("id", userId);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}