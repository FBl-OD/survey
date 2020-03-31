package com.example.lab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.OutputStream;

public class HomeActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    String report;
    LinearLayout layout;
    int j = 0, k = 0;
    Bundle bundle=new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        MainActivity.activityList.add(this);
        layout = findViewById(R.id.mainLayout);
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this, "parse fail", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                String str = parseQRcode(bitmap);
                AnalysisJson(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String parseQRcode(Bitmap bmp) {

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        QRCodeReader reader = new QRCodeReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);//优化精度
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");//解码设置编码方式为：utf-8
        try {
            Result result = reader.decode(new BinaryBitmap(
                    new HybridBinarizer(new RGBLuminanceSource(width, height, pixels))), hints);
            return result.getText();
        } catch (NotFoundException e) {
            Log.i("ansen", "" + e.toString());
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void AnalysisJson(String text) {
        try {
            JSONObject object = new JSONObject(text);
            JSONObject result=new JSONObject();
            JSONObject jsonObject = object.getJSONObject("survey");
            result.put("id",jsonObject.getString("id"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String date=format.format(new Date());
            result.put("date",date);
            result.put("EMEI","null");
            JSONArray questions=new JSONArray();
            //for(int i=0;i<jsonArray.length();i++){//survey
                //JSONObject jsonObject=jsonArray.getJSONObject(i);
            int len = jsonObject.getInt("len");
            result.put("len",len);
            JSONArray question = jsonObject.getJSONArray("questions");
            for (int j = 0; j < question.length(); j++) {//questions
                RadioGroup radioGroup = new RadioGroup(this);
                JSONObject jsonObject1 = question.getJSONObject(j);
                String question_text = String.valueOf(j + 1)+"." + jsonObject1.getString("question");
                report=report+"Question"+question_text+"\n";
                String type = jsonObject1.getString("type");
                TextView textView = new TextView(this);
                textView.setText(question_text);
                layout.addView(textView);
                if (type.equals("single")) {
                    layout.addView(radioGroup);
                }
                JSONArray option = jsonObject1.getJSONArray("options");
                for (int k = 0; k < option.length(); k++) {//options
                    JSONObject jsonObject11 = option.getJSONObject(k);
                    String str = jsonObject11.getString(String.valueOf(k + 1));
                    if (type.equals("single")) {
                        final RadioButton button = new RadioButton(this);
                        button.setText(str);
                        button.setTag(j);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(button.getTag().toString()!=null) {
                                    System.out.println(button.getTag().toString());
                                }
                                if(button.getText().toString()!=null) {
                                    System.out.println(button.getText().toString());
                                }
                                bundle.putString(button.getTag().toString(),button.getText().toString());
                            }
                        });
                        radioGroup.addView(button);
                    } else {
                        final CheckBox box = new CheckBox(this);
                        box.setText(str);
                        box.setTag(j);
                        box.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bundle.putString(box.getTag().toString(),box.getText().toString());
                            }
                        });
                        layout.addView(box);
                    }
                }
            }

            for (int i = 0; i < question.length(); i++) {//questions
                    JSONObject jsonObject1 = question.getJSONObject(i);
                    JSONObject q = new JSONObject();
                    String type = jsonObject1.getString("type");
                    q.put("type", type);
                    q.put("question", jsonObject1.getString("question"));
                    JSONArray array=new JSONArray();
                    JSONObject option=new JSONObject();
                    option.put(String.valueOf(i+1),bundle.getString(String.valueOf(i)));
                    array.put(option);
                    q.put("options",array);
                    questions.put(q);
            }
            result.put("questions",questions);
            final String filePath = getApplication().getFilesDir().getPath()+"/report.json";
            FileOutputStream fileOutputStream=new FileOutputStream(filePath);
            JsonWriter writer=new JsonWriter(new OutputStreamWriter(fileOutputStream));
            writer.beginObject();
            writer.name("result").value(result.toString());
            writer.endObject();
            writer.close();

            final Button next_button=new Button(this);
            next_button.setText("upload file");
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFile();
                }
            });
            layout.addView(next_button);
            }  catch (Exception e) {
            e.printStackTrace();
        }
    }

public void uploadFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int length =0;
                FileInputStream fis = null;
                DataOutputStream dos = null;
                Socket socket = null;
                OutputStream out =null;
                PrintWriter pw = null;
                byte[] sendByte = null;
                try {
                    socket = new Socket("10.0.2.2",7777);
                    out = socket.getOutputStream();
                    pw = new PrintWriter(out);
                    System.out.println("");
                    File file = new File(getFilesDir().toString()+File.separator + "result.json");
                    fis = new FileInputStream(file);
                    dos = new DataOutputStream(socket.getOutputStream());
                    sendByte = new byte[1024];
                    pw.write("123"+"\r\n");
                    pw.flush();
                    pw.write("456"+"\r\n");
                    pw.flush();
                    System.out.println("开始传输");
                    while((length=fis.read(sendByte))>0){
                        dos.write(sendByte, 0 , length);
                        dos.flush();
                    }
                    System.out.println("传输完成");
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally{
                    try {
                        fis.close();
                        dos.close();
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Intent intent = new Intent();
        intent.setClass(HomeActivity.this,FinishActivity.class);
        startActivity(intent);
    }

}

