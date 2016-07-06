package arefin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arefin.menuList.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
cd A:/Android_SDK/platform-tools/
adb shell
cd /data/data/com.example.arefin.menuList/shared_prefs
cat EventRecords.xml
*/


public class CreateActivity extends AppCompatActivity {

    int amount;
    String name,place;
    Button amountEnter;
    EditText amountVal,nameVal,locVal;
    TextView amountMsg,nameMsg,locMsg;
    int event_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        amountMsg=(TextView)findViewById(R.id.amountMsg);
        amountVal=(EditText)findViewById(R.id.amountVal);
        locMsg=(TextView)findViewById(R.id.locMsg);
        locVal=(EditText)findViewById(R.id.locVal);
        nameMsg=(TextView)findViewById(R.id.nameMsg);
        nameVal=(EditText)findViewById(R.id.nameVal);
        amountEnter=(Button)findViewById(R.id.amountEnter);
        amount=100;

        name="@strings/database_name";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        event_no=preferences.getInt("event_no",0)+1;
        Log.i("checkLog","event No "+event_no);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        amountEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // amount = Integer.parseInt(amountVal.getText().toString());
                name=nameVal.getText().toString();
                place=locVal.getText().toString();
                Toast.makeText(getBaseContext(), name+" Event Created",
                        Toast.LENGTH_LONG).show();
                editor.putString("name", name);
                editor.putString("timestamp",new Timestamp(System.currentTimeMillis()).toString());
                editor.putString("place",place);
                editor.putInt("event_no",event_no);
                editor.putBoolean("backedup",false);
                editor.apply();
                Intent createIntent = new Intent(CreateActivity.this, AttendanceActivity.class);
                startActivity(createIntent);
                finish();
            }
        });
    }



}
