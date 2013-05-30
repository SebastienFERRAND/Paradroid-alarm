package com.adylitica.database;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	
	public long createAlarm(int hour, int minute, String day, int snooze) {

		sqlLtSt = database.compileStatement("INSERT INTO " + DataBaseHelper.DATABASE_TABLE_ALARM 
				+ " VALUES(NULL, ?, ?, ?, '', ?)");
		sqlLtSt.bindLong(1, hour);
		sqlLtSt.bindLong(2, minute);
		sqlLtSt.bindString(3, day);
		sqlLtSt.bindLong(4, snooze);
		long id = sqlLtSt.executeInsert();
		
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

	public void deleteAlarm(long id) {
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

}
