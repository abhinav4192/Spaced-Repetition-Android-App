package fightingpit.spacedrepetition.Engine;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

//import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace_Table;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;

/**
 * Utility class containing methods which interact with DB.
 * All methods which read and write from DB should be maintained in this class.
 */

public class StorageUtils {


    private static final String TAG = "++GARG1++" + StorageUtils.class.getSimpleName();

    /**
     * Private Constructor to prevent initialization.
     */
    private StorageUtils() {
    }

    //    private static DatabaseMethods getDatabaseMethods() {
    //        return ((GlobalApplication) ContextManager.getCurrentActivityContext()
    //                .getApplicationContext())
    //                .getDatabaseMethods();
    //    }


    /**
     * Adds a new repetition pattern in DB.
     *
     * @param iName             Name of the new pattern
     * @param iRepetitionSpaces Arraylist of Spaced Days in pattern. (example: 0,2,9,30,90)
     * @return true if pattern was added in DB, false otherise.
     */
    public static boolean addRepetitionPattern(String iName, ArrayList<Integer> iRepetitionSpaces) {

        boolean aReturnValue = true;
        // TODO: Check for name in existing patterns
        String aPatternId = CommonUtils.generateId();
        //DatabaseMethods aDatabaseMethods = getDatabaseMethods();

        RepetitionPattern aRepetitionPattern = new RepetitionPattern(aPatternId, iName,
                iRepetitionSpaces.size());
        aRepetitionPattern.save();
        for (int i = 1; i <= iRepetitionSpaces.size(); ++i) {
            Integer aSpaceValue = iRepetitionSpaces.get(i - 1);
            if (i > 1) {
                aSpaceValue -= iRepetitionSpaces.get(i - 2);
            }
            RepetitionPatternSpace aRepetitionPatternSpace = new RepetitionPatternSpace
                    (aPatternId, i, aSpaceValue);
            aRepetitionPatternSpace.save();
        }

        return aReturnValue;
    }

    public static boolean addTask(String iName, String iComment, String iPatternId) {
            boolean aReturnValue = true;
            // TODO: Check for error cases. Like warning for existing name.

            TaskDetail aTaskDetail = new TaskDetail();
            aTaskDetail.setId(CommonUtils.generateId());
            aTaskDetail.setName(iName);
            aTaskDetail.setComment(iComment);
            aTaskDetail.setPatternID(iPatternId);
            aTaskDetail.setCurrentRepetition(0);
            aTaskDetail.save();

            for (RepetitionPatternSpace aRepetitionPatternSpace : SQLite.select().from(RepetitionPatternSpace
                    .class).where(RepetitionPatternSpace_Table.Id.eq(aTaskDetail.getPatternID())).queryList
                    ()) {
                if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                    aTaskDetail.setTime(CommonUtils.getOffsetTimeInMillis(null, aRepetitionPatternSpace
                            .getSpace()));g
                }
            }
            Task aTask = new Task(aTaskDetail.getId(),aTaskDetail.getName(),aTaskDetail.getTime());
            aTask.save();
            return aReturnValue;

        }

    /**
     * Test Utility to print pattern and pattern spaces present in DB.
     */
    public static void printPatterns() {

        List<RepetitionPattern> aRepetitionPatterns = SQLite.select().from(RepetitionPattern
                .class).queryList();
        Log.d(TAG, "aRepetitionPatterns Size:" + aRepetitionPatterns.size());
        for (RepetitionPattern r : aRepetitionPatterns) {
            Log.d(TAG, "Pattern:" + r.toString());
            List<RepetitionPatternSpace> aRepetitionPatternSpaces = SQLite.select().from(RepetitionPatternSpace
                    .class).where(RepetitionPatternSpace_Table.Id.eq(r.getId())).queryList();
            Log.d(TAG, "aRepetitionPatternSpaces Size:" + aRepetitionPatternSpaces.size());
            for (RepetitionPatternSpace rps : aRepetitionPatternSpaces) {
                Log.d(TAG, "PatternSpace:" + rps.toString());
            }
        }
    }

    /**
     * Test Utility
     */
    public static void printTasks() {
        for (TaskDetail td : SQLite.select().from(TaskDetail
                .class).queryList()) {
            if (td.getComment() == null)
                Log.d(TAG, "Task Details:" + "Comment is null");
            Log.d(TAG, "Task Details:" + td.toString());
        }
    }

    /**
     * Test Utility
     */
    public static void printScheduledTasks() {

        List<Task> aTasks = SQLite.select().from(Task
                .class).queryList();
        Log.d(TAG, "printScheduledTasks size :" + aTasks.size());
        for (Task td : aTasks) {
            Log.d(TAG, "Tasks:" + td.toString());
        }
    }
}
