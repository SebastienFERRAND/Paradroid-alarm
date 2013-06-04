package com.paradroid.adapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.adylitica.database.DataBaseHelper;
import com.example.paradroidalarm.R;
import com.paradroid.paradroidalarm.MainActivity;
import com.paradroid.paradroidalarm.PickADayActivity;
import com.paradroid.paradroidalarm.MainActivity.TimePickerFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;


public class AlarmAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
	private ToggleButton onOff;
	private TimePickerFragment df;
    public AlarmAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context); 
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.alarm_row, parent, false);
        return v;
    }

    /**
     * @author will
     * 
     * @param   v
     *          The view in which the elements we set up here will be displayed.
     * 
     * @param   context
     *          The running context where this ListView adapter will be active.
     * 
     * @param   c
     *          The Cursor containing the query results we will display.
     */

    @Override
    public void bindView(View v, Context context, Cursor c) {
    	int _id = (int) c.getDouble(DataBaseHelper.DATABASE_ID_ALARM_INT);
    	int hour = (int) c.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
    	int minute = (int) c.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
    	int day = (int) c.getDouble(DataBaseHelper.DATABASE_DAY_ALARM_INT);
        /**
         * Set Date
         */

        TextView hour_text = (TextView) v.findViewById(R.id.time);
        if (hour_text != null) {
        	hour_text.setText(hour + ":" + minute);
        }

        hour_text.setTag(_id);
        
        hour_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.fromModify = true;
				MainActivity.idToModify = (Integer) v.getTag();
				MainActivity.loadTimer();
			}
		});
        
        TextView day_text = (TextView) v.findViewById(R.id.days);
        
        DateFormat formatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        
        if (day_text != null) {
        	day_text.setText(formatter.format(calendar.getTime()));
        }

        day_text.setTag(_id);
        day_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent(mContext, PickADayActivity.class);
				viewIntent.putExtra("idNote", (Integer) v.getTag());
				((MainActivity) mContext).startActivityForResult(viewIntent, 1);
				
			}
		});
        
		onOff = (ToggleButton) v.findViewById(R.id.togglebuttononoff);
		onOff.setChecked(true);
		onOff.setTag(_id);
		onOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				int id = (Integer) buttonView.getTag();
				
				Cursor cur = MainActivity.nds.getAlarm((int) id);
				cur.moveToFirst();
				int hourP = (int) cur.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
		    	int minuteP = (int) cur.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
				
				if (isChecked){
					Cursor c = MainActivity.nds.getAlarm(id);
					c.moveToFirst();
					MainActivity.on(id, minuteP, hourP, c.getInt(DataBaseHelper.DATABASE_DAY_ALARM_INT));
				}else{
					MainActivity.off(id);
				}
				
			}
		});
		
        
        Button deleteAlarm = (Button) v.findViewById(R.id.deleteAlarm);
        deleteAlarm.setTag(_id);
        deleteAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int id = (Integer) v.getTag();
				MainActivity.nds.deleteAlarm(id);
				MainActivity.offAndOut(id);
			}
		});
    
    }
}