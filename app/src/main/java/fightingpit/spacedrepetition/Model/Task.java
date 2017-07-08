package fightingpit.spacedrepetition.Model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import fightingpit.spacedrepetition.Engine.Database.AppDatabase;

/**
 * Created by abhinavgarg on 07/07/17.
 */
@Table(database = AppDatabase.class)
public class Task extends BaseModel{
    @PrimaryKey
    private String Id;

    @Column
    private String Name;

    @Column
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
