package com.winit.airarabia.insider;

import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.winit.airarabia.R;
import com.winit.airarabia.utils.LogUtils;

public class GCMNotificationIntentService extends IntentService 
{
	
	private Notification notification;
	private Intent notificationIntent;
	private PendingIntent pendingIntent;
     
	public static int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	
	private static final String messageTag = "subject";
	private static final String urlTag = "messageurl";
	private static final String smsbody = "smsbody";
	private static final String notification_type = "notification_type";
	
	private static final String strType = "type";
	
	String message = "";
	String url = "";
	String type = "";
	
	
	int icon = R.drawable.airarabia_name_logo;
	long when = System.currentTimeMillis();
	//String title = getApplicationContext().getString(R.string.app_name);

	public GCMNotificationIntentService() 
	{
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		Bundle bundle = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		Toast.makeText(this, "GCMNotificationIntentService", Toast.LENGTH_LONG).show();
		//String messageType = gcm.getMessageType(intent);
//        String actualMessage = ""+bundle.get(AppConstants.MESSAGE_KEY);
        
        if(bundle != null && (bundle.containsKey(urlTag.toLowerCase()) || bundle.containsKey(urlTag.toUpperCase())))
		{
			
//			if(bundle != null && bundle.keySet() != null)
//			{
//				Set<String> setKeys = bundle.keySet();
//				for (String string : setKeys) 
//				{
//					if(string.toLowerCase().equalsIgnoreCase(messageTag.toLowerCase()))
//					{
//						message = bundle.getString(string);
//					}
//					else if(string.toLowerCase().equalsIgnoreCase(urlTag.toLowerCase()))
//					{
//						url = bundle.getString(string);
//					}
//					else if(string.toLowerCase().equalsIgnoreCase(strType.toLowerCase()))
//					{
//						type = bundle.getString(string);
//					}
//				}
//			}
//			
//			LogUtils.infoLog("Url", url+"=");
//			LogUtils.infoLog("message", message+"=");
//			LogUtils.infoLog("Type", type+"=");
//			
//			if(isAppRunning())
//			{
//				notificationIntent = new Intent(getApplicationContext(),CEPNotificationDailogActivity.class);
//				notificationIntent.putExtra("CEPMessage", message);
//				notificationIntent.putExtra("Type", type);
//				notificationIntent.putExtra("Url", url);
//			}
//			else
//			{
//				if(MasafiPreferences.getIsLoggedIn(getApplicationContext()))
//				{
//					notificationIntent = new Intent(getApplicationContext(),CategoriesListActivity.class);
//					notificationIntent.putExtra("From", "NotificationScreen");
//					notificationIntent.putExtra("CEPMessage", message);
//					notificationIntent.putExtra("Type", type);
//					notificationIntent.putExtra("Url", url);
//				}
//				else
//				{
//					notificationIntent = new Intent(getApplicationContext(),CEPNotificationDailogActivity.class);
//					notificationIntent.putExtra("CEPMessage", message);
//					notificationIntent.putExtra("Type", type);
//					notificationIntent.putExtra("Url", url);
//				}
//			}
//			
//			mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
//			
//			notification = new Notification(icon, message, when);
//			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//					| Intent.FLAG_ACTIVITY_NEW_TASK);
//			int count = (int) System.currentTimeMillis();
//			pendingIntent = PendingIntent.getActivity(this, count,
//					notificationIntent, 0);
//			notification.setLatestEventInfo(this, "Masafi", message, pendingIntent);
//			notification.flags |= Notification.FLAG_AUTO_CANCEL;
//			mNotificationManager.notify(NOTIFICATION_ID++, notification);

			
//			pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//					this).setSmallIcon(R.drawable.logo)
//					.setContentTitle("GCM Notification")
//					.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//					.setContentText(message);
//
//			mBuilder.setContentIntent(pendingIntent);
//			mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
			
		}
        
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	
	private boolean isAppRunning()
	{
		boolean isAppRunning = false;
		ActivityManager activityManager  = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppInfo =  activityManager.getRunningAppProcesses();
        
        for(RunningAppProcessInfo appProcess : runningAppInfo)
        {
            if(appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE)
            {
                if(appProcess.processName.equalsIgnoreCase("com.winit.masafi.activities"))
                {
                	isAppRunning = true;
                	break;
                }
            }
        }
        return isAppRunning;
	}
	
	//local notification
//	private void sendNotification(String msg) 
//	{
//		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, CategoriesListActivity.class), 0);
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this).setSmallIcon(R.drawable.logo)
//				.setContentTitle("GCM Notification")
//				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//				.setContentText(msg);
//
//		mBuilder.setContentIntent(contentIntent);
//		mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
//		Log.d(TAG, "Notification sent successfully.");
//	}
}
