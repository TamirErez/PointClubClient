package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.rest.RestController;
import pointclub.pointclubclient.service.ActivityLauncherService;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_STATUS_TAG = "Server Status";
    private int userId;
    protected final ActivityLauncherService<Intent, ActivityResult> activityLauncher = ActivityLauncherService.registerActivityForResult(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServer();
        registerUser();
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