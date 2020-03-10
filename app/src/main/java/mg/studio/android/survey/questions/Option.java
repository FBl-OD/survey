package mg.studio.android.survey.questions;

public class Option {
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;

    @Override
    public String toString() {
        return "Option{" +
                "number='" + number + '\'' +
                '}';
    }
}
