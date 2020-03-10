package mg.studio.android.survey.staticsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import mg.studio.android.survey.R;

public class Activity_Question5 extends AppCompatActivity {

    private static final String ANSWER5_KEY = "answer5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_five);
    }

    public void click5(View v){
        StringBuffer sb=new StringBuffer();
        LinearLayout linear=(LinearLayout)findViewById(R.id.linear_seven_id5);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this, Activity_Question6.class);
        int count=linear.getChildCount();

        for (int i = 0; i < count; i++) {
            CheckBox cb = (CheckBox) linear.getChildAt(i);
            if (cb.isChecked()) {
                sb.append(cb.getText().toString()+"\n");
            }
        }
        if(sb.length()==0){
            Toast.makeText(Activity_Question5.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
        }else {
            bundle.putString(ANSWER5_KEY, sb.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
