package mg.studio.android.survey.staticsurvey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import mg.studio.android.survey.R;
import mg.studio.android.survey.staticsurvey.Activity_Finish;

public class Activity_Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        showData();

        int permission_write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_write != PackageManager.PERMISSION_GRANTED || permission_read != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "正在请求权限", Toast.LENGTH_SHORT).show();
            //申请权限，特征码自定义为1，可在回调时进行相关判断
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Toast.makeText(this, getFilesDir().toString(), Toast.LENGTH_SHORT).show();
        File file = new File(getFilesDir() + "/report.json");
        File file1 = new File(getExternalFilesDir(null)+"/report.json");
        if(file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {

            }
        }
        if(file1.exists()){
            try{
                file.createNewFile();
            }catch(Exception e){

            }
        }


        StringBuilder s1 = new StringBuilder();
        int count;
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while ((line = buffreader.readLine()) != null) {
                    s1.append(line);
                }
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {

        } catch (IOException e) {

        }

        Toast.makeText(getApplicationContext(), file1.toString(), Toast.LENGTH_SHORT).show();
        count = search(s1.toString(), "report");
        String report = "report";
        String reportNow = report + String.valueOf(count);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String s2 = "answer";

        try {
            JSONObject root = new JSONObject();//实例一个JSONObject对象
            JSONArray re = new JSONArray();//实例一个JSON数组

            for (int i = 0; i < 12; i++) {
                String ans = s2 + String.valueOf(i + 1);
                JSONObject ansList = new JSONObject();//实例一个ans1的JSON对象
                ansList.put(ans, bundle.getString(ans));
                re.put(i, ansList);
            }
            intent.putExtras(bundle);
            root.put(reportNow, re);

            //In line 117-138 below, I wrote the investigation data to the file,
            // and the file file1(create by getExternalFilesDir()) is the same as the file(create by getFilesDir()) operation,
            // so I only wrote the file(create by getFilesDir()) instead of file1(create by getExternalFilesDir())
            RandomAccessFile randomFile = null;
            try {
                randomFile = new RandomAccessFile(file, "rw");
                long fileLength = randomFile.length();
                if (fileLength != 0) {
                    randomFile.seek(fileLength - 1);
                    randomFile.writeBytes(",");
                    randomFile.writeBytes(root.toString().substring(1));
                } else {
                    randomFile.seek(fileLength);
                    randomFile.writeBytes(root.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Toast.makeText(getApplicationContext(), "数据已自动存储！", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int search(String str, String strRes) {//查找字符串里与指定字符串相同的个数
        int n = 0;
        while (str.indexOf(strRes) != -1) {
            int i = str.indexOf(strRes);
            n++;
            str = str.substring(i + 1);
        }
        return n;
    }


    private void showData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView[] tv = new TextView[12];
        findViewsBySameIds(tv, "tv_report_A");
        tv[0].setText(bundle.getString("answer1"));
        String ans = "answer";
        for (int i = 0; i < 12; i++) {
            int s = i + 1;
            String index = ans + String.valueOf(s);
            tv[i].setText(bundle.getString(index));
        }
    }

    public void click_report(View v) {
        Intent intent = getIntent();
        intent.setClass(this, Activity_Finish.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected <T> void findViewsBySameIds(T[] views, String samePhase) {
        for (int i = 0; i < views.length; i++) {
            String resid = samePhase + String.valueOf(i + 1);
            int resID = getResources().getIdentifier(resid, "id", getPackageName());
            views[i] = (T) findViewById(resID);
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

}
