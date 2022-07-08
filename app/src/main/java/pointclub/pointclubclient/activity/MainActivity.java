package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import pointclub.pointclubclient.R;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;
import pointclub.pointclubclient.service.ActivityLauncherService;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;
import pointclub.pointclubclient.service.PopupService;

public class MainActivity extends AppCompatActivity {

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
                LogService.warn(LogTag.SERVER_STATUS, "Failed to Call Server, trying again");
                checkServerConnection();
            } else if (booleanResponse.isSuccessful()) {
                LogService.info(LogTag.SERVER_STATUS, "Alive");
            } else {
                LogService.warn(LogTag.SERVER_STATUS, "Bad Request");
            }
        });
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
            LogService.info(LogTag.QUERY_USER, users.get(0).toString());
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
                    LogService.info(LogTag.REGISTER_USER, "New User id: " + data.getIntExtra("id", -1));
                } else {
                    LogService.error(LogTag.REGISTER_USER, "Failed to Register User");
                }
            }
        });
    }
}