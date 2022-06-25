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
        isServerAlive();
        getToken();
    }

    private void isServerAlive() {
        RestController.getInstance().isAlive(booleanResponse -> {
            if(booleanResponse.isSuccessful()){
                Log.i("Server Status", "Alive");
            }
            else{
                Log.w("Server Status", "Bad Request");
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