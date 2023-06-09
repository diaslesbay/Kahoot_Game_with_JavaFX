package Structure;


public abstract class Questions {
    protected String description;
    protected String answer;

    abstract void setAnswer(String answer);
    abstract void setDescription(String description);
    abstract String getDescription();
    abstract String getAnswer();
}