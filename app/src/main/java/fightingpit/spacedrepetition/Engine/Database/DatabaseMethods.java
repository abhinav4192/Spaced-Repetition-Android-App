package fightingpit.spacedrepetition.Engine.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;

/**
 * Contains implementation of methods which read/write/update Database.
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
        aWritableDatabase.close();
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    public ArrayList<RepetitionPattern> getAllRepetitionPattern()
    {
        ArrayList<RepetitionPattern> aRepetitionPatterns = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();
        Cursor aCursor = aReadableDatabase.query(DatabaseContract.RepetitionPattern.TABLE_NAME,
                null, null, null, null, null, null);
        aCursor.moveToFirst();
        while(!aCursor.isAfterLast()){
            RepetitionPattern aCurrentPattern = new RepetitionPattern();
            aCurrentPattern.setId(aCursor.getString(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPattern.ID)));
            aCurrentPattern.setName(aCursor.getString(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPattern.NAME)));
            aCurrentPattern.setRepetitions(aCursor.getInt(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPattern.REPETITIONS)));
            aRepetitionPatterns.add(aCurrentPattern);
            aCursor.moveToNext();
        }
        aCursor.close();
        aReadableDatabase.close();

        return aRepetitionPatterns;
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
        aWritableDatabase.close();
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    public ArrayList<RepetitionPatternSpace> getRepetitionPatternSpace(String iId){

        ArrayList<RepetitionPatternSpace> aRepetitionPatternSpaces = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();

        String aSelection = DatabaseContract.RepetitionPatternSpace.ID + "=?";
        String[] aSelectionArgs = {iId};

        Cursor aCursor = aReadableDatabase.query(DatabaseContract.RepetitionPatternSpace.TABLE_NAME,
                null, aSelection, aSelectionArgs, null, null, DatabaseContract
                        .RepetitionPatternSpace.REPETITION_NUMBER);
        aCursor.moveToFirst();
        while(!aCursor.isAfterLast()){
            RepetitionPatternSpace aCurrentPatternSpace = new RepetitionPatternSpace();
            aCurrentPatternSpace.setId(aCursor.getString(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPatternSpace.ID)));
            aCurrentPatternSpace.setRepetitionNumber(aCursor.getInt(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPatternSpace.REPETITION_NUMBER)));
            aCurrentPatternSpace.setSpace(aCursor.getInt(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.RepetitionPatternSpace.SPACE)));
            aRepetitionPatternSpaces.add(aCurrentPatternSpace);
            aCursor.moveToNext();
        }
        aCursor.close();
        aReadableDatabase.close();

        return aRepetitionPatternSpaces;
    }

    public boolean addTaskDetail(TaskDetail iTaskDetail)
    {
        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.TaskDetails.ID,iTaskDetail.getId());
        aContentValues.put(DatabaseContract.TaskDetails.NAME,iTaskDetail.getName());
        aContentValues.put(DatabaseContract.TaskDetails.COMMENT,iTaskDetail.getComment());
        aContentValues.put(DatabaseContract.TaskDetails.PATTERN_ID,iTaskDetail.getPatternID());
        aContentValues.put(DatabaseContract.TaskDetails.CURRENT_REPETITION,iTaskDetail.getCurrentRepetition());

        long aRowID= aWritableDatabase.insert(DatabaseContract.TaskDetails.TABLE_NAME,null,
                aContentValues);
        aWritableDatabase.close();
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    public boolean addScheduledTask(Task iTask)
    {
        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.ScheduledTasks.ID,iTask.getId());
        aContentValues.put(DatabaseContract.ScheduledTasks.NAME,iTask.getName());
        aContentValues.put(DatabaseContract.ScheduledTasks.TIME,iTask.getTime());

        long aRowID= aWritableDatabase.insert(DatabaseContract.ScheduledTasks.TABLE_NAME,null,
                aContentValues);
        aWritableDatabase.close();
        if(aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }
}
