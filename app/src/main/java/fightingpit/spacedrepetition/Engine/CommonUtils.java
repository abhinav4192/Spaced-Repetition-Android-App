package fightingpit.spacedrepetition.Engine;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;


/**
 * Utility class containing common methods.
 */

public class CommonUtils {

    private static final String TAG = "++ABG55++" + CommonUtils.class.getSimpleName();

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
        // Log.d(TAG, "Old:" + aTime.toString());
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
        //Log.d(TAG, "New:" + aTime.toString());
        return aNewMillis.toString();
    }

    public static String getDayStartMillis(String iMillis) {

        Calendar aCalendar = Calendar.getInstance();
        if (iMillis != null) {
            Long aMillis = Long.parseLong(iMillis);
            aCalendar.setTimeInMillis(aMillis);
        }
        aCalendar.set(Calendar.HOUR_OF_DAY, 0);
        aCalendar.set(Calendar.MINUTE, 0);
        aCalendar.set(Calendar.SECOND, 0);
        aCalendar.set(Calendar.MILLISECOND, 0);
        Long aReturnMillis = aCalendar.getTimeInMillis();
        return aReturnMillis.toString();
    }

    /**
     * Get week start Time in Milliseconds
     *
     * @return week start Time in Milliseconds
     */
    public static String getCurrentWeekStartMillis() {

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        aCalendar.set(Calendar.MINUTE, 0);
        aCalendar.set(Calendar.SECOND, 0);
        aCalendar.set(Calendar.MILLISECOND, 0);
        aCalendar.set(Calendar.HOUR_OF_DAY, 0);
        //Log.d(TAG, "Today:" + aTime.getTime());
        aCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //Log.d(TAG, "Week Date:" +   aTime.getTime());
        Long aReturnMillis = aCalendar.getTimeInMillis();
        return aReturnMillis.toString();
    }

    /**
     * Get Week End(next week start) Time in Milliseconds
     *
     * @return week end Time in Milliseconds
     */
    public static String getCurrentWeekEndMillis() {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(Long.parseLong(getCurrentWeekStartMillis()));
        aCalendar.add(Calendar.DATE, 7);
        Long aReturnMillis = aCalendar.getTimeInMillis();
        return aReturnMillis.toString();
    }

    public static String getDateFromMillis(String iMillis) {
        Long aMillis = Long.parseLong(iMillis);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(aMillis);
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return aSimpleDateFormat.format(aCalendar.getTime());
    }

    public static String getDayFromMillis(String iMillis) {
        Long aMillis = Long.parseLong(iMillis);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTimeInMillis(aMillis);
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("EEEE");
        return aSimpleDateFormat.format(aCalendar.getTime());
    }

    /**
     * Testing method.
     */
    public static void testImplementation() {

        if (DatabaseMethods.getScheduledTasks(null, null).size() <= 0) {

            ArrayList<Integer> p = new ArrayList<>();
            p.add(-10);
            p.add(3);
            p.add(5);
            DatabaseMethods.addRepetitionPattern("Default", p);

            ArrayList<Integer> q = new ArrayList<>();
            q.add(5);
            q.add(10);
            q.add(12);
            q.add(18);
            DatabaseMethods.addRepetitionPattern("Random", q);
            //DatabaseMethods.printPatterns();

            for (Integer i = 500; i < 1000; ++i) {
                DatabaseMethods.addTask("Abhinav " + i.toString(), null, SQLite.select().from
                        (RepetitionPattern
                                .class).queryList().get(1).getId());
                //                DatabaseMethods.addTask("Abhinav 2", null, SQLite.select().from
                // (RepetitionPattern
                //                        .class).queryList().get(1).getId());
            }

            for (Integer i = 0; i < 500; ++i) {
                DatabaseMethods.addTask("Garg " + i.toString(), null, SQLite.select().from
                        (RepetitionPattern
                                .class).queryList().get(0).getId());
                //                DatabaseMethods.addTask("Cat", null, SQLite.select().from
                // (RepetitionPattern
                //                        .class).queryList().get(0).getId());
            }

            DatabaseMethods.addTask("Garg", null, SQLite.select().from(RepetitionPattern
                    .class).queryList().get(0).getId());
            DatabaseMethods.addTask("Cat", null, SQLite.select().from(RepetitionPattern
                    .class).queryList().get(0).getId());
            //DatabaseMethods.printTaskDetails();
            //DatabaseMethods.printScheduledTasks();
        }
    }
}
