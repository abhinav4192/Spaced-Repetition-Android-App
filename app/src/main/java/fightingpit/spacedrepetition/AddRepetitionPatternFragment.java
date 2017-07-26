package fightingpit.spacedrepetition;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fightingpit.spacedrepetition.Adapter.NumberSelectorAdapter;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRepetitionPatternFragment extends Fragment {

    @BindView(R.id.et_pattern_name_farp)
    MaterialEditText mPatternNameView;
    @BindView(R.id.ll_pattern_selector_farp)
    LinearLayout mPatternSelectorLayout;
    ArrayList<String> mDayNumberList = new ArrayList<>();
    ArrayList<Boolean> mDaysSelectedList = new ArrayList<>();
    ArrayList<Boolean> mOriginalDaysSelectedList;
    private Unbinder mUnbinder;


    public AddRepetitionPatternFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_repetition_pattern, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void initialize() {

        mPatternNameView.addValidator(new RegexpValidator("Pattern Name should be 3 or more " +
                "characters",
                "^....*$"));

        for (Integer i = 1; i <= 365; i++) {
            mDayNumberList.add(i.toString());
            mDaysSelectedList.add(false);
        }
    }

    @OnClick(R.id.ll_pattern_selector_farp)
    public void patternSelectorClicked() {
        mOriginalDaysSelectedList = new ArrayList<>(mDaysSelectedList);
        final NumberSelectorAdapter aNumberSelectorAdapter = new NumberSelectorAdapter(mDayNumberList,
                mDaysSelectedList);

        MaterialDialog dialog = new MaterialDialog.Builder(ContextManager
                .getCurrentActivityContext())
                .customView(R.layout.pattern_selector_layout, false)
                .title("Select Repetition Days")
                .positiveText("OK")
                .negativeText("CANCEL")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction
                            which) {
                        if (which == DialogAction.POSITIVE) {
                            mOriginalDaysSelectedList = new ArrayList<>(mDaysSelectedList);
                        } else if (which == DialogAction.NEGATIVE) {
                            for (int i = 0; i < mDaysSelectedList.size(); i++) {
                                mDaysSelectedList.set(i, mOriginalDaysSelectedList.get(i));
                            }
                        }
                    }
                })
                .show();
        GridView aPatternSelector = (GridView) dialog.getCustomView().findViewById(R.id
                .gv_number_selector_psl);
        aPatternSelector.setAdapter(aNumberSelectorAdapter);
    }

    @OnClick(R.id.bt_add_pattern_farp)
    public void addPatternClicked() {


        ArrayList<Integer> aPattern = new ArrayList<>();
        for(Integer i = 0; i< mDaysSelectedList.size(); i++)
            if(mDaysSelectedList.get(i)) aPattern.add(Integer.parseInt(mDayNumberList.get(i)));

        if(aPattern.size() <=0){
            mPatternNameView.validate();
            Toast.makeText(ContextManager.getCurrentActivityContext(),"Please select repetition " +
                    "pattern",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mPatternNameView.validate()){
            DatabaseMethods.addRepetitionPattern(mPatternNameView.getText().toString(),aPattern);
            ((Activity) ContextManager.getCurrentActivityContext()).finish();
        }

    }
}
