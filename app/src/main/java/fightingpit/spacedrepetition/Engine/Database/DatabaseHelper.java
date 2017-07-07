package fightingpit.spacedrepetition.Engine.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import fightingpit.spacedrepetition.Engine.ContextManager;

/**
 * Methods to create/update database on schema change.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Context DB_CONTEXT;

    public DatabaseHelper() {
        super(ContextManager.getCurrentActivityContext(), DatabaseContract.DATABASE_NAME, null, DatabaseContract
                .DATABASE_VERSION);
    }


    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating Table
        for (int i = 0; i < DatabaseContract.SQL_CREATE_TABLE_ARRAY.length; i++) {
            db.execSQL(DatabaseContract.SQL_CREATE_TABLE_ARRAY[i]);
        }
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = 0; i < DatabaseContract.SQL_DROP_TABLE_ARRAY.length; i++) {
            db.execSQL(DatabaseContract.SQL_DROP_TABLE_ARRAY[i]);
        }
        onCreate(db);
    }
}

