package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        MainActivity.activityList.add(this);
    }

    public void finish(View v){
        Intent intent=new Intent();
        intent.setClass(this,UnlockActivity.class);
        startActivity(intent);
    }
}
