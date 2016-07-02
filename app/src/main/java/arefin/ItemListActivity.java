package arefin;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IMultiChoiceListDialogListener;

public class ItemListActivity extends AppCompatActivity implements
        IMultiChoiceListDialogListener {
    boolean firstTimeStartup[];
    int[][] selected;
    int itemNum;
    int[] priceList;
    String[] descList,users;
    LinearLayout.LayoutParams lp;
    LinearLayout menuLayout;
    Button[] menuItems;
    List<String> userlist;
    ArrayList<ArrayList<String>> orderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        retrieve_sharedArray();
        selected=new int[itemNum][itemNum];
        firstTimeStartup=new boolean[itemNum];
        /*
        Bundle b=this.getIntent().getExtras();
        descList=b.getStringArray("descList");
        itemNum=b.getInt("itemNum");
        priceList=b.getIntArray("priceList"); */


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("users")) {
            Set<String> set = preferences.getStringSet("users", null);
            userlist = new ArrayList<String>(set);
            Collections.sort(userlist, String.CASE_INSENSITIVE_ORDER);
            users=new String[userlist.size()];
            for (int i = 0; i < userlist.size(); i++) {
                users[i]=userlist.get(i);
                Log.i("fahim", i + " " + users[i]);
            }
        }

        getSupportActionBar().setTitle("Set Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewGenerator();

        orderer= new ArrayList<>();
        for( int i = 0; i < itemNum; i++) {
            orderer.add(new ArrayList<String>());
        }

    }


    public void retrieve_sharedArray()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        itemNum = preferences.getInt("itemNum", 0);
        descList = new String[itemNum];
        for(int i=0; i<itemNum; i++)
            descList[i]=preferences.getString("desc_" + i, null);
        priceList=new int[itemNum];
        for(int i=0; i<itemNum; i++)
            priceList[i]=Integer.parseInt(preferences.getString("price_" + i, null));
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
                    Log.i("fahim","Button Pressed "+ v.getId());
                }
            });
            menuLayout.addView(menuItems[l]);
        }
    }


    @Override
    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode) {
        selected[requestCode]=selectedPositions;
        firstTimeStartup[requestCode]=false;
        for(int i=0;i<selectedPositions.length;i++)
            Log.i("fahim","Selected "+i+" "+selected[0][i]);
        for(int i : selectedPositions){
            String selected=users[i];
            menuGenerator(requestCode,selected);
        }
    }


    public void menuGenerator(int item, String name)
    {
        orderer.get(item).add(name);
        Log.i("fahim","Item "+item+ " name "+name);
    }
}
