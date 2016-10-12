package arefin.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class AttendeeDB
{
    final static String LOG_TAG="AREFIN";

    public static final String KEY_ATTENDEE_ID="_id";      //Attendee ID -int
    public static final String KEY_ATTENDEE_NAME="Name";    //Name of Attendee -string
    public static final String KEY_ATTENDEE_TOTAL="Total";   //Total Bill for person -int
    public static final String KEY_ATTENDEE_PAID="Paid";    //Amount Paid by person -int

    //ID, name, total , paid
    public void createAttendeeTable(SQLiteDatabase db, String attendeeTable)
    {

        String CREATE_TABLE = "CREATE TABLE " + attendeeTable + "("
                + KEY_ATTENDEE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ATTENDEE_NAME +" TEXT , "
                + KEY_ATTENDEE_TOTAL +" INTEGER , "
                + KEY_ATTENDEE_PAID +" INTEGER  "
                + " )";

        db.execSQL(CREATE_TABLE);
    }



    public int insertAttendee(Attendee attendee)
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDEE_NAME,attendee.name);
        contentValues.put(KEY_ATTENDEE_TOTAL,attendee.total);
        contentValues.put(KEY_ATTENDEE_PAID,attendee.paid);
        long row = db.insert(Attendee.TABLE,null,contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection

        Log.d(LOG_TAG,"Attendee Added"+row);
        return (int) row;
    }

}
