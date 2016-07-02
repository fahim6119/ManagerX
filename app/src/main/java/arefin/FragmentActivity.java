package arefin;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawer;
    int itemNum;
    int[] priceList;
    String[] descList,users;
    List<String> userlist;
    ArrayList<ArrayList<String>> orderer;
    LinearLayout.LayoutParams lp;
    LinearLayout fragLayout;
    Button[] menuOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        retrieve_sharedArray();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        ViewGenerator();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void ViewGenerator() {
       fragLayout = (LinearLayout) findViewById(R.id.fragment_layout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 10, 5, 10);
        int item=1;
        int itemOrders=orderer.get(item).size();
        menuOrders = new Button[itemOrders];
        fragLayout.removeAllViews();
        for (int l = 0; l < itemOrders; l++) {
            menuOrders[l] = new Button(this);
            menuOrders[l].setTextSize(15);
            menuOrders[l].setLayoutParams(lp);
            menuOrders[l].setId(l);
            //menuOrders[l].setTextColor(getResources().getColor(R.color.textGray));
            //menuOrders[l].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_menu));

            menuOrders[l].setTextColor(getResources().getColor(R.color.mainText));
            menuOrders[l].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_main));
            menuOrders[l].setWidth(0);
            menuOrders[l].setText(orderer.get(1).get(l));
            Log.i("fahim","order for item 2 "+ orderer.get(1).get(l));
            fragLayout.addView(menuOrders[l]);
        }
    }

    public void retrieve_sharedArray()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        itemNum = preferences.getInt("itemNum", 0);
        descList = new String[itemNum];
        orderer = new ArrayList<>();
        for(int i=0; i<itemNum; i++)
            descList[i]=preferences.getString("desc_" + i, null);
        priceList=new int[itemNum];
        for(int i=0; i<itemNum; i++)
            priceList[i]=Integer.parseInt(preferences.getString("price_" + i, null));

        if (preferences.contains("users")) {
            Set<String> set = preferences.getStringSet("users", null);
            userlist = new ArrayList<String>(set);
            Collections.sort(userlist, String.CASE_INSENSITIVE_ORDER);
            users=new String[userlist.size()];
            for (int i = 0; i < userlist.size(); i++) {
                users[i]=userlist.get(i);
            }
        }

        for(int l=0;l<itemNum;l++)
        {
            Set<String> set = preferences.getStringSet("menu_"+l, null);
            orderer.add(new ArrayList<String>(set));
            Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;*/

            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager=getFragmentManager();
        if (id == R.id.nav_about) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame,new FirstFragment()).commit();
        } else if (id == R.id.nav_feedback) {
            // fragmentManager.beginTransaction().replace(R.id.content_frame,new SecondFragment()).commit();

        } else if (id == R.id.nav_rate) {
            // fragmentManager.beginTransaction().replace(R.id.content_frame,new ThirdFragment()).commit();

        }else if (id == R.id.nav_history) {

        }
        else if (id == R.id.nav_members) {

        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
