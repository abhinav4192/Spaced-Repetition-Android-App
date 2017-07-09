package fightingpit.spacedrepetition.Model.comparators;

import java.util.Comparator;

import fightingpit.spacedrepetition.Model.Task;

/**
 * Created by abhinavgarg on 09/07/17.
 */
public class SortTaskByTime implements Comparator<Task> {
    @Override
    public int compare(Task task, Task t1) {
        int aResult = task.getTime().compareTo(t1.getTime());
        if (aResult == 0) {
            aResult = task.getName().compareTo(t1.getName());
        }
        return aResult;
    }
}
