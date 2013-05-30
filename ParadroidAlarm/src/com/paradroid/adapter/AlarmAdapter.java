package com.paradroid.adapter;
import com.adylitica.database.DataBaseHelper;
import com.example.paradroidalarm.R;
import com.paradroid.paradroidalarm.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


public class AlarmAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
	private ToggleButton onOff;
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
    	double _id = c.getDouble(DataBaseHelper.DATABASE_ID_ALARM_INT);
    	int hour = (int) c.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
    	int minute = (int) c.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
    	String day = c.getString(DataBaseHelper.DATABASE_DAY_ALARM_INT);
        String sound = c.getString(DataBaseHelper.DATABASE_SOUND_ALARM_INT);
        double time_snooze = c.getDouble(DataBaseHelper.DATABASE_TIME_SNOOZE_ALARM_INT);

        /**
         * Set Date
         */

        TextView hour_text = (TextView) v.findViewById(R.id.time);
        if (hour_text != null) {
        	hour_text.setText(hour + ":" + minute);
        }       
        
        TextView minute_text = (TextView) v.findViewById(R.id.nextring);
        if (minute_text != null) {
        	minute_text.setText(minute + "");
        }  
        
        TextView day_text = (TextView) v.findViewById(R.id.days);
        if (day_text != null) {
        	day_text.setText(day + "");
        }  
        

		onOff = (ToggleButton) v.findViewById(R.id.togglebuttononoff);
		onOff.setChecked(true);
		onOff.setTag(_id);
		onOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				double id = (Double) buttonView.getTag();
				
				Cursor cur = MainActivity.nds.getAlarm((int) id);
				cur.moveToFirst();
				int hourP = (int) cur.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
		    	int minuteP = (int) cur.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
		    	int day = (int) cur.getDouble(DataBaseHelper.DATABASE_DAY_ALARM_INT);
				
				if (isChecked){
					MainActivity.on(id, minuteP, hourP);
				}else{
					MainActivity.off(id);
				}
				
			}
		});

        /**
         * Decide if we should display the paper clip icon denoting image attachment
         */

        TextView sound_text = (TextView) v.findViewById(R.id.sound);
        if (sound_text != null) {
        	sound_text.setText(sound);
        }       
        /**
         * Decide if we should display the deletion indicator
         */
        TextView time_snooze_text = (TextView) v.findViewById(R.id.snooze_time);

        if (time_snooze_text != null) {
        	time_snooze_text.setText(time_snooze + "");
        }
        
        Button deleteAlarm = (Button) v.findViewById(R.id.deleteAlarm);
        deleteAlarm.setTag(_id);
        deleteAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				double id = (Double) v.getTag();
				MainActivity.nds.deleteAlarm((long) id);
				MainActivity.offAndOut(id);
			}
		});
        
    }
}