package arefin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.batfia.arefin.ManagerX.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.Database.MenuItem;
import arefin.Database.MenuItemDB;
import arefin.Database.Order;
import arefin.Database.OrderDB;

/**
 * Created by Arefin on 08-Jul-16.
 */

public class PaymentFragment extends Fragment
{
    int eventID;
    ArrayList<Attendee> attendeeList;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    View rootView;

    List<String> userlist=new ArrayList<String>();
    ListView listView;
    myAdapter mAdapter;

    public PaymentFragment() { }

    public void updatetoDB()
    {
        if(attendeeList==null)
            return;
        for(int i=0;i<attendeeList.size();i++)
        {
            AttendeeDB.update(attendeeList.get(i));
        }
    }

    public void addOrder(String name, double price)
    {
        int index=userlist.indexOf(name);
        if(index==-1)
            return;
        double val=attendeeList.get(index).total+price;
        attendeeList.get(index).total=val;
        mAdapter.notifyDataSetChanged();
    }

    public void removeOrder(String name, double price)
    {
        int index=userlist.indexOf(name);
        double val=attendeeList.get(index).total-price;
        attendeeList.get(index).total=val;
        mAdapter.notifyDataSetChanged();
        Log.i("checkLog","Order received the name "+name+"  for removal "+price);
    }

    public static PaymentFragment newInstance()
    {
        PaymentFragment cFragment = new PaymentFragment();
        return cFragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventID=app.currentEventID;
        rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        attendeeList=new ArrayList<>();
        retrieve_sharedArray();
        listView = (ListView) rootView.findViewById(R.id.listPayment);
       // adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userlist);
        mAdapter=new myAdapter(getActivity().getBaseContext(), attendeeList);
        setupOrders();

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=(String) listView.getItemAtPosition(position);
                EditProfileDialog(text,position);
                Log.i("checkLog","Position "+position + " name "+text);
            }
        });
        //listview.setAdapter();
        return rootView;
    }

    public void EditProfileDialog(String oldName,int position)
    {
        final Dialog dialog = new Dialog(getActivity());
        final int pos=position;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_paid, null);
        dialog.setContentView(dialogView);

        dialog.setCancelable(true);

        Button amountButton = (Button) dialogView.findViewById(R.id.amountButton);
        final EditText amountPaid = (EditText) dialogView.findViewById(R.id.amountPaid);
        final TextView paid_textView=(TextView)dialogView.findViewById(R.id.paid_textView);
        final Button amountCancel=(Button) dialogView.findViewById(R.id.amountCancel);

        amountPaid.setHint(Double.toString(attendeeList.get(pos).paid));
        paid_textView.setText("Total Amount Paid By "+oldName);
        amountCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        amountButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                String amountStr= amountPaid.getText().toString().trim();
                if(TextUtils.isEmpty(amountStr))
                {
                    Toast.makeText(getContext(), "Invalid Amount",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount=Double.parseDouble(amountStr);
                attendeeList.get(pos).paid=amount;
                AttendeeDB.update(attendeeList.get(pos));
                mAdapter.notifyDataSetChanged();
                //updateUsername(pos,userName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void addVat(double vat,double discount)
    {
        double val=(vat-discount)*0.01;
        for(int i=0;i<attendeeList.size();i++)
        {
            double x;
            x=attendeeList.get(i).total;
            double totalVal=x+ (int) Math.round(x * val);
            attendeeList.get(i).total=totalVal;
            AttendeeDB.update(attendeeList.get(i));

        }
        mAdapter.notifyDataSetChanged();
    }

    public void retrieve_sharedArray()
    {
        userlist=new ArrayList<>();
        attendeeList= AttendeeDB.getAttendeesByEvent(eventID);
        for(int i=0;i<attendeeList.size();i++)
        {
            userlist.add(attendeeList.get(i).name);
        }
        //Update Paid and total
    }

    public void setupOrders()
    {
        ArrayList<MenuItem> menuItemList= MenuItemDB.getItemsByEvent(eventID);
        for(int i=0;i<menuItemList.size();i++)
        {
            ArrayList<Order> orderList= OrderDB.getOrdersByItem(eventID,menuItemList.get(i).serial);
            double price=menuItemList.get(i).price;
            for(int k=0;k<orderList.size();k++)
            {
                int userID=orderList.get(k).attendeeID;
                addOrder(AttendeeDB.getAttendeeByID(userID).name,price);
            }
        }
    }

}

class myAdapter extends BaseAdapter {

    Context context;
    List<Attendee> attendees;
    ArrayList<Double> due;
    private static LayoutInflater inflater = null;

    public myAdapter(Context context, List<Attendee> attendees) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.attendees=attendees;
        due=new ArrayList<>(attendees.size());
        for(int i=0;i<attendees.size();i++)
        {
            double val=attendees.get(i).total-attendees.get(i).paid;
            due.add(val);
        }
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return attendees.size();
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return attendees.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        for(int i=0;i<attendees.size();i++)
        {
            double val=attendees.get(i).total-attendees.get(i).paid;
            due.set(i,val);
        }
        Attendee attendee=attendees.get(position);
        TextView text = (TextView) vi.findViewById(R.id.username);
        text.setText(attendee.name);
        TextView paidText = (TextView) vi.findViewById(R.id.userPaid);
        paidText.setText("Paid : "+String.format( "%.2f", attendee.paid ));
        TextView dueText = (TextView) vi.findViewById(R.id.userDue);
        dueText.setText("Due : "+String.format( "%.2f", due.get(position)));
        TextView totalText = (TextView) vi.findViewById(R.id.userTotal);
        totalText.setText("Total : "+String.format( "%.2f", attendee.total ));
        return vi;
    }
}
