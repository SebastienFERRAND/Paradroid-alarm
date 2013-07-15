package com.paradroid.paradroidalarm;

import java.util.ArrayList;
import java.util.Calendar;

import com.paradroid.database.AlarmDataSource;
import com.paradroid.database.DataBaseHelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;


public class MyBroadcastReceiver extends BroadcastReceiver {
	

	public static AlarmDataSource nds;
	private static Context contextf;
	private static Cursor c;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	contextf = context;
		nds = new AlarmDataSource(context);

		Cursor c = nds.getAllAlarm();
		
		while (c.moveToNext()) {
		    int day = c.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT);
		    int hour = c.getInt(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
		    int id = c.getInt(DataBaseHelper.DATABASE_ID_ALARM_INT);
		    int minute = c.getInt(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
		    int sound = c.getInt(DataBaseHelper.DATABASE_SOUND_ALARM_INT);
		    int snooze = c.getInt(DataBaseHelper.DATABASE_TIME_SNOOZE_ALARM_INT);
		    int onOff = c.getInt(DataBaseHelper.DATABASE_ON_OFF_INT);

		    if (onOff == 1){
			    this.on(id, minute, hour, day);
		    }
		    
		}
		
    }
    
    public static int getNextRing(Calendar cal, ArrayList<Integer> listDays) {
		int day = cal.get(Calendar.DAY_OF_WEEK);
		Calendar today = Calendar.getInstance();
				for (int i = 0; i < listDays.size(); i++){
					//			lov.v("DAYS", "List days " + MainActivity.fromIntToDay(listDays.get(i)));
				}

		if (!(listDays.size() == 0)){
			for (int i = day; i <= 7; i++){
				if (listDays.contains(i)){
					if (day == i){
						if(cal.after(today)){
							return i;
						}
						//					}else{
						//						lov.v("DAYS", "return " + MainActivity.fromIntToDay(listDays.get(i)));
					}else{
						return i;
					}

				}
			}

			for (int i = 1; i < day; i++){
				if (listDays.contains(i)){
					return i;
				}
			}

		}
		return day;
	}
    
    public static void on(int id, int minute, int hourOfDay, int days) {

		//Create an offset from the current time in which the alarm will go off.
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

		ArrayList<Integer> listDays = MainActivity.intToArray(days);

		int nextDay;

		nextDay = getNextRing(cal, listDays);

		if ((nextDay == days) && (cal.before(today))){
			cal.add(Calendar.DATE, 7);
		}

		cal.set(Calendar.DAY_OF_WEEK, nextDay);


		Intent intent = new Intent(contextf, AlarmReceiverActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("minute", minute);
		intent.putExtra("hourOfDay", hourOfDay);
		intent.putExtra("days", days);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);

		PendingIntent pendingIntent = PendingIntent.getActivity(contextf,
				(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)contextf.getSystemService(Activity.ALARM_SERVICE);

		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}
}