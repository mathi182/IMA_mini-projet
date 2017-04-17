package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;

public class AlarmTypeListAdapter extends RecyclerView.Adapter<AlarmTypeListAdapter.AlarmViewHolder> {

    private List<AlarmType> alarmTypes;
    private Context context;

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTypeName;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            alarmTypeName = (TextView)itemView.findViewById(R.id.accelerometer_type_name);
        }
    }

    public AlarmTypeListAdapter(List<AlarmType> accelerometerAlarmTypes, Context context) {
        this.context = context;
        alarmTypes = accelerometerAlarmTypes;
    }

    @Override
    public AlarmTypeListAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarmtype_list_row, parent, false);

        return new AlarmTypeListAdapter.AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmTypeListAdapter.AlarmViewHolder holder, int position) {
        AlarmType alarm = alarmTypes.get(position);
        holder.alarmTypeName.setText(alarm.getDescription());
    }

    @Override
    public int getItemCount() {
        return alarmTypes.size();
    }


}
