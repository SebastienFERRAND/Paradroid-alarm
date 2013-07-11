package com.paradroid.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

	public static boolean getTalk(){
		initParamHelper(con);
		boolean talk = settings.getBoolean("talkBool", false);
		return talk;
	}

	public static void pushSnooze(float value){

		initParamHelper(con);
		editor.putFloat("snooze", value);
		editor.commit();
	}

	public static float getSnooze(){

		initParamHelper(con);
		float value = settings.getFloat("snooze", 5);
		return value;
	}

}
