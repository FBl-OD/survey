package mg.studio.android.survey.staticsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import mg.studio.android.survey.R;

public class Activity_Question1 extends AppCompatActivity {

    private static final String ANSWER1_KEY = "answer1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_one);

    }

    public void click1(View v) {
        Button btn = findViewById(R.id.bt_next1);
        RadioGroup rg = findViewById(R.id.rg_seven_id1);
        RadioButton rb;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this, Activity_Question2.class);
        int count = rg.getChildCount();

        for (int i = 0; i < count; i++) {
            rb = (RadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                bundle.putString(ANSWER1_KEY, rb.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            if(i==(count-1)&&!rb.isChecked()){
                Toast.makeText(Activity_Question1.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
            }
        }
    }

}