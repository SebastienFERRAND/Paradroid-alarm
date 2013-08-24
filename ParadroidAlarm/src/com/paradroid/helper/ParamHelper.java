package com.paradroid.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class ParamHelper {

	private static SharedPreferences settings;
	private static final String PREFS_NAME = "prefFileTime";
	private static SharedPreferences.Editor editor;
	private static Context con;

	public static void initParamHelper(Context context){
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		con = context;
	}


	public static void pushTalk(boolean value){
		editor.putBoolean("talkBool", value);
		editor.commit();
	}
	
	public static void pushEnableVoice(boolean value){
		editor.putBoolean("enableVoice", value);
		editor.commit();
	} 

	public static void pushSnooze(int value){

		initParamHelper(con);
		editor.putInt("snooze", value);
		editor.commit();
	}
	
	public static void pushIntensity(int value){

		initParamHelper(con);
		editor.putInt("intensity", value);
		editor.commit();
	}
	
	public static void pushTimeOpen(int value){

		initParamHelper(con);
		editor.putInt("time_open", value);
		editor.commit();
	}
	
	public static void pushURISong(String value){

		initParamHelper(con);
		editor.putString("URISong", value);
		editor.commit();
	}
	
	public static void pushIntervalSongVoice(int seconds){

		initParamHelper(con);
		editor.putInt("IntervalSongVoice", seconds);
		editor.commit();
	}

	public static boolean getTalk(){
		initParamHelper(con);
		boolean talk = settings.getBoolean("talkBool", false);
		return talk;
	}

	public static int getSnooze(){

		initParamHelper(con);
		int value = settings.getInt("snooze", 5);
		return value;
	}
	
	public static boolean getEnableVoice(){

		initParamHelper(con);
		boolean value = settings.getBoolean("enableVoice", true);
		return value;
	}
	
	public static int getIntensity(){

		initParamHelper(con);
		int value = settings.getInt("intensity", 2500);
		return value;
	}
	
	public static int getTimeOpen(){

		initParamHelper(con);
		int value = settings.getInt("time_open", 0);
		return value;
	}

	public static String getURISong(){

		initParamHelper(con);
		String value = settings.getString("URISong", "");
		return value;
	}
	
	
	public static int getIntervalSongVoice(){

		initParamHelper(con);
		int value = settings.getInt("IntervalSongVoice", 20);
		return value;
	}

}
