package com.example.muziq;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.muziq.Services.NotificationActionService;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PREVIOUS= "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";

    public static Notification notification;
    private static Bitmap icon;

    public static void createNotification(Context context,Track track,int playbutton,int pos,int size){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");

            if(track.getImage()==null){
                icon = ((BitmapDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.brightplayerimage, null)).getBitmap();
            } else {
                icon = BitmapFactory.decodeByteArray(track.getImage(), 0, track.getImage().length);
            }

            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if(pos==-1) {
                pendingIntentPrevious = null;
                drw_previous = 0;
            }
            else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_PREVIOUS);
                pendingIntentPrevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_UPDATE_CURRENT);
                drw_previous =  R.drawable.ic_baseline_skip_previous_24;
            }

            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
             PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent pendingIntentNext;
            int drw_next;
            if(pos==-1) {
                pendingIntentNext = null;
                drw_next = 0;
            }
            else {
                Intent intentNext = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT);
                drw_next =  R.drawable.ic_baseline_skip_next_24;
            }

            Intent resultIntent = new Intent(context,PlayerActivity1.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drw_previous,"previous",pendingIntentPrevious)
                    .addAction(playbutton,"Play",pendingIntentPlay)
                    .addAction(drw_next,"Next",pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                       .setShowActionsInCompactView(0 , 1 , 2)
                       .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1,notification);
        }
    }
}
