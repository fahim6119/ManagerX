package arefin;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import arefin.dialogs.iface.IMultiChoiceListDialogListener;

public class FragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,IMultiChoiceListDialogListener {

    DrawerLayout drawer;
    int itemNum;
    int[] priceList;
    String[] descList,users;
    List<String> userlist;
    public ArrayList<ArrayList<String>> orderer;
    OrderFragment fragments[];
    int[] fragment_id;
    Adapter adapter;


    @Override
    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int choice) {

        Log.i("fahimOrder","Items Selected ");
        int check,frag;
        check=choice%10;
        frag=(choice-check)/10;
        fragments[frag].onListItemsSelected(values,selectedPositions,check);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        retrieve_sharedArray();
        fragments=new OrderFragment[itemNum];
        fragment_id=new int[itemNum];
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setItemIconTintList(null);

        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onBackPressed()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        for(int i=0;i<itemNum;i++)
        {
            ArrayList<Integer> selection=fragments[i].backUpSelected();
            //When a fragment hasn't been opened, don't change the current preference
            StringBuilder str = new StringBuilder();
            for (int k = 0; k < selection.size(); k++) {
                str.append(selection.get(k)).append(",");
            }
            editor.putString("selected_"+i, str.toString());
            editor.apply();
        }
        Intent i = new Intent(FragmentActivity.this, StartActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
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
    public void onResume()
    {
        super.onResume();
        Log.i("FahimOrders","Resume");
        for(int i=0;i<itemNum;i++)
            fragments[i].updateView(i);
        adapter.notifyDataSetChanged();
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
        if (id == R.id.nav_about)
        {
            Intent createIntent = new Intent(FragmentActivity.this, CreditsActivity.class);
            startActivity(createIntent);
        }
        else if (id == R.id.nav_feedback) {
            drawer.closeDrawers();
            Uri uri = Uri.parse("https://fahim6119.wordpress.com/contact/"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else if (id == R.id.nav_exit) {
            finish();
        }
        else if (id == R.id.nav_history) {

        }
        else if (id == R.id.nav_create) {
            Intent createIntent = new Intent(FragmentActivity.this, CreateActivity.class);
            startActivity(createIntent);
        }
        else if (id == R.id.nav_members)
        {
            Intent createIntent = new Intent(FragmentActivity.this, AttendanceActivity.class);
            startActivity(createIntent);
        }

        else if (id == R.id.nav_items)
        {
            Intent createIntent = new Intent(FragmentActivity.this, MenuCreatorActivity.class);
            startActivity(createIntent);
        }

        else if (id == R.id.nav_order)
        {
            Intent createIntent = new Intent(FragmentActivity.this, ItemListActivity.class);
            startActivity(createIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        for(int i=0;i<itemNum;i++) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("orders", orderer.get(i));
            bundle.putInt("fragId",i);
            fragments[i]=new OrderFragment();
            adapter.addFragment(fragments[i], "Item" + (i + 1));
            adapter.getItem(i).setArguments(bundle);
        }
        viewPager.setAdapter(adapter);
    }

}
