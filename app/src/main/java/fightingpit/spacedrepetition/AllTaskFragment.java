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

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Adapter.SectionedTaskAdapter;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByTime;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTaskFragment extends Fragment {


    @BindView(R.id.rv_fat)
    RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    public AllTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_task, container, false);
        ButterKnife.bind(this, view);

        new SetUpView().execute();

        return view;
    }

    private class SetUpView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
            //TreeMap<String, List<Task>> aTreeMap = new TreeMap<>();

            Log.d("++TIME++", "AllTask start");
            String aDayStartMillis = CommonUtils.getDayStartMillis(null);
            List<Task> aOverDueTasks = DatabaseMethods.getScheduledTasks(null, aDayStartMillis);
            if (aOverDueTasks.size() > 0) {
                Collections.sort(aOverDueTasks, new SortTaskByTime());
                mSectionedRecyclerViewAdapter.addSection(new SectionedTaskAdapter("OverDue",
                        aOverDueTasks));
            }

            List<Task> aUpcomingTasks = DatabaseMethods.getScheduledTasks(aDayStartMillis, null);
            if (aUpcomingTasks.size() > 0) {
                Collections.sort(aUpcomingTasks, new SortTaskByTime());
                mSectionedRecyclerViewAdapter.addSection(new SectionedTaskAdapter("Upcoming",
                        aUpcomingTasks));
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
