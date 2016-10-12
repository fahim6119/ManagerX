package arefin;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.batfia.arefin.MenuAssistant.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import arefin.dialogs.fragment.SimpleDialogFragment;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    ArrayList<File> historyFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("Previous Saved Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        historyFiles = new ArrayList<File>();
        listItems=new ArrayList<>();

            String folder_main = getBaseContext().getString(R.string.app_name);
            String myPath = Environment.getExternalStorageDirectory().toString()+ "/" + folder_main;

            File f = new File(myPath);
            if(f.exists()==false) {
                finish();
                return;
            }
            File file[] = f.listFiles();
            for(int i=0;i<file.length;i++)
            {
                historyFiles.add(file[i]);
                String name=file[i].getName();
                listItems.add(name.substring(0, name.length() - 4));
            }

            listView=(ListView)findViewById(R.id.listHistory);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selectedFile=historyFiles.get(position);
                Log.i("checkLog","Selected File "+listItems.get(position));
                String history=readFile(selectedFile);

                SimpleDialogFragment frag=new arefin.dialogs.fragment.SimpleDialogFragment();
                frag.createBuilder(getBaseContext(), getSupportFragmentManager())
                        .setTitle("Event details of "+ listItems.get(position))
                        .setMessage(history)
                        .setCancelable(true)
                        .setNeutralButtonText("CLOSE")
                        .show();

            }
        });

    }

    String readFile(File file)
    {
        BufferedReader br=null;
        StringBuilder text = new StringBuilder();
        try
        {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (IOException e) {
            Log.i("checkLog","error in file");
        }
        finally{
            if(br!=null)
                try {
                    br.close();
                    return text.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return text.toString();
    }

}
