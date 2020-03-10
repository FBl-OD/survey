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

public class Activity_Question11 extends AppCompatActivity {

    private static final String ANSWER11_KEY = "answer11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_eleven);
    }

    public void click11(View v) {
        Button btn = findViewById(R.id.bt_next11);
        RadioGroup rg = findViewById(R.id.rg_two_id11);
        RadioButton rb;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this, Activity_Question12.class);
        int count = rg.getChildCount();

        for (int i = 0; i < count; i++) {
            rb = (RadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                bundle.putString(ANSWER11_KEY, rb.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            if(i==(count-1)&&!rb.isChecked()){
                Toast.makeText(Activity_Question11.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
