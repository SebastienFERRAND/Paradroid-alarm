package com.paradroid.paradroidalarm;

import java.io.IOException;
import java.util.ArrayList;

import com.paradroid.paradroidalarm.R;
import com.paradroid.database.AlarmDataSource;
import com.paradroid.database.DataBaseHelper;
import com.paradroid.helper.ParamHelper;
import com.paradroid.helper.SoundHelper;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

public class AlarmReceiverActivity extends Activity {
	private MediaPlayer mMediaPlayer; 
	private MediaPlayer mMediaPlayer1; 
	private MediaPlayer mMediaPlayer2; 
	private MediaPlayer mMediaPlayer3;
	private MediaPlayer mMediaPlayer4;

	private AudioManager audioManager;

	private static final int REQUEST_CODE = 1234;

	private boolean stopALarmbool = false;
	private Context con;

	private Handler handlerRecon = new Handler();
	private Handler handlerSound = new Handler();

	private int id;
	private int minute;
	private int hour;
	private int onOffp;
	private ArrayList<Integer> listDays;
	public AlarmDataSource ndsA;

	private WakeLock wakeLock;

	private int numberOfLoop = 0;

	private boolean continueRing = true;

	private SoundHelper sm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ParamHelper.initParamHelper(this);

		sm = new SoundHelper();  

		Intent intent = getIntent();

		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
		wakeLock.acquire();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		id = intent.getIntExtra("id", 0);
		minute = intent.getIntExtra("minute", 0);
		hour = intent.getIntExtra("hourOfDay", 0);
		int days = intent.getIntExtra("days", 0);
		
		listDays = MainActivity.intToArray(days);
		con = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm);

		Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

		stopAlarm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				sm.stop();
				stopSound();
				stopALarmbool = true;
				finish();
				return false;
			}
		});

		ndsA = new AlarmDataSource(con);
		Cursor c = ndsA.getAlarm(id);
		c.moveToFirst();
		onOffp = c.getInt(DataBaseHelper.DATABASE_ON_OFF_INT);
		Log.v("ID", onOffp + "");

		if (onOffp == 1){
			if(ParamHelper.getTalk()){
				playSoundTalk(con);
			}else{
				playSoundMusic(con, getAlarmUri());
			}

			if (ParamHelper.getEnableVoice()){
				handlerRecon.postDelayed(startRecognition, 5000);//Message will be delivered in 1 second
			}
		}else{
			this.finish();
		}
	}

	//Here's a runnable/handler combo
	private Runnable startRecognition = new Runnable()
	{
		@Override
		public void run()
		{
			if(!stopALarmbool){
				stopSound();
//				playBip();

				try{
					sm.start();
					sm.getAmplitude();
					handlerSound.postDelayed(startSound, 5000);//Message will be delivered in 5 second.
				}catch(ActivityNotFoundException e){
					Toast.makeText(con, 
							"The voice recongnition is not available on this phone", 
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		private void playBip() {

			mMediaPlayer4 = new MediaPlayer();

			//SON 1
			AssetFileDescriptor afd = con.getResources().openRawResourceFd(R.raw.beep);
			try {
				mMediaPlayer4.reset();
				mMediaPlayer4.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					mMediaPlayer4.setAudioStreamType(AudioManager.STREAM_ALARM);
					mMediaPlayer4.prepare();
					mMediaPlayer4.start();
				}

			} 
			catch (IllegalArgumentException e) {    } 
			catch (IllegalStateException e) { } 
			catch (IOException e) { } 

		}
	};

	//Here's a runnable/handler combo
	private Runnable startSound = new Runnable()
	{
		@Override
		public void run()
		{
			if(!stopALarmbool){
//				con = this;

				//				finishActivity(REQUEST_CODE);
				//				sr.stopListening();
				double amp = sm.getAmplitude();
				sm.stop();
				Log.v("REBOOT", "max amp " + amp);

				if (amp > 2500){
					MainActivity.snooze(con, id, minute, hour);
					stopSound();
					stopALarmbool = true;
					finish();
				}


				if(ParamHelper.getTalk()){
					playSoundTalk(con);
				}else{
					playSoundMusic(con, getAlarmUri());
				}
				handlerRecon.postDelayed(startRecognition, 5000);//Message will be delivered in 5 second.
			}
		}
	};

	private void playSoundTalk(Context context) {
		mMediaPlayer1 = new MediaPlayer();
		mMediaPlayer2 = new MediaPlayer();
		mMediaPlayer3 = new MediaPlayer();

		//SON 1
		AssetFileDescriptor afd = con.getResources().openRawResourceFd(R.raw.hour);
		try {
			mMediaPlayer1.reset();
			mMediaPlayer1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//SON 2
		AssetFileDescriptor afd2 = con.getResources().openRawResourceFd(R.raw.minute);
		try {
			mMediaPlayer2.reset();
			mMediaPlayer2.setDataSource(afd2.getFileDescriptor(), afd2.getStartOffset(), afd2.getDeclaredLength());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//SON 3
		AssetFileDescriptor afd3 = con.getResources().openRawResourceFd(R.raw.talky);
		try {
			mMediaPlayer3.reset();
			mMediaPlayer3.setDataSource(afd3.getFileDescriptor(), afd3.getStartOffset(), afd3.getDeclaredLength());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer1.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer1.prepare();
				mMediaPlayer1.start();
			}

		} 
		catch (IllegalArgumentException e) {    } 
		catch (IllegalStateException e) { } 
		catch (IOException e) { } 


		mMediaPlayer1.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer player) {
				player.stop();

				try {
					if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
						mMediaPlayer2.setAudioStreamType(AudioManager.STREAM_ALARM);
						mMediaPlayer2.prepare();
						mMediaPlayer2.start();
					}

				} 
				catch (IllegalArgumentException e) {    } 
				catch (IllegalStateException e) { } 
				catch (IOException e) { } 
			}

		});

		mMediaPlayer2.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer player) {
				player.stop();

				try {
					if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
						mMediaPlayer3.setAudioStreamType(AudioManager.STREAM_ALARM);
						mMediaPlayer3.prepare();
						mMediaPlayer3.start();
					}

				} 
				catch (IllegalArgumentException e) {    } 
				catch (IllegalStateException e) { } 
				catch (IOException e) { } 
			}

		});

	}

	private void playSoundMusic(Context context, Uri alert) {

		numberOfLoop++;
		if (numberOfLoop > 10){
			stopSound();
			stopALarmbool = true;
			finish();
		}


		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			try {
				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				}

			} 
			catch (IllegalArgumentException e) {    } 
			catch (IllegalStateException e) { } 
			catch (IOException e) { } 

		} catch (IOException e) {
			System.out.println("OOPS");
		}
	}

	private void stopSound(){
		if(ParamHelper.getTalk()){
			if(mMediaPlayer1.isPlaying()){
				mMediaPlayer1.stop();
			}

			if(mMediaPlayer2.isPlaying()){
				mMediaPlayer2.stop();
			}

			if(mMediaPlayer3.isPlaying()){
				mMediaPlayer3.stop();
			}

		}else{
			mMediaPlayer.stop();
		}
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


	@Override
	public void onDestroy(){
		if (onOffp == 1){
			stopSound();
		}else{

		}
		stopALarmbool = true;
		Cursor c = ndsA.getAlarm(id);
		c.moveToFirst();
		MainActivity.on(con, id, hour, minute, c.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT));
		MainActivity.refresh();
		wakeLock.release();

		super.onDestroy();
	}

}	