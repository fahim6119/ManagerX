package com.example.asus1.menuList;


import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

class dbHelp extends SQLiteOpenHelper{
    public static final String db_name = "Collection.db";
    public static final String table = "Pahela_Collection";
    public static final String col1 = "ID";
    public static final String col2 = "Roll";

    private static final int db_version = 1;

    dbHelp (Context context,String name)
    {
        super(context, db_name, null, db_version);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + table + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ROLL INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+table);
        onCreate(db);
    }
    public boolean insertData(String roll )
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2,roll);
        long res = db.insert(table,null,contentValues);
        if(res == -1)
            return false;
        else return true;
    }
    public Cursor getData()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+table,null);
        return cursor;
    }
}
