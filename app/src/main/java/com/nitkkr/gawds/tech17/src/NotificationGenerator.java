package com.nitkkr.gawds.tech17.src;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Event;
import com.nitkkr.gawds.tech17.activity.Exhibition;
import com.nitkkr.gawds.tech17.activity.Home;
import com.nitkkr.gawds.tech17.activity.NotificationPage;
import com.nitkkr.gawds.tech17.activity.TeamPage;
import com.nitkkr.gawds.tech17.database.Database;
import com.nitkkr.gawds.tech17.model.EventKey;

import java.util.Date;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by Home Laptop on 21-Dec-16.
 */

public class NotificationGenerator
{
	Context context;
	private int LastId;

	public NotificationGenerator(Context context)
	{
		this.context = context;
		SharedPreferences preferences = context.getSharedPreferences("Generator", Context.MODE_PRIVATE);
		LastId = preferences.getInt("LastId", 1000);
	}

	private NotificationCompat.Builder basicBuild(int IconID, String Ticker)
	{
		return new NotificationCompat.Builder(context)
				.setSmallIcon(IconID)
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), IconID))
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setTicker(Ticker)
				.setAutoCancel(true);
	}

	private void complexBuild(int IconID, String Label, String Ticker, String Message, RemoteViews view, Intent intent)
	{
		NotificationCompat.Builder builder = basicBuild(IconID, Ticker);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			builder = builder.setContent(view)
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
		}
		else
		{
			builder = builder.setContentTitle(Label)
					.setContentText(Message)
					.setSmallIcon(IconID)
					.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), IconID))
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
					.setWhen(new Date().getTime());
		}

		PendingIntent pIntent = getActivity(context, LastId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pIntent);

		Notification notification = builder.build();

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		( (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE) ).notify(LastId, notification);

		saveCache();
	}

	public void eventNotification(String Label, String Ticker, String Message, EventKey key)
	{
		int IconID = 0;

		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.layout_notification_event);

		Bundle bundle = new Bundle();
		bundle.putSerializable("Event", key);
		bundle.putInt("NotificationID", LastId);
		Intent intent;

		if (key.getEventID() != Database.getInstance().getExhibitionDB().getExhibition(key.getEventID()).getEventID())
		{
			intent = new Intent(context, Event.class);
		}
		else
		{
			intent = new Intent(context, Exhibition.class);
		}

		intent.putExtras(bundle);

		complexBuild(IconID, Label, Ticker, Message, view, intent);
	}

	public void eventResultNotification(String Label, String Ticker, String Message, EventKey key)
	{
		int IconID = 0;

		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.layout_notification_event);

		Bundle bundle = new Bundle();
		bundle.putSerializable("Event", key);
		bundle.putInt("NotificationID", LastId);
		bundle.putInt("PageID", 3);
		Intent intent = new Intent(context, Event.class);

		intent.putExtras(bundle);

		complexBuild(IconID, Label, Ticker, Message, view, intent);
	}

	public void inviteNotification(long Count)
	{
		NotificationCompat.Builder builder = basicBuild(R.drawable.people_icon, "Team Invite");

		builder = builder.setContentTitle("Team Invite")
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setWhen(new Date().getTime())
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_MAX);

		if (Count==1)
			builder=builder.setContentText("You have " + Count + " new Team Invite");
		else
			builder=builder.setContentText("You have " + Count + " new Team Invites");

		Intent intent=new Intent(context, TeamPage.class);
		intent.putExtra("PageID",1);
		intent.putExtra("IsNotification",true);

			PendingIntent pIntent = PendingIntent.getActivity(context, 998, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			builder = builder.setContentIntent(pIntent);

		Notification notification = builder.build();

			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;

		( (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE) ).notify(998, notification);
	}

	public void messageNotification(long Count)
	{
		NotificationCompat.Builder builder = basicBuild(R.drawable.notification_icon, "Techspardha '17");

		builder = builder.setContentTitle("Techspardha '17")
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setWhen(new Date().getTime())
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_MAX);
		if(Count==1)
			builder=builder.setContentText("You have " + Count + " new Notification");
		else
			builder=builder.setContentText("You have " + Count + " new Notifications");

		Intent intent=new Intent(context, NotificationPage.class);
		intent.putExtra("PageID",1);
		intent.putExtra("IsNotification",true);

		PendingIntent pIntent = PendingIntent.getActivity(context, 996, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		builder = builder.setContentIntent(pIntent);

		Notification notification = builder.build();

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		( (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE) ).notify(996, notification);
	}

	public void simpleNotification(String Label, String Ticker, String Message)
	{
		int IconID = 0;

		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.layout_notification_event);

		Intent intent = new Intent(context, Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		complexBuild(IconID, Label, Ticker, Message, view, intent);
	}

	public int pdfNotification(String Label, String Ticker, String Message)
	{
		pdfNotification(LastId, Label, Ticker, Message, null, false);
		saveCache();
		return LastId - 1;
	}

	public void pdfNotification(int ID, String Label, String Ticker, String Message, Intent intent, boolean cancelOnClick)
	{
		NotificationCompat.Builder builder = basicBuild(R.drawable.download_icon, Ticker);

		builder = builder.setContentTitle(Label)
				.setContentText(Message)
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setWhen(new Date().getTime())
				.setAutoCancel(true)
				.setPriority(Notification.PRIORITY_MAX);

		if (intent != null)
		{
			PendingIntent pIntent = PendingIntent.getActivity(context, ID, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			builder = builder.setContentIntent(pIntent);
		}
		Notification notification = builder.build();

		if (cancelOnClick)
		{
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}

		( (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE) ).notify(ID, notification);
	}

	private void saveCache()
	{
		SharedPreferences.Editor editor = context.getSharedPreferences("Generator", Context.MODE_PRIVATE).edit();
		LastId++;
		editor.putInt("LastId", LastId);
		editor.apply();
	}
}
