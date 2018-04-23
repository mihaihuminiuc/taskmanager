package com.example.home.taskmanager.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.listadapter.AlarmListAdapter;
import com.example.home.taskmanager.listeners.AlarmActivityListener;
import com.example.home.taskmanager.model.Alarm;

public class AlarmListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private RecyclerView mAlarmList;
    private AlarmListAdapter mAlarmListAdapter;
    private Alarm mCurrentAlarm;
    private Button mAddAllarm;
    private AlarmActivityListener mAlarmActivityListener;

    private Context mContext;

    public AlarmListFragment newInstance(){
        return new AlarmListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initUI(view);
        setupActions();
    }

    private void initUI(View view){
        mAlarmList = view.findViewById(R.id.alarm_list);
        mAddAllarm = view.findViewById(R.id.add_alarm);

        mAlarmListAdapter = new AlarmListAdapter(mContext);
        mAlarmList.setAdapter(mAlarmListAdapter);
    }

    private void setupActions() {
        mAddAllarm.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mAlarmActivityListener.editAlarm(1);
    }

    @Override
    public void onAttach(Context  context){
        super.onAttach(context);

        if(context instanceof AlarmActivityListener)
            mAlarmActivityListener = (AlarmActivityListener) context;
        else {
            throw new RuntimeException(context.toString()
                    + " must implement AlarmActivityListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mAlarmActivityListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_alarm:
                mAlarmActivityListener.addAlarm();
                break;
        }
    }
}
