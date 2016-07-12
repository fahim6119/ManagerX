package arefin;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.batfia.arefin.MenuAssistant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Arefin on 08-Jul-16.
 */

public class PaymentFragment extends Fragment
{
    int itemNum;
    int[] priceList;
    String[] descList,users;
    public ArrayList<ArrayList<String>> orderer;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listOrders = new ArrayList<String>();
    ArrayList<Integer> paid= new ArrayList<>();
    ArrayList<Integer> total= new ArrayList<>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    View rootView;

    List<String> userlist=new ArrayList<String>();
    ListView listView;
    myAdapter mAdapter;
    int userNum;

    public PaymentFragment() { }

    public void addOrder(String name, int price)
    {
        int index=userlist.indexOf(name);
        if(index==-1)
            return;
        int val=total.get(index)+price;
        total.set(index,val);
        mAdapter.notifyDataSetChanged();
        Log.i("checkLog","Order received the name "+name+" with price "+price);
    }

    public void removeOrder(String name, int price)
    {
        int index=userlist.indexOf(name);
        int val=total.get(index)-price;
        total.set(index,val);
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
        rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        retrieve_sharedArray();
        listView = (ListView) rootView.findViewById(R.id.listPayment);
       // adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userlist);
        mAdapter=new myAdapter(getActivity().getBaseContext(), userlist,total,paid);
        for(int i=0;i<orderer.size();i++)
        {
            for(int j=0;j<orderer.get(i).size();j++)
            {
                addOrder(orderer.get(i).get(j),priceList[i]);
            }
        }
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
                int amount=Integer.parseInt(amountStr);
                paid.set(pos,amount);
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
        for(int i=0;i<userNum;i++)
        {
            int x;
            x=total.get(i);
            int totalVal=x+ (int) Math.round(x * val);
            x=paid.get(i);
            int paidVal=x+ (int) Math.round(x * val);
            paid.set(i,paidVal);
            total.set(i,totalVal);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void retrieve_sharedArray()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        itemNum = preferences.getInt("itemNum", 0);
        descList = new String[itemNum];
        orderer = new ArrayList<>();
        for(int i=0; i<itemNum; i++)
            descList[i]=preferences.getString("desc_" + i, null);
        priceList=new int[itemNum];
        for(int i=0; i<itemNum; i++)
            priceList[i]=preferences.getInt("price_" + i, 0);

        if (preferences.contains("users"))
        {
            Set<String> set = preferences.getStringSet("users", null);
            userlist=new ArrayList<String>(set);
            Collections.sort(userlist, String.CASE_INSENSITIVE_ORDER);
            userNum=userlist.size();
            paid=new ArrayList<>(userNum);
            total=new ArrayList<>(userNum);
            for(int i=0;i<userNum;i++)
            {
                paid.add(0);
                total.add(0);
            }
        }

        for(int l=0;l<itemNum;l++)
        {
            Set<String> set = preferences.getStringSet("menu_"+l, null);
            orderer.add(new ArrayList<String>(set));
            Collections.sort(orderer.get(l), String.CASE_INSENSITIVE_ORDER);
        }


        for(int i=0;i<userlist.size();i++)
        {
            String name=userlist.get(i);
            if(preferences.contains("paid_"+name))
            {
                int val = preferences.getInt("paid_"+ name, 0);
                paid.set(i, val);
                Log.i("checkLog","paid_"+ name+" "+paid.get(i));
            }
        }

    }


}

class myAdapter extends BaseAdapter {

    Context context;
    List<String> username;
    ArrayList<Integer> paid,due,total;
    private static LayoutInflater inflater = null;

    public myAdapter(Context context, List<String> username,ArrayList<Integer>total,ArrayList<Integer> paid) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.username = username;
        this.total=total;
        this.paid=paid;
        due=new ArrayList<>(username.size());
        for(int i=0;i<username.size();i++)
        {
            int val=total.get(i)-paid.get(i);
            due.add(val);
        }
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return username.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return username.get(position);
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
        for(int i=0;i<username.size();i++)
        {
            int val=total.get(i)-paid.get(i);
            due.set(i,val);
        }
        TextView text = (TextView) vi.findViewById(R.id.username);
        text.setText(username.get(position));
        TextView paidText = (TextView) vi.findViewById(R.id.userPaid);
        paidText.setText("Paid : "+Integer.toString(paid.get(position)));
        TextView dueText = (TextView) vi.findViewById(R.id.userDue);
        dueText.setText("Due : "+Integer.toString(due.get(position)));
        TextView totalText = (TextView) vi.findViewById(R.id.userTotal);
        totalText.setText("Total : "+Integer.toString(total.get(position)));
        return vi;
    }
}
