package arefin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.batfia.arefin.ManagerX.R;

import java.sql.Timestamp;

import arefin.Database.Event;
import arefin.Database.EventDB;
import arefin.Database.SuggestionActivity;
import arefin.dialogs.fragment.SimpleDialogFragment;
import arefin.dialogs.iface.ISimpleDialogListener;

/*
cd A:/Android_SDK/platform-tools/
adb shell
cd /data/data/com.batfia.arefin.ManagerX/shared_prefs
cat EventRecords.xml
*/


public class CreateActivity extends AppCompatActivity implements ISimpleDialogListener {

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
        //amountMsg=(TextView)findViewById(R.id.amountMsg);
        //amountVal=(EditText)findViewById(R.id.amountVal);
        locMsg=(TextView)findViewById(R.id.locMsg);
        locVal=(EditText)findViewById(R.id.locVal);
        nameMsg=(TextView)findViewById(R.id.nameMsg);
        nameVal=(EditText)findViewById(R.id.nameVal);
        amountEnter=(Button)findViewById(R.id.amountEnter);
        amount=100;

        amountEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // amount = Integer.parseInt(amountVal.getText().toString());
                name=nameVal.getText().toString();
                place=locVal.getText().toString();
                Toast.makeText(getBaseContext(), name+" Event Created",
                        Toast.LENGTH_LONG).show();
                String timeStamp=new Timestamp(System.currentTimeMillis()).toString();
                Event event=new Event(name,place,timeStamp);
                event_no= EventDB.insertEvent(event);
                event.serial=event_no;
                app.currentEventID=event_no;
                createPrompt();
            }
        });
    }

    public void createPrompt()
    {
        SimpleDialogFragment.createBuilder(this,
                getSupportFragmentManager()).setTitle("Suggestion!")
                .setMessage("Do you want to add attendees from previous events?")
                .setPositiveButtonText("Yes")
                .setNegativeButtonText("No")
                .setCancelable(false)
                .show();

    }


    @Override
    public void onNegativeButtonClicked(int requestCode) {
        Intent oldIntent = new Intent(CreateActivity.this, AttendanceActivity.class);
        startActivity(oldIntent);
        finish();
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        Intent oldIntent = new Intent(CreateActivity.this, SuggestionActivity.class);
        startActivity(oldIntent);
        finish();
    }
}
