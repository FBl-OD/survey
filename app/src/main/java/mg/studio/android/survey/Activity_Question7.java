package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Activity_Question7 extends AppCompatActivity {

    private static final String ANSWER7_KEY = "answer7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_seven);
    }

    public void click7(View v) {
        Button btn = findViewById(R.id.bt_next7);
        RadioGroup rg = findViewById(R.id.rg_five_id7);
        RadioButton rb;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this,Activity_Question8.class);
        int count = rg.getChildCount();

        for (int i = 0; i < count; i++) {
            rb = (RadioButton) rg.getChildAt(i);
            if (rb.isChecked()) {
                bundle.putString(ANSWER7_KEY, rb.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            if(i==(count-1)&&!rb.isChecked()){
                Toast.makeText(Activity_Question7.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
