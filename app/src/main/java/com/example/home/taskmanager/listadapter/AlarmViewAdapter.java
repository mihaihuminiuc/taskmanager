package com.example.home.taskmanager.listadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.holder.AlarmViewHolder;
import com.example.home.taskmanager.listeners.AlarmClickListener;
import com.example.home.taskmanager.model.AlarmModel;

import java.util.List;

public class AlarmViewAdapter extends RecyclerView.Adapter{

    private List<AlarmModel> mAlarmList;
    private AlarmClickListener mAlarmClickListener;
    private Context mContext;

    public AlarmViewAdapter(List<AlarmModel> alarms, AlarmClickListener alarmClickListener, Context context){
        this.mAlarmList = alarms;
        this.mAlarmClickListener = alarmClickListener;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AlarmViewHolder) holder).bindData(mContext, mAlarmList.get(position),mAlarmClickListener);
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.row;
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

    public void updateList(List<AlarmModel> alarms){
        if (alarms != null) {
            this.mAlarmList = alarms;
        }
        notifyDataSetChanged();
    }
}
