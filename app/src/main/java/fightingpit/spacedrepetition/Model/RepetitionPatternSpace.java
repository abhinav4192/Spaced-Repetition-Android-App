package fightingpit.spacedrepetition.Model;

/**
 * Created by abhinavgarg on 07/07/17.
 */
public class RepetitionPatternSpace {

    private String Id;
    private Integer RepetitionNumber;
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
}
