package arefin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.batfia.arefin.MenuAssistant.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IListDialogListener;
import arefin.dialogs.iface.IMultiChoiceListDialogListener;


public class StartActivity extends AppCompatActivity implements IListDialogListener, IMultiChoiceListDialogListener {

    boolean update=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // SavedEvent savedEvent = new SavedEvent(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor =sharedRecords.edit();
        if(update==true) {
            editor.clear();
            editor.apply();
        }
        else {
            String oldEvent = preferences.getString("name", null);
            Log.i("checkLog", "Old Event name " + oldEvent);
            Set<String> records = new HashSet<String>();
            if (sharedRecords.contains("records")) {
                Set<String> set = sharedRecords.getStringSet("records", null);
                records.addAll(set);
            }
            if (oldEvent != null)
                records.add(oldEvent);
            editor.putStringSet("records", records);
            editor.apply();
        }
    }

    public void onClickClearButton(View v)
    {
        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedRecords.edit();
        Set<String> set = sharedRecords.getStringSet("records", null);
        if(set!=null && set.isEmpty()==false) {
            ArrayList<String> recordlist = new ArrayList<String>(set);
            Collections.sort(recordlist, String.CASE_INSENSITIVE_ORDER);
            String records[] = new String[recordlist.size()];
            for (int i = 0; i < recordlist.size(); i++) {
                records[i] = recordlist.get(i);
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
    }

    public void onClickCreateButton(View v){
        exportHistory();
        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String oldName=preferences.getString("name",null);
        if(sharedRecords.contains("record_"+oldName)==false)
            backingUp();
        Intent createIntent = new Intent(StartActivity.this, CreateActivity.class);
        startActivity(createIntent);

    }
    public void onClickOldButton(View v){
        //Intent oldIntent = new Intent(StartActivity.this, AttendanceActivity.class);

        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedRecords.edit();
        Set<String> set = sharedRecords.getStringSet("records", null);
        if(set!=null && set.isEmpty()==false) {
            ArrayList<String> recordlist = new ArrayList<String>(set);
            Collections.sort(recordlist, String.CASE_INSENSITIVE_ORDER);
            String records[] = new String[recordlist.size()];
            for (int i = 0; i < recordlist.size(); i++) {
                records[i] = recordlist.get(i);
            }

            ListDialogFragment frag = new arefin.dialogs.fragment.ListDialogFragment();
            frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                    .setTitle("Import Old Event")
                    .setItems(records)
                    .setCancelable(false)
                    .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                    .setRequestCode(0)
                    .show();

        }
        else
        {
            Intent oldIntent = new Intent(StartActivity.this, FragmentActivity.class);
            startActivity(oldIntent);
        }
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        Log.i("checkLog","Clicked on "+value);
        restoring(value.toString());

        Intent oldIntent = new Intent(StartActivity.this, FragmentActivity.class);
        startActivity(oldIntent);
    }

    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode) {
        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedRecords.edit();
        Set<String> set = sharedRecords.getStringSet("records", null);
        if (set != null && set.isEmpty() == false) {
            ArrayList<String> recordlist = new ArrayList<String>(set);
            Collections.sort(recordlist, String.CASE_INSENSITIVE_ORDER);
            for (int i : selectedPositions) {
                String selected = recordlist.get(i);
                Log.i("checkLog", "Selected " + selected);
                recordlist.remove(i);
                editor.remove("record_"+selected);
            }
            Set<String> records = new HashSet<String>(recordlist);
            editor.putStringSet("records", records);
            editor.apply();
        }
    }

    private void exportHistory()
    {
        //SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("users")==false)
            return;
        Log.i("checkLog","File Writing began");
        String name= preferences.getString("name",null);
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
            SavedEvent savedEvent =new SavedEvent(getBaseContext());
            pw.println(savedEvent.toString());
            /*
            Map<String,?> prefsMap = preferences.getAll();

            for(Map.Entry<String,?> entry : prefsMap.entrySet())
            {
                pw.println(entry.getKey() + ": " + entry.getValue().toString());
            }*/
            pw.close();
            fw.close();
            Toast.makeText(getBaseContext(), "Records of Event " + name + " exported to SD Card",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.i("checkLog", e.toString()+ " "+getClass().getName());
        }
    }


    private void backingUp() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedRecords.edit();
        String name = preferences.getString("name", null);
        //Log.i("checkLog","JSON object writing for "+name);
        if (name == null)
            return;

        SavedEvent savedEvent = new SavedEvent(getBaseContext());
        if(preferences.contains("itemNum")==false)
            return;
        //int paidSize=savedEvent.paidList.length;
        int itemNum=preferences.getInt("itemNum",0);
        String[] descList = new String[itemNum];
        int[] priceList = new int[itemNum];
       // int[] paidList=new int[paidSize];
        String[] servedList = new String[itemNum];
        for (int i = 0; i < itemNum; i++) {
            descList[i] = preferences.getString("desc_" + i, null);
            priceList[i] = preferences.getInt("price_" + i, 0);
            servedList[i] = preferences.getString("selected_" + i, null);
        }

        /*
        for (int i = 0; i <paidSize; i++)
        {
            ArrayList<String> userlist= new ArrayList<>(savedEvent.users);
            String userName=userlist.get(i);
            paidList[i]=preferences.getInt("paid_"+userName,0);
        }
                String[] record=new String[5];
        */

        String[] record=new String[4];
        record[0]=GsonInsert(savedEvent);
        record[1]=GsonInsert(priceList);
        record[2]=GsonInsert(descList);
        record[3]=GsonInsert(servedList);
       // record[4]=GsonInsert(paidList);
        editor.putString("record_"+name,GsonInsert(record));




        editor.commit();
        if(record[0]!=null) {
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.putBoolean("backedup",true);
        }
        Log.i("checkLog","JSON object written with "+ name);

    }

    public String GsonInsert(Object o)
    {
        Gson gson = new Gson();
        String user_json = gson.toJson(o);
        Log.i("checkLog","JSON object  "+user_json);
        return user_json;
    }

    private void restoring(String name)
    {
        String[] records=GsonRetrieve(name);
        Gson gson = new Gson();
        SavedEvent savedEvent= gson.fromJson(records[0], SavedEvent.class);

        Gson priceGson =new Gson();
        int[] price=priceGson.fromJson(records[1],int[].class);

        Gson descGson= new Gson();
        String[] desc =descGson.fromJson(records[2],String[].class);

        Gson servedGson= new Gson();
        String[] served =servedGson.fromJson(records[3],String[].class);

        //Gson paidGson= new Gson();
       // int[] paid =paidGson.fromJson(records[4],int[].class);

        savedEvent.priceList=price;
        savedEvent.descList=desc;
        savedEvent.servedList=served;
        //savedEvent.paidList=paid;
        Log.i("checkLog","Retrieved "+savedEvent.toString());

        savedEvent.writeToPreference(getBaseContext());

    }

    public String[] GsonRetrieve(String name)
    {
        SharedPreferences preferences = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String record_id="record_"+name;
        String record_gson=preferences.getString(record_id, "");
        String[] record =gson.fromJson(record_gson,String[].class);
        return record;
    }

}
