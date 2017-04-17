package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;

public class AlarmTypeSpinnerAdapter extends ArrayAdapter<AlarmType> {

    List<AlarmType> alarmTypes;
    LayoutInflater inflator;

    public AlarmTypeSpinnerAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        alarmTypes = (List<AlarmType>) objects;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return alarmTypes.size();
    }

    @Override
    public AlarmType getItem(int position)
    {
        return alarmTypes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflator.inflate(R.layout.alarmtype_spinner_row, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.alarm_type_name);
        tv.setText(alarmTypes.get(position).toString());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.alarm_type_logo);
        imageView.setImageResource(alarmTypes.get(position).getLogoResource());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
