package com.paradroid.paradroidalarm;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appflood.AppFlood;
import com.paradroid.paradroidalarm.R;
import com.paradroid.helper.ParamHelper;
import com.paradroid.helper.SoundHelper;

import android.widget.SeekBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

	private SoundHelper sm;

	private SeekBar seekBar;

	final int MAX_INTENSITY = 33000;

	private Button browseMusic;

	final int RINGTONE_PICK = 1;
	
	private TextView chooseRingtone;
	
	private EditText seconds_ringing;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.settings);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		
		seconds_ringing = (EditText) findViewById(R.id.pick_ring_for);
		seconds_ringing.setText(ParamHelper.getIntervalSongVoice() + "");

		sm = new SoundHelper();
		
		chooseRingtone = (TextView) findViewById(R.id.choose_ringtone);
		chooseRingtone.setText(getResources().getString(R.string.choose_ringtone) + " " + ParamHelper.getURISong());

		browseMusic = (Button) findViewById(R.id.browseButton);
		browseMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType(MediaStore.Audio.Media.CONTENT_TYPE);
				intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
				intent.putExtra("extra", Intent.EXTRA_TITLE);
				startActivityForResult(Intent.createChooser(intent, "Ringtone"), RINGTONE_PICK);

			}
		});

		activateButton = (Button) findViewById(R.id.activateButton);

		con = this;

		seekBar = (SeekBar)findViewById(R.id.seekBar1); 
		seekBarValue = (TextView)findViewById(R.id.real_intensity);


		int pourcentage = ParamHelper.getIntensity() * 100 / MAX_INTENSITY;

		seekBarValue.setText(pourcentage + "%");

		seekBar.setMax(MAX_INTENSITY);

		seekBar.setProgress(ParamHelper.getIntensity());

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			@Override 
			public void onProgressChanged(SeekBar seekBar, int progress, 
					boolean fromUser) { 
				// TODO Auto-generated method stub 
				int newProgress = progress;

				int pourcentage = newProgress * 100 / MAX_INTENSITY;

				seekBarValue.setText(String.valueOf(pourcentage) + "%");
				ParamHelper.pushIntensity(newProgress);
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
					activateButton.setText(getResources().getString(R.string.tap));
					activated = false;

					double amp = sm.getAmplitude();
					sm.stop();

					int pourcentage = (int) (amp * 100 / MAX_INTENSITY);

					seekBar.setProgress((int) amp);
					seekBarValue.setText(String.valueOf(pourcentage) + "%"); 
					ParamHelper.pushIntensity((int) amp);


				}else{

					activateButton.setBackgroundDrawable(con.getResources().getDrawable(R.drawable.round_green));
					activateButton.setText(getResources().getString(R.string.tap_again));
					activated = true;

					sm.start();
					sm.getAmplitude();
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
		
		try{
			Log.v("RECON", snoozeMinutes.getText().toString());
			ParamHelper.pushIntervalSongVoice(Integer.parseInt(seconds_ringing.getText().toString()));
		}catch(Exception e){

		}
		

		try{
			sm.stop();
		}catch(Exception e){

		}

		super.onDestroy();
	}

	public void openWebURL( String inURL ) {
		Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

		startActivity( browse );
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {

		case RINGTONE_PICK:
			if (RESULT_OK == resultCode) {
				Log.v("Test", intent.getData() + "");
				
				ParamHelper.pushURISong(intent.getData()+"");
				
			}
			break;
		}
	}
}
