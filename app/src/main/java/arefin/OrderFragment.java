package arefin;

/**
 * Created by Arefin on 03-Jul-16.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.batfia.arefin.MenuAssistant.R;

import arefin.dialogs.fragment.ListDialogFragment;

public class OrderFragment extends Fragment implements View.OnClickListener {
    ListView listView;
    ImageButton addButton, removeButton;
    String uName;
    TextView fragment_detail;
    View rootView;
    int frag_id;
    int add_id, remove_id;
    int[] selections;

    int itemNum;
    int[] priceList;
    String[] descList, users, orders;
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
    ArrayList<String> listOrders = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        retrieve_sharedArray();
        listOrders = getArguments().getStringArrayList("orders");
        orders = new String[listOrders.size()];
        orders = listOrders.toArray(orders);
        frag_id = getArguments().getInt("fragId");
        fragment_detail = (TextView) rootView.findViewById(R.id.frag_detail_view);
        fragment_detail.setText("Ordered By " + orders.length + ", Price " + priceList[frag_id]);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listOrders);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        clickCounter = 0;

        addButton = (ImageButton) rootView.findViewById(R.id.imageButtonAdd);
        addButton.setOnClickListener(this);

        removeButton = (ImageButton) rootView.findViewById(R.id.imageButtonRemove);
        removeButton.setOnClickListener(this);

        return rootView;

    }

    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int choice) {

        ArrayList<String> newMembers = new ArrayList<>(selectedPositions.length);

        //if Add
        if (choice == 1) {
            for (int i : selectedPositions) {
                newMembers.add(newUsers[i]);
            }
            addOrder(newMembers);
        }

        if (choice == 2) {
            for (int i : selectedPositions) {
                newMembers.add(orders[i]);
            }
            removeOrder(newMembers);
        }
    }

    public void updateView(int fragment_id)
    {
        if(getActivity()!=null)
        {
            retrieve_sharedArray();
        }
        Log.i("FahimOrders","For Fragment "+fragment_id + " selection begins");
        if(selections!=null && fragment_id==frag_id)
        {
            for(int i:selections) {
                listView.setItemChecked(i,true);
            }
        }
    }

    public ArrayList<Integer> backUpSelected()
    {
        ArrayList<Integer> selectedArray=new ArrayList();
        if(listView==null)
            return selectedArray;
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        int size = checked.size(); // number of name-value pairs in the array
        if(size==0) {
            retrieve_sharedArray();
            for(int i=0;selections!=null && i<selections.length;i++)
            {
                selectedArray.add(selections[i]);
            }
            Log.i("FahimOrders","Old back up for fragment " +frag_id+" sent");
        }
        else {
            for (int i = 0; i < size; i++) {
                int key = checked.keyAt(i);
                boolean value = checked.get(key);
                if (value) {
                    selectedArray.add(key);
                }
            }
            Log.i("FahimOrders","New back up for fragment " +frag_id+" sent");
        }

        return selectedArray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageButtonAdd:
                add_member();
                Log.i("fahimOrder","member add request");
                break;
            case R.id.imageButtonRemove:
                Log.i("fahimOrder","member remove request");
                remove_member();
                break;
        }
    }


    public void add_member()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        Set<String> userSet = preferences.getStringSet("users", null);
        Set<String> newUserSet=new HashSet<String>();
        newUserSet.addAll(userSet);
        //updateUserList
        userlist = new ArrayList<String>(userSet);

        users=new String[userlist.size()];
        for (int i = 0; i < userlist.size(); i++) {
            users[i]=userlist.get(i);
        }

        //Find Remaining Users
        Set<String> orderSet = new HashSet<String>();
        orderSet.addAll(listOrders);
        newUserSet.removeAll(orderSet);
        ArrayList<String> newUserlist = new ArrayList<String>(newUserSet);
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

    public void addOrder(ArrayList<String> userSet)
    {
        //Update Order List for this item
        int counter=userSet.size();
        for(int i=0;i<counter;i++)
            listOrders.add(userSet.get(i));
        updateList();
        if(counter!=0)
            Snackbar.make(rootView,"New order from "+ counter +" people accepted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        if(userSet.size()!=0)
            Snackbar.make(rootView,"Order from "+ userSet.size()+" people Removed", Snackbar.LENGTH_LONG)
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
        Log.i("FahimOrders","Retrieving Preferences for Fragment "+frag_id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        if(preferences.contains("name")==false)
        {
            Toast.makeText(getActivity().getBaseContext(), "No records Available",
                    Toast.LENGTH_LONG).show();
            return;
        }
        itemNum = preferences.getInt("itemNum", 0);
        descList = new String[itemNum];
        orderer = new ArrayList<>();
        for(int i=0; i<itemNum; i++)
            descList[i]=preferences.getString("desc_" + i, null);
        priceList=new int[itemNum];
        for(int i=0; i<itemNum; i++)
            priceList[i]=preferences.getInt("price_" + i, 0);

        for(int l=0;l<itemNum;l++)
        {
            Set<String> set = preferences.getStringSet("menu_"+l, null);
            orderer.add(new ArrayList<String>(set));
            Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
        }

        String savedString = preferences.getString("selected_"+frag_id, null);
        Log.i("FahimOrders","selected for " +frag_id+ " "+savedString);
        //StringTokenizer st = new StringTokenizer(savedString, ",");
        //selections = new int[listOrders.size()];
        if(savedString!=null) {
            if (savedString.equals(""))
                selections = null;
            else {
                List<String> items = new ArrayList<>(Arrays.asList(savedString.split(",")));
                selections = new int[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    selections[i] = Integer.parseInt(items.get(i));
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        updateView(frag_id);
        //INSERT CUSTOM CODE HERE
    }

}
