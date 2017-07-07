package fightingpit.spacedrepetition.Engine.Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public final class DatabaseMethods extends DatabaseHelper{

    public boolean addRepetitionPattern(RepetitionPattern iRepetitionPattern){

        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.RepetitionPattern.ID,iRepetitionPattern.getId());
        aContentValues.put(DatabaseContract.RepetitionPattern.NAME,iRepetitionPattern.getName());
        aContentValues.put(DatabaseContract.RepetitionPattern.REPETITIONS,iRepetitionPattern.getRepetitions());

        long aRowID= aWritableDatabase.insert(DatabaseContract.RepetitionPattern.TABLE_NAME,null,
                aContentValues);
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    public boolean addRepetitionPatternSpace(RepetitionPatternSpace iRepetitionPatternSpace){

        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.ID,iRepetitionPatternSpace.getId());
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.REPETITION_NUMBER,
                iRepetitionPatternSpace.getRepetitionNumber());
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.SPACE,iRepetitionPatternSpace
                .getSpace());

        long aRowID= aWritableDatabase.insert(DatabaseContract.RepetitionPatternSpace.TABLE_NAME,null,
                aContentValues);
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }
}
