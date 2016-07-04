package arefin;

/**
 * Created by Arefin on 03-Jul-16.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.arefin.menuList.R;

import arefin.dialogs.fragment.ListDialogFragment;
import arefin.dialogs.iface.IMultiChoiceListDialogListener;

public class OrderFragment extends Fragment implements View.OnClickListener {
    ListView listView;
    Button addButton,removeButton;
    String uName;
    TextView fragment_detail;
    View rootView;
    int frag_id;
    int add_id,remove_id;
    int[] selections;

    int itemNum;
    int[] priceList;
    String[] descList,users,orders;
    List<String> userlist;
    ArrayList<ArrayList<String>> orderer;
    String[] newUsers;
    public static OrderFragment newInstance() {
        OrderFragment cFragment = new OrderFragment();
        return cFragment;
    }

    public OrderFragment() {
    }

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listOrders=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.collection_list, container, false);
        retrieve_sharedArray();
        listOrders=getArguments().getStringArrayList("orders");
        orders=new String[listOrders.size()];
        orders = listOrders.toArray(orders);
        frag_id=getArguments().getInt("fragId");
        fragment_detail=(TextView)rootView.findViewById(R.id.frag_detail_view);
        fragment_detail.setText("Ordered By "+ orders.length+ ", Price "+priceList[frag_id]);
        listView=(ListView)rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listOrders);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        clickCounter=0;

        addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        removeButton = (Button) rootView.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(this);

        return rootView;

    }

    public void dummy()
    {
        Log.i("fahimOrder","Call R");
    }


    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int choice) {

        Log.i("fahimOrder","Call Received ");
        ArrayList<String> newMembers=new ArrayList<>(selectedPositions.length);

        //if Add
        if(choice==1)
        {
            for(int i : selectedPositions)
            {
                newMembers.add(newUsers[i]);
                Log.i("fahimOrder","Add order "+newUsers[i]);
            }
            addOrder(newMembers);
        }

        if(choice==2)
        {
            for(int i : selectedPositions)
            {
                newMembers.add(orders[i]);
                Log.i("fahimOrder","Remove order "+orders[i]);
            }
            removeOrder(newMembers);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_button:
                add_member();
                Log.i("fahimOrder","member add request");
                break;
            case R.id.remove_button:
                Log.i("fahimOrder","member remove request");
                remove_member();
                break;
        }
    }

    public void add_member()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        Set<String> userSet = preferences.getStringSet("users", null);

        //updateUserList
        userlist = new ArrayList<String>(userSet);
        users=new String[userlist.size()];
        for (int i = 0; i < userlist.size(); i++) {
            users[i]=userlist.get(i);
        }

        //Find Remaining Users
        Set<String> orderSet = new HashSet<String>();
        orderSet.addAll(listOrders);
        userSet.removeAll(orderSet);
        ArrayList<String> newUserlist = new ArrayList<String>(userSet);
        Collections.sort(newUserlist, String.CASE_INSENSITIVE_ORDER);
        newUsers=new String[newUserlist.size()];
        for (int i = 0; i < newUserlist.size(); i++) {
            newUsers[i]=newUserlist.get(i);
        }
        ListDialogFragment frag=new arefin.dialogs.fragment.ListDialogFragment();
        int code=frag_id*10 +1 ; //1 for add
        frag.createBuilder(getActivity().getBaseContext(), getActivity().getSupportFragmentManager())
                .setTitle("Add orders for Item "+(frag_id+1))
                .setItems(newUsers)
                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                .setRequestCode(code)
                .setCheckedItems(selections)
                .show();
    }

    public void remove_member()
    {
        ListDialogFragment frag=new arefin.dialogs.fragment.ListDialogFragment();

        int code=frag_id*10 +2 ; //2 for remove
        frag.createBuilder(getActivity().getBaseContext(), getActivity().getSupportFragmentManager())
                .setTitle("Remove orders for Item "+(frag_id+1))
                .setItems(orders)
                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                .setRequestCode(code)
                .show();
    }

    public void removeOrder(ArrayList<String> userSet)
    {
        //Update Order List for this item
        listOrders.removeAll(userSet);
        updateList();
        Snackbar.make(rootView,"Order from "+ userSet.size()+" people Removed", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void addOrder(ArrayList<String> userSet)
    {
        //Update Order List for this item
        int counter=userSet.size();
        for(int i=0;i<counter;i++)
            listOrders.add(userSet.get(i));
        updateList();
        Snackbar.make(rootView,"New order from "+ counter +" people accepted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void updateList()
    {
        //Update Shared Preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(listOrders);
        editor.putStringSet("menu_" + frag_id, set);
        editor.apply();

        //Update local variables & UI
        orders=new String[listOrders.size()];
        orders = listOrders.toArray(orders);
        fragment_detail.setText("Ordered By "+ listOrders.size()+ ", Price "+priceList[frag_id]);
        adapter.notifyDataSetChanged();
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

        /*
        String savedString = preferences.getString("selected_"+frag_id, null);
        StringTokenizer st = new StringTokenizer(savedString, ",");
        selections = new int[listOrders.size()];
        for (int i = 0; i < listOrders.size(); i++)
        {
            selections[i] = Integer.parseInt(st.nextToken());
        }
        */
    }
}
