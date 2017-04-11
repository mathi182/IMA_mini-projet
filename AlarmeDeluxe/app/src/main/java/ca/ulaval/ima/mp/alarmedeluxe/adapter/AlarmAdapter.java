package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.MainActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.customization.HomeFragment;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.updateAlarmManager;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;
    private Context context;

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView alarmTitle, alarmTime;
        public ImageView alarmToggle;
        public LinearLayout layoutCheckbox;
        public ImageButton btn_deleteAlarm;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            alarmTitle = (TextView)itemView.findViewById(R.id.alarm_title);
            alarmTime = (TextView)itemView.findViewById(R.id.alarm_time);
            alarmToggle = (ImageView)itemView.findViewById(R.id.alarm_toggle);
            alarmToggle.setOnClickListener(this);
            layoutCheckbox = (LinearLayout)itemView.findViewById(R.id.layout_checkbox_alarm);
            btn_deleteAlarm = (ImageButton) itemView.findViewById(R.id.btn_deleteAlarm);
            btn_deleteAlarm.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Alarm alarm = alarms.get(this.getAdapterPosition());

            switch (v.getId()) {
                case R.id.alarm_toggle:
                    alarm.setActive(!alarm.isActive());
                    updateAlarmManager(alarm);
                    break;
                case R.id.btn_deleteAlarm:
                    ((MainActivity)context).deleteAlarm(alarm);
                    alarms.remove(getAdapterPosition());
                    layoutCheckbox.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    break;
            }

            notifyItemChanged(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (layoutCheckbox.getVisibility() == View.GONE) {
                layoutCheckbox.setVisibility(View.VISIBLE);
            } else {
                layoutCheckbox.setVisibility(View.GONE);
            }
            return true;
        }
    }

    public AlarmAdapter(List<Alarm> alarms, Context context) {
        this.alarms = alarms;
        this.context = context;
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
        holder.alarmTime.setText(alarm.getStringTime());

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
