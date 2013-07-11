package com.paradroid.adapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.paradroid.paradroidalarm.R;
import com.paradroid.database.DataBaseHelper;
import com.paradroid.paradroidalarm.MainActivity;
import com.paradroid.paradroidalarm.PickADayActivity;
import com.paradroid.paradroidalarm.MainActivity.TimePickerFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;


public class AlarmAdapter extends CursorAdapter {
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ToggleButton onOff;
	private TimePickerFragment df;
	public AlarmAdapter(Context context, Cursor c) {
		super(context, c);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context); 
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mLayoutInflater.inflate(R.layout.alarm_row, parent, false);


		TextView hour_text = (TextView) v.findViewById(R.id.time);
		if (hour_text != null) {
		}

		TextView day_text = (TextView) v.findViewById(R.id.days);
		if (day_text != null) {
		}

		onOff = (ToggleButton) v.findViewById(R.id.togglebuttononoff);
		onOff.setChecked(true);

		Button deleteAlarm = (Button) v.findViewById(R.id.deleteAlarm);

		return v;
	}


	@Override
	public void bindView(View v, Context context, Cursor c) {
		int _id = (int) c.getDouble(DataBaseHelper.DATABASE_ID_ALARM_INT);
		int hour = (int) c.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
		int minute = (int) c.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
		int day = (int) c.getDouble(DataBaseHelper.DATABASE_DAY_ALARM_INT);
		/**
		 * Set Date
		 */

		 TextView hour_text = (TextView) v.findViewById(R.id.time);
		String zeroHour ="";
		String zeroMinute ="";
		if (hour_text != null) {
			if (hour < 10){
				zeroHour = "0";
			}

			if (minute < 10){
				zeroMinute = "0";
			}
			hour_text.setText(zeroHour + hour + ":" + zeroMinute + minute);
		}

		hour_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.fromModify = true;
				MainActivity.idToModify = (Integer) v.getTag();
				Log.v("ID", "id time : " + MainActivity.idToModify);
				MainActivity.loadTimer();
			}
		});


		GregorianCalendar calendar = new GregorianCalendar();

		//        Log.v("DAYS", "THIS IS THE LIST" + day);

		ArrayList<Integer> listDays = MainActivity.intToArray(day);

		Log.v("BEG", "TO ISENGARD ");

		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		int numberDay = MainActivity.getNextRing(calendar, listDays);

		calendar.set(Calendar.DAY_OF_WEEK, numberDay);

		TextView day_text = (TextView) v.findViewById(R.id.days);
		if (day_text != null) {
			//        	Log.v("DAYS", numberDay + " ; numberDay");
			day_text.setText(MainActivity.fromIntToDay(numberDay));
		}

		day_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent(mContext, PickADayActivity.class);
				viewIntent.putExtra("idNote", (Integer) v.getTag());
				((MainActivity) mContext).startActivityForResult(viewIntent, 1);

			}
		});

		onOff = (ToggleButton) v.findViewById(R.id.togglebuttononoff);
		onOff.setChecked(true);
		onOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				int id = (Integer) buttonView.getTag();

				Cursor cur = MainActivity.nds.getAlarm((int) id);
				cur.moveToFirst();
				int hourP = (int) cur.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
				int minuteP = (int) cur.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);

				if (isChecked){
					Cursor c = MainActivity.nds.getAlarm(id);
					c.moveToFirst();
					MainActivity.on(id, minuteP, hourP, c.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT));
				}else{
					MainActivity.off(id);
				}

			}
		});


		Button deleteAlarm = (Button) v.findViewById(R.id.deleteAlarm);
		deleteAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = (Integer) v.getTag();
				MainActivity.nds.deleteAlarm(id);
				MainActivity.offAndOut(id);
			}
		});
		onOff.setTag(_id);
		deleteAlarm.setTag(_id);
		day_text.setTag(_id);
		hour_text.setTag(_id);

	}
}