package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        showData();
    }

    private void showData(){
        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        TextView[] tv=new TextView[12];
        findViewsBySameIds(tv,"tv_report_A");
        tv[0].setText(bundle.getString("answer1"));
        String ans="answer";
        for(int i=0;i<12;i++){
            int s=i+1;
            String index=ans+String.valueOf(s);
            tv[i].setText(bundle.getString(index));
        }
    }

    public void click_report(View v){
        Intent intent=getIntent();
        intent.setClass(this,Activity_Finish.class);
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


}
