package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Activity_Question12 extends AppCompatActivity {

    private static final String ANSWER12_KEY = "answer12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_twelve);
    }

    public void click12(View v) {
        Button btn = findViewById(R.id.bt_next12);
        RadioGroup rg = findViewById(R.id.rg_five_id12);
        RadioButton rb;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this, Activity_Report.class);
        int count = rg.getChildCount();

        for (int i = 0; i < count; i++) {
            rb = (RadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                bundle.putString(ANSWER12_KEY, rb.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            if(i==(count-1)&&!rb.isChecked()){
                Toast.makeText(Activity_Question12.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
