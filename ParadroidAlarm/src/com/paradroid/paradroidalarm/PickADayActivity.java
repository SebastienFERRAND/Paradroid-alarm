package com.paradroid.paradroidalarm;


import java.util.ArrayList;

import com.example.paradroidalarm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;


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
		id = intent.getIntExtra("id", 0);
		ArrayList<Integer> days = MainActivity.nds.getDays(id);
		if (days.contains(1)){
			sunday.setChecked(true);
		}else if (days.contains(2)){
			monday.setChecked(true);
		}else if (days.contains(3)){
			tuesday.setChecked(true);
		}else if (days.contains(4)){
			wednesday.setChecked(true);
		}else if (days.contains(5)){
			thursday.setChecked(true);
		}else if (days.contains(6)){
			friday.setChecked(true);
		}else if (days.contains(7)){
			saturday.setChecked(true);
		}
		
		
		super.onCreate(savedInstanceState);

	}
}
