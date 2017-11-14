package ytstudios.wall.bucket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

/**
 * Created by Yugansh Tyagi on 26-10-2017.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {

    String key;
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            key = remoteMessage.getData().get("KEY");
            Log.d("FCM KEY ", key);
        }catch (Exception e){}
        switch (key) {
            case "Update":
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Log.i("URI RATE APP", uri.toString());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                break;
            case "Category":
                intent = new Intent(this, CategoryDetailsFragment.class);
                pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "WallBucket");
        notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
