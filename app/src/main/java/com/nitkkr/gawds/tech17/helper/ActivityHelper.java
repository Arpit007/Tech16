package com.nitkkr.gawds.tech17.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.nitkkr.gawds.tech17.Main;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.activity.Home;
import com.nitkkr.gawds.tech17.activity.ListPage;
import com.nitkkr.gawds.tech17.api.Query;
import com.nitkkr.gawds.tech17.src.UpdateCheck;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Home Laptop on 20-Nov-16.
 */

public class ActivityHelper
{
	private static Context context = null;
	private static Bitmap bm;

	public static void setStatusBarColor(Activity activity)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			try
			{
				/*Window window = activity.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(ContextCompat.getColor(activity, statusBarColorID));*/
				if(bm!=null)
				{
					ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("Techspardha", bm, ContextCompat.getColor(activity, R.color.pin_screen_color));
					activity.setTaskDescription(taskDescription);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static Context getApplicationContext()
	{
		Context context1= Main.getContext();
		if(context1!=null)
			return context1;
		return context;
	}

	public static void setUpHelper(Context context)
	{
		ActivityHelper.context = context;
		try
		{
			bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ts_logo2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context,"Low on Memory, Exiting",Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					System.exit(-1);
				}
			}, 2000);
		}
	}

	public static boolean revertToHomeIfLast(Activity activity)
	{
		if (activity.isTaskRoot())
		{
			revertToHome(activity);
			return true;
		}
		return false;
	}

	public static void revertToHome(Activity activity)
	{
		Intent intent = new Intent(activity, Home.class);
		intent.putExtra("AnimStart", false);
		activity.startActivity(intent);
		activity.finish();
		activity.overridePendingTransition(R.anim.anim_none, R.anim.anim_none);
	}

	public static void startListActivity(Activity activity, String Label, Query query)
	{
		Intent intent = new Intent(activity, ListPage.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("Query", query);
		bundle.putString("Label", Label);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	public static boolean isDebugMode(Context context)
	{
		int appFlags=context.getApplicationInfo().flags;
		//return (appFlags & ApplicationInfo.FLAG_DEBUGGABLE)!=0;
		return false;
	}

	public static boolean isInternetConnected()
	{
		return isInternetConnected(getApplicationContext());
	}

	public static boolean isInternetConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public static void setCreateAnimation(Activity activity)
	{
		activity.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_none);
	}

	public static void setExitAnimation(Activity activity)
	{
		activity.overridePendingTransition(R.anim.anim_none, R.anim.anim_right_out);
	}

	public static void comingSoonSnackBar(String Message, Activity activity)
	{
		if (UpdateCheck.getInstance().isUpdateAvailable())
		{
			Snackbar.make(activity.findViewById(android.R.id.content), "Update to Access this Feature", Snackbar.LENGTH_LONG)
					.setAction("Update Now", new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							if(!isDebugMode(context))
								Answers.getInstance().logCustom(new CustomEvent("Updated App"));

							SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.Misc_Prefs), Context.MODE_PRIVATE).edit();

							Date date = new Date(preferences.getLong("Update_Date", new Date().getTime()));

							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							calendar.add(Calendar.HOUR, context.getResources().getInteger(R.integer.AfterUpdateHours));

							editor.putBoolean("Update", false);

							editor.putLong("Update_Date", calendar.getTime().getTime());
							editor.apply();

							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
							intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
							context.startActivity(intent);
						}
					})
					.setActionTextColor(ContextCompat.getColor(activity, R.color.neon_green))
					.show();
		}
		else
		{
			Snackbar.make(activity.findViewById(android.R.id.content), Message, Snackbar.LENGTH_SHORT).show();
		}
	}
}
