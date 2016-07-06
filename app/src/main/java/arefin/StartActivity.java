package arefin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.arefin.menuList.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IListDialogListener;


public class StartActivity extends AppCompatActivity implements IListDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences sharedRecords = getSharedPreferences("EventRecords", Context.MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor =sharedRecords.edit();
        String oldEvent=preferences.getString("name",null);
        Log.i("checkLog","Old Event name "+oldEvent);

        Set<String> records = new HashSet<String>();
        if(sharedRecords.contains("records")) {
            Set<String> set = sharedRecords.getStringSet("records", null);
            records.addAll(set);
        }
        if(oldEvent!=null)
            records.add(oldEvent);
        editor.putStringSet("records",records);
        editor.apply();
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
        if(set.isEmpty()==false) {
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

    private void exportHistory()
    {
        //SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("users")==false)
            return;
        Log.i("FahimFile","File Writing began");
        String name= preferences.getString("name",null);
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // Create Folder
        String folder_main = "MenuList";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        File myPath = new File(Environment.getExternalStorageDirectory()+ "/" + folder_main);
        File myFile = new File(myPath, name+"_"+date+".txt");

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
        Log.i("checkLog","JSON object writing for "+name);
        if (name == null)
            return;

        SavedEvent savedEvent = new SavedEvent(getBaseContext());

        int itemNum=preferences.getInt("itemNum",0);
        String[] descList = new String[itemNum];
        int[] priceList = new int[itemNum];
        String[] paidList=new String[itemNum];

        for (int i = 0; i < itemNum; i++) {
            descList[i] = preferences.getString("desc_" + i, null);
            priceList[i] = preferences.getInt("price_" + i, 0);
            paidList[i]=preferences.getString("selected_"+i,null);
        }

        String[] record=new String[4];
        record[0]=GsonInsert(savedEvent);
        record[1]=GsonInsert(priceList);
        record[2]=GsonInsert(descList);
        record[3]=GsonInsert(paidList);

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

        Gson paidGson= new Gson();
        String[] paid =paidGson.fromJson(records[3],String[].class);

        savedEvent.priceList=price;
        savedEvent.descList=desc;
        savedEvent.paidList=paid;
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
