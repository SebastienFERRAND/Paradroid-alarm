package com.paradroid.paradroidalarm;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appflood.AppFlood;
import com.paradroid.paradroidalarm.R;
import com.paradroid.helper.ParamHelper;

import android.widget.SeekBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettingsActivity extends SherlockFragmentActivity{

	//	private ToggleButton tb;
	private EditText snoozeMinutes;
	private Button contactButton;
	private Button rateButton;


	private Button activateButton;

	private boolean activated = false;

	private Context con;
	
	private TextView seekBarValue;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.settings);

		ActionBar ab = getSupportActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);

		activateButton = (Button) findViewById(R.id.activateButton);

		con = this;

		SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar1); 
		seekBarValue = (TextView)findViewById(R.id.real_intensity);
		
		seekBar.setMax(1000);

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			@Override 
			public void onProgressChanged(SeekBar seekBar, int progress, 
					boolean fromUser) { 
				// TODO Auto-generated method stub 
				int newProgress = progress + 200;
				
				seekBarValue.setText(String.valueOf(newProgress)); 
			} 

			@Override 
			public void onStartTrackingTouch(SeekBar seekBar) { 
				// TODO Auto-generated method stub 
			} 

			@Override 
			public void onStopTrackingTouch(SeekBar seekBar) { 
				// TODO Auto-generated method stub 
			} 
		}); 
	

	activateButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (activated){

				activateButton.setBackgroundDrawable(con.getResources().getDrawable(R.drawable.round_red));
				activated = false;
			}else{

				activateButton.setBackgroundDrawable(con.getResources().getDrawable(R.drawable.round_green));
				activated = true;
			}
		}
	});

	snoozeMinutes = (EditText) findViewById(R.id.pick_time_snooze);
	int minute = (int) ParamHelper.getSnooze();
	snoozeMinutes.setText(minute+"");

	ToggleButton enableVoiceButton = (ToggleButton) findViewById(R.id.togglebuttonenablevoice);
	enableVoiceButton.setChecked(ParamHelper.getEnableVoice());

	enableVoiceButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked){
				ParamHelper.pushEnableVoice(true);
			}else{
				ParamHelper.pushEnableVoice(false);
			}
		}
	});

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

	rateButton = (Button) this.findViewById(R.id.buttonRate);
	rateButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			openWebURL("https://play.google.com/store/apps/details?id=com.paradroid.paradroidalarm");

		}
	});
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
	try{
		Log.v("RECON", snoozeMinutes.getText().toString());
		ParamHelper.pushSnooze(Integer.parseInt(snoozeMinutes.getText().toString()));
	}catch(Exception e){

	}
	super.onDestroy();
}

public void openWebURL( String inURL ) {
	Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

	startActivity( browse );
}
}
