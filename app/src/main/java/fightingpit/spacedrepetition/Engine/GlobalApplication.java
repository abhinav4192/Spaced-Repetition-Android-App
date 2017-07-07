package fightingpit.spacedrepetition.Engine;

import android.app.Application;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;

/**
 * Created by abhinavgarg on 07/07/17.
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
