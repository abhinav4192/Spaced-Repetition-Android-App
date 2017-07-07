package fightingpit.spacedrepetition.Engine;

import android.util.Log;

import java.util.ArrayList;

import fightingpit.spacedrepetition.Engine.Database.DatabaseMethods;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.Model.RepetitionPatternSpace;

/**
 * Utility class containing methods which interact with DB.
 * All methods which read and write from DB should be maintained in this class.
 */

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    /**
     * Adds a new repetition pattern in DB.
     * @param iName Name of the new pattern
     * @param iRepetitionSpaces Arraylist of Spaced Days in pattern. (example: 0,2,9,30,90)
     * @return true if pattern was added in DB, false otherise.
     */
    public static boolean addRepetitionPattern(String iName, ArrayList<Integer> iRepetitionSpaces){

        boolean aReturnValue = true;
        // TODO: Check for name in existing patterns
        String patternId = CommonUtils.generateId();
        DatabaseMethods databaseMethods = ((GlobalApplication) ContextManager.getCurrentActivityContext().getApplicationContext())
                .getDatabaseMethods();
        aReturnValue = databaseMethods.addRepetitionPattern(new RepetitionPattern(patternId,iName,
                iRepetitionSpaces.size()));
        if(aReturnValue) {
            for (int i = 0; i < iRepetitionSpaces.size(); ++i) {
                if(aReturnValue) {
                    aReturnValue = aReturnValue && databaseMethods.addRepetitionPatternSpace(new
                            RepetitionPatternSpace
                            (patternId, i,
                                    iRepetitionSpaces.get(i)));
                }else{
                    // TODO: Handle error
                }
            }
        }
        else{
            // TODO: Handle error
        }
        return aReturnValue;
    }

    /**
     * Test Utility to print pattern and pattern spaces present in DB.
     */
    public static void printPatterns()
    {
        DatabaseMethods databaseMethods = ((GlobalApplication) ContextManager.getCurrentActivityContext().getApplicationContext())
                .getDatabaseMethods();
        for(RepetitionPattern r: databaseMethods.getAllRepetitionPattern())
        {
            Log.d(TAG,"Pattern:" + r.toString());
            for(RepetitionPatternSpace rps: databaseMethods.getRepetitionPatternSpace(r.getId())){
                Log.d(TAG,"PatternSpace:" + rps.toString());
            }
        }
    }
}
