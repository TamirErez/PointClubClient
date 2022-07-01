package pointclub.pointclubclient.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.activity.MainActivity;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("New token:", token);
        //TODO: send token to server
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //TODO: handle message
        Log.d("msg", "onMessageReceived");
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage);
        } else {
            Log.d("got message",
                    " sender is:" + remoteMessage.getData().get("sender")
                            + " data is: " + remoteMessage.getData().get("content"));
        }
    }

    private void handleNotification(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        manager.notify(0, builder.build());
    }
}
