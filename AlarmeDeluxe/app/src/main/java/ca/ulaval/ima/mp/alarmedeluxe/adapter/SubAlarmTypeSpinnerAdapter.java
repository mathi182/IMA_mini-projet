package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;

public class SubAlarmTypeSpinnerAdapter extends ArrayAdapter<AlarmType> {

    List<AlarmType> alarmTypes;
    LayoutInflater inflator;

    public SubAlarmTypeSpinnerAdapter(Context context, int resource, List objects) {
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

    public void setItems(List<AlarmType> alarmTypes) {
        this.alarmTypes = alarmTypes;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflator.inflate(R.layout.alarm_types_subelement_row, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.txt_alarmType_subelement);
        tv.setText(alarmTypes.get(position).getDescription());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
