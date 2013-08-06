package com.paradroid.helper;

import java.io.IOException;

import android.media.MediaRecorder;
import android.util.Log;

public class SoundHelper {

	private MediaRecorder mRecorder = null;

	public void start() {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile("/dev/null"); 
			try {
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mRecorder.start();
		}
	}

	public void stop() {
		if (mRecorder != null) {
			try{
				mRecorder.stop();       
				mRecorder.release();
				mRecorder = null;
			}catch(Exception e){
				
			}
		}
	}

	public double getAmplitude() {
		if (mRecorder != null){
			return  mRecorder.getMaxAmplitude();
		}else{
			return 0;
		}

	}
}