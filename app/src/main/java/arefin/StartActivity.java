package arefin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.arefin.menuList.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClickCreateButton(View v){
        Intent createIntent = new Intent(StartActivity.this, CreateActivity.class);
        startActivity(createIntent);
    }
    public void onClickOldButton(View v){
       // Intent oldIntent = new Intent(StartActivity.this, CollectorActivity.class);
        Intent oldIntent = new Intent(StartActivity.this, MenuCreatorActivity.class);
        startActivity(oldIntent);
    }


}
