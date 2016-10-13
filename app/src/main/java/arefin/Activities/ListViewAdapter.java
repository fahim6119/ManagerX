package arefin.Activities;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;


/**
 * Created by Arefin on 27-Aug-16.
 */


public class ListViewAdapter extends ArrayAdapter<Object>
{
    // Declare Variables
    int resourceId;
    Context context;
    LayoutInflater inflater;
    List itemList;
    private SparseBooleanArray mSelectedItemsIds;


    public ListViewAdapter(Context context, int resourceId,
                           List itemList) {
        super(context, resourceId, itemList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.itemList = itemList;
        this.resourceId=resourceId;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemList.size();
    }


    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView = android.view.View.inflate(context, resourceId, null);
        return convertView;
    }

    @Override
    public void remove(Object object)
    {
        int index=itemList.indexOf(object);
        itemList.remove(index);
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return itemList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value)
    {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}