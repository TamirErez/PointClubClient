package pointclub.pointclubclient.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import lombok.SneakyThrows;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
        getToken();
    }

    private void test() {
        Call<Boolean> call = RetrofitClient.getInstance().getApi().getIsAlive();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Boolean isAlive = response.body();
                Toast.makeText(getApplicationContext(), "Is alive: " + isAlive, Toast.LENGTH_LONG).show();
            }

            @SneakyThrows
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //TODO: handle fail to connect to server
                throw t;
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

                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.i("Token", token);
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });
    }
}