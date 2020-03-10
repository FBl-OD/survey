package mg.studio.android.survey.staticsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import mg.studio.android.survey.R;

public class Activity_Question6 extends AppCompatActivity {

    private static final String ANSWER6_KEY = "answer6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_six);
    }

    public void click6(View v) {
        EditText et=findViewById(R.id.et_input6_1);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intent.setClass(this, Activity_Question7.class);
        if(et.getText().length()==0){
            Toast.makeText(Activity_Question6.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
        }else{
            bundle.putString(ANSWER6_KEY,et.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        EditText et=findViewById(R.id.et_input6_1);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) { //需要隐藏软键盘

                InputMethodManager imm = (InputMethodManager)     getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                et.setCursorVisible(false);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


     //判断当前点击的位置是否为EditText

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) { //如果点击的view是EditText
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
