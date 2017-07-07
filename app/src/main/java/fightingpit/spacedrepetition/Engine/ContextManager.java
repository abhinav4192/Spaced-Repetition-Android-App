package fightingpit.spacedrepetition.Engine;

import android.content.Context;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public final class ContextManager {

    static Context sCurrentActivityContext = null;

    public static void setCurrentActivityContext(Context currentActivityContext) {
        sCurrentActivityContext = currentActivityContext;
    }

    public static Context getCurrentActivityContext() {
        return sCurrentActivityContext;
    }
}