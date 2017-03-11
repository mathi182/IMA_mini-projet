package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTitle, alarmTime;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            alarmTitle = (TextView)itemView.findViewById(R.id.alarm_title);
            alarmTime = (TextView)itemView.findViewById(R.id.alarm_time);
        }
    }

    public AlarmAdapter(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_row, parent, false);

        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.alarmTitle.setText(alarm.getTitle());
        holder.alarmTime.setText(alarm.getTime());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }


}
