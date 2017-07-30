package fightingpit.spacedrepetition;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fightingpit.spacedrepetition.Adapter.PatternListAdapter;
import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepetitionPatternMainFragment extends Fragment implements PatternListAdapter
        .OnPatternSelectedListener {

    @BindView(R.id.lv_patter_list_frpm)
    ListView mPatternListView;
    @BindString(R.string.AddPatternId)
    String ADD_PATTERN_ID;
    private Unbinder mUnbinder;
    private PatternListAdapter mPatternListAdapter;
    private List<RepetitionPattern> mRepetitionPatterns;

    public RepetitionPatternMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repetition_pattern_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        setUpPatterView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    void setUpPatterView() {
        // Get All Patterns from DB
        mRepetitionPatterns = DatabaseMethods.getAllRepetitionPattern();

        // Add a Add new pattern at start
        mRepetitionPatterns.add(0, new RepetitionPattern(ADD_PATTERN_ID, "Add Pattern", 0));

        mPatternListAdapter = new PatternListAdapter(this,
                mRepetitionPatterns);
        mPatternListView.setAdapter(mPatternListAdapter);
    }

    @Override
    public void onPatternSelected(final int position, final RepetitionPattern iPattern) {
        if (iPattern.getId().equalsIgnoreCase(ADD_PATTERN_ID)) {
            getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp, new
                    AddRepetitionPatternFragment())
                    .commit();
        } else {
            if (!iPattern.getName().equalsIgnoreCase("Default")) {
                EditRepetitionPatternFragment aFragment = new EditRepetitionPatternFragment();

                Bundle aBundle = new Bundle();
                aBundle.putString("Id", iPattern.getId());
                aBundle.putString("Name", iPattern.getName());
                aFragment.setArguments(aBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp,
                        aFragment)
                        .commit();
            }

        }
    }
}
