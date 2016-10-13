package arefin;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.Database.Event;
import arefin.Database.EventDB;
import arefin.Database.MenuItem;
import arefin.Database.MenuItemDB;
import arefin.Database.Order;
import arefin.Database.OrderDB;

/**
 * Created by Arefin on 05-Jul-16.
 */

public class SavedEvent
{
    int event_no,itemNum;
    public String name,timestamp,place;
    Event event;

    ArrayList<Attendee> attendees;
    ArrayList<ArrayList<Order>> orderList;
    ArrayList<MenuItem> menuItems;
    ArrayList<String> userList;
    ArrayList<ArrayList<String>> servedList,orderers;

    SavedEvent(String name)
    {
        event= EventDB.getEventByName(name);
        this.name=name;
        event_no=event.serial;
        timestamp=event.timestamp;
        place=event.place;

        attendees= AttendeeDB.getAttendeesByEvent(event_no);
        userList=new ArrayList<>();
        for(int i=0;i<attendees.size();i++)
            userList.add(attendees.get(i).name);
        Collections.sort(userList);


        menuItems= MenuItemDB.getItemsByEvent(event_no);
        itemNum=menuItems.size();

        orderList=new ArrayList<>();
        servedList=new ArrayList<>();
        orderers=new ArrayList<>();

        for(int i=0;i<itemNum;i++)
        {
            int menuID=menuItems.get(i).serial;
            orderList.add( OrderDB.getOrdersByItem(event_no,menuID));
            servedList.add(new ArrayList<String>());
            orderers.add(new ArrayList<String>());
            ArrayList<Order> ordersForItem=orderList.get(i);
            Log.i("checkLog","ServedList getting Initialized");
            for(int j=0;j<ordersForItem.size();j++)
            {
                Order order=ordersForItem.get(j);
                if(order==null)
                    continue;
                String orderedBy=AttendeeDB.getAttendeeByID(order.attendeeID).name;
                orderers.get(i).add(orderedBy);
                if(order.served==1)
                {
                    servedList.get(i).add(orderedBy);
                }
            }
        }
        Log.i("checkLog","Event Initialized");
    }



    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("********** Event "+ event_no+" **********\n");
        sb.append("\n");
        sb.append("Event Name : "+name);
        sb.append("\n");
        sb.append("Location : "+place);
        sb.append("\n");
        sb.append("Start Time : "+timestamp + "\n");
        sb.append("\n");

        if(userList==null)
            sb.append("UserList is Blank\n");
        else {
            sb.append("Attendees : " + userList.size());
            sb.append("\n");
            sb.append(userList.toString() + "\n");
            sb.append("\n");
        }

        sb.append("Number of Items : "+itemNum+"\n");

        for(int i=0;i<itemNum;i++)
        {
            StringBuilder itemBuilder=new StringBuilder();
            itemBuilder.append("\nItem "+(i+1)+" : "+menuItems.get(i).description);
            itemBuilder.append(" ; price : "+menuItems.get(i).price);
            sb.append(itemBuilder.toString());
            sb.append("\n");
            sb.append("\nOrdered By : "+orderers.get(i).size());
            sb.append("\n");
            sb.append(orderers.get(i).toString());
            sb.append("\n");

            sb.append("\n Served to : "+servedList.get(i).size()+"\n");
            ArrayList<String> servedTo=servedList.get(i);
            for(int k=0;k<servedTo.size();k++) {
                sb.append(servedTo.get(k));
                if(k!=servedTo.size()-1)
                    sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("\nPayments Made : \n");
        for(int k=0;k<userList.size();k++)
        {
            Attendee attendee=attendees.get(k);
            String name=attendee.name;
            //sb.append(name + " "+ paidList[k]+",");
            sb.append(name+"\t : Bill : "+attendee.total+", Paid : "+attendee.paid+" , Due : "+(attendee.total-attendee.paid)+"\n");
        }
        return sb.toString();
    }
}
