package arefin.Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class OrderDB
{
    final static String LOG_TAG="AREFIN";
    public static final String KEY_ORDER_MENU_ID = "menu_id";  //Menu Item
    public static final String KEY_ORDER_ATTENDEE_ID = "Ordered";    //Attendee ID
    public static final String KEY_ORDER_SERVED = "Served";   //Served or Not
    public static final String KEY_ORDER_QUANTITY = "Quantity"; //Number of orders by a person


    //ItemNumber, Ordered by, Served, Quantity
    public void createOrderTable(SQLiteDatabase db, String Table)
    {

        db.execSQL("create table " + Table + "( "
                +"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORDER_MENU_ID+" INTEGER , "
                + KEY_ORDER_ATTENDEE_ID+" TEXT , "
                + KEY_ORDER_SERVED+" INTEGER, "
                + KEY_ORDER_QUANTITY+" INTEGER "
                +")");
    }

    public int insertOrder(Order order)
    {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ORDER_MENU_ID,order.item);
        contentValues.put(KEY_ORDER_ATTENDEE_ID,order.attendee);
        contentValues.put(KEY_ORDER_SERVED,order.served);
        contentValues.put(KEY_ORDER_QUANTITY,order.quantity);
        long row = db.insert(Order.TABLE,null,contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection

        Log.d(LOG_TAG,"Order Added"+row);
        return (int) row;
    }
}
