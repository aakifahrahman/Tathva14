package org.tathva.triloaded.gcm;

import org.tathva.triloaded.announcements.Announcement;
import org.tathva.triloaded.announcements.NotifyDB;
import org.tathva.triloaded.events.NormalEvent;
import org.tathva.triloaded.events.Updater;
import org.tathva.triloaded.mainmenu.R;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {

private static final String DATA = "data";
	
	NotifyDB db = new NotifyDB(this);
	
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String jString;
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
         String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  
             if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            } 
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
               
            	/**** Retrieving Message Content ***/
            	jString = extras.getString(DATA);
            	Log.d("debug", jString);
            	Notification notification = new Notification(jString);
            	
                sendNotification(notification);
            }
        }
        
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    private void sendNotification(Notification notification) {
    	
        if(notification.getType() == Notification.TYPE_EVENT_UPDATE){
        	Updater updater=new Updater(this);
    		String eventId = notification.getEventId();
    		String sql = notification.getSql();
    		if(!eventId.equals("")){
    			//updater.updateEventContents(eventId);
				//db.putLocalUpdate(eventId, result);
    		}
    		
    		if(!sql.equals("")){
    			
    		}
    		
    	}else if(notification.getType() == Notification.TYPE_ANNOUNCEMENT){
    		int type = notification.getAnnouncementType();
    		int id = notification.getAnnouncementId();
    		String title = notification.getAnnouncementHeading();
    		String body = notification.getAnnouncementText();
    		String time = notification.getAnnouncementTime();
    		Announcement announcement = new Announcement(id,String.valueOf(type), title, body, time);
    	    db.Add(announcement); 
    	}
        
      
        /*
         if(isWishListed(code)){
         putNotification(announcement)
         }
         */
        Log.i("aaki", "db created");
    }
    
//    private void putNotification(Announcement announcement){
//    	mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);  
//    	Intent intent = new Intent(this, NotificationActivity.class);
//        intent.putExtra("jsonData", jString);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, announcement.gettype(), intent, 0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//            .setContentTitle(announcement.getTitle())
//            .setSmallIcon(R.drawable.menu_announce)
//            .setStyle(new NotificationCompat.BigTextStyle()
//            .bigText(announcement.getBody()))
//            .setContentText(announcement.getBody());
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(announcement.gettype(), mBuilder.build());
//    }
    
    
    
}

