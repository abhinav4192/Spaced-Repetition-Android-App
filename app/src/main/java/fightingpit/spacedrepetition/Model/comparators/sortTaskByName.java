package fightingpit.spacedrepetition.Model.comparators;

import java.util.Comparator;

import fightingpit.spacedrepetition.Model.Task;

/**
 * Created by abhinavgarg on 09/07/17.
 */
public class SortTaskByName implements Comparator<Task> {
    @Override
    public int compare(Task task, Task t1) {
        return task.getName().compareTo(t1.getName());
    }
}
