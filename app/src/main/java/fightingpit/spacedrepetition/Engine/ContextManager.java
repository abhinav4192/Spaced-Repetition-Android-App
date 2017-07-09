package fightingpit.spacedrepetition.Engine;

import android.content.Context;

/**
 * Provides easy access to Current Context
 * All activities should update this context on creation.
 */
public final class ContextManager {

    static Context sCurrentActivityContext = null;

    public static Context getCurrentActivityContext() {
        return sCurrentActivityContext;
    }

    public static void setCurrentActivityContext(Context currentActivityContext) {
        sCurrentActivityContext = currentActivityContext;
    }
}