package mg.studio.android.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.view.View;

public class Activity_Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_survey);
    }

    public void finish(View v){
        finish();
    }
}
