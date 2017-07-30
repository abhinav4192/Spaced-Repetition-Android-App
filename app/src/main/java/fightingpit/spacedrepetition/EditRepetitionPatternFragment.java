package fightingpit.spacedrepetition;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditRepetitionPatternFragment extends Fragment {

    @BindView(R.id.et_pattern_name_ferp)
    MaterialEditText mPatternNameView;
    private Unbinder mUnbinder;
    private String mID;
    private String mName;


    public EditRepetitionPatternFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_repetition_pattern, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle aBundle = this.getArguments();
        mID = aBundle.getString("Id");
        mName = aBundle.getString("Name");
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
        mPatternNameView.setText(mName);
    }

    @OnClick(R.id.iv_done_ferp)
    public void OnCheckClicked() {
        if (mPatternNameView.validate()) {
            DatabaseMethods.updateRepetitionPatternNameById(mID, mPatternNameView.getText()
                    .toString());
            getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp,
                    new RepetitionPatternMainFragment())
                    .commit();
        }
    }

    @OnClick(R.id.iv_close_ferp)
    public void OnCloseClicked() {
        getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp,
                new RepetitionPatternMainFragment())
                .commit();
    }

    @OnClick(R.id.iv_delete_ferp)
    public void OnDeleteClicked() {
        new MaterialDialog.Builder(ContextManager
                .getCurrentActivityContext())
                .title("Delete " + mName)
                .content("This will also delete all the tasks using this repetition " +
                        "pattern")
                .positiveText("CONFIRM")
                .negativeText("CANCEL")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                    DialogAction
                            which) {
                        if (which == DialogAction.POSITIVE) {
                            new deletePattern().execute();
                            ((Activity) ContextManager.getCurrentActivityContext()).finish();
                        }

                    }
                })
                .show();
    }


    private class deletePattern extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseMethods.deleteRepetitionPattern(mID);
            return null;

        }
    }

}
