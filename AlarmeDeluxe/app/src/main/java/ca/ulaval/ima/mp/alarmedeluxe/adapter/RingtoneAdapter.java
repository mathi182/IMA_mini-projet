package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class RingtoneAdapter extends ArrayAdapter<Uri> {

    private List<Uri> uris = new ArrayList<>();
    private LayoutInflater inflator;
    private Context context;

    public RingtoneAdapter(Context context, int resource, List<Uri> list) {
        super(context, resource, list);

        this.context = context;
        uris = list;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return uris.size();
    }

    @Override
    public Uri getItem(int position) {
        return uris.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflator.inflate(R.layout.alarm_types_subelement_row, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.txt_alarmType_subelement);
        Ringtone ringtone = RingtoneManager.getRingtone(context,uris.get(position));
        tv.setText(ringtone.getTitle(context));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}

