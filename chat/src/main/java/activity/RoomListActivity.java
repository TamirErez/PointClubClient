package activity;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adapter.RoomListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pointclub.shared.enums.RegisterOption;
import pointclub.shared.model.chat.Room;
import pointclub.chat.R;
import pointclub.shared.model.User;
import pointclub.shared.rest.RestController;
import pointclub.shared.service.RegisterService;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import rest.ChatRestController;

public class RoomListActivity extends AppCompatActivity {

    private RoomListAdapter roomAdapter;
    private List<Room> roomList = new ArrayList<>();
    private RecyclerView roomRecycler;
    private RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        registerService = new RegisterService(this);
        registerUserIfNotExist();
        setupServer();
        initRooms();
        initRoomRecycler();
        setRegisterRoomButtonAction();
    }

    private void setupServer() {
        checkServerConnection();
        if(isUserExist()) {
            getToken();
        }
    }

    private void registerUserIfNotExist() {
        if (!isUserExist()) {
            registerService.register(RegisterOption.USER, result ->
                    LogService.info(LogTag.REGISTER, "New User id: " + result.getId()));
        }
    }

    private void addRoom(String roomName) {
        roomList.add(0, Room.find(Room.class, "name = ?", roomName).get(0));
        roomAdapter.notifyItemInserted(0);
        roomRecycler.smoothScrollToPosition(0);
    }

    private void setRegisterRoomButtonAction() {
        findViewById(R.id.add_room_button).setOnClickListener(v -> registerRoom());
    }

    private void registerRoom() {
        registerService.register(RegisterOption.ROOM, result -> {
            addRoom(result.getValue());
            LogService.info(LogTag.REGISTER, "New Room id: " + result.getId());
        });
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