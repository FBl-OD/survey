package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

}
