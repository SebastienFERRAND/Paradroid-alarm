package com.adylitica.database;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.paradroid.paradroidalarm.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class AlarmDataSource {

	private static SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private static String[] allColumnsAlarm = { DataBaseHelper.DATABASE_ID_ALARM,
		DataBaseHelper.DATABASE_HOUR_ALARM,DataBaseHelper.DATABASE_MINUTE_ALARM,
		DataBaseHelper.DATABASE_DAY_ALARM, DataBaseHelper.DATABASE_SOUND_ALARM,
		DataBaseHelper.DATABASE_TIME_SNOOZE_ALARM};

	private static String[] dayColumn = {DataBaseHelper.DATABASE_DAY_ALARM};

	private SQLiteStatement sqlLtSt;

	public AlarmDataSource(Context context) {
		dbHelper = new DataBaseHelper(context);
		try{
			if(database==null){
				database = dbHelper.getWritableDatabase();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void close() {
		dbHelper.close();
	}

	public int createAlarm(int hour, int minute, int day, int snooze) {

		sqlLtSt = database.compileStatement("INSERT INTO " + DataBaseHelper.DATABASE_TABLE_ALARM 
				+ " VALUES(NULL, ?, ?, ?, '', ?)");
		sqlLtSt.bindLong(1, hour);
		sqlLtSt.bindLong(2, minute);
		sqlLtSt.bindLong(3, day);
		sqlLtSt.bindLong(4, snooze);
		int id = (int) sqlLtSt.executeInsert();

		return id;

	}

	public Cursor getAllAlarm() {

		Cursor cursor = database.query(DataBaseHelper.DATABASE_TABLE_ALARM, 
				allColumnsAlarm,
				null,
				null,
				null,
				null,
				null
				);

		return cursor;

	}

	public void deleteAlarm(int id) {
		sqlLtSt = database.compileStatement("DELETE FROM " + DataBaseHelper.DATABASE_TABLE_ALARM + " WHERE " 
				+ DataBaseHelper.DATABASE_ID_ALARM + "=" + id);
		sqlLtSt.execute();
	}

	public Cursor getAlarm(int id) {

		Cursor cursor = database.query(DataBaseHelper.DATABASE_TABLE_ALARM, 
				allColumnsAlarm,
				"(" + DataBaseHelper.DATABASE_ID_ALARM + "=" + id + ")", 
				null,
				null,
				null,
				null
				);

		return cursor;
	}

	public ArrayList<Integer> getDays(int id) {
		ArrayList<Integer> days = new ArrayList<Integer>();
		Cursor cursor = database.query(DataBaseHelper.DATABASE_TABLE_ALARM, 
				allColumnsAlarm,
				"(" + DataBaseHelper.DATABASE_ID_ALARM + "=" + id + ")", 
				null,
				null,
				null,
				null
				);

		cursor.moveToFirst();
		int day = cursor.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT);

		Log.v("Test", day + " : day");
		
		days = MainActivity.intToArray(day);
		
		return days;
	}

	public void modifyDays(int id, int days) {
		ContentValues args = new ContentValues();
		args.put(DataBaseHelper.DATABASE_DAY_ALARM, days);
		database.update(DataBaseHelper.DATABASE_TABLE_ALARM, args, DataBaseHelper.DATABASE_ID_ALARM + "=" + id, null);
	}

}
