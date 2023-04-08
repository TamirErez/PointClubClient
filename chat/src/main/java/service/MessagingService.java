package service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import activity.RoomListActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pointclub.shared.model.User;
import pointclub.shared.rest.RestController;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class MessagingService extends FirebaseMessagingService {

    public static final String TYPE_MESSAGE_RECEIVED = "MESSAGE_RECEIVED";

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
        LogService.info(LogTag.MESSAGE, "got message of type %s",
                remoteMessage.getData().get("type"));
        handleMessageReceived(remoteMessage);
    }

    private void handleMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String type = validateMessageType(remoteMessage);
        if (type == null) return;
        switch (Objects.requireNonNull(type)) {
            case "message":
                handleMessage(remoteMessage);
                break;
            case "user":
                handleUser(remoteMessage);
                break;
            default:
                LogService.warn(LogTag.MESSAGE, "Unexpected value: " + type);
        }
    }

    @Nullable
    private String validateMessageType(@NonNull RemoteMessage remoteMessage) {
        String type;
        try {
            type = remoteMessage.getData().get("type");
        } catch (NullPointerException e) {
            LogService.warn(LogTag.MESSAGE, "Got message without a type");
            return null;
        }
        return type;
    }

    private void handleMessage(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage);
        } else {
            sendBroadcastOfType(remoteMessage, TYPE_MESSAGE_RECEIVED);
        }
    }

    private void handleUser(RemoteMessage remoteMessage) {
        Map<String, String> messageData = remoteMessage.getData();
        try {
            new User(Integer.parseInt(messageData.get("serverId")),
                    messageData.get("name"),
                    messageData.get("token")).save();
            LogService.info(LogTag.MESSAGE, "Got new user %s", messageData.get("name"));
        }
        catch (NumberFormatException e){
            LogService.warn(LogTag.MESSAGE, "Got invalid server id %s", messageData.get("serverId"));
        }
    }

    private void sendBroadcastOfType(@NonNull RemoteMessage remoteMessage, String type) {
        Intent intent = new Intent(type);
        intent.putExtra("message", remoteMessage);
        sendBroadcast(intent);
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
