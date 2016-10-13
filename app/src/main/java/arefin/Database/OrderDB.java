package arefin.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class OrderDB
{
    final static String LOG_TAG="checkLog";
    public static final String KEY_ORDER_ID="_id";
    public static final String KEY_ORDER_MENU_ID = "menu_id";  //Menu Item
    public static final String KEY_ORDER_ATTENDEE_ID = "Ordered";    //Attendee ID
    public static final String KEY_ORDER_SERVED = "Served";   //Served or Not
    public static final String KEY_ORDER_QUANTITY = "Quantity"; //Number of orders by a person
    public static final String KEY_ORDER_EVENT_ID = "event_id"; //Item Number


    //ItemNumber, Ordered by, Served, Quantity
    public static String createTable()
    {

        String CREATE_TABLE="create table " + Order.TABLE + "( "
                + KEY_ORDER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORDER_MENU_ID +" INTEGER , "
                + KEY_ORDER_EVENT_ID + " INTEGER, "
                + KEY_ORDER_ATTENDEE_ID+" INTEGER , "
                + KEY_ORDER_SERVED+" INTEGER, "
                + KEY_ORDER_QUANTITY+" INTEGER, "
                +"FOREIGN KEY("+KEY_ORDER_ATTENDEE_ID+") REFERENCES "+Attendee.TABLE+"("+AttendeeDB.KEY_ATTENDEE_ID+") ON UPDATE CASCADE ON DELETE CASCADE"
                +")";
        return CREATE_TABLE;
    }

    public static void update(Order order) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ORDER_MENU_ID,order.itemID);
        contentValues.put(KEY_ORDER_EVENT_ID,order.eventID);
        contentValues.put(KEY_ORDER_ATTENDEE_ID,order.attendeeID);
        contentValues.put(KEY_ORDER_SERVED,order.served);
        contentValues.put(KEY_ORDER_QUANTITY,order.quantity);
        contentValues.put(KEY_ORDER_ID,order.serial);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Order.TABLE, contentValues, KEY_ORDER_ID + "= ?", new String[] { String.valueOf(order.serial) });

        Log.d(LOG_TAG,"Order updated "+order.toString());
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection
    }

    public static void deleteOrderbyID(int ID) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Order.TABLE, KEY_ORDER_ID + "= ?", new String[] { String.valueOf(ID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deleteOrderbyEvent(int eventID) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Order.TABLE, KEY_ORDER_EVENT_ID + "= ?", new String[] { String.valueOf(eventID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deleteOrderbyAttendee(int attendeeID) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Order.TABLE, KEY_ORDER_ATTENDEE_ID + "= ?", new String[] { String.valueOf(attendeeID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }


    public static int insertOrder(Order order)
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ORDER_MENU_ID,order.itemID);
        contentValues.put(KEY_ORDER_EVENT_ID,order.eventID);
        contentValues.put(KEY_ORDER_ATTENDEE_ID,order.attendeeID);
        contentValues.put(KEY_ORDER_SERVED,order.served);
        contentValues.put(KEY_ORDER_QUANTITY,order.quantity);
        long row = db.insert(Order.TABLE,null,contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection

        Log.d(LOG_TAG,"Order Added"+row);
        return (int) row;
    }

    public static ArrayList<Order> getOrdersByItem(int eventID,int itemID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ORDER_ID+","+
                KEY_ORDER_MENU_ID + "," +
                KEY_ORDER_EVENT_ID + "," +
                KEY_ORDER_ATTENDEE_ID + "," +
                KEY_ORDER_SERVED + "," +
                KEY_ORDER_QUANTITY +
                " FROM " + Order.TABLE
                + " WHERE " +
                KEY_ORDER_MENU_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        //Student student = new Student();
        ArrayList<Order> orderList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(itemID) } );
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {

                Order order=new Order();
                order.serial=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID));
                order.itemID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_MENU_ID));
                order.eventID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_EVENT_ID));
                order.attendeeID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ATTENDEE_ID));
                order.served=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_SERVED));
                order.quantity=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_QUANTITY));
                if(order.eventID==eventID)
                    orderList.add(order);


            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return orderList;
    }

    public static ArrayList<Order> getOrdersByEvent(int eventID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ORDER_ID+","+
                KEY_ORDER_MENU_ID + "," +
                KEY_ORDER_EVENT_ID + "," +
                KEY_ORDER_ATTENDEE_ID + "," +
                KEY_ORDER_SERVED + "," +
                KEY_ORDER_QUANTITY +
                " FROM " + Order.TABLE
                + " WHERE " +
                KEY_ORDER_EVENT_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        //Student student = new Student();
        ArrayList<Order> orderList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(eventID) } );
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {

                Order order=new Order();
                order.serial=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID));
                order.itemID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_MENU_ID));
                order.eventID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_EVENT_ID));
                order.attendeeID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ATTENDEE_ID));
                order.served=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_SERVED));
                order.quantity=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_QUANTITY));
                orderList.add(order);


            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return orderList;
    }

    //Get orders by a specific Person
    public static ArrayList<Order> getOrdersByAttendee(int attendeeID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ORDER_ID+","+
                KEY_ORDER_MENU_ID + "," +
                KEY_ORDER_EVENT_ID + "," +
                KEY_ORDER_ATTENDEE_ID + "," +
                KEY_ORDER_SERVED + "," +
                KEY_ORDER_QUANTITY +
                " FROM " + Order.TABLE
                + " WHERE " +
                KEY_ORDER_ATTENDEE_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        //Student student = new Student();
        ArrayList<Order> orderList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(attendeeID) } );
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {

                Order order=new Order();
                order.serial=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID));
                order.itemID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_MENU_ID));
                order.eventID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_EVENT_ID));
                order.attendeeID=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ATTENDEE_ID));
                order.served=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_SERVED));
                order.quantity=cursor.getInt(cursor.getColumnIndex(KEY_ORDER_QUANTITY));
                orderList.add(order);


            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return orderList;
    }

    //Get orders by a specific Person
    public static Order getItemOrderByAttendee(int attendeeID,int itemID)
    {
        Order targetOrder=null;
        ArrayList<Order> orderList=getOrdersByAttendee(attendeeID);
        for(int i=0;i<orderList.size();i++)
        {
            Order order=orderList.get(i);
            if(order.itemID==itemID)
                targetOrder=order;
        }
        return targetOrder;
    }

}
