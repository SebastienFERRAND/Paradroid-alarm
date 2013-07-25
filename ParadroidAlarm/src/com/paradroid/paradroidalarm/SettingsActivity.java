package com.paradroid.paradroidalarm;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.paradroid.paradroidalarm.R;
import com.paradroid.helper.ParamHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SettingsActivity extends SherlockFragmentActivity{
	
//	private ToggleButton tb;
	private EditText snoozeMinutes;
	private Button contactButton;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.settings);
		
		ActionBar ab = getSupportActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		
		contactButton = (Button) this.findViewById(R.id.buttonContact);
		
		contactButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"paradroidco@gmail.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
				i.putExtra(Intent.EXTRA_TEXT   , "Dear developper,");
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					
				}
				
			}
		});
		
		
//		tb = (ToggleButton) findViewById(R.id.togglebuttontalk);
//		tb.setChecked(ParamHelper.getTalk());
		/*snoozeMinutes = (EditText) findViewById(R.id.pick_time_snooze);
		
		snoozeMinutes.setText(ParamHelper.getSnooze()+"");
		
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
				
			}
		});*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.settingsmenu, menu);
		return true;
	} 

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		// do stuff with CalendarContract
		case R.id.action_back:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/*public void onToggleClicked(View view) {
		
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			ParamHelper.pushTalk(true);
		} else {
			ParamHelper.pushTalk(false);
		}
	}*/

	@Override
	public void onDestroy(){
		/*try{
			
		}catch(Exception e){
			ParamHelper.pushSnooze(Float.parseFloat(snoozeMinutes.toString()));
		}*/
		super.onDestroy();
	}
}
