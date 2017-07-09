package fightingpit.spacedrepetition.Model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.InheritedColumn;
import com.raizlabs.android.dbflow.annotation.InheritedPrimaryKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import fightingpit.spacedrepetition.Engine.Database.AppDatabase;

/**
 * Created by abhinavgarg on 07/07/17.
 */

@Table(database = AppDatabase.class,
        inheritedPrimaryKeys = {@InheritedPrimaryKey(column = @Column, primaryKey = @PrimaryKey,
                fieldName = "Id")}, inheritedColumns = {@InheritedColumn(column = @Column,
        fieldName = "Name"), @InheritedColumn(column = @Column,
        fieldName = "Time")})
public class TaskDetail extends Task {

    @Column
    private String Comment;

    @Column
    private String PatternID;

    @Column
    private Integer CurrentRepetition;

    public TaskDetail() {
    }

    public TaskDetail(String id, String name, String time) {
        super(id, name, time);
    }

    public TaskDetail(String id, String name, String time, String comment, String patternID,
                      Integer currentRepetition) {
        super(id, name, time);
        Comment = comment;
        PatternID = patternID;
        CurrentRepetition = currentRepetition;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPatternID() {
        return PatternID;
    }

    public void setPatternID(String patternID) {
        PatternID = patternID;
    }

    public Integer getCurrentRepetition() {
        return CurrentRepetition;
    }

    public void setCurrentRepetition(Integer currentRepetition) {
        CurrentRepetition = currentRepetition;
    }

    @Override
    public String toString() {
        String aReturnValue = super.toString();
        aReturnValue += " Comment:" + Comment + " PatternId:" + PatternID + " CurrRep:" +
                CurrentRepetition;
        return aReturnValue;
    }
}
