package fightingpit.spacedrepetition.Engine;

import android.util.Log;

import java.util.ArrayList;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.Model.TaskDetail;

/**
 * Utility class containing methods which interact with DB.
 * All methods which read and write from DB should be maintained in this class.
 */

public class StorageUtils {


    private static final String TAG = "++ABG++" + StorageUtils.class.getSimpleName();

    /**
     * Private Constructor to prevent initialization.
     */
    private StorageUtils() {
    }

    private static DatabaseMethods getDatabaseMethods() {
        return ((GlobalApplication) ContextManager.getCurrentActivityContext()
                .getApplicationContext())
                .getDatabaseMethods();
    }


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
        DatabaseMethods aDatabaseMethods = getDatabaseMethods();
        aReturnValue = aDatabaseMethods.addRepetitionPattern(new RepetitionPattern(aPatternId,
                iName,
                iRepetitionSpaces.size()));
        if (aReturnValue) {
            for (int i = 1; i <= iRepetitionSpaces.size(); ++i) {
                if (aReturnValue) {
                    Integer aSpaceValue = iRepetitionSpaces.get(i - 1);
                    if (i > 1) {
                        aSpaceValue -= iRepetitionSpaces.get(i - 2);
                    }
                    aReturnValue = aReturnValue && aDatabaseMethods.addRepetitionPatternSpace(new
                            RepetitionPatternSpace
                            (aPatternId, i, aSpaceValue));
                } else {
                    // TODO: Handle error
                }
            }
        } else {
            // TODO: Handle error
        }
        return aReturnValue;
    }

    public static boolean addTask(String iName, String iComment, String iPatternId) {
        boolean aReturnValue = true;
        // TODO: Check for error cases. Like warning for existing name.
        DatabaseMethods aDatabaseMethods = getDatabaseMethods();

        TaskDetail aTaskDetail = new TaskDetail();
        aTaskDetail.setId(CommonUtils.generateId());
        aTaskDetail.setName(iName);
        aTaskDetail.setComment(iComment);
        aTaskDetail.setPatternID(iPatternId);
        aTaskDetail.setCurrentRepetition(0);
        aDatabaseMethods.addTaskDetail(aTaskDetail);
        for (RepetitionPatternSpace aRepetitionPatternSpace : aDatabaseMethods
                .getRepetitionPatternSpace(iPatternId)) {
            if (aRepetitionPatternSpace.getRepetitionNumber() == 1) {
                aTaskDetail.setTime(CommonUtils.getOffsetTimeInMillis(null, aRepetitionPatternSpace
                        .getSpace()));
            }
        }
        aDatabaseMethods.addScheduledTask(aTaskDetail);
        return aReturnValue;

    }

    /**
     * Test Utility to print pattern and pattern spaces present in DB.
     */
    public static void printPatterns() {
        DatabaseMethods databaseMethods = getDatabaseMethods();
        for (RepetitionPattern r : databaseMethods.getAllRepetitionPattern()) {
            Log.d(TAG, "Pattern:" + r.toString());
            for (RepetitionPatternSpace rps : databaseMethods.getRepetitionPatternSpace(r.getId()
            )) {
                Log.d(TAG, "PatternSpace:" + rps.toString());
            }
        }
    }

    /**
     * Test Utility
     */
    public static void printTasks() {
        DatabaseMethods databaseMethods = getDatabaseMethods();
        for (TaskDetail td : databaseMethods.getTasks(null)) {
            if (td.getComment() == null)
                Log.d(TAG, "Task Details:" + "Comment is null");
            Log.d(TAG, "Task Details:" + td.toString());
        }
    }

    /**
     * Test Utility
     */
    public static void printScheduledTasks() {
        DatabaseMethods databaseMethods = getDatabaseMethods();
        for (Task td : databaseMethods.getScheduledTasks(CommonUtils.getOffsetTimeInMillis(0, null),
                CommonUtils.getOffsetTimeInMillis(0, 2))) {

            Log.d(TAG, "Tasks:" + td.toString());
        }
    }
}
