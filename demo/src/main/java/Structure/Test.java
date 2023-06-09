package Structure;
import java.util.ArrayList;

public class Test extends Questions {
    public ArrayList<String> options;

    @Override
    public void setAnswer(String answer) {
        super.answer=answer;
    }
    @Override
    public void setDescription(String description) {
        super.description=description;
    }
    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    String getAnswer() {
        return null;
    }
}
