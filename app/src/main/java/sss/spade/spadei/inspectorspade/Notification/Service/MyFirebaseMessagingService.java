package sss.spade.spadei.inspectorspade.Notification.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import sss.spade.spadei.inspectorspade.DatabaseHandler.Beans.GetProfile;
import sss.spade.spadei.inspectorspade.DatabaseHandler.DatabaseHandler;
import sss.spade.spadei.inspectorspade.Notification.NotificationActivity;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;


/**
 * Created by hp on 9/4/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    private ScheduledExecutorService scheduleTaskExecutor;


    Context context;
    //private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = this;


        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        remoteMessage.getData();
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());


        try {
            sendNotification((remoteMessage.getData()), remoteMessage.getSentTime());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendNotification(Map<String, String> msg, long sentTime) throws JSONException, IOException {

        //JSONObject data = msg.getJSONObject("data");

        String message=msg.get("message");

        String flowers=msg.get("image");
        String title=msg.get("title");
        long time=sentTime;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());        //Bitmap icon = null;


        int i,j ;

        i=SharedPreferencesMethod.getInt(context,"count");




        if(message!=null)
        {
            SharedPreferencesMethod.setInt(context,"count",i);
        }

        j= SharedPreferencesMethod.getInt(context,"count");

        //i=SharedPreferencesMethod.setInt();
        //DataHandler.newInstance().setMessage(message);


        SharedPreferencesMethod.setString(context,"message"+Integer.toString(j),message);

        DatabaseHandler db = new DatabaseHandler(this);
        db.addNotification(new GetProfile(message,title,formattedDate));


        //String title = data.getString("title");
        //String message = data.getString("message");
        /*boolean isBackground = data.getBoolean("is_background");
        String imageUrl = data.getString("image");
        String timestamp = data.getString("timestamp");
        JSONObject payload = data.getJSONObject("payload");*/
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();


        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/raw/notification");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        //Notification notification =notificationBuilder.setSmallIcon(R.mipmap.alert)
        Notification notification;
        notification=notificationBuilder.setSmallIcon(R.drawable.ic_perm_identity_black_48dp).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_perm_identity_black_48dp)).setStyle(new NotificationCompat.BigTextStyle().bigText(message+flowers))
                .setWhen(0).setContentTitle(title)
                .setContentText(message)
                .setContentInfo(formattedDate)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setSound(alarmSound)
                .setContentIntent(pendingIntent).build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        //DataHandler.newInstance().setMessage(message);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(m, notification);

    }
}


