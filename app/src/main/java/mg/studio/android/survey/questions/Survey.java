package mg.studio.android.survey.questions;

import java.util.ArrayList;

public class Survey {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    private int len;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> question) {
        this.questions = question;
    }

    private ArrayList<Question> questions;

    @Override
    public String toString() {
        return "Survey{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", len=" + len +
                ", question=" + questions +
                '}';
    }
}
