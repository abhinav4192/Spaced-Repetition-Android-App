package fightingpit.spacedrepetition;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
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
import fightingpit.spacedrepetition.Engine.SettingManager;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByName;
import fightingpit.spacedrepetition.Model.comparators.SortTaskByTime;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduledTaskFragment extends Fragment {


    @BindString(R.string.Overdue)
    String OVERDUE;
    @BindString(R.string.Upcoming)
    String UPCOMING;
    @BindString(R.string.Today)
    String TODAY;
    @BindString(R.string.Tomorrow)
    String TOMORROW;
    @BindView(R.id.rv_fat)
    RecyclerView mRecyclerView;
    @BindString(R.string.fragment_switch_key)
    String FRAGMENT_KEY;
    private LinearLayout mButtonsLayout;
    private FloatingActionButton mAddTaskButton;
    private TextView mRemoveButton;
    private TextView mDoneButton;
    private TextView mRescheduleButton;
    private Fragment mFragment;
    private Integer mFragmentCode;
    private HashMap<String, SectionedTaskAdapter> mAdapterMap = new HashMap<>();
    private HashMap<String, TreeSet<Integer>> mSelectedItemMap = new HashMap<>();
    private Integer mCountItemsSelected = 0;
    private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;
    private Unbinder mUnbinder;
    private String mRescheduleTime;


    public ScheduledTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_task, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mFragment = this;
        mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();

        Bundle aBundle = this.getArguments();
        mFragmentCode = aBundle.getInt(FRAGMENT_KEY, 1);
        initialize();
        setUpView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private void initialize() {
        mAddTaskButton = (FloatingActionButton) ((Activity) ContextManager
                .getCurrentActivityContext())
                .findViewById(R.id.fab);

        mButtonsLayout = (LinearLayout) ((Activity) ContextManager.getCurrentActivityContext())
                .findViewById(R.id.ll_cm);

        mRemoveButton = (TextView) ((Activity) ContextManager.getCurrentActivityContext())
                .findViewById(R.id.tv_cm_remove);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonAction();
            }
        });

        mDoneButton = (TextView) ((Activity) ContextManager.getCurrentActivityContext())
                .findViewById(R.id.tv_cm_done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCountItemsSelected > 0) {
                    new updateTasks().execute();
                }
            }
        });

        mRescheduleButton = (TextView) ((Activity) ContextManager.getCurrentActivityContext())
                .findViewById(R.id.tv_cm_reschedule);
        mRescheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCountItemsSelected > 0) {
                    rescheduleButtonAction();
                }
            }
        });

    }

    private void setUpView() {
        mSectionedRecyclerViewAdapter.removeAllSections();
        mAdapterMap.clear();
        mSelectedItemMap.clear();
        mCountItemsSelected = 0;
        switch (mFragmentCode) {
            case 1:
                new SetUpTodayView().execute();
                break;
            case 2:
                new SetUpSevenDaysView().execute();
                break;
            case 3:
                new SetUpAllDaysView().execute();
                break;
        }
    }

    public void addSelectedItem(String iTag, Integer iPosition) {
        if (mSelectedItemMap.get(iTag).add(iPosition)) {
            mCountItemsSelected++;
        }
        if (mCountItemsSelected == 1) {
            mButtonsLayout.setVisibility(View.VISIBLE);
            mAddTaskButton.setVisibility(View.GONE);
        }
    }

    public void removeSelectedItem(String iTag, Integer iPosition) {
        if (mSelectedItemMap.get(iTag).remove(iPosition)) {
            mCountItemsSelected--;
        }
        if (mCountItemsSelected == 0) {
            mButtonsLayout.setVisibility(View.GONE);
            mAddTaskButton.setVisibility(View.VISIBLE);
        }
    }

    public boolean isItemSelected(String iTag, Integer iPosition) {
        return mSelectedItemMap.get(iTag).contains(iPosition);
    }

    private void addOverdueTasks() {
        String aDayStartMillis = CommonUtils.getDayStartMillis(null);
        List<Task> aOverDueTasks = DatabaseMethods.getScheduledTasks(null, aDayStartMillis);
        if (aOverDueTasks.size() > 0) {
            Collections.sort(aOverDueTasks, new SortTaskByTime());

            SectionedTaskAdapter aAdapter = new SectionedTaskAdapter(mFragment, OVERDUE,
                    aOverDueTasks);
            mSectionedRecyclerViewAdapter.addSection(OVERDUE, aAdapter);
            mAdapterMap.put(OVERDUE, aAdapter);
            mSelectedItemMap.put(OVERDUE, new TreeSet<Integer>(Collections.reverseOrder()));

        }
    }

    private void addTodayTasks() {
        List<Task> aUpcomingTasks = DatabaseMethods.getScheduledTasks(CommonUtils
                .getDayStartMillis(null), CommonUtils.getOffsetTimeInMillis(null, 1));
        if (aUpcomingTasks.size() > 0) {
            Collections.sort(aUpcomingTasks, new SortTaskByName());
            SectionedTaskAdapter aAdapter = new SectionedTaskAdapter(mFragment, TODAY,
                    aUpcomingTasks);
            mSectionedRecyclerViewAdapter.addSection(TODAY, aAdapter);
            mAdapterMap.put(TODAY, aAdapter);
            mSelectedItemMap.put(TODAY, new TreeSet<Integer>(Collections.reverseOrder()));
        }
    }

    private void addTomorrowTasks() {
        List<Task> aUpcomingTasks = DatabaseMethods.getScheduledTasks(CommonUtils
                .getOffsetTimeInMillis(null, 1), CommonUtils.getOffsetTimeInMillis(null, 2));
        if (aUpcomingTasks.size() > 0) {
            Collections.sort(aUpcomingTasks, new SortTaskByTime());
            SectionedTaskAdapter aAdapter = new SectionedTaskAdapter(mFragment, TOMORROW,
                    aUpcomingTasks);
            mSectionedRecyclerViewAdapter.addSection(TOMORROW, aAdapter);
            mAdapterMap.put(TOMORROW, aAdapter);
            mSelectedItemMap.put(TOMORROW, new TreeSet<Integer>(Collections.reverseOrder()));
        }
    }

    private void addSevenDayRemainingTasks() {
        TreeMap<String, List<Task>> aTreeMap = new TreeMap<>();
        for (Task aTask : DatabaseMethods.getScheduledTasks(CommonUtils.getOffsetTimeInMillis
                        (null, 2),
                CommonUtils.getOffsetTimeInMillis(null, 7))) {
            String aTimeStamp = CommonUtils.getDayStartMillis(aTask.getTime());

            if (!aTreeMap.containsKey(aTimeStamp)) {
                aTreeMap.put(aTimeStamp, new ArrayList<Task>());
            }
            aTreeMap.get(aTimeStamp).add(aTask);
        }

        for (TreeMap.Entry<String, List<Task>> aEntry : aTreeMap.entrySet()) {
            String aDate = CommonUtils
                    .getWeekDayFromMillis(aEntry.getKey());
            List<Task> aNames = aEntry.getValue();
            Collections.sort(aNames, new SortTaskByName());

            SectionedTaskAdapter aAdapter = new SectionedTaskAdapter
                    (mFragment, aDate, aNames);
            mSectionedRecyclerViewAdapter.addSection(aDate, aAdapter);
            mAdapterMap.put(aDate, aAdapter);
            mSelectedItemMap.put(aDate, new TreeSet<Integer>(Collections.reverseOrder()));
        }
    }

    private void addUpcomingTasks() {
        List<Task> aUpcomingTasks = DatabaseMethods.getScheduledTasks(CommonUtils
                .getOffsetTimeInMillis(null, 1), null);
        if (aUpcomingTasks.size() > 0) {
            Collections.sort(aUpcomingTasks, new SortTaskByTime());
            SectionedTaskAdapter aAdapter = new SectionedTaskAdapter(mFragment, UPCOMING,
                    aUpcomingTasks);
            mSectionedRecyclerViewAdapter.addSection(UPCOMING, aAdapter);
            mAdapterMap.put(UPCOMING, aAdapter);
            mSelectedItemMap.put(UPCOMING, new TreeSet<Integer>(Collections.reverseOrder()));
        }
    }

    private void updateUIAfterSetup() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContextManager
                .getCurrentActivityContext()));
        mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
    }

    private void deleteButtonAction() {
        if (mCountItemsSelected > 0) {

            String aContent = "Permanently delete " + mCountItemsSelected.toString();
            if (mCountItemsSelected == 1) {
                aContent += " task";
            } else
                aContent += " tasks";

            new MaterialDialog.Builder(ContextManager.getCurrentActivityContext())
                    .content(aContent)
                    .positiveText("CONFIRM")
                    .negativeText("CANCEL")
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                                which) {
                            if (which == DialogAction.POSITIVE) {
                                List<String> aTaskIdList = new ArrayList<>();
                                for (HashMap.Entry<String, TreeSet<Integer>> aMapEntry :
                                        mSelectedItemMap.entrySet()) {
                                    String aKey = aMapEntry.getKey();
                                    List<Task> aTasks = mAdapterMap.get(aKey).getItemList();
                                    for (Integer aPosition : aMapEntry.getValue()) {
                                        aTaskIdList.add(aTasks.get(aPosition).getId());
                                        aTasks.remove(aPosition.intValue());
                                    }
                                    if (aTasks.size() <= 0) {
                                        mSectionedRecyclerViewAdapter.removeSection(aKey);
                                    }
                                    aMapEntry.getValue().clear();
                                }
                                mCountItemsSelected = 0;
                                mSectionedRecyclerViewAdapter.notifyDataSetChanged();
                                mButtonsLayout.setVisibility(View.GONE);
                                mAddTaskButton.setVisibility(View.VISIBLE);
                                new deleteTasks().execute(aTaskIdList);
                            } else if (which == DialogAction.NEGATIVE) {
                                Toast.makeText(ContextManager.getCurrentActivityContext(),
                                        "Delete" +
                                                " Canceled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        }
    }

    public void rescheduleButtonAction() {

        String aTimeStamp = CommonUtils.getDayStartMillis(null);
        final String mMinTimeAllowed = aTimeStamp;
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(Long.parseLong(aTimeStamp));
        aCalendar.add(Calendar.YEAR, 2);
        Long aMaxTime = aCalendar.getTimeInMillis();
        final String mMaxTimeAllowed = aMaxTime.toString();

        MaterialDialog dialog = new MaterialDialog.Builder(ContextManager
                .getCurrentActivityContext())
                .customView(R.layout.date_picker_layout, true)
                .positiveText("OK")
                .negativeText("CANCEL")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        if (which == DialogAction.POSITIVE) {
                            DatePicker aDatePicker = (DatePicker) dialog.getCustomView()
                                    .findViewById(R.id
                                            .dp_dpl);
                            String aSelectedMillis = CommonUtils.getMilliseconds(aDatePicker
                                    .getDayOfMonth(), aDatePicker.getMonth(), aDatePicker.getYear
                                    ());
                            if (aSelectedMillis.compareTo(mMinTimeAllowed) < 0) {
                                mRescheduleTime = mMinTimeAllowed;
                            } else if (aSelectedMillis.compareTo(mMaxTimeAllowed) > 0) {
                                mRescheduleTime = mMaxTimeAllowed;
                            } else {
                                mRescheduleTime = aSelectedMillis;
                            }

                            String aContent = "";
                            if (mCountItemsSelected == 1) {
                                aContent = "Reschedule " + mCountItemsSelected
                                        .toString() + " task to " + CommonUtils.getDateFromMillis
                                        (mRescheduleTime) + " ?";
                            } else {
                                aContent = "Reschedule " + mCountItemsSelected
                                        .toString() + " tasks to " + CommonUtils.getDateFromMillis
                                        (mRescheduleTime) + " ?";
                            }
                            new MaterialDialog.Builder(ContextManager.getCurrentActivityContext())
                                    .content(aContent)
                                    .positiveText("CONFIRM")
                                    .negativeText("CANCEL")
                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog,
                                                            @NonNull DialogAction which) {
                                            if (which == DialogAction.POSITIVE) {
                                                new rescheduleTasks().execute();
                                            } else if (which == DialogAction.NEGATIVE) {
                                                Toast.makeText(ContextManager
                                                        .getCurrentActivityContext(), "Reschedule" +
                                                        " Canceled", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .show();
                        }

                    }
                })
                .show();

        DatePicker aDatePicker = (DatePicker) dialog.getCustomView().findViewById(R.id
                .dp_dpl);
        Calendar aCal = Calendar.getInstance();
        aCal.setTimeInMillis(Long.parseLong(mMinTimeAllowed));
        aDatePicker.updateDate(aCal.get(Calendar.YEAR), aCal.get(Calendar.MONTH),
                aCal.get(Calendar.DAY_OF_MONTH));
        aDatePicker.setMinDate(Long.parseLong(mMinTimeAllowed));
        aDatePicker.setMaxDate(Long.parseLong(mMaxTimeAllowed));

    }

    private class SetUpTodayView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            addOverdueTasks();
            addTodayTasks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUIAfterSetup();
        }
    }

    private class SetUpSevenDaysView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("++TIME++", "This week start");
            addOverdueTasks();
            addTodayTasks();
            addTomorrowTasks();
            addSevenDayRemainingTasks();
            Log.d("++TIME++", "This week start");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUIAfterSetup();
        }
    }

    private class SetUpAllDaysView extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("++TIME++", "AllTask start");
            addOverdueTasks();
            addTodayTasks();
            addUpcomingTasks();
            Log.d("++TIME++", "AllTask stop");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUIAfterSetup();
        }
    }

    private class deleteTasks extends AsyncTask<List<String>, Void, Void> {
        @Override
        protected Void doInBackground(List<String>... lists) {
            for (String iId : lists[0]) {
                DatabaseMethods.deleteTask(iId);
            }
            return null;
        }
    }

    private class rescheduleTasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            List<Task> aTaskList = new ArrayList<>();
            for (HashMap.Entry<String, TreeSet<Integer>> aMapEntry : mSelectedItemMap.entrySet()) {
                String aKey = aMapEntry.getKey();
                List<Task> aTasks = mAdapterMap.get(aKey).getItemList();
                for (Integer aPosition : aMapEntry.getValue()) {
                    aTaskList.add(aTasks.get(aPosition));
                    aTasks.remove(aPosition.intValue());
                }
                if (aTasks.size() <= 0) {
                    mSectionedRecyclerViewAdapter.removeSection(aKey);
                }
                aMapEntry.getValue().clear();
            }
            mCountItemsSelected = 0;

            for (Task aTask : aTaskList) {
                aTask.setTime(mRescheduleTime);
                DatabaseMethods.updateTask(aTask);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mButtonsLayout.setVisibility(View.GONE);
            mAddTaskButton.setVisibility(View.VISIBLE);
            setUpView();
            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private class updateTasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            List<Task> aTaskList = new ArrayList<>();
            for (HashMap.Entry<String, TreeSet<Integer>> aMapEntry : mSelectedItemMap.entrySet()) {
                String aKey = aMapEntry.getKey();
                List<Task> aTasks = mAdapterMap.get(aKey).getItemList();
                for (Integer aPosition : aMapEntry.getValue()) {
                    aTaskList.add(aTasks.get(aPosition));
                    aTasks.remove(aPosition.intValue());
                }
                if (aTasks.size() <= 0) {
                    mSectionedRecyclerViewAdapter.removeSection(aKey);
                }
                aMapEntry.getValue().clear();
            }
            mCountItemsSelected = 0;

            HashMap<String, Integer> aPatternIDMaxRepMap = new HashMap<>();
            for (Task aTask : aTaskList) {
                TaskDetail aTaskDetail = DatabaseMethods.getTaskDetailByID(aTask.getId());
                Integer aRepetitionMax = -1;

                if (!aPatternIDMaxRepMap.containsKey(aTaskDetail.getPatternID())) {
                    RepetitionPattern aRepetitionPattern = DatabaseMethods
                            .getRepetitionPatternFromId(aTaskDetail.getPatternID());
                    aRepetitionMax = aRepetitionPattern
                            .getRepetitions();
                    aPatternIDMaxRepMap.put(aRepetitionPattern.getId(), aRepetitionMax);
                } else {
                    aRepetitionMax = aPatternIDMaxRepMap.get(aTaskDetail.getPatternID());
                }
                if (aTaskDetail.getCurrentRepetition() < aRepetitionMax) {
                    Integer aNewRepNumber = aTaskDetail.getCurrentRepetition() + 1;

                    if (SettingManager.isTodayBaseDate()) {
                        aTask.setTime(CommonUtils.getOffsetTimeInMillis(null, DatabaseMethods
                                .getRepetitionPatternSpace(aTaskDetail
                                        .getPatternID(), aNewRepNumber).getSpace()));
                    } else {
                        aTask.setTime(CommonUtils.addDaysToMillis(aTask.getTime(), DatabaseMethods
                                .getRepetitionPatternSpace(aTaskDetail
                                        .getPatternID(), aNewRepNumber).getSpace()));
                    }

                    DatabaseMethods.updateTask(aTask);
                    aTaskDetail.setCurrentRepetition(aNewRepNumber);
                    DatabaseMethods.updateTaskDetails(aTaskDetail);

                } else {
                    DatabaseMethods.deleteTask(aTask.getId());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mButtonsLayout.setVisibility(View.GONE);
            mAddTaskButton.setVisibility(View.VISIBLE);
            setUpView();
            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
        }


    }

}
