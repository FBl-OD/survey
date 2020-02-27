package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Activity_Question4 extends AppCompatActivity {

    private static final String ANSWER4_KEY = "answer4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_four);
    }

    public void click4(View v){
        StringBuffer sb=new StringBuffer("");
        LinearLayout linear=(LinearLayout)findViewById(R.id.linear_seven_id4);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this,Activity_Question5.class);
        int count=linear.getChildCount();

        for (int i = 0; i < count; i++) {
            CheckBox cb = (CheckBox) linear.getChildAt(i);
            if (cb.isChecked()) {
               sb.append(cb.getText().toString()+"\n");
            }
        }

        if(sb.length()==0){
            Toast.makeText(Activity_Question4.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
        }else {
            bundle.putString(ANSWER4_KEY, sb.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        }

}
