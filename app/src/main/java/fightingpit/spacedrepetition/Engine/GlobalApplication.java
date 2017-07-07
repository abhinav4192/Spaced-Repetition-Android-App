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
        mDatabaseMethods.updateWordList();
        mTextToSpeechManager = new TextToSpeechManager();
        mTextToSpeechManager.init();
    }

    public DatabaseMethods getDatabaseMethods() {
        return mDatabaseMethods;
    }

    public TextToSpeechManager getTextToSpeechManager() {
        return mTextToSpeechManager;
    }

}
