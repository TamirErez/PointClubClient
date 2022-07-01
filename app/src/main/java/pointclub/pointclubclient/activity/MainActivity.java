package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.rest.RestController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServer();
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
}