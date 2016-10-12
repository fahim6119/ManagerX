package arefin.Database;


import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import arefin.app;

public class dbHelp extends SQLiteOpenHelper{
    public static final String db_name = "MenuAssistant.db";
    final static String LOG_TAG = "AREFIN";

    private static final int db_version = 1;

    public dbHelp()
    {
        super(app.getContext(), db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(LOG_TAG,"Started Creating DB");
        try
        {
            db.execSQL(EventDB.createTable());
            db.execSQL(AttendeeDB.createTable());
            db.execSQL(MenuItemDB.createTable());
            db.execSQL(OrderDB.createTable());
        }
        catch (Exception e)
        {
            Log.i(LOG_TAG,"Exception while creating DB");
        }
        Log.i(LOG_TAG,"Created DB");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+Event.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+Attendee.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+MenuItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+Order.TABLE);
        onCreate(db);
    }


}
