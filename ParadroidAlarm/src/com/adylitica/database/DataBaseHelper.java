package com.adylitica.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "db";

	public static final String DATABASE_TABLE_ALARM = "alarm";

	public static final String DATABASE_ID_ALARM = "_id";
	public static final String DATABASE_HOUR_ALARM = "hour";
	public static final String DATABASE_MINUTE_ALARM = "minute";
	public static final String DATABASE_DAY_ALARM = "day";
	public static final String DATABASE_SOUND_ALARM = "sound";
	public static final String DATABASE_TIME_SNOOZE_ALARM = "time_snooze";


	public static final int DATABASE_ID_ALARM_INT = 0;
	public static final int DATABASE_HOUR_ALARM_INT = 1;
	public static final int DATABASE_MINUTE_ALARM_INT = 2;
	public static final int DATABASE_DAY_ALARM_INT = 3;
	public static final int DATABASE_SOUND_ALARM_INT = 4;
	public static final int DATABASE_TIME_SNOOZE_ALARM_INT = 5;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + DATABASE_TABLE_ALARM + 
				" (" + DATABASE_ID_ALARM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ DATABASE_HOUR_ALARM + " INTEGER, "
				+ DATABASE_MINUTE_ALARM + " INTEGER, "
				+ DATABASE_DAY_ALARM + " STRING, "
				+ DATABASE_SOUND_ALARM + " TEXT DEFAULT \'default\', "  
				+ DATABASE_TIME_SNOOZE_ALARM + " INTEGER )");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
