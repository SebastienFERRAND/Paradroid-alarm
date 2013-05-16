package com.paradroid.paradroidalarm;

import com.example.helper.ParamHelper;
import com.example.paradroidalarm.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity{
	
	ToggleButton tb;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.settings);
		tb = (ToggleButton) findViewById(R.id.togglebuttontalk);
		
		tb.setChecked(ParamHelper.getTalk());
	}

	public void onToggleClicked(View view) {
		
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		
		

		if (on) {
			ParamHelper.pushTalk(true);
			Log.v("Test", "on");
		} else {
			ParamHelper.pushTalk(false);
			Log.v("Test", "off");
			
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
