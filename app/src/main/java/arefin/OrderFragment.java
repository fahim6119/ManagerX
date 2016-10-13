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

import android.os.Bundle;
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

import com.batfia.arefin.ManagerX.R;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.Database.MenuItem;
import arefin.Database.MenuItemDB;
import arefin.Database.Order;
import arefin.Database.OrderDB;
import arefin.dialogs.fragment.ListDialogFragment;

public class OrderFragment extends Fragment implements View.OnClickListener {

    int eventID,itemID;
    onOrdered mCallback;
    ListView listView;
    ImageButton addButton, removeButton;
    String uName;
    TextView fragment_detail;
    View rootView;
    int frag_id;
    int add_id, remove_id;
    int[] selections;

    int itemNum;
    double[] priceList;
    String[] descList, users, orders;
    ArrayList<Order> orderList;
    List<String> userlist;
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
        frag_id = getArguments().getInt("fragId");
        eventID=app.currentEventID;
        itemID=getArguments().getInt("ItemSerial");

        retrieve_sharedArray();
        listOrders = new ArrayList<>();
        for(int i=0;i<orderList.size();i++)
        {
            int userID=orderList.get(i).attendeeID;
            String name=AttendeeDB.getAttendeeByID(userID).name;
            listOrders.add(name);
        }
        orders = new String[listOrders.size()];
        listOrders.toArray(orders);
        Log.i("checkLog","for fragment "+frag_id+" orders "+orderList.size());
        fragment_detail = (TextView) rootView.findViewById(R.id.frag_detail_view);
        fragment_detail.setText(descList[frag_id]+"(" + orders.length + "), Price " + priceList[frag_id]);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listOrders);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        clickCounter = 0;

        for(int i=0;i<orderList.size();i++)
        {
            if(orderList.get(i).served==1)
            {
                Log.i("checkLog","set selection for "+orderList.get(i).serial);
                listView.setItemChecked(i,true);
            }
        }
        addButton = (ImageButton) rootView.findViewById(R.id.imageButtonAdd);
        addButton.setOnClickListener(this);

        removeButton = (ImageButton) rootView.findViewById(R.id.imageButtonRemove);
        removeButton.setOnClickListener(this);

        try {
            mCallback = (onOrdered) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }

        return rootView;

    }



    // Container Activity must implement this interface
    public interface onOrdered{
        public void onitemOrdered(String name,int fragment_id,Order order);
        public void onitemRemoved(String name,int fragment_id,int orderID);
    }

    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int choice) {
        Log.i("checkLog","List Item Selected");
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

    public void backUpSelected()
    {
        if(listView==null)
            return;
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        int size = checked.size(); // number of name-value pairs in the array
        for (int i = 0; i < size; i++) {
            int key = checked.keyAt(i);
            boolean value = checked.get(key);
            Order order=orderList.get(key);
            if (value && order.served==0)
            {
                orderList.get(key).served=1;
                OrderDB.update(orderList.get(key));
            }
            else if(value==false && order.served==1)
            {
                orderList.get(key).served = 0;
                OrderDB.update(orderList.get(key));
            }
            Log.i("checkLog",frag_id+" "+i+" Checked "+key+" "+value);
        }
        Log.i("FahimOrders","New back up for fragment " +frag_id+" sent");
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
        ArrayList<String> newUserlist = new ArrayList<String>();
        newUserlist.addAll(userlist);
        newUserlist.removeAll(listOrders);
        newUsers=new String[newUserlist.size()];
        for (int i = 0; i < newUserlist.size(); i++) {
            newUsers[i]=newUserlist.get(i);
        }
        ListDialogFragment frag=new arefin.dialogs.fragment.ListDialogFragment();
        int code=frag_id*10 +1 ; //1 for add
        frag.createBuilder(getActivity().getBaseContext(), getActivity().getSupportFragmentManager())
                .setTitle("Add orders for "+descList[frag_id])
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
        for(int i=0;i<counter;i++) {
            listOrders.add(userSet.get(i));
            int attendeeID=AttendeeDB.getAttendeeByName(eventID,userSet.get(i)).serial;
            Order order=new Order(eventID,itemID,attendeeID,0,1);
            mCallback.onitemOrdered(userSet.get(i),frag_id,order);
        }
        updateList();
        if(counter!=0)
            Snackbar.make(rootView,"New order from "+ counter +" people accepted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void remove_member()
    {
        ListDialogFragment frag=new arefin.dialogs.fragment.ListDialogFragment();
        Log.i("checkLog",frag_id+"List of Orders "+ listOrders.get(0)+" "+listOrders.get(1));
        int code=frag_id*10 +2 ; //2 for remove

        frag.createBuilder(getActivity().getBaseContext(), getActivity().getSupportFragmentManager())
                .setTitle("Remove orders for "+descList[frag_id])
                .setItems(orders)
                .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE)
                .setRequestCode(code)
                .show();
    }

    public void removeOrder(ArrayList<String> userSet)
    {
        //Update Order List for this item
        for(int i=0;i<userSet.size();i++)
        {
            int index=listOrders.indexOf(userSet.get(i));
            if(index==-1)
                return;
            mCallback.onitemRemoved(userSet.get(i), frag_id,orderList.get(index).serial );
        }
        listOrders.removeAll(userSet);
        updateList();
        if(userSet.size()!=0)
            Snackbar.make(rootView,"Order from "+ userSet.size()+" people Removed", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void updateList()
    {
        //Update local variables & UI
        orders=new String[listOrders.size()];
        listOrders.toArray(orders);
        fragment_detail.setText("Ordered By "+ listOrders.size()+ ", Price "+priceList[frag_id]);
        adapter.notifyDataSetChanged();
    }

    public void retrieve_sharedArray()
    {
        orderList= OrderDB.getOrdersByItem(eventID,itemID);
        Log.i("checkLog",eventID+ " for Item "+itemID+" orders "+listOrders.size());
        ArrayList<MenuItem> menuItemList= MenuItemDB.getItemsByEvent(eventID);
        itemNum = menuItemList.size();
        descList = new String[itemNum];
        priceList=new double[itemNum];
        for(int i=0; i<itemNum; i++)
        {
            descList[i] = menuItemList.get(i).description;
            priceList[i] = menuItemList.get(i).price;
        }
        userlist=new ArrayList<>();
        ArrayList<Attendee> attendeeList= AttendeeDB.getAttendeesByEvent(eventID);
        for(int i=0;i<attendeeList.size();i++)
        {
            userlist.add(attendeeList.get(i).name);
        }

        users=new String[userlist.size()];
        for (int i = 0; i < userlist.size(); i++) {
            users[i]=userlist.get(i);
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
