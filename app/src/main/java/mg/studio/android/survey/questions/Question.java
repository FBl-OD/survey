package mg.studio.android.survey.questions;

import java.util.ArrayList;

public class Question {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;


    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOption(ArrayList<Option> options) {
        this.options = options;
    }

    private ArrayList<Option> options;

    @Override
    public String toString() {
        return "Question{" +
                "type='" + type + '\'' +
                ", question='" + question + '\'' +
                ", option=" + options +
                '}';
    }
}
