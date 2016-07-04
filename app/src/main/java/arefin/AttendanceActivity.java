package arefin;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AttendanceActivity extends AppCompatActivity {

    ListView listView;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("users"))
        {
            Set<String> set = preferences.getStringSet("users", null);
            listItems=new ArrayList<String>(set);
            Collections.sort(listItems, String.CASE_INSENSITIVE_ORDER);
        }

        listView=(ListView)findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        clickCounter=0;
        getSupportActionBar().setTitle("Attendees");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void updateList(String uName)
    {
        clickCounter++;
        listItems.add(uName);
        adapter.notifyDataSetChanged();
        Toast.makeText(getBaseContext(), uName +" is attending",
                Toast.LENGTH_LONG).show();
    }

    public void sendNext(View v)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(listItems);
        editor.putStringSet("users",set );
        editor.commit();
        Intent createIntent = new Intent(AttendanceActivity.this, MenuCreatorActivity.class);
        startActivity(createIntent);
        finish();
    }

    public void getPayment()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_payment_receiver, null);
        dialog.setContentView(dialogView);

        dialog.setCancelable(true);

        Button usernameSetButton = (Button) dialogView.findViewById(R.id.usernameSetButton);
        final EditText newUsername = (EditText) dialogView.findViewById(R.id.newUsername);

        usernameSetButton.setOnClickListener(new View.OnClickListener()
        {
             public void onClick(View v) {
                 String userName= newUsername.getText().toString().trim();
                 if(TextUtils.isEmpty(userName))
                 {
                     Toast.makeText(getBaseContext(), "Invalid Name",
                             Toast.LENGTH_SHORT).show();
                     return;
                 }
                 updateList(userName);
                 dialog.dismiss();
             }
         });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.collector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
        }

        if (id == R.id.action_add)
        {
            getPayment();
        }

        return super.onOptionsItemSelected(item);
    }


}
