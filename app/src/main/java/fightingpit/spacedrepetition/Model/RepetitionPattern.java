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
public class RepetitionPattern extends BaseModel{

    @PrimaryKey
    private String Id;

    @Column
    private String Name;

    @Column
    private Integer Repetitions;

    public RepetitionPattern() {
    }


    public RepetitionPattern(String id, String name, Integer repetitions) {
        Id = id;
        Name = name;
        Repetitions = repetitions;
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

    public Integer getRepetitions() {
        return Repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        Repetitions = repetitions;
    }

    @Override
    public String toString() {
        String aReturnString = "";
        aReturnString += "Id:" + Id + " Name:" + Name + " Rep:" + Repetitions;
        return aReturnString;
    }
}
