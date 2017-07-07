package fightingpit.spacedrepetition.Model;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public class Task {
    private String Id;
    private String Name;
    private String Time;

    public Task() {
    }

    public Task(String id, String name, String time) {
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        String aReturnValue = "";
        aReturnValue += "Id:" + Id + " Name:" + Name + " Time:" + Time;
        return aReturnValue;
    }
}
