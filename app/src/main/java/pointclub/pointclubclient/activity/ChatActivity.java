package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.adapter.MessageListAdapter;
import pointclub.pointclubclient.model.Message;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.rest.RestController;

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
        getRoomMessages();
        setupMessageRecycler();
        messageEditor = findViewById(R.id.message_editor);
        setActionListenerOnMessageEditor();
    }

    private void extractRoomFromIntent() {
        room = Room.findById(Room.class, getIntent().getExtras().getLong("roomId"));
    }

    private void getRoomMessages() {
        messageList = room.getMessages();
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
                        new Date(), room, User.getCurrentUser());
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
    }

    private void sendMessageToServer(Message newMessage) {
        RestController.getInstance().sendMessage(newMessage, response -> {
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