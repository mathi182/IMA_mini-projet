package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.media.Ringtone;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class RingtoneAdapter extends ArrayAdapter<Ringtone> {

    private List<Ringtone> ringtones = new ArrayList<>();
    private LayoutInflater inflator;
    private Context context;

    public RingtoneAdapter(Context context, int resource, List<Ringtone> list) {
        super(context, resource, list);

        this.context = context;
        ringtones = list;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ringtones.size();
    }

    @Override
    public Ringtone getItem(int position) {
        return ringtones.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflator.inflate(R.layout.alarm_types_subelement_row, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.txt_alarmType_subelement);
        tv.setText(ringtones.get(position).getTitle(context));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

