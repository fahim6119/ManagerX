package arefin.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.batfia.arefin.ManagerX.R;

import java.util.ArrayList;
import java.util.List;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.Database.MenuItem;
import arefin.Database.MenuItemDB;
import arefin.Database.Order;
import arefin.Database.OrderDB;
import arefin.app;

public class MenuEditActivity extends AppCompatActivity
{

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listOrders ;
    ArrayList<MenuItem> menuItems;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    ListView listView;
    int eventID;

    Context context;
    MyAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        context=getApplicationContext();
        eventID= app.currentEventID;
        listOrders=new ArrayList<>();
        menuItems= MenuItemDB.getItemsByEvent(eventID);
        for(int i=0;i<menuItems.size();i++)
        {
            MenuItem menuItem=menuItems.get(i);
            listOrders.add(menuItem.description+","+menuItem.price);
        }
        listView=(ListView)findViewById(R.id.menu_listview);
        baseAdapter = new MyAdapter(context,R.layout.menu_listview_item,menuItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemID=menuItems.get(position).serial;
                editItem(position);
            }
        });

        if (listView != null) {
            listView.setAdapter(baseAdapter);
            registerForContextMenu(listView);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            ListChoiceListener listChoiceListener=new ListChoiceListener(listView,baseAdapter);
            listView.setMultiChoiceModeListener(listChoiceListener);
        }

    }

    @Override
    public void onBackPressed()
    {
        //saveSharedPreferences();
        Intent i = new Intent(MenuEditActivity.this, FragmentActivity.class);
        startActivity(i);
        finish();
    }

    public void addItem(View v)
    {
        createItem();
    }

    private void createItem()
    {
        TextView dialog_msg;
        final EditText dialog_name, dialog_price;
        Button set;

        final Dialog dialog = new Dialog(MenuEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menuitem);
        dialog_msg = (TextView) dialog.findViewById(R.id.dialog_message);
        dialog_name = (EditText) dialog.findViewById(R.id.dialog_name);
        dialog_price = (EditText) dialog.findViewById(R.id.dialog_price);
        set = (Button) dialog.findViewById(R.id.btn_set);
        set.setText("ADD");
        dialog_msg.setText("Add New Item");

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dialog_name.getText().toString();
                if (name.isEmpty())
                    return;
                Double price;
                String priceStr = dialog_price.getText().toString().trim();
                try {
                    price =Double.parseDouble(priceStr);
                }
                catch (NumberFormatException e)
                {
                    dialog_price.setError("Invalid Price");
                    return;
                }
                MenuItem tempItem=new MenuItem(eventID,name,price);
                int serial=MenuItemDB.insertMenu(tempItem);
                tempItem.serial=serial;
                menuItems.add(tempItem);
                baseAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void editItem(final int position)
    {
        MenuItem menuItem=menuItems.get(position);
        TextView dialog_msg;
        final EditText dialog_name, dialog_price;
        Button set;

        final Dialog dialog = new Dialog(MenuEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menuitem);
        dialog_msg = (TextView) dialog.findViewById(R.id.dialog_message);
        dialog_name = (EditText) dialog.findViewById(R.id.dialog_name);
        dialog_price = (EditText) dialog.findViewById(R.id.dialog_price);
        set = (Button) dialog.findViewById(R.id.btn_set);

        dialog_msg.setText("Edit Item");

        dialog_name.setText(menuItem.description);
        dialog_price.setText(String.format( "%.2f", menuItem.price));

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dialog_name.getText().toString();
                if (name.isEmpty())
                    return;
                final int index=position;
                Double price;
                String priceStr = dialog_price.getText().toString().trim();
                try {
                    price =Double.parseDouble(priceStr);
                }
                catch (NumberFormatException e)
                {
                    dialog_price.setError("Invalid Price");
                    return;
                }
                MenuItem tempItem=menuItems.get(index);
                tempItem.description=name;
                tempItem.price=price;
                menuItems.set(position, tempItem);
                baseAdapter.notifyDataSetChanged();
                MenuItemDB.update(tempItem);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    class MyAdapter extends ListViewAdapter
    {
        public MyAdapter(Context context, int resourceId,
                            List<MenuItem> itemList) {
            super(context, resourceId, itemList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;
            holder = new ViewHolder();
            convertView = android.view.View.inflate(context, R.layout.menu_listview_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.price = (TextView) convertView.findViewById(R.id.item_price);

            convertView.setTag(holder);

            final MenuItem item = menuItems.get(position);
            holder.title.setText(item.description);
            holder.price.setText("Price : "+String.format( "%.2f", item.price));

            return convertView;
        }

        class ViewHolder {
            TextView title, price;
        }

        @Override
        public void remove(Object object)
        {
            int index=menuItems.indexOf(object);
            MenuItem menuItem=menuItems.get(index);
            int itemID=menuItem.serial;
            MenuItemDB.deleteItembyID(itemID);
            ArrayList<Order> orders=OrderDB.getOrdersByItem(eventID,itemID);
            OrderDB.deleteOrderbyitemID(itemID);
            for(int i=0;i<orders.size();i++)
            {
                int attendeeID=orders.get(i).attendeeID;
                Attendee attendee=AttendeeDB.getAttendeeByID(attendeeID);
                attendee.total=0;
                AttendeeDB.update(attendee);
            }

            menuItems.remove(index);
            notifyDataSetChanged();
        }
    }

}
