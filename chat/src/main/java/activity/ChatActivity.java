package activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.MessageListAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Message;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import pointclub.chat.R;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import rest.ChatRestController;

public class ChatActivity extends AppCompatActivity {

    private MessageListAdapter messageAdapter;
    private RecyclerView messageRecycler;
    private List<Message> messageList;
    private EditText messageEditor;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        extractRoomFromIntent();
        addUserToRoom();
        getRoomMessages();
        setupMessageRecycler();
        messageEditor = findViewById(R.id.message_editor);
        setActionListenerOnMessageEditor();
    }

    private void addUserToRoom() {
        ChatRestController.getInstance().addUserToRoom(new RoomWithUser(User.getCurrentUser().getServerId(), room.getServerId()),
                response -> LogService.info(LogTag.ROOM, "Added User to room " + room));
    }

    private void extractRoomFromIntent() {
        room = (Room) getIntent().getExtras().get("room");
    }

    private void getRoomMessages() {
        messageList = Message.getMessagesOfRoom(room.getServerId());
        cleanupMessages();
    }

    private void cleanupMessages() {
        List<Message> badMessages = new ArrayList<>();
        messageList.forEach(message -> {
            if (message.getRoomId() == -1 || message.getSenderId() == -1) {
                badMessages.add(message);
            }
        });
        messageList.removeAll(badMessages);
        badMessages.forEach(message -> message.delete());
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
}