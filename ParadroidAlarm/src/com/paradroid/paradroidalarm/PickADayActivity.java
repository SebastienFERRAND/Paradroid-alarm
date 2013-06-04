package com.paradroid.paradroidalarm;


import java.util.ArrayList;

import com.adylitica.database.DataBaseHelper;
import com.example.paradroidalarm.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class PickADayActivity extends Activity {

	private int id;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;
	private CheckBox sunday;

	public void onCreate(Bundle savedInstanceState) {

		this.setContentView(R.layout.pick_days);

		monday = (CheckBox) findViewById(R.id.day1);
		tuesday = (CheckBox) findViewById(R.id.day2);
		wednesday = (CheckBox) findViewById(R.id.day3);
		thursday = (CheckBox) findViewById(R.id.day4);
		friday = (CheckBox) findViewById(R.id.day5);
		saturday = (CheckBox) findViewById(R.id.day6);
		sunday = (CheckBox) findViewById(R.id.day7);

		Intent intent = getIntent();
		id = intent.getIntExtra("idNote", 0);
		ArrayList<Integer> days = MainActivity.nds.getDays(id);
		if (days.contains(1)){
			sunday.setChecked(true);
		}
		if (days.contains(2)){
			monday.setChecked(true);
		}
		if (days.contains(3)){
			tuesday.setChecked(true);
		}
		if (days.contains(4)){
			wednesday.setChecked(true);
		}
		if (days.contains(5)){
			thursday.setChecked(true);
		}
		if (days.contains(6)){
			friday.setChecked(true);
		}
		if (days.contains(7)){
			saturday.setChecked(true);
		}

		//Change on destroy
		
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public void onDestroy(){
		int days = 0;
		if (sunday.isChecked()){
			days = 1;
		}
		if (monday.isChecked()){
			days += 20;
		}
		if (tuesday.isChecked()){
			days += 300;
		}
		if (wednesday.isChecked()){
			days += 4000;
		}
		if (thursday.isChecked()){
			days += 50000;
		}
		if (friday.isChecked()){
			days += 600000;
		}
		if (saturday.isChecked()){
			days += 7000000;
		}
		
		MainActivity.nds.modifyDays(id, days);
		
		MainActivity.off(id);
		Cursor c = MainActivity.nds.getAlarm(id);
		c.moveToFirst();
		MainActivity.on(id, c.getInt(DataBaseHelper.DATABASE_MINUTE_ALARM_INT), c.getInt(DataBaseHelper.DATABASE_HOUR_ALARM_INT), days);
		
		super.onDestroy();
	}
	
}
