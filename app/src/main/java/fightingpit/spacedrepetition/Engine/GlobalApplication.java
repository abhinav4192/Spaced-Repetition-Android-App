package fightingpit.spacedrepetition.Engine;

import android.app.Application;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;

/**
 * Initializes resources at application level.
 * Should be used maintaining resources which are frequently needed and costly to initialize.
 */
public class GlobalApplication extends Application {
    DatabaseMethods mDatabaseMethods = null;

    public void init()
    {
        mDatabaseMethods = new DatabaseMethods();
    }

    public DatabaseMethods getDatabaseMethods() {
        return mDatabaseMethods;
    }


}
