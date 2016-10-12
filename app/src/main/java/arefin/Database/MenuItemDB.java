package arefin.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class MenuItemDB {
    final static String LOG_TAG = "AREFIN";

    public static final String KEY_MENU_ID = "id"; //Item Number
    public static final String KEY_MENU_EVENT_ID = "event_id"; //Item Number
    public static final String KEY_MENU_DESCRIPTION = "description"; //Details of Item
    public static final String KEY_MENU_PRICE = "price";   //Price of Item

    //ItemNumber, Description, Details
    public String createMenuTable() {
        String CREATE_TABLE = "create table " + MenuItem.TABLE + "( "
                + KEY_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MENU_EVENT_ID + " INTEGER, "
                + KEY_MENU_DESCRIPTION + " TEXT , "
                + KEY_MENU_PRICE + " REAL "
                + ")";
        return CREATE_TABLE;
    }

    public int insertMenu(MenuItem menuItem) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MENU_EVENT_ID,menuItem.eventID);
        contentValues.put(KEY_MENU_DESCRIPTION, menuItem.description);
        contentValues.put(KEY_MENU_PRICE, menuItem.price);
        long row = db.insert(MenuItem.TABLE, null, contentValues);
        DatabaseManager.getInstance().closeDatabase(); // Closing database connection
        Log.d(LOG_TAG, "MenuItem Added" + row);
        return (int) row;
    }

    public ArrayList<MenuItem> getItemsByEvent(int eventID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_MENU_ID + "," +
                KEY_MENU_EVENT_ID + "," +
                KEY_MENU_DESCRIPTION + "," +
                KEY_MENU_PRICE +
                " FROM " + MenuItem.TABLE
                + " WHERE " +
                KEY_MENU_EVENT_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string


        //Student student = new Student();
        ArrayList<MenuItem> itemList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(eventID) } );
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                MenuItem menuItem=new MenuItem();

                menuItem.serial=cursor.getInt(cursor.getColumnIndex(KEY_MENU_ID));
                menuItem.eventID=cursor.getInt(cursor.getColumnIndex(KEY_MENU_EVENT_ID));
                menuItem.description=cursor.getString(cursor.getColumnIndex(KEY_MENU_DESCRIPTION));
                menuItem.price=cursor.getDouble(cursor.getColumnIndex(KEY_MENU_PRICE));

                itemList.add(menuItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return itemList;
    }

    public MenuItem getItemByID(int eventID,int ID)
    {
        //Open connection to read only
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  "SELECT  " +
                KEY_MENU_ID + "," +
                KEY_MENU_EVENT_ID + "," +
                KEY_MENU_DESCRIPTION + "," +
                KEY_MENU_PRICE +
                " FROM " + MenuItem.TABLE
                + " WHERE " +
                KEY_MENU_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        MenuItem menuItem=new MenuItem();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                int eID=cursor.getInt(cursor.getColumnIndex(KEY_MENU_EVENT_ID));
                if(eID!=eventID)
                {
                    continue;
                }
                menuItem.serial=cursor.getInt(cursor.getColumnIndex(KEY_MENU_ID));
                menuItem.eventID=eID;
                menuItem.description=cursor.getString(cursor.getColumnIndex(KEY_MENU_DESCRIPTION));
                menuItem.price=cursor.getDouble(cursor.getColumnIndex(KEY_MENU_PRICE));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return menuItem;
    }

    public double getPriceByID(int eventID,int ID)
    {
        MenuItem menuItem=getItemByID(eventID,ID);
        return menuItem.price;
    }
}
