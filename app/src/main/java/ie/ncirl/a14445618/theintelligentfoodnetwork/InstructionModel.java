package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 24/02/2018.
 */

public class InstructionModel {
    private String stepNumber;
    private String instruction;

    public InstructionModel() {
    }

    public InstructionModel(String stepNumber, String instruction) {
        this.stepNumber = stepNumber;
        this.instruction = instruction;
    }

    public String getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(String stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
