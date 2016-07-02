package arefin;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemListActivity extends AppCompatActivity{

    int itemNum;
    int[] priceList;
    String[] descList;
    LinearLayout.LayoutParams lp;
    LinearLayout menuLayout;
    TextView[] menuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Bundle b=this.getIntent().getExtras();
        descList=b.getStringArray("descList");
        itemNum=b.getInt("itemNum");
        priceList=b.getIntArray("priceList");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("users")) {
            Set<String> set = preferences.getStringSet("users", null);
            List<String> list = new ArrayList<String>(set);
            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

            for (int i = 0; i < list.size(); i++) {
                Log.i("fahim", i + " " + list.get(i));
            }
        }
        getSupportActionBar().setTitle("Set Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewGenerator();

    }

    public void ViewGenerator()
    {
        menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,    LinearLayout.LayoutParams.WRAP_CONTENT);
        menuItems=new TextView[itemNum];
        menuLayout.removeAllViews();
        for(int l=0; l<itemNum; l++)
        {
            menuItems[l] = new TextView(this);
            menuItems[l].setTextSize(15);
            menuItems[l].setLayoutParams(lp);
            menuItems[l].setId(l);
            menuItems[l].setText("Item " +(l + 1)+" Price "+priceList[l] );
            menuItems[l].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /////Do some job on button click
                    Log.i("fahim","Button Pressed "+ v.getId());
                }
            });
            menuLayout.addView(menuItems[l]);
        }
    }



    public void menuGenerator(View v)
    {
        int btnId=v.getId();

    }


}
