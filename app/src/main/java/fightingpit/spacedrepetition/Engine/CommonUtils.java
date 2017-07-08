package fightingpit.spacedrepetition.Engine;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;

/**
 * Utility class containing common methods.
 */

public class CommonUtils {

    private static final String TAG = "++ABG++" + CommonUtils.class.getSimpleName();

    /**
     * Private Constructor to prevent initialization.
     */
    private CommonUtils() {
    }

    /**
     * Uses current time in milliseconds to generate ID.
     *
     * @return as string current time in milliseconds
     */
    public static String generateId() {
        Long time = System.currentTimeMillis();
        return time.toString();
    }

    /**
     * Convert integer to its ordinal (example: 2 to 2nd)
     *
     * @param i integer to be converted
     * @return ordinal value
     */
    public static String getOrdinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }

    /**
     * Method to get Time in milliseconds by modifying current time in default Timezone.
     * Get current time and set Minutes,Seconds,Milliseconds to zero. This modified time is
     * further modified by input parameters.
     *
     * @param iSetHours If not null, sets Hour_of_day to iSetHours. If null, set Hour_of_day to
     *                  zero.
     * @param iDays     If not null, add iDays to time. (Make iDays negative to subtract days)
     * @return Time in milliseconds as a string
     */
    public static String getOffsetTimeInMillis(Integer iSetHours, Integer iDays) {
        Calendar aTime = Calendar.getInstance();
        Log.d(TAG, "Old:" + aTime.toString());
        aTime.set(Calendar.MINUTE, 0);
        aTime.set(Calendar.SECOND, 0);
        aTime.set(Calendar.MILLISECOND, 0);
        if (iSetHours != null) {
            aTime.set(Calendar.HOUR_OF_DAY, iSetHours);
        } else {
            aTime.set(Calendar.HOUR_OF_DAY, 0);
        }
        if (iDays != null) {
            aTime.add(Calendar.DATE, iDays);
        }
        Long aNewMillis = aTime.getTimeInMillis();
        Log.d(TAG, "New:" + aTime.toString());
        return aNewMillis.toString();
    }

    /**
     * Testing method.
     */
    public static void testImplementation() {
                ArrayList<Integer> p = new ArrayList<>();
                p.add(1);
                p.add(3);
                p.add(5);
                StorageUtils.addRepetitionPattern("Default", p);

                ArrayList<Integer> q = new ArrayList<>();
                q.add(5);
                q.add(10);
                q.add(12);
                q.add(18);
                StorageUtils.addRepetitionPattern("Random", q);
        StorageUtils.printPatterns();

                DatabaseMethods dbm = ((GlobalApplication) ContextManager
         .getCurrentActivityContext()
                        .getApplicationContext())
                        .getDatabaseMethods();
                StorageUtils.addTask("Task 2", null, dbm.getAllRepetitionPattern().get(0).getId());
        StorageUtils.printTasks();
        StorageUtils.printScheduledTasks();
    }
}
