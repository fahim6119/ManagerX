package arefin;

/**
 * Created by Arefin on 03-Jul-16.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.arefin.menuList.R;

public class OrderFragment extends Fragment {
    ListView listView;
    Button ClickMe;
    String uName;
    TextView totalCollection;
    View rootView;

    int itemNum;
    int[] priceList;
    String[] descList,users;
    List<String> userlist;
    ArrayList<ArrayList<String>> orderer;
    public static OrderFragment newInstance() {
        OrderFragment cFragment = new OrderFragment();
        return cFragment;
    }

    public OrderFragment() {
    }

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter;

    public void updateList(String uName)
    {
        clickCounter++;
        totalCollection.setText("Collected from: "+clickCounter);
        listItems.add(uName);
        adapter.notifyDataSetChanged();
        Snackbar.make(rootView,"payment from "+ uName +" accepted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.collection_list, container, false);
        retrieve_sharedArray();
        listItems=new ArrayList<>(userlist);
        listView=(ListView)rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        totalCollection=(TextView)rootView.findViewById(R.id.totalCollection);
        clickCounter=0;

        /*
        ClickMe = (Button) rootView.findViewById(R.id.addBtn);
        uName="Fahim";

        ClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.add(uName+" : "+clickCounter++);
                adapter.notifyDataSetChanged();
            }
        }); */

        return rootView;

    }

    public void retrieve_sharedArray()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
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
}
