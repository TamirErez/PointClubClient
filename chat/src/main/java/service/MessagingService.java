package service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import activity.RoomListActivity;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import pointclub.shared.rest.RestController;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        LogService.info(LogTag.TOKEN, token);
        RestController.getInstance().updateToken(token,
                response -> LogService.info(LogTag.TOKEN, "Updated Token In Server"));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LogService.info(LogTag.MESSAGE, "got message",
                " sender is:" + remoteMessage.getData().get("sender")
                        + " data is: " + remoteMessage.getData().get("content"));
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage);
        } else {
            Intent intent = new Intent("MESSAGE_RECEIVED");
            intent.putExtra("message", remoteMessage);
            sendBroadcast(intent);
        }
    }

    private void handleNotification(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        manager.notify(0, builder.build());
    }
}
