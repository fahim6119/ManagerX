package arefin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.arefin.menuList.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;


public class StartActivity extends AppCompatActivity {

    SavedEvent savedEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //backingUp();
        //restoring();
    }

    public void onClickCreateButton(View v){
        exportHistory();
        Intent createIntent = new Intent(StartActivity.this, CreateActivity.class);
        startActivity(createIntent);
    }
    public void onClickOldButton(View v){
        //Intent oldIntent = new Intent(StartActivity.this, AttendanceActivity.class);
        Intent oldIntent = new Intent(StartActivity.this, FragmentActivity.class);
        startActivity(oldIntent);
    }

    private void exportHistory()
    {
        //SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
            int event_no=preferences.getInt("event_no",0);
            String timestamp= preferences.getString("timestamp",null);
            //String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            String place= preferences.getString("place",null);

            int itemNum=preferences.getInt("itemNum",0);

            Set<String> users=preferences.getStringSet("users",null);
            ArrayList<String> userList=new ArrayList<>(users);
            Collections.sort(userList);

            String[] descList = new String[itemNum];
            for(int i=0; i<itemNum; i++)
                descList[i]=preferences.getString("desc_" + i, null);

            int[] priceList=new int[itemNum];
            for(int i=0; i<itemNum; i++)
                priceList[i]=Integer.parseInt(preferences.getString("price_" + i, null));

            ArrayList<ArrayList<String>> orderer = new ArrayList<>();
            for(int l=0;l<itemNum;l++)
            {
                Set<String> set = preferences.getStringSet("menu_"+l, null);
                orderer.add(new ArrayList<String>(set));
                Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
            }

            pw.println("********** Event "+ event_no+"**********\n");
            pw.println("Event Name : "+name);
            pw.println("Location : "+place);
            pw.println("TimeStamp : "+timestamp + "\n");

            pw.println("Attendees : "+userList.size());
            pw.print(userList.toString()+"\n");
            pw.println("");

            pw.println("Number of Items : "+itemNum);
            pw.println("");

            for(int i=0;i<itemNum;i++)
            {
                StringBuilder itemBuilder=new StringBuilder();
                itemBuilder.append("Item "+(i+1)+" : "+descList[i]);
                itemBuilder.append(" ; price : "+priceList[i]);
                pw.println(itemBuilder.toString());
                pw.println("\nOrdered By : "+orderer.get(i).size());
                pw.println(orderer.get(i).toString());
                pw.println("");
            }

            /*
            Map<String,?> prefsMap = preferences.getAll();

            for(Map.Entry<String,?> entry : prefsMap.entrySet())
            {
                pw.println(entry.getKey() + ": " + entry.getValue().toString());
            }*/
            pw.close();
            fw.close();
            Toast.makeText(getBaseContext(), "Records of Event "+name+" exported to SD Card",
                    Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Log.i("arefin", e.toString()+ " "+getClass().getName());
        }
    }

    private void backingUp()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name= preferences.getString("name",null);
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName=name+"_"+date;
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = getBaseContext().openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            os = new ObjectOutputStream(fos);
            //Map<String,?> prefsMap = preferences.getAll();
            SavedEvent savedEvent=new SavedEvent(getBaseContext());
            os.writeObject(savedEvent);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void restoring()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name= preferences.getString("name",null);
        String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName=name+"_"+date;
        FileInputStream fis = null;
        try {
            fis = getBaseContext().openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            savedEvent= (SavedEvent) is.readObject();
            Log.i("arefin",savedEvent.name);
            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
