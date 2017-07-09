package fightingpit.spacedrepetition;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Adapter.SectionedTaskAdapter;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByName;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThisWeekFragment extends Fragment {

    @BindView(R.id.rv_ftw)
    RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    public ThisWeekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_this_week, container, false);
        ButterKnife.bind(this, view);

        new SetUpView().execute();


        return view;
    }


    private class SetUpView extends AsyncTask<Void, Void, Void> {
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
                String aDate = aEntry.getKey();
                List<Task> aNames = aEntry.getValue();
                Collections.sort(aNames, new SortTaskByName());
                mSectionedRecyclerViewAdapter.addSection(new SectionedTaskAdapter(CommonUtils
                        .getDayFromMillis(aDate), aNames));
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
}
