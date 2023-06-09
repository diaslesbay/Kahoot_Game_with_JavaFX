package Structure;
public class FillIn extends Questions {
    @Override
    public void setAnswer(String answer) {
        super.answer=answer;
    }

    @Override
    public void setDescription(String description) {
        super.description=description;
    }
    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public String getDescription() {
        return description;
    }


    public String toString(){
        return getDescription();
    }
}

