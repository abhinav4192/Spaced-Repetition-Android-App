package fightingpit.spacedrepetition.Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fightingpit.spacedrepetition.R;

/**
 * Created by abhinavgarg on 18/07/17.
 */
public class SettingManager {

    static SharedPreferences mSharedPref;
    static SharedPreferences mDefaultSharedPref;
    static SharedPreferences.Editor mEditor;

    private static final String ALPHA_OR_SHUFFLE = "ALPHA_OR_SHUFFLE";

    private static void updateSettingManager() {
        mSharedPref = ContextManager.getCurrentActivityContext()
                .getSharedPreferences(ContextManager.getCurrentActivityContext().getResources()
                        .getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        mDefaultSharedPref = PreferenceManager.getDefaultSharedPreferences(ContextManager
                .getCurrentActivityContext());
        mEditor = mSharedPref.edit();
    }

    public static boolean isTodayBaseDate() {
        updateSettingManager();
        return mDefaultSharedPref.getBoolean(ContextManager.getCurrentActivityContext()
                .getResources().getString(R.string.pref_next_time_ref_today_key), false);
    }
}
