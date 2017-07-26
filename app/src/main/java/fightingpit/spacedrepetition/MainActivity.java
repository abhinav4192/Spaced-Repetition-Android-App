package fightingpit.spacedrepetition;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindString;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Engine.ContextManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_TASK_ACTIVITY = 101;
    public static final int SETTINGS_ACTIVITY = 102;
    public static final int MANAGE_PATTERNS_ACTIVITY = 103;
    int mNavigationSelectedId = R.id.this_week;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    @BindString(R.string.fragment_switch_key) String FRAGMENT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextManager.setCurrentActivityContext(this);
        ButterKnife.bind(this);
        //((GlobalApplication) getApplicationContext()).init();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        Intent i = new Intent(ContextManager.getCurrentActivityContext(),
                                AddTaskActivity.class);
                        startActivityForResult(i,ADD_TASK_ACTIVITY);

                    }
                });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        CommonUtils.testImplementation();

        updateNavigationView(mNavigationSelectedId, true);
        mNavigationView.setCheckedItem(mNavigationSelectedId);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextManager.setCurrentActivityContext(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ContextManager.getCurrentActivityContext(),
                    SettingsActivity.class);
            startActivityForResult(i,SETTINGS_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        updateNavigationView(id, false);
        return true;
    }

    /**
     * Changes the current display based on user action.
     *
     * @param id          NavigationItem Id to be displayed/updated.
     * @param forceUpdate if current navigationItemId is same is param id, update will only be done
     *                    if forceUpdate is true. This is prevent unnecessary actions if user
     *                    select same navigationItem again.
     */
    public void updateNavigationView(int id, boolean forceUpdate) {
        if (forceUpdate || id != mNavigationSelectedId) {

            if( id == R.id.today_tasks){
                //mToolbar.setTitle("Today");
                Fragment aFragment = new ScheduledTaskFragment();
                Bundle aBundle = new Bundle();
                aBundle.putInt(FRAGMENT_KEY,1);
                aFragment.setArguments(aBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_cm, aFragment).commit();
                mNavigationSelectedId = id;
            } else if (id == R.id.this_week) {
                //mToolbar.setTitle("Week");
                Fragment aFragment = new ScheduledTaskFragment();
                Bundle aBundle = new Bundle();
                aBundle.putInt(FRAGMENT_KEY,2);
                aFragment.setArguments(aBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_cm, aFragment).commit();
                mNavigationSelectedId = id;
            } else if (id == R.id.all_tasks) {
                //mToolbar.setTitle("All");
                Fragment aFragment = new ScheduledTaskFragment();
                Bundle aBundle = new Bundle();
                aBundle.putInt(FRAGMENT_KEY,3);
                aFragment.setArguments(aBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_cm, aFragment).commit();
                mNavigationSelectedId = id;

            } else if (id == R.id.manage_patterns) {

                Intent i = new Intent(ContextManager.getCurrentActivityContext(),
                        ManagePatternsActivity.class);
                startActivityForResult(i,MANAGE_PATTERNS_ACTIVITY);
            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContextManager.setCurrentActivityContext(this);

        switch (requestCode) {
            case ADD_TASK_ACTIVITY:
                updateNavigationView(mNavigationSelectedId, true);
                break;
            case SETTINGS_ACTIVITY:
                updateNavigationView(mNavigationSelectedId, true);
                break;
            case MANAGE_PATTERNS_ACTIVITY:
                mNavigationView.setCheckedItem(mNavigationSelectedId);
                break;
            default:
                break;
        }
    }

}
