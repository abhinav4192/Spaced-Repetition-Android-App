package fightingpit.spacedrepetition.Model;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public class Task {
    private String Id;
    private String Name;
    private Integer Time;

    public Task() {
    }

    public Task(String id, String name, Integer time) {
        Id = id;
        Name = name;
        Time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getTime() {
        return Time;
    }

    public void setTime(Integer time) {
        Time = time;
    }
}
