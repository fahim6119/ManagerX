package arefin.Database;


import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import arefin.app;

public class dbHelp extends SQLiteOpenHelper{
    public static final String db_name = "MenuAssistant.db";

    private static final int db_version = 1;

    public dbHelp()
    {
        super(app.getContext(), db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i("Arefin","Started Creating DB");
        try
        {
            db.execSQL(EventDB.createTable());
        }
        catch (Exception e)
        {
            Log.i("Arefin","Exception while creating DB");
        }
        Log.i("Arefin","Created DB");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+Event.TABLE);
        onCreate(db);
    }


}
