package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmReceiver;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static android.content.Context.ALARM_SERVICE;
import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.updateAlarmManager;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;
    private  Activity activity;

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView alarmTitle, alarmTime;
        public ImageView alarmToggle;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            alarmTitle = (TextView)itemView.findViewById(R.id.alarm_title);
            alarmTime = (TextView)itemView.findViewById(R.id.alarm_time);
            alarmToggle = (ImageView)itemView.findViewById(R.id.alarm_toggle);
            alarmToggle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Alarm alarm = alarms.get(this.getAdapterPosition());

            switch (v.getId()) {
                case R.id.alarm_toggle:
                    alarm.setActive(!alarm.isActive());
                    updateAlarmManager(alarm);
                    break;
            }

            notifyItemChanged(getAdapterPosition());
        }
    }

    public AlarmAdapter(List<Alarm> alarms, Activity activity) {
        this.alarms = alarms;
        this.activity = activity;
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

        if (alarm.isActive()) {
            holder.alarmToggle.setImageResource(R.mipmap.ic_activatedclock);
        } else {
            holder.alarmToggle.setImageResource(R.mipmap.ic_deactivatedclock);
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}
