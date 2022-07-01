package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getIntent().getStringExtra("name"));
        binding.fab.setOnClickListener(v -> addMessageToView("Test Message" + counter));*/
    }

    private void addMessageToView(String message) {
        binding.chatContent.content.addView(buildTextView(message));
        binding.chatContent.getRoot().arrowScroll(View.FOCUS_DOWN);
        counter++;
    }

    private TextView buildTextView(String message) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(message);
        return textView;
    }
}