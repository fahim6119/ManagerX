package arefin.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class EventDB
{
    final static String LOG_TAG="AREFIN";
    public String AttendeeTable,MenuTable,OrderTable;

    public static final String KEY_EVENT_ID="_id";
    public static final String KEY_EVENT_NAME="Event_name";
    public static final String KEY_EVENT_PLACE="Place";
    public static final String KEY_EVENT_TIMESTAMP="TimeStamp";  //TimeStamp
    //public String event_5="ItemNum";    //Number of Items

    //ID, event, place, timestamp, number of items

    public static String createTable()
    {
        String CREATE_TABLE="create table " + Event.TABLE+ "( "
                + KEY_EVENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EVENT_NAME+" TEXT , "
                + KEY_EVENT_PLACE+" TEXT , "
                + KEY_EVENT_TIMESTAMP+" TEXT  "
                +")";
        return CREATE_TABLE;
    }

    public static int insertEvent(Event event)
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EVENT_NAME,event.name);
        contentValues.put(KEY_EVENT_PLACE,event.place);
        contentValues.put(KEY_EVENT_TIMESTAMP,event.timestamp);
        long row = db.insert(Event.TABLE,null,contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection

        Log.d(LOG_TAG,"Event Added"+row);
        return (int) row;
    }

    public static void deleteByName(String eventName) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Event.TABLE, KEY_EVENT_NAME + "= ?", new String[] { String.valueOf(eventName) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deletebyPlace(String place) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Event.TABLE, KEY_EVENT_PLACE + "= ?", new String[] { String.valueOf(place) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Event.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }


    public static void update(Event event) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EVENT_NAME,event.name);
        contentValues.put(KEY_EVENT_PLACE,event.place);
        contentValues.put(KEY_EVENT_TIMESTAMP,event.timestamp);


        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Event.TABLE, contentValues, KEY_EVENT_ID + "= ?", new String[] { String.valueOf(event.serial) });

        Log.d(LOG_TAG,"Event updated "+event.name+" "+event.toString());
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection
    }

    public static Event getEventByID(int ID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_EVENT_ID + "," +
                KEY_EVENT_NAME + "," +
                KEY_EVENT_PLACE + "," +
                KEY_EVENT_TIMESTAMP +
                " FROM " + Event.TABLE
                + " WHERE " +
                KEY_EVENT_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        Event event = null;
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                event=new Event();
                event.serial=cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID));
                event.name=cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME));
                event.place=cursor.getString(cursor.getColumnIndex(KEY_EVENT_PLACE));
                event.timestamp=cursor.getString(cursor.getColumnIndex(KEY_EVENT_TIMESTAMP));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return event;
    }

    public static Event getEventByName(String name)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_EVENT_ID + "," +
                KEY_EVENT_NAME + "," +
                KEY_EVENT_PLACE + "," +
                KEY_EVENT_TIMESTAMP +
                " FROM " + Event.TABLE
                + " WHERE " +
                KEY_EVENT_NAME + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(name) } );

        Event event = null;
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                event=new Event();
                event.serial=cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID));
                event.name=cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME));
                event.place=cursor.getString(cursor.getColumnIndex(KEY_EVENT_PLACE));
                event.timestamp=cursor.getString(cursor.getColumnIndex(KEY_EVENT_TIMESTAMP));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return event;
    }

    public static ArrayList<Event> getAllEvents()
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_EVENT_ID + "," +
                KEY_EVENT_NAME + "," +
                KEY_EVENT_PLACE + "," +
                KEY_EVENT_TIMESTAMP +
                " FROM " + Event.TABLE;

        //Student student = new Student();
        ArrayList<Event> eventList = new ArrayList<Event>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Event event=new Event();
                event.serial=cursor.getInt(cursor.getColumnIndex(KEY_EVENT_ID));
                event.name=cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME));
                event.place=cursor.getString(cursor.getColumnIndex(KEY_EVENT_PLACE));
                event.timestamp=cursor.getString(cursor.getColumnIndex(KEY_EVENT_TIMESTAMP));

                eventList.add(event);

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return eventList;
    }

}
