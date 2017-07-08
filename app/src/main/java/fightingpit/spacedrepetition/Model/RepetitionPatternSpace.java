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
public class RepetitionPatternSpace extends BaseModel{

    @PrimaryKey
    private String Id;

    @PrimaryKey
    private Integer RepetitionNumber;

    @Column
    private Integer Space;

    public RepetitionPatternSpace() {

    }

    public RepetitionPatternSpace(String id, Integer repetitionNumber, Integer space) {
        Id = id;
        RepetitionNumber = repetitionNumber;
        Space = space;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Integer getRepetitionNumber() {
        return RepetitionNumber;
    }

    public void setRepetitionNumber(Integer repetitionNumber) {
        RepetitionNumber = repetitionNumber;
    }

    public Integer getSpace() {
        return Space;
    }

    public void setSpace(Integer space) {
        Space = space;
    }

    @Override
    public String toString() {
        String aReturnString = "";
        aReturnString += "Id:" + Id + " RepetitionNumber:" + RepetitionNumber + " Space:" + Space;
        return  aReturnString;
    }
}
