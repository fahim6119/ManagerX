package arefin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Created by Arefin on 05-Jul-16.
 */

public class SavedEvent
{
    int event_no,itemNum;
    public String name,timestamp,place;
    Set<String> users;
    String[] descList;
    int[] priceList;
    ArrayList<ArrayList<String>> orderer;
    SavedEvent(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("FahimFile","File Writing began");
        String name= preferences.getString("name",null);
        int event_no=preferences.getInt("event_no",0);
        String timestamp= preferences.getString("timestamp",null);
        //String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String place= preferences.getString("place",null);

        int itemNum=preferences.getInt("itemNum",0);

        Set<String> users=preferences.getStringSet("users",null);
        ArrayList<String> userList=new ArrayList<>(users);
        Collections.sort(userList);

        descList = new String[itemNum];
        for(int i=0; i<itemNum; i++)
            descList[i]=preferences.getString("desc_" + i, null);

        priceList=new int[itemNum];
        for(int i=0; i<itemNum; i++)
            priceList[i]=Integer.parseInt(preferences.getString("price_" + i, null));

        orderer = new ArrayList<>();
        for(int l=0;l<itemNum;l++)
        {
            Set<String> set = preferences.getStringSet("menu_"+l, null);
            orderer.add(new ArrayList<String>(set));
            Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
        }

    }
}
