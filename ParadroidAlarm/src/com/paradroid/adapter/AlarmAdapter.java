package com.paradroid.adapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.paradroid.paradroidalarm.R;
import com.paradroid.business.RowInfo;
import com.paradroid.database.DataBaseHelper;
import com.paradroid.paradroidalarm.MainActivity;
import com.paradroid.paradroidalarm.PickADayActivity;
import com.paradroid.paradroidalarm.MainActivity.TimePickerFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;


public class AlarmAdapter extends CursorAdapter {
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private TimePickerFragment df;
	private Typeface tf ;

	static final int ANIMATION_DURATION = 200;
	
	public AlarmAdapter(Context context, Cursor c) {
		super(context, c);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context); 
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/digital-7.ttf");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = mLayoutInflater.inflate(R.layout.alarm_row, parent, false);

		TextView hour_text = (TextView) v.findViewById(R.id.time);
		hour_text.setTypeface(tf);

		//		TextView day_text = (TextView) v.findViewById(R.id.days);
		//		day_text.setTypeface(tf);

		return v;
	}


	@Override
	public void bindView(View v, Context context, Cursor c) {
		int _id = (int) c.getDouble(DataBaseHelper.DATABASE_ID_ALARM_INT);
		int hour = (int) c.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
		int minute = (int) c.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
		int day = (int) c.getDouble(DataBaseHelper.DATABASE_DAY_ALARM_INT);
		int onOff = (int) c.getDouble(DataBaseHelper.DATABASE_ON_OFF_INT);

		/**
		 * Set Date
		 */

		boolean onOffb = true;

		if(onOff == 1){
			onOffb = true;
		}else{
			onOffb = false;
		}

		ToggleButton onOffButton = (ToggleButton) v.findViewById(R.id.togglebuttononoff);
		onOffButton.setChecked(onOffb);
		
		onOffButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int id = (Integer) v.getTag();

				Cursor cur = MainActivity.nds.getAlarm(id);
				
				if (cur.getCount() !=0){

					cur.moveToFirst();
					int hourP = (int) cur.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
					int minuteP = (int) cur.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
					int onOff = (int) cur.getDouble(DataBaseHelper.DATABASE_ON_OFF_INT);

					if (onOff == 0){
						MainActivity.nds.modifyCheck(id, 1);
						MainActivity.refresh();
//						MainActivity.on(id, minuteP, hourP, cur.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT));
					}else{
						MainActivity.nds.modifyCheck(id, 0);
						MainActivity.refresh();
//						MainActivity.off(id);
					}
				}
			}
		});

			

		TextView hour_text = (TextView) v.findViewById(R.id.time);
		String zeroHour ="";
		String zeroMinute ="";
		String ampm ="";
		if (hour_text != null) {
			if (hour < 10){
				zeroHour = "0";
			}

			if (minute < 10){
				zeroMinute = "0";
			}

			if (!DateFormat.is24HourFormat(mContext))
			{
				if(hour>12)
				{
					hour -= 12;
					ampm = "pm";
				} else {
					ampm = "am";
				}
			}else{
				
			}
			hour_text.setText(zeroHour + hour + ":" + zeroMinute + minute + " " + ampm);
		}

		hour_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity.fromModify = true;
				MainActivity.idTime = (Integer) v.getTag();
				MainActivity.loadTimer();
			}
		});


		GregorianCalendar calendar = new GregorianCalendar();

		//        lov.v("DAYS", "THIS IS THE LIST" + day);

		ArrayList<Integer> listDays = MainActivity.intToArray(day);

		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		int numberDay = MainActivity.getNextRing(calendar, listDays);

		calendar.set(Calendar.DAY_OF_WEEK, numberDay);

		TextView day_text = (TextView) v.findViewById(R.id.days);
		if (day_text != null) {
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


		Button deleteAlarm = (Button) v.findViewById(R.id.deleteAlarm);
		deleteAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RowInfo rid = (RowInfo) v.getTag();
				deleteCell(rid.v, rid.indexInfo, rid.idInfo);
				MainActivity.offAndOut(rid.idInfo);
			}
		});

		RowInfo ri = new RowInfo(_id, v.getId(), v);
		deleteAlarm.setTag(ri);
		
		onOffButton.setTag(_id);
		day_text.setTag(_id);
		hour_text.setTag(_id);

	}


//	public static class ViewHolder {
//		public ToggleButton togglebuttononoff;
//		public TextView time;
//		public TextView days;
//		public Button deleteAlarm;
//	}
	
	private void deleteCell(final View v, final int index, final int id) {
		final int initialHeight = v.getMeasuredHeight();
		Log.v("REBOOT", "Index " + index );
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				MainActivity.nds.deleteAlarm(id);
				MainActivity.refresh();
			}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationStart(Animation animation) {}
		};

		collapse(v, al);
	}

	private void collapse(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.getLayoutParams().height = initialHeight;
				}
				else {
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al!=null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}
}