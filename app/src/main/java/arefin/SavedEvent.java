package arefin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Arefin on 05-Jul-16.
 */

public class SavedEvent
{
    int event_no,itemNum;
    String[] descList,paidList;
    int[] priceList;
    public String name,timestamp,place;
    Set<String> users;
    List<Set<String>> menuSet;
    SavedEvent(Context context) {
        initialize(context);
    }

    SavedEvent()
    {

    }

    public void initialize(Context context)
    {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> description;
        List<Integer> price;
        if(preferences.contains("name")) {
            name = preferences.getString("name", null);
            event_no = preferences.getInt("event_no", 1);
            timestamp = preferences.getString("timestamp", null);
            //String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            place = preferences.getString("place", null);
        }

        if(preferences.contains("itemNum"))
        {
            itemNum = preferences.getInt("itemNum", 0);

            menuSet = new ArrayList<>(itemNum);


            if(preferences.contains("users"))
            {
                ArrayList<String> userList;
                users = preferences.getStringSet("users", null);
                userList = new ArrayList<>(users);
                Collections.sort(userList);
            }

            description=new ArrayList<>(itemNum);
            descList = new String[itemNum];
            for (int i = 0; i < itemNum; i++) {
                descList[i] = preferences.getString("desc_" + i, null);
                description.add(descList[i]);
            }

            price=new ArrayList<>(itemNum);
            priceList = new int[itemNum];
            for (int i = 0; i < itemNum; i++) {
                priceList[i] = preferences.getInt("price_" + i, 0);
                price.add(priceList[i]);
            }

            if(preferences.contains("menu_0"))
            {
                ArrayList<ArrayList<String>> orderer;
                orderer = new ArrayList<>();
                for (int l = 0; l < itemNum; l++) {
                    menuSet.add(preferences.getStringSet("menu_" + l, null));
                    orderer.add(new ArrayList<String>(menuSet.get(l)));
                    Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
                }
            }

            paidList= new String[itemNum];
            for(int i=0;i<itemNum;i++)
            {
                paidList[i] = preferences.getString("selected_"+i, null);
            }
        }
    }

    public void writeToPreference(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.putBoolean("backedup",false);
        editor.putInt("event_no",event_no);
        editor.putString("name", name);
        editor.putString("timestamp",new Timestamp(System.currentTimeMillis()).toString());
        editor.putString("place",place);
        editor.putStringSet("users",users );
        editor.putInt("itemNum", itemNum);
        for(int i=0;i<itemNum; i++)
        {
            editor.putInt("price_" + i, priceList[i]);
            editor.putString("desc_" + i, descList[i]);
            editor.putStringSet("menu_" + i, menuSet.get(i));
            editor.putString("selected_"+i,paidList[i]);
        }
        editor.apply();

    }

    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("********** Event "+ event_no+"**********\n");
        sb.append("\n");
        sb.append("Event Name : "+name);
        sb.append("\n");
        sb.append("Location : "+place);
        sb.append("\n");
        sb.append("TimeStamp : "+timestamp + "\n");
        sb.append("\n");

        if(users==null)
            sb.append("UserList is Blank\n");
        else {
            sb.append("Attendees : " + users.size());
            sb.append("\n");
            sb.append(users.toString() + "\n");
            sb.append("\n");
        }

        sb.append("Number of Items : "+itemNum+"\n");

        for(int i=0;i<itemNum;i++)
        {
            StringBuilder itemBuilder=new StringBuilder();
            itemBuilder.append("\nItem "+(i+1)+" : "+descList[i]);
            itemBuilder.append(" ; price : "+priceList[i]);
            sb.append(itemBuilder.toString());
            sb.append("\n");
            sb.append("\nOrdered By : "+menuSet.get(i).size());
            sb.append("\n");
            sb.append(menuSet.get(i).toString());
            sb.append("\n");
            String savedString =paidList[i];
            int[] selections;
            ArrayList<String> paidMembers=new ArrayList<String>(menuSet.get(i));
            sb.append("\n Paid By : "+paidMembers.size()+"\n");
            if(savedString!=null) {
                if (savedString.equals(""))
                    selections = null;
                else {
                    List<String> items = new ArrayList<>(Arrays.asList(savedString.split(",")));
                    selections = new int[items.size()];
                    for (int j = 0; j < items.size(); j++) {
                        selections[j] = Integer.parseInt(items.get(j));
                    }
                    for(int k=0;k<selections.length;k++)
                        sb.append(paidMembers.get(selections[k])+", ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
