package fightingpit.spacedrepetition.Model;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public class TaskDetail extends Task {
    private String Comment;
    private String PatternID;
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
