package fightingpit.spacedrepetition.Engine.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;

/**
 * Contains implementation of methods which read/write/update Database.
 */
public final class DatabaseMethods extends DatabaseHelper {

    /**
     * Add a repetitionPattern in DB.
     *
     * @param iRepetitionPattern repetitionPattern to Add.
     * @return true if successfully added. False Otherwise.
     */

    public boolean addRepetitionPattern(RepetitionPattern iRepetitionPattern) {

        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.RepetitionPattern.ID, iRepetitionPattern.getId());
        aContentValues.put(DatabaseContract.RepetitionPattern.NAME, iRepetitionPattern.getName());
        aContentValues.put(DatabaseContract.RepetitionPattern.REPETITIONS, iRepetitionPattern
                .getRepetitions());

        long aRowID = aWritableDatabase.insert(DatabaseContract.RepetitionPattern.TABLE_NAME, null,
                aContentValues);
        aWritableDatabase.close();
        if (aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    /**
     * Get all RepetitionPatterns from DB.
     *
     * @return ArrayList of RepetitionPatterns
     */
    public ArrayList<RepetitionPattern> getAllRepetitionPattern() {
        ArrayList<RepetitionPattern> aRepetitionPatterns = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();
        Cursor aCursor = aReadableDatabase.query(DatabaseContract.RepetitionPattern.TABLE_NAME,
                null, null, null, null, null, null);
        aCursor.moveToFirst();
        while (!aCursor.isAfterLast()) {
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

    /**
     * Add repetition space for a repetition pattern.
     *
     * @param iRepetitionPatternSpace Repetition space to add
     * @return true if successfully added. False Otherwise.
     */
    public boolean addRepetitionPatternSpace(RepetitionPatternSpace iRepetitionPatternSpace) {

        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.ID, iRepetitionPatternSpace
                .getId());
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.REPETITION_NUMBER,
                iRepetitionPatternSpace.getRepetitionNumber());
        aContentValues.put(DatabaseContract.RepetitionPatternSpace.SPACE, iRepetitionPatternSpace
                .getSpace());

        long aRowID = aWritableDatabase.insert(DatabaseContract.RepetitionPatternSpace
                        .TABLE_NAME, null,
                aContentValues);
        aWritableDatabase.close();
        if (aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    /**
     * Get All repetition spaces for patterns in DB.
     *
     * @param iId If null, get All repetition spaces for All patterns in DB.
     *            If not null,  get All repetition spaces for pattern ID iId.
     * @return ArrayList of Repetition Spaces
     */
    public ArrayList<RepetitionPatternSpace> getRepetitionPatternSpace(String iId) {

        ArrayList<RepetitionPatternSpace> aRepetitionPatternSpaces = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();

        String aSelection = DatabaseContract.RepetitionPatternSpace.ID + "=?";
        String[] aSelectionArgs = {iId};

        Cursor aCursor = aReadableDatabase.query(DatabaseContract.RepetitionPatternSpace.TABLE_NAME,
                null, aSelection, aSelectionArgs, null, null, DatabaseContract
                        .RepetitionPatternSpace.REPETITION_NUMBER);
        aCursor.moveToFirst();
        while (!aCursor.isAfterLast()) {
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

    /**
     * Add Task Details in DB
     *
     * @param iTaskDetail Task Details to be added
     * @return true if successfully added. False Otherwise.
     */
    public boolean addTaskDetail(TaskDetail iTaskDetail) {
        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.TaskDetails.ID, iTaskDetail.getId());
        aContentValues.put(DatabaseContract.TaskDetails.NAME, iTaskDetail.getName());
        aContentValues.put(DatabaseContract.TaskDetails.COMMENT, iTaskDetail.getComment());
        aContentValues.put(DatabaseContract.TaskDetails.PATTERN_ID, iTaskDetail.getPatternID());
        aContentValues.put(DatabaseContract.TaskDetails.CURRENT_REPETITION, iTaskDetail
                .getCurrentRepetition());

        long aRowID = aWritableDatabase.insert(DatabaseContract.TaskDetails.TABLE_NAME, null,
                aContentValues);
        aWritableDatabase.close();
        if (aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    /**
     * Schedule a Task
     *
     * @param iTask task to be scheduled
     * @return true if successfully added. False Otherwise.
     */
    public boolean addScheduledTask(TaskDetail iTask) {
        boolean aReturnValue = false;
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.ScheduledTasks.ID, iTask.getId());
        aContentValues.put(DatabaseContract.ScheduledTasks.NAME, iTask.getName());
        aContentValues.put(DatabaseContract.ScheduledTasks.TIME, iTask.getTime());

        long aRowID = aWritableDatabase.insert(DatabaseContract.ScheduledTasks.TABLE_NAME, null,
                aContentValues);
        aWritableDatabase.close();
        if (aRowID != -1)
            aReturnValue = true;
        return aReturnValue;
    }

    /**
     * Get Task Details from DB.
     *
     * @param iTaskId If not null, get Task Details for iTaskId. If null, get task details for all
     *                tasks.
     * @return ArrayList of Task Details.
     */
    public ArrayList<TaskDetail> getTasks(String iTaskId) {
        ArrayList<TaskDetail> aTaskDetails = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();

        String aSelection = null;
        String[] aSelectionArgs = null;

        if (iTaskId != null) {
            aSelection = DatabaseContract.TaskDetails.ID + "=?";
            aSelectionArgs = new String[]{iTaskId};
        }
        Cursor aCursor = aReadableDatabase.query(DatabaseContract.TaskDetails.TABLE_NAME,
                null, aSelection, aSelectionArgs, null, null, null);
        aCursor.moveToFirst();
        while (!aCursor.isAfterLast()) {
            TaskDetail aTaskDetail = new TaskDetail();
            aTaskDetail.setTime(null);
            aTaskDetail.setId(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .TaskDetails.ID)));
            aTaskDetail.setName(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .TaskDetails.NAME)));
            aTaskDetail.setComment(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .TaskDetails.COMMENT)));
            aTaskDetail.setPatternID(aCursor.getString(aCursor.getColumnIndexOrThrow
                    (DatabaseContract
                            .TaskDetails.PATTERN_ID)));
            aTaskDetail.setCurrentRepetition(aCursor.getInt(aCursor.getColumnIndexOrThrow
                    (DatabaseContract.TaskDetails.CURRENT_REPETITION)));
            aTaskDetails.add(aTaskDetail);
            aCursor.moveToNext();
        }
        aCursor.close();
        aReadableDatabase.close();

        return aTaskDetails;
    }

    /**
     * Get tasks schedule between provided time.
     *
     * @param iAfterTime  If not null, only show tasks after iAfterTime
     * @param iBeforeTime If not null, only show tasks before iBeforeTime
     * @return ArrayList for tasks found between given time.
     */
    public ArrayList<Task> getScheduledTasks(String iAfterTime, String iBeforeTime) {
        ArrayList<Task> aTasks = new ArrayList<>();
        SQLiteDatabase aReadableDatabase = getReadableDatabase();

        String aSelection = null;
        String[] aSelectionArgs = null;
        if (iAfterTime != null && iBeforeTime != null) {
            aSelection = DatabaseContract.ScheduledTasks.TIME + ">=? AND " +
                    DatabaseContract.ScheduledTasks.TIME + "<?";
            aSelectionArgs = new String[]{iAfterTime, iBeforeTime};
        } else if (iAfterTime != null) {
            aSelection = DatabaseContract.ScheduledTasks.TIME + ">=?";
            aSelectionArgs = new String[]{iAfterTime};
        } else if (iBeforeTime != null) {
            aSelection = DatabaseContract.ScheduledTasks.TIME + "<?";
            aSelectionArgs = new String[]{iBeforeTime};
        }
        Cursor aCursor = aReadableDatabase.query(DatabaseContract.ScheduledTasks.TABLE_NAME,
                null, aSelection, aSelectionArgs, null, null, null);
        aCursor.moveToFirst();
        while (!aCursor.isAfterLast()) {
            Task aTask = new Task();
            aTask.setId(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .ScheduledTasks.ID)));
            aTask.setName(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .ScheduledTasks.NAME)));
            aTask.setTime(aCursor.getString(aCursor.getColumnIndexOrThrow(DatabaseContract
                    .ScheduledTasks.TIME)));

            aTasks.add(aTask);
            aCursor.moveToNext();
        }
        aCursor.close();
        aReadableDatabase.close();

        return aTasks;
    }


    /**
     * Update Scheduled task time
     *
     * @param iTakskId Id of task to be updated
     * @param iNewTime update to this time
     */
    public void updateScheduledTime(String iTakskId, String iNewTime) {
        SQLiteDatabase aWritableDatabase = getWritableDatabase();

        ContentValues aContentValues = new ContentValues();
        aContentValues.put(DatabaseContract.ScheduledTasks.TIME, iNewTime);

        String aSelection = DatabaseContract.ScheduledTasks.ID + "=?";
        String[] aSelectionArgs = {iTakskId};
        aWritableDatabase.update(DatabaseContract.ScheduledTasks.TABLE_NAME, aContentValues,
                aSelection, aSelectionArgs);
        aWritableDatabase.close();
    }

    /**
     * Update Task Details
     *
     * @param iTakskId            Id of task to be update
     * @param iName               New Task name. Null if no update required.
     * @param iComment            New Comment. Null if no update required.
     * @param iToUpdateRepetition true if repetition update is required.
     * @param iRepetition         If iToUpdateRepetition is true, update current repetition to
     *                            iRepetition(can ube updated to null)
     */
    public void updateTask(@NonNull String iTakskId, @Nullable String iName, @Nullable String
            iComment, @NonNull boolean iToUpdateRepetition, @Nullable Integer iRepetition) {

        SQLiteDatabase aWritableDatabase = getWritableDatabase();
        ContentValues aContentValues = new ContentValues();
        String aSelection = DatabaseContract.TaskDetails.ID + "=?";
        String[] aSelectionArgs = {iTakskId};

        if (iName != null) {
            aContentValues.put(DatabaseContract.TaskDetails.NAME, iName);
        }
        if (iComment != null) {
            aContentValues.put(DatabaseContract.TaskDetails.COMMENT, iComment);
        }
        if (iToUpdateRepetition) {
            aContentValues.put(DatabaseContract.TaskDetails.CURRENT_REPETITION, iRepetition);
        }
        aWritableDatabase.update(DatabaseContract.TaskDetails.TABLE_NAME, aContentValues,
                aSelection, aSelectionArgs);
        aWritableDatabase.close();
    }
}
