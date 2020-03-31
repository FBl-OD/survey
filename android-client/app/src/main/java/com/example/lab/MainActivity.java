package com.example.lab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Activity> activityList = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MainActivity.activityList.add(this);
    }

    public void welClick(View v){
        CheckBox cb=findViewById(R.id.cb_accept);
        if(cb.isChecked()){
            Intent intent=new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
        }
    }

}
