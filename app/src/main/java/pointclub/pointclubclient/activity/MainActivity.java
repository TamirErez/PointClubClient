package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;
import pointclub.pointclubclient.service.ActivityLauncherService;
import pointclub.pointclubclient.service.PopupService;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_STATUS_TAG = "Server Status";
    public static final String popupRegisterMessage = "I see it's your first time in pointclub!\nPlease Register";
    private int userId;
    protected final ActivityLauncherService<Intent, ActivityResult> activityLauncher = ActivityLauncherService.registerActivityForResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServer();
        registerUserIfNotExist();
    }

    private void setupServer() {
        checkServerConnection();
        getToken();
    }

    private void checkServerConnection() {
        RestController.getInstance().isAlive(booleanResponse -> {
            if (booleanResponse == null) {
                Log.w(SERVER_STATUS_TAG, "Failed to Call Server, trying again");
                checkServerConnection();
            } else if (booleanResponse.isSuccessful()) {
                Log.i(SERVER_STATUS_TAG, "Alive");
            } else {
                Log.w(SERVER_STATUS_TAG, "Bad Request");
            }
        });
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    Log.i("Token", token);
                });
    }

    private void registerUserIfNotExist() {
        if (!isUserExist()) {
            PopupService.showPopup(this, this::registerUser, popupRegisterMessage);
        }
    }

    private boolean isUserExist() {
        List<User> users = User.listAll(User.class);
        if (users.size() > 0) {
            Log.i("Query User", users.get(0).toString());
            return true;
        }
        return false;
    }

    private void registerUser() {
        Intent intent = new Intent(this, RegisterActivity.class);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Log.i("Register User", "New User id: " + data.getIntExtra("id", -1));
                } else {
                    Log.e("Register User", "Failed to Register User");
                }
            }
        });
    }
}