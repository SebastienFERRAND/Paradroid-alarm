package com.paradroid.paradroidalarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.paradroid.adapter.AlarmAdapter;
import com.paradroid.database.AlarmDataSource;
import com.paradroid.database.DataBaseHelper;
import com.paradroid.helper.ParamHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appflood.AppFlood;
import com.appflood.AppFlood.AFEventDelegate;
import com.appflood.AppFlood.AFRequestDelegate;

public class MainActivity extends SherlockFragmentActivity {

	public static final String APP_TAG = "AlarmApp";
	private static TimePickerFragment df;
	private ListView listAlarms;
	private static Cursor c;
	private static AlarmAdapter aa;
	public static AlarmDataSource nds;
	public static MainActivity ma;

	public static boolean fromModify = false;
	public static int idTime;

	private TextView txti;
	private ImageView alarm_face;
	
	private static boolean fired;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

//		AppFlood.initialize(this, "1hHVHQBuMVjzx6wY", "jDIj7PZ81499L51fdf416", AppFlood.AD_ALL);
//		AppFlood.destroy();
		
		ActionBar ab = getSupportActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);

		ParamHelper.initParamHelper(this);

		ma = this;

		nds = new AlarmDataSource(this);

		c = nds.getAllAlarm();
		aa = new AlarmAdapter(this, c);

		alarm_face = (ImageView) findViewById(R.id.alarm_face);
		alarm_face.setTag(R.drawable.final_icon_smile);
		alarm_face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int idIm = (Integer) v.getTag();
				
				switch(idIm){
				case R.drawable.final_icon_smile:
					alarm_face.setImageResource(R.drawable.final_icon_normal);
					alarm_face.setTag(R.drawable.final_icon_normal);
					txti.setText("Hey !");
					break;
				case R.drawable.final_icon_normal:
					alarm_face.setImageResource(R.drawable.final_icon_unhappy);
					alarm_face.setTag(R.drawable.final_icon_unhappy);
					txti.setText("Ouch !");
					break;
				case R.drawable.final_icon_unhappy:
					alarm_face.setVisibility(View.GONE);
					txti.setVisibility(View.GONE);
					break;
				}
			}
		});

		txti = (TextView) findViewById(R.id.instructions_text);
		txti.setTag(1);
		txti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int idIm = (Integer) v.getTag();
				
				switch(idIm){
				case 1:
					txti.setText(R.string.instructions2);
					txti.setTag(2);
					break;
				case 2:
					txti.setText(R.string.instructions3);
					txti.setTag(3);
					break;
				case 3:
					txti.setText(R.string.instructions4);
					txti.setTag(4);
					break;
				case 4:
					txti.setText(R.string.instructions5);
					txti.setTag(5);
					break;
				case 5:
					txti.setText(R.string.instructions6);
					txti.setTag(6);
					break;
				case 6:
					txti.setText(R.string.instructions1);
					txti.setTag(1);
					break;
				}
			}
		});

		ParamHelper.pushTimeOpen(ParamHelper.getTimeOpen() + 1);

		listAlarms = (ListView) findViewById(R.id.listAlarm);
		listAlarms.setAdapter(aa);

		/*addAlarm = (Button) findViewById(R.id.add_alarm);
		addAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				loadTimer();
			}
		});*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.delete_or_update_time_menu, menu);
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		// do stuff with CalendarContract
		case R.id.action_settings:
			Intent viewIntent = new Intent(this, SettingsActivity.class);
			startActivityForResult(viewIntent, 2);

			break;

		case R.id.add_alarm:
			loadTimer();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener,
	TimePickerDialog.OnDismissListener{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			if (fromModify){
				Cursor cur = nds.getAlarm(idTime);
				cur.moveToFirst();
				hour = cur.getInt(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
				minute = cur.getInt(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
			}

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onDismiss(DialogInterface dialog){
			super.onDismiss(dialog);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			
			if (fired == true) {
	            Log.i("PEW PEW", "Double fire occured. Silently-ish returning");
	            return;
	        } else {
	            //first time fired
	            fired = true;
	        }
			int dayOfWeek = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
			ArrayList<Integer> days = new ArrayList<Integer>();
			days.add(dayOfWeek);
			Cursor c = MainActivity.nds.getAlarm(idTime);
			c.moveToFirst();
			if (fromModify){
				days = MainActivity.nds.getDays(idTime);
				MainActivity.nds.modifyTime(idTime, hourOfDay, minute);
				MainActivity.on(ma, idTime, minute, hourOfDay, MainActivity.arrayListToInt(days));
				refresh();
				fromModify = false;
			}else{
				int id = createAlarm(hourOfDay, minute, days);

				Intent viewIntent = new Intent(ma, PickADayActivity.class);
				viewIntent.putExtra("idNote", (Integer) id);
				((MainActivity) ma).startActivityForResult(viewIntent, 1);
			}
		}
	}

	public static int createAlarm(int hourOfDay, int minute, ArrayList<Integer> days){

		int dayOfWeek = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
		int id = nds.createAlarm(hourOfDay, minute, dayOfWeek, 5);
		idTime = id;
		refresh();
		Cursor c = MainActivity.nds.getAlarm(id);
		c.moveToFirst();
		MainActivity.on(ma, id, minute, hourOfDay, c.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT));
		return id;
	}

	public static int arrayListToInt(ArrayList<Integer> days) {
		int day = 0;
		if (days.contains(1)){
			day+=1;
		}
		if (days.contains(2)){
			day+=20;
		}
		if (days.contains(3)){
			day+=300;
		}
		if (days.contains(4)){
			day+=4000;
		}
		if (days.contains(5)){
			day+=50000;
		}
		if (days.contains(6)){
			day+=600000;
		}
		if (days.contains(7)){
			day+=7000000;
		}
		return day;
	}

	public static void off(int id) {
		//		c = nds.getAllAlarm();
		//		aa.changeCursor(c);
		Intent intent = new Intent(ma, AlarmReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(ma,
				(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}

	public static void offAndOut(int id) {
		Intent intent = new Intent(ma, AlarmReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(ma,
				(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pendingIntent);

		refresh();
	}

	public static void on(Context cont, int id, int minute, int hourOfDay, int days) {

		//Create an offset from the current time in which the alarm will go off.
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

		ArrayList<Integer> listDays = MainActivity.intToArray(days);

		int nextDay;

		nextDay = getNextRing(cal, listDays);

		cal.set(Calendar.DAY_OF_WEEK, nextDay);

		if (cal.before(today)){
			cal.add(Calendar.DATE, 7);
		}

		Intent intent = new Intent(cont, AlarmReceiverActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("minute", minute);
		intent.putExtra("hourOfDay", hourOfDay);
		intent.putExtra("days", days);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		
		Log.v("Test", cal.get(Calendar.HOUR_OF_DAY)+"");

		PendingIntent pendingIntent = PendingIntent.getActivity(cont,
				(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)cont.getSystemService(Activity.ALARM_SERVICE);

		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}

	public static int getNextRing(Calendar cal, ArrayList<Integer> listDays) {
		int day = cal.get(Calendar.DAY_OF_WEEK);
		Calendar today = Calendar.getInstance();
		if (listDays.size() == 0){
			return 0;
		}

		for (int i = 0; i < listDays.size(); i++){
			//			Log.v("DAYS", "List days " + MainActivity.fromIntToDay(listDays.get(i)));
		}

		if (!(listDays.size() == 0)){
			for (int i = day; i <= 7; i++){
				if (listDays.contains(i)){
					if (day == i){
						if(cal.after(today)){
							return i;
						}
						//					}else{
						//						Log.v("DAYS", "return " + MainActivity.fromIntToDay(listDays.get(i)));
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

	public static ArrayList<Integer> intToArray(int days) {

		ArrayList<Integer> arrayDays = new ArrayList<Integer>();
		while (days > 0) {
			int value = days%10;
			arrayDays.add(value);
			days/=10;
		}
		return arrayDays;
	}

	public static void snooze(Context cont, int id, int minute, int hourOfDay) {

		//Create an offset from the current time in which the alarm will go off.
		Calendar cal = Calendar.getInstance();
		minute += ParamHelper.getSnooze();
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

		Intent intent = new Intent(cont, AlarmReceiverActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("minute", minute);
		intent.putExtra("hourOfDay", hourOfDay);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);

		PendingIntent pendingIntent = PendingIntent.getActivity(cont,
				(int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)cont.getSystemService(Activity.ALARM_SERVICE);

		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
		
//		Toast.makeText(cont.getApplicationContext(), cont.getResources().getString(R.string.snoozed), Toast.LENGTH_LONG).show();
	}

	public static void loadTimer() {

		fired = false;
		df = new TimePickerFragment();
		df.show(ma.getSupportFragmentManager(), "timePicker");
		

	}

	public static String fromIntToDay(int day){
		String stringDay = "";
		if (day == 1){
			stringDay = ma.getString(R.string.day7);
		}else if (day == 2){
			stringDay = ma.getString(R.string.day1);
		}else if (day == 3){
			stringDay = ma.getString(R.string.day2);	
		}else if (day == 4){
			stringDay = ma.getString(R.string.day3);
		}else if (day == 5){
			stringDay = ma.getString(R.string.day4);
		}else if (day == 6){
			stringDay = ma.getString(R.string.day5);
		}else if (day == 7){
			stringDay = ma.getString(R.string.day6);

		}
		return stringDay;
	}

	public static void refresh() {
		try{
			c = nds.getAllAlarm();
			aa.changeCursor(c);
		}catch(Exception e){

		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
		overridePendingTransition(R.anim.back_to_left,R.anim.back_to_right);
	}

	@Override
	public void onDestroy(){
		//		AppFlood.showFullScreen(this);
		super.onDestroy();
	}
}
