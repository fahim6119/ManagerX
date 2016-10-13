package arefin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.batfia.arefin.MenuAssistant.R;

import java.util.ArrayList;
import java.util.Arrays;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.Database.MenuItem;
import arefin.Database.MenuItemDB;
import arefin.Database.Order;
import arefin.Database.OrderDB;
import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IMultiChoiceListDialogListener;

public class ItemListActivity extends AppCompatActivity implements
        IMultiChoiceListDialogListener {
    boolean firstTimeStartup[];
    int[][] selected;
    int itemNum;
    double[] priceList;
    String[] descList,users;
    LinearLayout.LayoutParams lp;
    LinearLayout menuLayout;
    Button[] menuItems;
    ArrayList<MenuItem> menuItemList;
    ArrayList<Attendee> attendeeList;
    int eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        eventID=app.currentEventID;

        retrieve_sharedArray();

        selected=new int[itemNum][itemNum];
        firstTimeStartup=new boolean[itemNum];
        attendeeList= AttendeeDB.getAttendeesByEvent(eventID);
        users=new String[attendeeList.size()];
        for (int i = 0; i < attendeeList.size(); i++)
        {
            users[i]=attendeeList.get(i).name;
            Log.i("checkLog","user "+users[i]);
        }

        getSupportActionBar().setTitle("Set Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewGenerator();

    }


    public void retrieve_sharedArray()
    {
        menuItemList= MenuItemDB.getItemsByEvent(eventID);
        itemNum = menuItemList.size();
        descList = new String[itemNum];
        priceList=new double[itemNum];
        for(int i=0; i<itemNum; i++)
        {
            descList[i] = menuItemList.get(i).description;
            priceList[i] = menuItemList.get(i).price;
        }
    }

    public void ViewGenerator()
    {
        menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,    LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 20, 15, 20);
        menuItems=new Button[itemNum];
        menuLayout.removeAllViews();
        Arrays.fill(firstTimeStartup,true);
        for(int l=0; l<itemNum; l++)
        {
            menuItems[l] = new Button(this);
            menuItems[l].setTextSize(15);
            menuItems[l].setLayoutParams(lp);
            menuItems[l].setId(l);
            //menuItems[l].setTextColor(getResources().getColor(R.color.textGray));
            //menuItems[l].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_menu));

            menuItems[l].setTextColor(getResources().getColor(R.color.mainText));
            menuItems[l].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_main));

            menuItems[l].setWidth(0);
            menuItems[l].setText("Item " +(l + 1)+" Price "+priceList[l] );
            menuItems[l].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /////Do some job on button click
                    int id=v.getId();
                    ListDialogFragment frag=new arefin.dialogs.fragment.ListDialogFragment();
                    if(firstTimeStartup[id]==true) {

                        frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                                .setTitle("Menu "+(id+1)+" Ordered by ")
                                .setItems(users)
                                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                                .setRequestCode(id)
                                .show();
                    }
                    else
                    {
                        frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                                .setTitle("Menu "+(id+1)+" Ordered by ")
                                .setItems(users)
                                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                                .setCheckedItems(selected[id])
                                .setRequestCode(id)
                                .show();

                    }
                    Log.i("checkLog","Button Pressed "+ v.getId());
                }
            });
            menuLayout.addView(menuItems[l]);
        }
    }


    @Override
    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int menuItemNumber) {
        selected[menuItemNumber]=selectedPositions;
        firstTimeStartup[menuItemNumber]=false;
        for(int i : selectedPositions){
            String selected=users[i];
            menuGenerator(menuItemNumber,selected);
        }
    }


    public void menuGenerator(int item, String name)
    {
        int attendeeID=AttendeeDB.getAttendeeByName(eventID,name).serial;
        int menuID=menuItemList.get(item).serial;
        Order order=new Order(eventID,menuID,attendeeID,0,1);
        OrderDB.insertOrder(order);
        Log.i("checkLog","Order Inserted : Item "+item+ " name "+name);
    }

    public void onClickNextButton(View v)
    {
        Intent createIntent = new Intent(ItemListActivity.this,FragmentActivity.class);
       // createIntent.putExtras(b);
        startActivity(createIntent);
    }
}
