package com.example.lab;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import static com.example.lab.MainActivity.activityList;

public class UnlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_unlock );
        activityList.add(this);
    }

    public void exit(){
        for(Activity act:activityList){
            act.finish();
        }
        System.exit(0);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
