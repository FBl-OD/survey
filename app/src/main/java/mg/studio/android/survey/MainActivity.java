package mg.studio.android.survey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import mg.studio.android.survey.questions.GetJsonData;
import mg.studio.android.survey.questions.Survey;
import mg.studio.android.survey.util.DensityUtil;

public class MainActivity extends AppCompatActivity {
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void welClick(View v) {
        CheckBox cb = findViewById(R.id.cb_accept);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(MainActivity.this,Activity_Question1.class);
        intent.putExtras(bundle);
        if (cb.isChecked()) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "请同意上述条款", Toast.LENGTH_SHORT).show();
        }
    }
*/

    int current = 0;
    Bundle bundle = new Bundle();
    Intent intent =new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取本次survey对象从assets文件夹中
        final Survey survey = new GetJsonData().parseJSONSurvey(this);
        setTitle(String.valueOf("survey ID: " + survey.getId().toString() + " questions: " + survey.getLen()));
        //创建一个最外层的根布局
        final LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setBackgroundColor(Color.WHITE);
        //设置线性布局中子View的摆放为竖直方向
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        //创建一个根部局的LayoutParams
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置根部局的Margin值
        rootParams.setMargins(DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 5));
        rootParams.gravity = Gravity.CENTER_HORIZONTAL;
        rootLayout.setLayoutParams(rootParams);
        //设置最外层根部局布局的背景颜色
        rootLayout.setBackgroundColor(Color.WHITE);
        //将当前创建的根部局设置给Activity
        setContentView(rootLayout);

        //创建文件
        int permission_write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_write != PackageManager.PERMISSION_GRANTED || permission_read != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "正在请求权限", Toast.LENGTH_SHORT).show();
            //申请权限，特征码自定义为1，可在回调时进行相关判断
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Toast.makeText(this, getFilesDir().toString(), Toast.LENGTH_SHORT).show();
        File file = new File(getFilesDir() + "/report.json");
        if(file.exists()) {
            try {
                boolean l=file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        //动态界面产生和每个界面的确定按钮操作
        final int len = survey.getLen();
        final Button btn[] = new Button[len];
        final RadioGroup rg = new RadioGroup(this);
        intent.putExtras(bundle);
        for (int k = 0; k < len; k++) {
            btn[k]=new Button(this);
        }
        btn[current]=createLayout(rootLayout, survey, current, rg,btn[current]);
        for (int k = 0; k < len; k++) {
            btn[k].setTag(k);
            System.out.println("k= "+k);
            btn[k].setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    System.out.println("i= "+i);
                    if (survey.getQuestions().get(i).getType().equals("single")) {
                        if (i < len-1) {
                            RadioButton rb;
                            int count = rg.getChildCount();
                            for (int j = 0; j < count; j++) {
                                rb = (RadioButton) rg.getChildAt(j);
                                if (rb.isChecked()) {
                                    bundle.putString("answer"+String.valueOf(i+1), rb.getText().toString());
                                    intent.putExtras(bundle);
                                    current+=1;
                                    rootLayout.removeAllViewsInLayout();
                                    rg.removeAllViews();
                                    btn[current]=createLayout(rootLayout, survey, current, rg,btn[current]);
                                    break;
                                }
                                if (j == (count - 1) && !rb.isChecked()) {
                                    Toast.makeText(MainActivity.this, "请填写该问题以进行下一步", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            int m = (Integer) v.getTag();
                            RadioButton rb;
                            int count = rg.getChildCount();
                            for (int j = 0; j < count; j++) {
                                rb = (RadioButton) rg.getChildAt(j);
                                if (rb.isChecked()) {
                                    bundle=intent.getExtras();
                                    bundle.putString("answer" + String.valueOf(m + 1), rb.getText().toString());
                                    intent.putExtras(bundle);
                                    break;
                                }
                                if (j == (count - 1) && !rb.isChecked()) {
                                    Toast.makeText(MainActivity.this, "请填写该问题以完成问卷", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Toast.makeText(MainActivity.this, "已成功保存数据", Toast.LENGTH_SHORT).show();

                            JSONObject root = new JSONObject();//实例一个JSONObject对象
                            StringBuilder s1 = new StringBuilder();
                            try {
                                InputStream instream = new FileInputStream(new File(getFilesDir() + "/result.json"));
                                if (instream.toString().length()!=0) {
                                    InputStreamReader inputreader = new InputStreamReader(instream);
                                    BufferedReader buffreader = new BufferedReader(inputreader);
                                    String line;
                                    while ((line = buffreader.readLine()) != null) {
                                        s1.append(line);
                                    }
                                    root = new JSONObject(s1.toString());
                                    instream.close();
                                }
                            } catch (java.io.FileNotFoundException e) {

                            } catch (IOException e) {

                            }catch (JSONException e) {
                            }


                            //Json数据的创建和写入
                            try {
                                JSONObject second = new JSONObject();
                                second.put("id",survey.getId());
                                JSONArray arr=new JSONArray();//实例一个JSONArray对象
                                bundle=intent.getExtras();
                                for (int n = 0; n < survey.getQuestions().size(); n++) {
                                    JSONObject ans = new JSONObject();//实例一个ans的JSON对象
                                    ans.put("question", survey.getQuestions().get(n).getQuestion());
                                    ans.put("answer",bundle.getString("answer"+String.valueOf(n+1)));
                                    arr.put(ans);
                                }
                                intent.putExtras(bundle);
                                second.put("report",arr);
                                root.put("result",second);
                                FileOutputStream out=null;
                                File file=new File(getFilesDir() + "/result.json");
                                try {
                                    out = new FileOutputStream(file);
                                    out.write(root.toString().getBytes());
                                    out.flush();
                                    out.close();
                                }catch (FileNotFoundException e){
                                    e.printStackTrace();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, "已成功保存数据", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "数据保存至"+file.getPath() + "/result.json", Toast.LENGTH_SHORT).show();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(getFilesDir() + "/report.json");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
        }
    }

    public Button createLayout(LinearLayout rootLayout, Survey survey, int i, RadioGroup rg,Button btn) {
        TextView tv_number = new TextView(this);
        tv_number.setText(String.valueOf("QUESTION" + String.valueOf(i + 1)));
        tv_number.setTextSize(25);
        //创建问题号
        rootLayout.addView(tv_number);
        //当通过子View进行获取所在的布局的时候，rootLayout.addView(ivIco)需要写在前面，原因想起来也很清楚,当前的ImageView都还没有添加到父View如果直接通过getLayoutParams()当然会出先空指针异常(可以查看源码)
        LinearLayout.LayoutParams tvParams = (LinearLayout.LayoutParams) tv_number.getLayoutParams();
        tvParams.width = DensityUtil.dp2px(this, 200f);
        tvParams.height = DensityUtil.dp2px(this, 30f);
        tvParams.gravity = Gravity.TOP;
        tvParams.setMargins(0, DensityUtil.dp2px(this, 0), 0, 0);
        tv_number.setLayoutParams(tvParams);

        TextView tv_question = new TextView(this);
        tv_question.setText(survey.getQuestions().get(i).getQuestion());
        tv_question.setTextSize(20);
        //将创建问题
        rootLayout.addView(tv_question);
        //当通过子View进行获取所在的布局的时候，rootLayout.addView(ivIco)需要写在前面，原因想起来也很清楚,当前的ImageView都还没有添加到父View如果直接通过getLayoutParams()当然会出先空指针异常(可以查看源码)
        LinearLayout.LayoutParams tvParams1 = (LinearLayout.LayoutParams) tv_question.getLayoutParams();
        tvParams1.width = DensityUtil.dp2px(this, 400f);
        tvParams1.height = DensityUtil.dp2px(this, 60f);
        tvParams1.setMargins(50, DensityUtil.dp2px(this, 30), 0, 0);
        tv_question.setLayoutParams(tvParams1);

        //问题类型判断
        if (survey.getQuestions().get(i).getType().equals("single")) {
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            //改变rg的起始位置
            lp.leftMargin = 80;
            for (int j = 0; j < survey.getQuestions().get(i).getOptions().size(); j++) {
                RadioButton rb = new RadioButton(this);
                rb.setText(String.valueOf(survey.getQuestions().get(i).getOptions().get(j).getNumber()));
                rb.setTextSize(15);
                rg.addView(rb);
            }
            rg.setLayoutParams(lp);
            rootLayout.addView(rg);


        } else if (survey.getQuestions().get(i).getType().equals("multi")) {
            //其他情况

        }

        //完成或者下一题按钮
        if (i == survey.getLen() - 1) {
            btn.setText("FINISH");
        } else {
            btn.setText("NEXT");
        }

        btn.setTextSize(25);
        rootLayout.addView(btn);
        LinearLayout.LayoutParams btnParams1 = (LinearLayout.LayoutParams) btn.getLayoutParams();
        //rootLayout.getWidth();
        //btnParams1.bottomMargin=0;
        //btnParams1.setMargins(0,);
        return btn;
    }
}
