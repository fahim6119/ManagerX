package arefin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.batfia.arefin.ManagerX.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import arefin.Database.Event;
import arefin.Database.EventDB;
import arefin.Database.SuggestionActivity;
import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IListDialogListener;
import arefin.dialogs.iface.IMultiChoiceListDialogListener;


public class StartActivity extends AppCompatActivity implements IListDialogListener, IMultiChoiceListDialogListener {

    final static String LOG_TAG="checkLog";
    ArrayList<Event> eventList;
    String[] records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // SavedEvent savedEvent = new SavedEvent(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        eventList=EventDB.getAllEvents();
        records = new String[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            records[i] = eventList.get(i).name;
        }
    }

    public void onClickClearButton(View v)
    {
        eventList=EventDB.getAllEvents();
        records = new String[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            records[i] = eventList.get(i).name;
        }
        if(eventList.isEmpty() || eventList==null) {
            Log.i("checkLog","Event List Empty");
            Toast.makeText(getApplicationContext(), "You haven't created any events,yet", Toast.LENGTH_SHORT);
            return;
        }
        ListDialogFragment frag = new arefin.dialogs.fragment.ListDialogFragment();
        frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                .setTitle("Delete Old Event")
                .setItems(records)
                .setCancelable(false)
                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                .setRequestCode(1)
                .show();
    }

    public void onClickCreateButton(View v)
    {
        Intent createIntent = new Intent(StartActivity.this, CreateActivity.class);
        startActivity(createIntent);

    }
    public void onClickOldButton(View v){
        //Intent oldIntent = new Intent(StartActivity.this, AttendanceActivity.class);
        eventList=EventDB.getAllEvents();
        records = new String[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            records[i] = eventList.get(i).name;
        }
        if(eventList.isEmpty()|| eventList==null) {
            Log.i("checkLog","Event List Empty");
            Toast.makeText(getApplicationContext(), "You haven't created any events,yet", Toast.LENGTH_SHORT);
            return;
        }
        if(eventList.size()>1)
        {
            ListDialogFragment frag = new arefin.dialogs.fragment.ListDialogFragment();
            frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                    .setTitle("Import Old Event")
                    .setItems(records)
                    .setCancelable(false)
                    .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                    .setRequestCode(0)
                    .show();

        }
        else if(eventList.size()==1)
        {
            app.currentEventID=eventList.get(0).serial;
            Intent oldIntent = new Intent(StartActivity.this, FragmentActivity.class);
            startActivity(oldIntent);
        }
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        Log.i(LOG_TAG,"Clicked on "+value+" Request Code "+requestCode);
        //int index=eventList.indexOf(value.toString());
        app.currentEventID=eventList.get(number).serial;

        Intent oldIntent = new Intent(StartActivity.this, FragmentActivity.class);
        startActivity(oldIntent);
    }

    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode)
    {
            for (int i : selectedPositions)
            {
                String selected = eventList.get(i).name;
                //new SavedEvent(selected);
                //exportHistory(selected);
                EventDB.deleteByName(selected);
            }
        eventList=EventDB.getAllEvents();
        records = new String[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            records[i] = eventList.get(i).name;
        }
    }

    private void exportHistory(String eventName)
    {
        Log.i("checkLog","File Writing began");
        String name=eventName;
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // Create Folder
        String folder_main = getBaseContext().getString(R.string.app_name);
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        Log.i("checkLog","folder created "+f.exists());

        File myPath = new File(Environment.getExternalStorageDirectory()+ "/" + folder_main);
        File myFile = new File(myPath, name+"_"+date+".txt");

        Log.i("checkLog","filepath created "+myFile.exists());

        try
        {
            FileWriter fw = new FileWriter(myFile);
            PrintWriter pw = new PrintWriter(fw);
            SavedEvent savedEvent =new SavedEvent(eventName);
            pw.println(savedEvent.toString());

            pw.close();
            fw.close();
            Toast.makeText(getBaseContext(), "Records of Event " + name + " exported to SD Card",
                    Toast.LENGTH_LONG).show();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }
}
