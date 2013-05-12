package com.paradroid.paradroidalarm;

import java.util.Calendar;

import com.adylitica.database.AlarmDataSource;
import com.example.paradroidalarm.R;
import com.paradroid.adapter.AlarmAdapter;

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

public class MainActivity extends FragmentActivity {

	private Button addAlarm;
	private TimePickerFragment df;
	private ListView listAlarms;
	private static Cursor c;
	private static AlarmAdapter aa;
	private static AlarmDataSource nds;
	private static MainActivity ma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

				df = new TimePickerFragment();
				df.show(getSupportFragmentManager(), "timePicker");
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

			Log.v("Test", "item id " + info.id);
			Log.v("Test", "position " + info.position);
			return true;
		}else if(item.getTitle().equals("Delete Alarm")){

			nds.deleteAlarm(info.id);

			c = nds.getAllAlarm();
			aa.changeCursor(c);

			Log.v("Test", "item id " + info.id);
			Log.v("Test", "position " + info.position);
			return true;

		}else{
			return super.onContextItemSelected(item);
		}

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

			long id = nds.createAlarm(hourOfDay, minute, "lundi", 5);
			c = nds.getAllAlarm();
			aa.changeCursor(c);

			//Create an offset from the current time in which the alarm will go off.
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);


			Log.v("Test", cal.getTime() + "2");

			//Create a new PendingIntent and add it to the AlarmManager
			Intent intent = new Intent(ma, AlarmReceiverActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(ma,
					(int) id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = 
					(AlarmManager)ma.getSystemService(Activity.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					pendingIntent);
		}
	}

}
