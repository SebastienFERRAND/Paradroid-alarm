package com.paradroid.paradroidalarm;

import java.io.IOException;
import java.util.ArrayList;

import com.example.paradroidalarm.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmReceiverActivity extends Activity {
	private MediaPlayer mMediaPlayer; 
	private static final int REQUEST_CODE = 1234;

	private boolean stopALarmbool = false;
	private Context c;

	private Handler handlerRecon = new Handler();
	private Handler handlerSound = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm);

		Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
		stopAlarm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mMediaPlayer.stop();
				stopALarmbool = true;
				finish();
				return false;
			}
		});

		//        while (alarmOn){
			playSound(this, getAlarmUri());
			handlerRecon.postDelayed(startRecognition, 5000);//Message will be delivered in 1 second.
			//        }

	}

	//Here's a runnable/handler combo
	private Runnable startRecognition = new Runnable()
	{
		@Override
		public void run()
		{
			if(!stopALarmbool){
				stopSound();
				startVoiceRecognitionActivity();
				handlerSound.postDelayed(startSound, 5000);//Message will be delivered in 5 second.
			}
		}
	};

	//Here's a runnable/handler combo
	private Runnable startSound = new Runnable()
	{
		@Override
		public void run()
		{
			if(!stopALarmbool){
				Log.v("Test", "Indeed it goes here");
				finishActivity(REQUEST_CODE);
				playSound(c, getAlarmUri());
				handlerRecon.postDelayed(startRecognition, 5000);//Message will be delivered in 5 second.
			}
		}
	};

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e) {
			System.out.println("OOPS");
		}
	}

	private void stopSound(){
		mMediaPlayer.stop();
	}

	//Get an alarm sound. Try for an alarm. If none set, try notification, 
	//Otherwise, ringtone.
	private Uri getAlarmUri() {
		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}

	/**
	 * Fire an intent to start the voice recognition activity.
	 */
	private void startVoiceRecognitionActivity()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
		startActivityForResult(intent, REQUEST_CODE);
	}

	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
		{
			// Populate the wordsList with the String values the recognition engine thought it heard
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			if (matches.get(0).equals("stop")){
				this.onDestroy();
			}
			
			Log.v("Test", "matches " + matches.get(0));
			Log.v("Test", "RESULT ");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy(){
		stopALarmbool = true;
		stopSound();
		super.onDestroy();
	}
}	