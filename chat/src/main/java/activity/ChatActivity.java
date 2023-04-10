package activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import adapter.MessageListAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pointclub.chat.R;
import pointclub.shared.model.User;
import pointclub.shared.model.chat.Message;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import rest.ChatRestController;
import pointclub.shared.service.ServerSynchronizer;

public class ChatActivity extends AppCompatActivity {

    private MessageListAdapter messageAdapter;
    private RecyclerView messageRecycler;
    private final List<Message> messageList = new ArrayList<>();
    private EditText messageEditor;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        extractRoomFromIntent();
        getRoomUsers();
        addUserToRoom();
        getRoomMessages();
        setupMessageRecycler();
        messageEditor = findViewById(R.id.message_editor);
        setActionListenerOnMessageEditor();
        registerMessageBroadcast();
    }

    private void addUserToRoom() {
        //TODO: if this fails do not allow user to continue
        ChatRestController.getInstance().addUserToRoom(new RoomWithUser(User.getCurrentUser().getServerId(), room.getServerId()),
                response -> LogService.info(LogTag.ROOM, "Added User to room " + room));
    }

    private void extractRoomFromIntent() {
        room = (Room) getIntent().getExtras().get("room");
    }

    private void getRoomMessages() {
        ChatRestController.getInstance().getRoomMessages(room, listResponse -> {
            ServerSynchronizer.getInstance().synchronizeList(messageList, listResponse);
            messageAdapter.notifyItemRangeInserted(0, messageList.size());
            messageList.sort(Comparator.comparingInt(Message::getServerId));
            //TODO: scroll to bottom here
        });
    }

    private void setupMessageRecycler() {
        messageRecycler = findViewById(R.id.recycler_gchat);
        messageAdapter = new MessageListAdapter(messageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
    }

    private void setActionListenerOnMessageEditor() {
        messageEditor.setOnEditorActionListener((textView, actionId, event) -> {
            if (isEnterPressed(event) && textView.getText().length() > 0) {
                Message newMessage = new Message(-1, textView.getText().toString(),
                        new Date(), room.getServerId(), User.getCurrentUser().getServerId());
                addMessageToView(newMessage);
                sendMessageToServer(newMessage);
            }
            return true;
        });
    }

    private void addMessageToView(Message newMessage) {
        messageList.add(newMessage);
        messageAdapter.notifyItemInserted(messageAdapter.getItemCount() - 1);
        messageRecycler.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        //TODO: only do this if you sent the message
        messageEditor.setText("");
        newMessage.save();
        LogService.info(LogTag.MESSAGE, "Added message %s to room %s", newMessage.getContent(), newMessage.getRoomId() + "");
    }

    private void sendMessageToServer(Message newMessage) {
        ChatRestController.getInstance().sendMessage(newMessage, response -> {
            if (response != null && response.body() != null && response.isSuccessful()) {
                newMessage.setServerId(response.body());
                newMessage.save();
            }
        });
    }

    private boolean isEnterPressed(KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    private void registerMessageBroadcast() {
        BroadcastReceiver messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Received broadcast");
                RemoteMessage remoteMessage = (RemoteMessage) intent.getExtras().get("message");
                try {
                    Map<String, String> data = remoteMessage.getData();
                    Message message = new Message(
                            Integer.parseInt(data.get("id")),
                            data.get("content"),
                            new Date(Long.parseLong(data.get("sendTime"))),
                            room.getServerId(),
                            Integer.parseInt(data.get("sender"))
                    );
                    addMessageToView(message);
                } catch (NumberFormatException | NullPointerException e) {
                    LogService.error(LogTag.MESSAGE, "Got bad message", e);
                }
            }
        };

        registerReceiver(messageReceiver, new IntentFilter("MESSAGE_RECEIVED"));
    }

    private void getRoomUsers() {
        ChatRestController.getInstance().getRoomUsers(room, response -> {
            if (response != null && response.body() != null && response.isSuccessful()) {
                response.body().forEach(user -> user.save());
            }
        });
    }
}