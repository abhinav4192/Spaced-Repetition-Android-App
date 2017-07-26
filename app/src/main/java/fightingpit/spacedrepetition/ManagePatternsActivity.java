package fightingpit.spacedrepetition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Adapter.NumberSelectorAdapter;
import fightingpit.spacedrepetition.Engine.ContextManager;

public class ManagePatternsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patterns);
        ContextManager.setCurrentActivityContext(this);
        ButterKnife.bind(this);
        getFragmentManager().beginTransaction().replace(R.id.fl_main_fragment_amp, new RepetitionPatternMainFragment())
                .commit();


    }
}
