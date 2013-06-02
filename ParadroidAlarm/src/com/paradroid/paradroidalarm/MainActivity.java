package com.paradroid.paradroidalarm;

import java.util.Calendar;
import java.util.Locale;

import com.adylitica.database.AlarmDataSource;
import com.example.helper.ParamHelper;
import com.example.paradroidalarm.R;
import com.paradroid.adapter.AlarmAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity {

	public static final String APP_TAG = "AlarmApp";
	private Button addAlarm;
	private static TimePickerFragment df;
	private ListView listAlarms;
	private static Cursor c;
	private static AlarmAdapter aa;
	public static AlarmDataSource nds;
	public static MainActivity ma;
	
	public static boolean fromModify = false;
	public static int idToModify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParamHelper.initParamHelper(this);

		ma = this;

		nds = new AlarmDataSource(this);

		c = nds.getAllAlarm();
		aa = new AlarmAdapter(this, c);


		listAlarms = (ListView) findViewById(R.id.listAlarm);

		listAlarms.setAdapter(aa);

		addAlarm = (Button) findViewById(R.id.add_alarm);
		addAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				loadTimer();
			}
		});

		registerForContextMenu(listAlarms);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.delete_or_update_time_menu, menu);
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		if(item.getTitle().equals("Edit Alarm")) {

			return true;
		}else if(item.getTitle().equals("Delete Alarm")){

			nds.deleteAlarm((int) info.id);

			c = nds.getAllAlarm();
			aa.changeCursor(c);

			Intent intent = new Intent(ma, AlarmReceiverActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(ma,
					(int) info.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = 
					(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);
			am.cancel(pendingIntent);

			return true;

		}else{
			return super.onContextItemSelected(item);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// do stuff with CalendarContract
		switch(item.getItemId())
		{
		case 1:
			Intent viewIntent = new Intent(this, SettingsActivity.class);
			startActivityForResult(viewIntent, 2);

			return false;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action
		menu.add(1, 1, 0, "Settings");
		return true;
	}

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener,
	TimePickerDialog.OnDismissListener{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}


		public void onDismiss(DialogInterface dialog){
			super.onDismiss(dialog);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			
			if (fromModify){
				MainActivity.nds.deleteAlarm(idToModify);
				MainActivity.offAndOut(idToModify);
			}
			createAlarm(hourOfDay, minute);
			fromModify = false;
		}
	}
	
	public static void createAlarm(int hourOfDay, int minute){
		
		int dayOfWeek = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
		int id = nds.createAlarm(hourOfDay, minute, dayOfWeek, 5);
		c = nds.getAllAlarm();
		aa.changeCursor(c);
		MainActivity.on(id, minute, hourOfDay);
	}

	public static void off(int id) {
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

		c = nds.getAllAlarm();
		aa.changeCursor(c);
	}

	public static void on(int id, int minute, int hourOfDay) {
		
		//Create an offset from the current time in which the alarm will go off.
		Calendar cal = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

		// récupérer l'alarme en question et programmer au jour approprié
		if (cal.before(today)){
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
		
		Intent intent = new Intent(ma, AlarmReceiverActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("minute", minute);
		intent.putExtra("hourOfDay", hourOfDay);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);


		PendingIntent pendingIntent = PendingIntent.getActivity(ma,
				(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);

		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}

	public static void snooze(int id, int minute, int hourOfDay) {
		
		//Create an offset from the current time in which the alarm will go off.
		Calendar cal = Calendar.getInstance();
		minute +=5;
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);

		Intent intent = new Intent(ma, AlarmReceiverActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("minute", minute);
		intent.putExtra("hourOfDay", hourOfDay);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(ma,
				(int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
				(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);

		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
	}

	public static void deleteAlarm(int id) {
		nds.deleteAlarm(id);
	}

	public static void loadTimer() {

		df = new TimePickerFragment();
		df.show(ma.getSupportFragmentManager(), "timePicker");
		
	}


}
