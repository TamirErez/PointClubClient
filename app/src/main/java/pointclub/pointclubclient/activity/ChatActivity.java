package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pointclub.pointclubclient.R;
import pointclub.pointclubclient.adapter.MessageListAdapter;
import pointclub.pointclubclient.model.Message;
import pointclub.pointclubclient.model.User;

public class ChatActivity extends AppCompatActivity {

    private MessageListAdapter messageAdapter;
    private RecyclerView messageRecycler;
    private final List<Message> messageList = new ArrayList<>();
    private EditText messageEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageRecycler = findViewById(R.id.recycler_gchat);
        messageAdapter = new MessageListAdapter(messageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        messageEditor = findViewById(R.id.message_editor);
        setActionListenerOnMessageEditor();
    }

    private void setActionListenerOnMessageEditor() {
        messageEditor.setOnEditorActionListener((textView, actionId, event) -> {
            if (isEnterPressed(event) && textView.getText().length() > 0) {
                messageList.add(new Message(1, new User(20, "tamir"),
                        textView.getText().toString(), new Date()));
                messageAdapter.notifyItemInserted(messageAdapter.getItemCount() - 1);
                messageRecycler.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                messageEditor.setText("");
            }
            return true;
        });
    }

    private boolean isEnterPressed(KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }
}