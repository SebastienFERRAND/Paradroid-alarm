package com.paradroid.adapter;
import com.adylitica.database.DataBaseHelper;
import com.example.paradroidalarm.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AlarmAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
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
    	double hour = c.getDouble(DataBaseHelper.DATABASE_HOUR_ALARM_INT);
    	double minute = c.getDouble(DataBaseHelper.DATABASE_MINUTE_ALARM_INT);
    	String day = c.getString(DataBaseHelper.DATABASE_DAY_ALARM_INT);
        String sound = c.getString(DataBaseHelper.DATABASE_SOUND_ALARM_INT);
        double time_snooze = c.getDouble(DataBaseHelper.DATABASE_TIME_SNOOZE_ALARM_INT);

        /**
         * Next set the title of the entry.
         */

        TextView id_tv = (TextView) v.findViewById(R.id.id);
        if (id_tv != null) {
        	id_tv.setText(_id + "");
        }

        /**
         * Set Date
         */

        TextView hour_text = (TextView) v.findViewById(R.id.hour);
        if (hour_text != null) {
        	hour_text.setText(hour + "");
        }       
        
        TextView minute_text = (TextView) v.findViewById(R.id.minute);
        if (minute_text != null) {
        	minute_text.setText(minute + "");
        }  
        
        TextView day_text = (TextView) v.findViewById(R.id.day);
        if (day_text != null) {
        	day_text.setText(day + "");
        }  

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
    }
}