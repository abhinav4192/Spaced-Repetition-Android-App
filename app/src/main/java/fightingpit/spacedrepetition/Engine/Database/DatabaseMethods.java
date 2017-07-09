package fightingpit.spacedrepetition.Engine.Database;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;

import java.util.ArrayList;
import java.util.List;

import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace_Table;
import fightingpit.spacedrepetition.Model.RepetitionPattern_Table;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;
import fightingpit.spacedrepetition.Model.TaskDetail_Table;
import fightingpit.spacedrepetition.Model.Task_Table;

/**
 * Contains implementation of methods which read/write/update Database.
 */

public class DatabaseMethods {

    private static final String TAG = DatabaseMethods.class.getSimpleName();

    private DatabaseMethods() {
    }

    /**
     * Adds a new repetition pattern in DB.
     *
     * @param iName             Name of the new pattern
     * @param iRepetitionSpaces Arraylist of Spaced Days in pattern. (example: 0,2,9,30,90)
     * @return true if pattern was added in DB, false otherise.
     */
    public static void addRepetitionPattern(String iName, ArrayList<Integer> iRepetitionSpaces) {

        if (getRepetitionPatternFromName(iName) != null) {
            Log.d(TAG, "++++ Pattern exists in DB.");
        } else {

            Log.d(TAG, "++++ New Pattern does not exists in DB.");
            String aPatternId = CommonUtils.generateId();

            RepetitionPattern aRepetitionPattern = new RepetitionPattern(aPatternId, iName,
                    iRepetitionSpaces.size());
            aRepetitionPattern.save();

            List<RepetitionPatternSpace> aRepetitionPatternSpaceList = new ArrayList<>();
            for (int i = 1; i <= iRepetitionSpaces.size(); ++i) {
                Integer aSpaceValue = iRepetitionSpaces.get(i - 1);
                if (i > 1) {
                    aSpaceValue -= iRepetitionSpaces.get(i - 2);
                }
                aRepetitionPatternSpaceList.add(new RepetitionPatternSpace(aPatternId, i,
                        aSpaceValue));

            }
            FastStoreModelTransaction<RepetitionPatternSpace> aFastTransaction =
                    FastStoreModelTransaction
                            .insertBuilder(FlowManager
                                    .getModelAdapter
                                            (RepetitionPatternSpace.class)).addAll
                            (aRepetitionPatternSpaceList).build();
            FlowManager.getDatabase(AppDatabase.class).executeTransaction(aFastTransaction);
        }
    }

    public static void addTask(String iName, String iComment, String iPatternId) {
        // TODO: Check for error cases. Like warning for existing name.

        TaskDetail aTaskDetail = new TaskDetail();
        aTaskDetail.setId(CommonUtils.generateId());
        aTaskDetail.setName(iName);
        aTaskDetail.setComment(iComment);
        aTaskDetail.setPatternID(iPatternId);
        aTaskDetail.setCurrentRepetition(0);
        aTaskDetail.save();

        for (RepetitionPatternSpace aRepetitionPatternSpace : SQLite.select().from
                (RepetitionPatternSpace
                        .class).where(RepetitionPatternSpace_Table.Id.eq(aTaskDetail.getPatternID
                ()))
                .queryList
                        ()) {
            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                aTaskDetail.setTime(CommonUtils.getOffsetTimeInMillis(null, aRepetitionPatternSpace
                        .getSpace()));
            }
        }
        Task aTask = new Task(aTaskDetail.getId(), aTaskDetail.getName(), aTaskDetail.getTime());
        aTask.save();
    }

    public static RepetitionPattern getRepetitionPatternFromId(String iPatternId) {
        return SQLite.select().from(RepetitionPattern.class).where(RepetitionPattern_Table.Id.eq
                (iPatternId)).querySingle();
    }

    public static RepetitionPattern getRepetitionPatternFromName(String iPatternName) {
        return SQLite.select().from(RepetitionPattern.class).where(RepetitionPattern_Table.Name
                .eq(iPatternName)).querySingle();
    }

    /**
     * Get all RepetitionPatterns from DB.
     *
     * @return List of RepetitionPatterns
     */
    public static List<RepetitionPattern> getAllRepetitionPattern() {
        return SQLite.select().from(RepetitionPattern.class).queryList();
    }

    /**
     * Get All repetition spaces for patterns in DB.
     *
     * @param iPatternId get All repetition spaces for pattern ID iPatternId.
     * @return ArrayList of Repetition Spaces
     */
    public static List<RepetitionPatternSpace> getRepetitionPatternSpace(String iPatternId) {

        List<RepetitionPatternSpace> aRepetitionPatternSpaces = SQLite.select().from
                (RepetitionPatternSpace.class).where(RepetitionPatternSpace_Table.Id
                .eq(iPatternId)).queryList();

        return aRepetitionPatternSpaces;
    }

    /**
     * Get Task Details from DB.
     *
     * @param iTaskId If not null, get Task Details for iTaskId. If null, get task details for all
     *                tasks.
     * @return ArrayList of Task Details.
     */
    public static List<TaskDetail> getTaskDetails(String iTaskId) {
        List<TaskDetail> aTaskDetails;

        if (iTaskId == null) {
            aTaskDetails = SQLite.select().from(TaskDetail.class).queryList();
        } else {
            aTaskDetails = SQLite.select().from(TaskDetail.class).where(TaskDetail_Table.Id.eq
                    (iTaskId)).queryList();
        }
        return aTaskDetails;
    }

    /**
     * Get tasks schedule between provided time.
     *
     * @param iAfterTime  If not null, only show tasks after iAfterTime
     * @param iBeforeTime If not null, only show tasks before iBeforeTime
     * @return ArrayList for tasks found between given time.
     */
    public static List<Task> getScheduledTasks(String iAfterTime, String iBeforeTime) {
        List<Task> aTasks = new ArrayList<>();

        if (iAfterTime != null && iBeforeTime != null) {
            aTasks = SQLite.select().from(Task.class).where(Task_Table.Time.greaterThanOrEq
                    (iAfterTime), Task_Table.Time.lessThan(iBeforeTime)).queryList();
        } else if (iAfterTime == null && iBeforeTime == null) {
            aTasks = SQLite.select().from(Task.class).queryList();
        } else if (iAfterTime != null) {
            aTasks = SQLite.select().from(Task.class).where(Task_Table.Time.greaterThanOrEq
                    (iAfterTime)).queryList();
        } else if (iBeforeTime != null) {
            aTasks = SQLite.select().from(Task.class).where(Task_Table.Time.lessThan(iBeforeTime)
            ).queryList();
        }
        return aTasks;
    }

    /**
     * This will automatically update the record if it has already been saved and there is a
     * primary key that matches. If primary key does not matches, create the record.
     *
     * @param iTask Modified Task to be updated in DB
     */
    public static void updateTask(Task iTask) {
        iTask.save();
    }

    /**
     * This will automatically update the record details if it has already been saved and there is a
     * primary key that matches. If primary key does not matches, create the record.
     *
     * @param iTaskDetail
     */
    public static void updateTaskDetails(TaskDetail iTaskDetail) {
        iTaskDetail.save();
    }


    /**
     * Test Utility to print pattern and pattern spaces present in DB.
     */
    public static void printPatterns() {

        List<RepetitionPattern> aRepetitionPatterns = getAllRepetitionPattern();
        //Log.d(TAG, "aRepetitionPatterns Size:" + aRepetitionPatterns.size());
        for (RepetitionPattern r : aRepetitionPatterns) {
            Log.d(TAG, "Pattern:" + r.toString());
            List<RepetitionPatternSpace> aRepetitionPatternSpaces = getRepetitionPatternSpace(r
                    .getId());
            Log.d(TAG, "aRepetitionPatternSpaces Size:" + aRepetitionPatternSpaces.size());
            for (RepetitionPatternSpace rps : aRepetitionPatternSpaces) {
                Log.d(TAG, "PatternSpace:" + rps.toString());
            }
        }
    }

    /**
     * Test Utility
     */
    public static void printTaskDetails() {
        for (TaskDetail td : getTaskDetails(null)) {
            if (td.getComment() == null)
                Log.d(TAG, "Task Details:" + "Comment is null");
            Log.d(TAG, "Task Details:" + td.toString());
        }
    }

    /**
     * Test Utility
     */
    public static void printScheduledTasks() {

        List<Task> aTasks = getScheduledTasks(null, null);
        Log.d(TAG, "printScheduledTasks size :" + aTasks.size());
        for (Task td : aTasks) {
            Log.d(TAG, "Tasks:" + td.toString());
        }
    }
}