package arefin;

import android.app.Application;
import android.content.Context;

import arefin.Database.DatabaseManager;
import arefin.Database.dbHelp;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class app extends Application {
    private static Context context;
    private static dbHelp dbHelper;
    public static int currentEventID;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new dbHelp();
        DatabaseManager.initializeInstance(dbHelper);
    }

    public static Context getContext(){
        return context;
    }
}
