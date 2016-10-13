package arefin.Activities;

/**
 * Created by Arefin on 14-Oct-16.
 */

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.batfia.arefin.ManagerX.R;

/**
 * Created by Arefin on 28-Aug-16.
 */

public class ListChoiceListener implements AbsListView.MultiChoiceModeListener
{
    ListView lv;
    ListViewAdapter listViewAdapter;
    ListChoiceListener(ListView listView, ListViewAdapter listViewAdapter)
    {
        this.lv=listView;
        this.listViewAdapter=listViewAdapter;

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Capture total checked items
        final int checkedCount = lv.getCheckedItemCount();
        // Set the CAB title according to total checked items
        mode.setTitle(checkedCount + " Selected");
        // Calls toggleSelection method from ListViewAdapter Class
        listViewAdapter.toggleSelection(position);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                // Calls getSelectedIds method from ListViewAdapter Class
                SparseBooleanArray selected = listViewAdapter.getSelectedIds();
                // Captures all selected ids with a loop
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Object selecteditem = listViewAdapter.getItem(selected.keyAt(i));
                        // Remove selected items following the ids
                        listViewAdapter.remove(selecteditem);
                    }
                }
                // Close CAB
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Auto-generated method stub
        listViewAdapter.removeSelection();
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
        return false;
    }
}
