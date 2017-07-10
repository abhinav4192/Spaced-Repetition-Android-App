package fightingpit.spacedrepetition;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;

public class AddTaskActivity extends AppCompatActivity {

    String mName;
    String mPatternId;
    String mComment;
    String mTime;
    List<RepetitionPattern> mRepetitionPatterns;
    String mMaxTimeAllowed;
    String mMinTimeAllowed;

    @BindView(R.id.et_aat_name)
    TextView mTaskName;

    @BindView(R.id.tv_aat_repetition_pattern)
    TextView mRepetitionPatternTextView;

    @BindView(R.id.tv_adt_start_date)
    TextView mTaskStartDate;

    @BindView(R.id.et_aat_comments)
    TextView mTaskCommentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ContextManager.setCurrentActivityContext(this);
        ButterKnife.bind(this);

        String aTimeStamp = CommonUtils.getDayStartMillis(null);
        mMinTimeAllowed = aTimeStamp;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(aTimeStamp));
        c.add(Calendar.YEAR,2);
        Long aMaxTime = c.getTimeInMillis();
        mMaxTimeAllowed = aMaxTime.toString();


        mRepetitionPatterns = DatabaseMethods.getAllRepetitionPattern();
        for(RepetitionPattern aRepetitionPattern: mRepetitionPatterns){
            if(aRepetitionPattern.getName().equalsIgnoreCase("Default")){
                mPatternId = aRepetitionPattern.getId();
                mRepetitionPatternTextView.setText("Default");
                break;
            }
        }
        for(RepetitionPatternSpace aRepetitionPatternSpace:DatabaseMethods
                .getRepetitionPatternSpace(mPatternId)){
            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                mTime = CommonUtils.getOffsetTimeInMillis(0,aRepetitionPatternSpace.getSpace());
            }
        }
        mTaskStartDate.setText(CommonUtils.getDateFromMillis(mTime));

    }

    @OnClick(R.id.tv_adt_start_date)
    public void changeDate(){
        MaterialDialog dialog =new MaterialDialog.Builder(this)
                .customView(R.layout.date_picker_layout, true)
                .positiveText("OK")
                .negativeText("CANCEL")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(which==DialogAction.POSITIVE){
                            Log.d("fdfd", "Action Postive");
                            DatePicker aDatePicker = (DatePicker) dialog.getCustomView().findViewById(R.id
                             .dp_dpl);
                            String aSelectedMillis = CommonUtils.getMilliseconds(aDatePicker
                                    .getDayOfMonth(),aDatePicker.getMonth(),aDatePicker.getYear());
                            if(aSelectedMillis.compareTo(mMinTimeAllowed) <0){
                                mTime = mMinTimeAllowed;
                            }else if (aSelectedMillis.compareTo(mMaxTimeAllowed) >0){
                                mTime = mMaxTimeAllowed;
                            }else{
                                mTime = aSelectedMillis;
                            }
                            mTaskStartDate.setText(CommonUtils.getDateFromMillis(mTime));
                        }

                    }
                })
                .show();

        DatePicker aDatePicker = (DatePicker) dialog.getCustomView().findViewById(R.id
                .dp_dpl);

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(Long.parseLong(mTime));
        aDatePicker.updateDate(aCalendar.get(Calendar.YEAR),aCalendar.get(Calendar.MONTH),
                aCalendar.get(Calendar.DAY_OF_MONTH));
        aDatePicker.setMinDate(Long.parseLong(mMinTimeAllowed));
        aDatePicker.setMaxDate(Long.parseLong(mMaxTimeAllowed));

    }

    @OnClick(R.id.tv_aat_repetition_pattern)
    public void setRepetitionPattern(){
        ArrayList<String> aPatternNames = new ArrayList<>();
        for(RepetitionPattern aPattern : mRepetitionPatterns){
            aPatternNames.add(aPattern.getName());
        }

        new MaterialDialog.Builder(ContextManager.getCurrentActivityContext())
                .title("Select a Repetition Pattern")
                .items(aPatternNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mRepetitionPatternTextView.setText(mRepetitionPatterns.get(which).getName());
                        mPatternId = mRepetitionPatterns.get(which).getId();
                        for(RepetitionPatternSpace aRepetitionPatternSpace:DatabaseMethods
                                .getRepetitionPatternSpace(mPatternId)){
                            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                                mTime = CommonUtils.getOffsetTimeInMillis(0,aRepetitionPatternSpace.getSpace());
                            }
                        }
                        mTaskStartDate.setText(CommonUtils.getDateFromMillis(mTime));
                    }
                })
                .show();
    }

    @OnClick(R.id.bt_aat_add_task)
    public void addTaskClicked(){
        mName = mTaskName.getText().toString();
        mComment = mTaskCommentView.getText().toString();
        if(mName.isEmpty())
        {
            Toast.makeText(ContextManager.getCurrentActivityContext(),"Please Provide a task " +
                    "name",Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseMethods.addTask(mName,mComment,mPatternId,mTime);
        finish();
    }
}
