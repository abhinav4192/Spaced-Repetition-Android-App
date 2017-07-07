package fightingpit.spacedrepetition.Engine;

/**
 * Utility class containing common methods.
 */

public class CommonUtils {
    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * Uses current time in milliseconds to generate ID.
     * @return as string current time in milliseconds
     */
    public static String generateId()
    {
        Long time = System.currentTimeMillis();
        return time.toString();
    }

    /**
     * Convert integer to its ordinal (example: 2 to 2nd)
     * @param i integer to be converted
     * @return ordinal value
     */
    public static String getOrdinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
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
     * Testing method.
     */
    public static void testImplementation(){
        //        ArrayList<Integer> p = new ArrayList<>();
        //        p.add(1);
        //        p.add(3);
        //        p.add(5);
        //        StorageUtils.addRepetitionPattern("Default",p);
        //
        //        ArrayList<Integer> q = new ArrayList<>();
        //        q.add(5);
        //        q.add(10);
        //        q.add(12);
        //        q.add(18);
        //        StorageUtils.addRepetitionPattern("Random",q);
        //
        //        StorageUtils.printPatterns();
    }
}
