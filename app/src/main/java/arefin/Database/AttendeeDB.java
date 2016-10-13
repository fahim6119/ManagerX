package arefin.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class AttendeeDB
{
    final static String LOG_TAG="checkLog";

    public static final String KEY_ATTENDEE_ID="_id";      //Attendee ID -int
    public static final String KEY_ATTENDEE_NAME="Name";    //Name of Attendee -string
    public static final String KEY_ATTENDEE_TOTAL="Total";   //Total Bill for person -double
    public static final String KEY_ATTENDEE_PAID="Paid";    //Amount Paid by person -double
    public static final String KEY_ATTENDEE_EVENT_ID = "event_id"; //Item Number

    //ID, name, total , paid
    public static String createTable()
    {

        String CREATE_TABLE = "CREATE TABLE " + Attendee.TABLE + "("
                + KEY_ATTENDEE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ATTENDEE_EVENT_ID + " INTEGER, "
                + KEY_ATTENDEE_NAME +" TEXT , "
                + KEY_ATTENDEE_TOTAL +" REAL , "
                + KEY_ATTENDEE_PAID +" REAL  "
                + " )";

        return CREATE_TABLE;
    }



    public static int insertAttendee(Attendee attendee)
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDEE_EVENT_ID,attendee.eventID);
        contentValues.put(KEY_ATTENDEE_NAME,attendee.name);
        contentValues.put(KEY_ATTENDEE_TOTAL,attendee.total);
        contentValues.put(KEY_ATTENDEE_PAID,attendee.paid);
        long row = db.insert(Attendee.TABLE,null,contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection

        Log.d(LOG_TAG,"Attendee Added"+row);
        return (int) row;
    }

    public static void deleteByName(int eventID,String attendeeName) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Attendee.TABLE, KEY_ATTENDEE_NAME + "= ? AND "+KEY_ATTENDEE_EVENT_ID + " = ?", new String[] { String.valueOf(attendeeName),String.valueOf(eventID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deletebyID(int ID) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Attendee.TABLE, KEY_ATTENDEE_ID + "= ?", new String[] { String.valueOf(ID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deletebyEvent(int eventID) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Attendee.TABLE, KEY_ATTENDEE_EVENT_ID + "= ?", new String[] { String.valueOf(eventID) });
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void deleteAll() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Attendee.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();  // Closing database connection
    }

    public static void update(Attendee attendee) {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDEE_EVENT_ID,attendee.eventID);
        contentValues.put(KEY_ATTENDEE_NAME,attendee.name);
        contentValues.put(KEY_ATTENDEE_TOTAL,attendee.total);
        contentValues.put(KEY_ATTENDEE_PAID,attendee.paid);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Attendee.TABLE, contentValues,KEY_ATTENDEE_ID + "= ?", new String[] { String.valueOf(attendee.serial) });

        Log.d(LOG_TAG,"Attendee updated "+attendee.name+" "+attendee.toString());
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection
    }

    public static Attendee getAttendeeByID(int ID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ATTENDEE_ID + "," +
                KEY_ATTENDEE_EVENT_ID + "," +
                KEY_ATTENDEE_NAME + "," +
                KEY_ATTENDEE_TOTAL + "," +
                KEY_ATTENDEE_PAID +
                " FROM " + Attendee.TABLE
                + " WHERE " +
                KEY_ATTENDEE_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        Attendee attendee=null;
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                attendee=new Attendee();
                attendee.serial=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_ID));
                attendee.eventID=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_EVENT_ID));
                attendee.name=cursor.getString(cursor.getColumnIndex(KEY_ATTENDEE_NAME));
                attendee.total=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_TOTAL));
                attendee.paid=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_PAID));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return attendee;
    }

    public static Attendee getAttendeeByName(int eventID,String name)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ATTENDEE_ID + "," +
                KEY_ATTENDEE_EVENT_ID + "," +
                KEY_ATTENDEE_NAME + "," +
                KEY_ATTENDEE_TOTAL + "," +
                KEY_ATTENDEE_PAID +
                " FROM " + Attendee.TABLE
                + " WHERE " +
                KEY_ATTENDEE_NAME + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(name) } );
        Attendee attendee=null;
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                int eID=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_EVENT_ID));
                if(eID!=eventID)
                    continue;
                attendee=new Attendee();
                attendee.serial=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_ID));
                attendee.eventID=eID;
                attendee.name=cursor.getString(cursor.getColumnIndex(KEY_ATTENDEE_NAME));
                attendee.total=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_TOTAL));
                attendee.paid=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_PAID));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return attendee;

    }


    public static ArrayList<String> getAllAttendees()
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ATTENDEE_NAME +
                " FROM " + Attendee.TABLE;

        //Student student = new Student();
        ArrayList<String> attendeeList = new ArrayList<String>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(KEY_ATTENDEE_NAME));
                attendeeList.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return attendeeList;
    }

    public static ArrayList<Attendee> getAttendeesByEvent(int eventID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_ATTENDEE_ID + "," +
                KEY_ATTENDEE_EVENT_ID + "," +
                KEY_ATTENDEE_NAME + "," +
                KEY_ATTENDEE_TOTAL + "," +
                KEY_ATTENDEE_PAID +
                " FROM " + Attendee.TABLE
                + " WHERE " +
                KEY_ATTENDEE_EVENT_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        //Student student = new Student();
        ArrayList<Attendee> attendeeList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(eventID) } );
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Attendee attendee=new Attendee();
                attendee.serial=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_ID));
                attendee.eventID=cursor.getInt(cursor.getColumnIndex(KEY_ATTENDEE_EVENT_ID));
                attendee.name=cursor.getString(cursor.getColumnIndex(KEY_ATTENDEE_NAME));
                attendee.total=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_TOTAL));
                attendee.paid=cursor.getDouble(cursor.getColumnIndex(KEY_ATTENDEE_PAID));
                attendeeList.add(attendee);

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return attendeeList;
    }
}
