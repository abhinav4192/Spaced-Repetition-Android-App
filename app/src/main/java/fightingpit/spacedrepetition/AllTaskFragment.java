package fightingpit.spacedrepetition;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fightingpit.spacedrepetition.Adapter.SectionedTaskAdapter;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByName;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByTime;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTaskFragment extends Fragment {


    private Fragment mFragment;
    private Integer mFragmentCode;
    private HashMap<String, SectionedTaskAdapter> mAdapterMap = new HashMap<>();
    private HashMap<String, TreeSet<Integer>> mSelectedItemMap = new HashMap<>();
    private Integer mCountItemsSelected = 0;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private Unbinder mUnbinder;
    private final String OVERDUE = "Overdue";
    private final String UPCOMING = "Upcoming";
    @BindView(R.id.rv_fat) RecyclerView mRecyclerView;
    @BindString(R.string.fragment_switch_key) String FRAGMENT_KEY;
    LinearLayout mButtonsLayout;
    TextView mRemoveButton;


    public AllTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_task, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mFragment = this;

        initialize();

        Bundle aBundle = this.getArguments();
        mFragmentCode = aBundle.getInt(FRAGMENT_KEY,1);
        switch (mFragmentCode){
            case 1 :
                break;
            case 2 :
                new SetUpSevenDaysView().execute();
                break;
            case 3 :
                new SetUpAllDaysView().execute();
                break;
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initialize(){
        mButtonsLayout= (LinearLayout) ((Activity) ContextManager.getCurrentActivityContext()).findViewById(R
                .id.ll_cm);
        mRemoveButton = (TextView) ((Activity) ContextManager.getCurrentActivityContext()).findViewById(R
                .id.tv_cm_remove);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonAction();
            }
        });
    }

    public void addSelectedItem(String iTag, Integer iPosition){
        if(mSelectedItemMap.get(iTag).add(iPosition)){
            mCountItemsSelected++;
        }
        if(mCountItemsSelected == 1){
            mButtonsLayout.setVisibility(View.VISIBLE);
        }
    }

    public void removeSelectedItem(String iTag, Integer iPosition){
        if(mSelectedItemMap.get(iTag).remove(iPosition)){
            mCountItemsSelected--;
        }
        if(mCountItemsSelected == 0){
            mButtonsLayout.setVisibility(View.GONE);
        }
    }


    public boolean isItemSelected(String iTag, Integer iPosition){
        return mSelectedItemMap.get(iTag).contains(iPosition);
    }

    private void deleteButtonAction(){
        if(mCountItemsSelected > 0){
            for(HashMap.Entry<String, TreeSet<Integer>> aMapEntry : mSelectedItemMap.entrySet()){
                String aKey = aMapEntry.getKey();
                List<Task> aTasks = mAdapterMap.get(aKey).getItemList();
                for(Integer aPosition: aMapEntry.getValue()){
                    aTasks.remove(aPosition.intValue());
                }
                if(aTasks.size()<=0){
                    mSectionedRecyclerViewAdapter.removeSection(aKey);
                }
                aMapEntry.getValue().clear();
            }
            mCountItemsSelected = 0;
            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
            mButtonsLayout.setVisibility(View.GONE);
        }
    }

    private class SetUpSevenDaysView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();

            Log.d("++TIME++", "This week start");

            TreeMap<String, List<Task>> aTreeMap = new TreeMap<>();
            for (Task aTask : DatabaseMethods.getScheduledTasks(CommonUtils.getDayStartMillis(null),
                    CommonUtils.getOffsetTimeInMillis(null, 7))) {
                String aTimeStamp = CommonUtils.getDayStartMillis(aTask.getTime());

                if (!aTreeMap.containsKey(aTimeStamp)) {
                    aTreeMap.put(aTimeStamp, new ArrayList<Task>());
                }
                aTreeMap.get(aTimeStamp).add(aTask);
            }

            for (TreeMap.Entry<String, List<Task>> aEntry : aTreeMap.entrySet()) {
                String aDate = CommonUtils
                        .getDayFromMillis(aEntry.getKey());
                List<Task> aNames = aEntry.getValue();
                Collections.sort(aNames, new SortTaskByName());

                SectionedTaskAdapter aAdapter = new SectionedTaskAdapter
                        (mFragment,mSectionedRecyclerViewAdapter,aDate, aNames);
                mSectionedRecyclerViewAdapter.addSection(aDate,aAdapter);
                mAdapterMap.put(aDate, aAdapter);
                mSelectedItemMap.put(aDate, new TreeSet<Integer>(Collections.reverseOrder()));
            }

            Log.d("++TIME++", "This week start");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextManager
                    .getCurrentActivityContext()));
            mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);

        }
    }

    private class SetUpAllDaysView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
            //TreeMap<String, List<Task>> aTreeMap = new TreeMap<>();

            Log.d("++TIME++", "AllTask start");
            String aDayStartMillis = CommonUtils.getDayStartMillis(null);
            List<Task> aOverDueTasks = DatabaseMethods.getScheduledTasks(null, aDayStartMillis);
            if (aOverDueTasks.size() > 0) {
                Collections.sort(aOverDueTasks, new SortTaskByTime());

                SectionedTaskAdapter aAdapter = new SectionedTaskAdapter
                        (mFragment,mSectionedRecyclerViewAdapter, OVERDUE,
                                aOverDueTasks);
                mSectionedRecyclerViewAdapter.addSection(OVERDUE, aAdapter);
                mAdapterMap.put(OVERDUE, aAdapter);
                mSelectedItemMap.put(OVERDUE, new TreeSet<Integer>(Collections.reverseOrder()));

            }

            List<Task> aUpcomingTasks = DatabaseMethods.getScheduledTasks(aDayStartMillis, null);
            if (aUpcomingTasks.size() > 0) {
                Collections.sort(aUpcomingTasks, new SortTaskByTime());
                SectionedTaskAdapter aAdapter = new SectionedTaskAdapter
                        (mFragment,mSectionedRecyclerViewAdapter, UPCOMING,
                                aUpcomingTasks);
                mSectionedRecyclerViewAdapter.addSection(UPCOMING, aAdapter);
                mAdapterMap.put(UPCOMING, aAdapter);
                mSelectedItemMap.put(UPCOMING, new TreeSet<Integer>(Collections.reverseOrder()));
            }
            Log.d("++TIME++", "AllTask stop");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextManager
                    .getCurrentActivityContext()));
            mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
        }
    }
}
