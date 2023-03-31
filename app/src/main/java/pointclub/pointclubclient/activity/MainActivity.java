package pointclub.pointclubclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.adapter.RoomListAdapter;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.rest.ChatRestController;
import pointclub.shared.model.User;
import pointclub.shared.rest.RestController;
import pointclub.shared.service.ActivityLauncherService;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class MainActivity extends AppCompatActivity {

    protected final ActivityLauncherService<Intent, ActivityResult> activityLauncher = ActivityLauncherService.registerActivityForResult(this);
    private RoomListAdapter roomAdapter;
    private List<Room> roomList = new ArrayList<>();
    private RecyclerView roomRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServer();
        registerUserIfNotExist();
        initRooms();
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
        launchRegisterActivity(intent, registerOption);
    }

    @NonNull
    private Intent buildRegisterIntent(RegisterActivity.REGISTER_OPTION registerOption) {
        String title = getRegisterTitle(registerOption);
        Intent intent = new Intent(this, RegisterActivity.class);
        intent = intent.putExtra("option", registerOption);
        intent = intent.putExtra("title", title);
        return intent;
    }

    private String getRegisterTitle(RegisterActivity.REGISTER_OPTION registerOption) {
        switch (registerOption) {
            case room:
                LogService.info(LogTag.REGISTER, "Start Room Register");
                return "Please Enter Room Name";
            case user:
                LogService.info(LogTag.REGISTER, "Start User Register");
                return getResources().getString(R.string.register_user_message);
            default:
                return "Register";
        }
    }

    private void launchRegisterActivity(Intent intent, RegisterActivity.REGISTER_OPTION registerOption) {
        activityLauncher.launch(intent, result -> {
            if (isActivityResultOK(result)) {
                Intent registerData = result.getData();
                if (registerData != null) {
                    handleRegisterResult(registerData, registerOption);
                } else {
                    LogService.error(LogTag.REGISTER, "Failed to Register");
                }
            }
        });
    }

    private boolean isActivityResultOK(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            return true;
        } else {
            LogService.error(LogTag.REGISTER, "Activity failed");
            return false;
        }
    }

    private void handleRegisterResult(Intent registerData, RegisterActivity.REGISTER_OPTION registerOption) {
        switch (registerOption) {
            case user:
                LogService.info(LogTag.REGISTER, "New User id: " + registerData.getIntExtra("id", -1));
                break;
            case room:
                addRoom(registerData.getStringExtra("name"));
                LogService.info(LogTag.REGISTER, "New Room id: " + registerData.getIntExtra("id", -1));
                break;
        }
    }

    private void addRoom(String roomName) {
        roomList.add(0, Room.find(Room.class, "name = ?", roomName).get(0));
        roomAdapter.notifyItemInserted(0);
        roomRecycler.smoothScrollToPosition(0);
    }

    private void setRegisterRoomButtonAction() {
        findViewById(R.id.add_room_button).setOnClickListener(v -> showRegisterDialog(RegisterActivity.REGISTER_OPTION.room));
    }

    private void initRoomRecycler() {
        roomRecycler = findViewById(R.id.room_recycler);
        roomAdapter = new RoomListAdapter(roomList, this);
        roomRecycler.setLayoutManager(new LinearLayoutManager(this));
        roomRecycler.setAdapter(roomAdapter);
    }

    private void initRooms() {
        ChatRestController.getInstance().getAllRooms(listResponse -> {
            if (listResponse != null && listResponse.body() != null && listResponse.isSuccessful()) {
                roomList.addAll(listResponse.body());
                roomAdapter.notifyItemRangeInserted(0, roomList.size());
            }
            roomList.sort(Comparator.comparingInt(Room::getServerId));
            syncRoomsWithServer();
        });
    }

    private void syncRoomsWithServer() {
        List<Room> rooms = Room.listAll(Room.class);
        roomList.forEach(room -> {
            if (!rooms.contains(room)) {
                room.setId(null);
                room.save();
            }
        });
        rooms.removeAll(roomList);
        rooms.forEach(room -> room.delete());
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
                    //TODO: Update token only after user is created for the first time
                    RestController.getInstance().updateToken(token,
                            response -> LogService.info(LogTag.TOKEN, "Updated Token In Server"));
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