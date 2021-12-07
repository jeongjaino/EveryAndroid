package com.example.studywithtimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater layoutInflater = null;
    ArrayList<TimerItem> timerItemArrayList = new ArrayList<TimerItem>();

    public TimerAdapter(Context context,  ArrayList<TimerItem> arrayList) {
        mContext = context;
        timerItemArrayList = arrayList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return timerItemArrayList.size();
    }

    @Override
    public TimerItem getItem(int position) {
            return timerItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View item_view = layoutInflater.inflate(R.layout.timer_list_item, null);

        TextView dateText = (TextView)item_view.findViewById(R.id.date_text);
        TextView elapsedTimeText = (TextView)item_view.findViewById(R.id.elapsed_time_text);
        TextView startTimeText = (TextView)item_view.findViewById(R.id.start_time_text);
        TextView endTimeText = (TextView)item_view.findViewById(R.id.end_time_text);

        dateText.setText(timerItemArrayList.get(position).getDate());
        elapsedTimeText.setText(timerItemArrayList.get(position).getElapsedTime());
        startTimeText.setText(timerItemArrayList.get(position).getStartTime());
        endTimeText.setText(timerItemArrayList.get(position).getEndTime());

        return item_view;
    }
}
