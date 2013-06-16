package com.paradroid.paradroidalarm;

import com.example.helper.ParamHelper;
import com.example.paradroidalarm.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity{
	
	private ToggleButton tb;
	private EditText snoozeMinutes;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.settings);
		tb = (ToggleButton) findViewById(R.id.togglebuttontalk);
		tb.setChecked(ParamHelper.getTalk());
		snoozeMinutes = (EditText) findViewById(R.id.pick_time_snooze);
		
		snoozeMinutes.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable ed) {
				ParamHelper.pushSnooze(Float.parseFloat(ed.toString()));
			}
		});
	}

	public void onToggleClicked(View view) {
		
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			ParamHelper.pushTalk(true);
		} else {
			ParamHelper.pushTalk(false);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
