package fightingpit.spacedrepetition;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

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

    @BindView(R.id.ll_aat)
    LinearLayout mOuterLayout;

    @BindView(R.id.et_aat_name)
    MaterialEditText mTaskName;

    @BindView(R.id.tv_aat_repetition_pattern)
    TextView mRepetitionPatternTextView;

    @BindView(R.id.tv_adt_start_date)
    TextView mTaskStartDate;

    @BindView(R.id.et_aat_comments)
    MaterialEditText mTaskCommentView;

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
        c.add(Calendar.YEAR, 2);
        Long aMaxTime = c.getTimeInMillis();
        mMaxTimeAllowed = aMaxTime.toString();


        mRepetitionPatterns = DatabaseMethods.getAllRepetitionPattern();
        for (RepetitionPattern aRepetitionPattern : mRepetitionPatterns) {
            if (aRepetitionPattern.getName().equalsIgnoreCase("Default")) {
                mPatternId = aRepetitionPattern.getId();
                mRepetitionPatternTextView.setText("Default");
                break;
            }
        }
        for (RepetitionPatternSpace aRepetitionPatternSpace : DatabaseMethods
                .getRepetitionPatternSpace(mPatternId)) {
            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                mTime = CommonUtils.getOffsetTimeInMillis(0, aRepetitionPatternSpace.getSpace());
            }
        }
        mTaskStartDate.setText(CommonUtils.getDateFromMillis(mTime));
        setUpTaskNameEditText();
        setUpCommentsEditText();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof MaterialEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                final View aFinalView = v;
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // change image
                            aFinalView.clearFocus();
                            ((MaterialEditText) aFinalView).setCursorVisible(false);
                            ((MaterialEditText) aFinalView)
                                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }, 100);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @OnClick(R.id.tv_adt_start_date)
    public void changeDate() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.date_picker_layout, true)
                .positiveText("OK")
                .negativeText("CANCEL")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        if (which == DialogAction.POSITIVE) {
                            Log.d("fdfd", "Action Postive");
                            DatePicker aDatePicker = (DatePicker) dialog.getCustomView()
                                    .findViewById(R.id
                                    .dp_dpl);
                            String aSelectedMillis = CommonUtils.getMilliseconds(aDatePicker
                                    .getDayOfMonth(), aDatePicker.getMonth(), aDatePicker.getYear
                                    ());
                            if (aSelectedMillis.compareTo(mMinTimeAllowed) < 0) {
                                mTime = mMinTimeAllowed;
                            } else if (aSelectedMillis.compareTo(mMaxTimeAllowed) > 0) {
                                mTime = mMaxTimeAllowed;
                            } else {
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
        aDatePicker.updateDate(aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH),
                aCalendar.get(Calendar.DAY_OF_MONTH));
        aDatePicker.setMinDate(Long.parseLong(mMinTimeAllowed));
        aDatePicker.setMaxDate(Long.parseLong(mMaxTimeAllowed));

    }

    @OnClick(R.id.tv_aat_repetition_pattern)
    public void setRepetitionPattern() {
        ArrayList<String> aPatternNames = new ArrayList<>();
        for (RepetitionPattern aPattern : mRepetitionPatterns) {
            aPatternNames.add(aPattern.getName());
        }

        new MaterialDialog.Builder(ContextManager.getCurrentActivityContext())
                .title("Select a Repetition Pattern")
                .items(aPatternNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which,
                                            CharSequence text) {
                        mRepetitionPatternTextView.setText(mRepetitionPatterns.get(which).getName
                                ());
                        mPatternId = mRepetitionPatterns.get(which).getId();
                        for (RepetitionPatternSpace aRepetitionPatternSpace : DatabaseMethods
                                .getRepetitionPatternSpace(mPatternId)) {
                            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                                mTime = CommonUtils.getOffsetTimeInMillis(0,
                                        aRepetitionPatternSpace.getSpace());
                            }
                        }
                        mTaskStartDate.setText(CommonUtils.getDateFromMillis(mTime));
                    }
                })
                .show();
    }

    @OnClick(R.id.bt_aat_add_task)
    public void addTaskClicked() {


        mComment = mTaskCommentView.getText().toString();
        if (mTaskName.validate()) {
            mName = mTaskName.getText().toString();
            mComment = mTaskCommentView.getText().toString();
            DatabaseMethods.addTask(mName, mComment, mPatternId, mTime);
            finish();

        }
        return;

    }

    private void setUpTaskNameEditText() {

        mTaskName.addValidator(new RegexpValidator("Task Name should be 3 or more characters",
                "^....*$"));

        mTaskName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mTaskName.setCursorVisible(true);
                mTaskName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mTaskName.getRight() - mTaskName.getCompoundDrawables
                            ()[DRAWABLE_RIGHT].getBounds().width())) {
                        mTaskName.getText().clear();
                    }
                }
                return false;
            }
        });

        mTaskName.setOnEditorActionListener(new MaterialEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTaskName.getWindowToken(), 0);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // change image
                            mTaskName.clearFocus();
                            mTaskName.setCursorVisible(false);
                            mTaskName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }

                    }, 100);
                }
                return false;
            }
        });
    }

    public void setUpCommentsEditText() {
        mTaskCommentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mTaskCommentView.setCursorVisible(true);
                mTaskCommentView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                        .ic_close, 0);

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mTaskCommentView.getRight() - mTaskCommentView
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        mTaskCommentView.getText().clear();
                    }
                }
                return false;
            }
        });

        mTaskCommentView.setOnEditorActionListener(new MaterialEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Clear focus here from edittext
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTaskCommentView.getWindowToken(), 0);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // change image
                            mTaskCommentView.clearFocus();
                            mTaskCommentView.setCursorVisible(false);
                            mTaskCommentView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }

                    }, 100);


                }
                return false;
            }
        });
    }


}
