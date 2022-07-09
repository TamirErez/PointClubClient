package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pointclub.pointclubclient.R;
import pointclub.pointclubclient.adapter.RoomListAdapter;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;
import pointclub.pointclubclient.service.ActivityLauncherService;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;

public class MainActivity extends AppCompatActivity {

    public static final String popupRegisterMessage = "I see it's your first time in pointclub!\nPlease Register";
    protected final ActivityLauncherService<Intent, ActivityResult> activityLauncher = ActivityLauncherService.registerActivityForResult(this);
    private RoomListAdapter roomAdapter;
    private RecyclerView roomRecycler;
    private final List<Room> roomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServer();
        registerUserIfNotExist();
        initRoomRecycler();
        setRegisterRoomButtonAction();
    }

    private void setupServer() {
        checkServerConnection();
        getToken();
    }

    private void registerUserIfNotExist() {
        if (!isUserExist()) {
            showRegisterDialog(RegisterActivity.REGISTER_OPTION.user);
        }
    }

    private void showRegisterDialog(RegisterActivity.REGISTER_OPTION registerOption) {
        Intent intent = buildRegisterIntent(registerOption);
        launchRegisterActivity(intent);
    }

    @NonNull
    private Intent buildRegisterIntent(RegisterActivity.REGISTER_OPTION registerOption) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent = intent.putExtra("option", registerOption);
        String title = "Register";
        switch (registerOption) {
            case room:
                title = "Please Enter Room Name";
                LogService.info(LogTag.REGISTER, "Start Room Register");
                break;
            case user:
                LogService.info(LogTag.REGISTER, "Start User Register");
                title = popupRegisterMessage;
                break;
        }
        intent = intent.putExtra("title", title);
        return intent;
    }

    private void launchRegisterActivity(Intent intent) {
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    LogService.info(LogTag.REGISTER, "New id: " + data.getIntExtra("id", -1));
                } else {
                    LogService.error(LogTag.REGISTER, "Failed to Register");
                }
            }
        });
    }

    private void setRegisterRoomButtonAction() {
        findViewById(R.id.add_room_button).setOnClickListener(v -> showRegisterDialog(RegisterActivity.REGISTER_OPTION.room));
    }

    private void initRoomRecycler() {
        roomRecycler = findViewById(R.id.room_recycler);
        roomAdapter = new RoomListAdapter(roomList);
        roomRecycler.setLayoutManager(new LinearLayoutManager(this));
        roomRecycler.setAdapter(roomAdapter);
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

    private boolean isUserExist() {
        List<User> users = User.listAll(User.class);
        if (users.size() > 0) {
            LogService.info(LogTag.QUERY_USER, users.get(0).toString());
            return true;
        }
        return false;
    }
}