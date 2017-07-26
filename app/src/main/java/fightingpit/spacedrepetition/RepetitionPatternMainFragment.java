package fightingpit.spacedrepetition;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepetitionPatternMainFragment extends Fragment {


    private Unbinder mUnbinder;

    public RepetitionPatternMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repetition_pattern_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.bt_add_pattern_frrm) public void addPatternClicked(){
        getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp, new AddRepetitionPatternFragment())
                .commit();
    }


}
