package fightingpit.spacedrepetition.Engine.Database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by abhinavgarg on 08/07/17.
 */
@Database(name = AppDatabase.DATABASE_NAME, version = AppDatabase.DATABASE_VERSION)
public class AppDatabase{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppDatabase";
}
