package fightingpit.spacedrepetition.Engine;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Initializes resources at application level.
 * Should be used maintaining resources which are frequently needed and costly to initialize.
 */
public class GlobalApplication extends Application {

    public void onCreate() {
        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(this);
        // add for verbose logging
        //        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }


}
