package ca.ulaval.ima.mp.alarmedeluxe.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.YoutubeAlarmType;

public class YoutubeSearchListAdapter extends RecyclerView.Adapter<YoutubeSearchListAdapter.YoutubeSearchViewHolder> {

    private Context context;
    private List<SearchResult> results;
    private int selectedPosition = -1;
    private CheckBox lastSelected = null;

    public class YoutubeSearchViewHolder extends RecyclerView.ViewHolder{
        public CheckBox chk_youtubeSelected;

        public YoutubeSearchViewHolder(View itemView) {
            super(itemView);

            chk_youtubeSelected = (CheckBox)itemView.findViewById(R.id.chk_youtube_selected);
        }
    }

    public YoutubeSearchListAdapter(List<SearchResult> results, Context context) {
        this.context = context;
        this.results = results;
    }

    @Override
    public YoutubeSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_searchlist_row, parent, false);

        return new YoutubeSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubeSearchViewHolder holder, final int position) {
        SearchResult result = results.get(position);
        holder.chk_youtubeSelected.setText(result.getSnippet().getTitle());
        holder.chk_youtubeSelected.setSelected(selectedPosition == position);
        holder.chk_youtubeSelected.setTag(new Integer(position));

        holder.chk_youtubeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                int clicked = ((Integer)checkBox.getTag()).intValue();

                if (checkBox.isChecked()) {
                    if (lastSelected != null) {
                        lastSelected.setChecked(false);
                    }

                    lastSelected = checkBox;
                    selectedPosition = clicked;
                } else {
                    lastSelected = null;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public AlarmType getSelectedAlarmType() {
        if (selectedPosition < 0 || selectedPosition > results.size()) {
            return null;
        }

        AlarmType youtubeAlarmType = new YoutubeAlarmType();
        SearchResult result = results.get(selectedPosition);
        Bundle bundle = new Bundle();

        bundle.putString("description", result.getSnippet().getTitle());
        bundle.putString("url", result.getId().getVideoId());
        bundle.putInt("default", 1);
        youtubeAlarmType.buildFromBundle(bundle);

        return youtubeAlarmType;
    }
}
