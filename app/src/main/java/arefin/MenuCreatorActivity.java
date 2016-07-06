package arefin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.arefin.menuList.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MenuCreatorActivity extends AppCompatActivity {

    EditText itemNumText;
    Button itemNumButton,addMenuBtn;
    LinearLayout.LayoutParams lp,plp;
    LinearLayout descLayout,priceLayout;
    EditText[] price,description;

    int itemNum;
    int[] priceList;
    String[] descList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creator);

        getSupportActionBar().setTitle("Menu Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemNumText=(EditText) findViewById(R.id.itemNumText);
        itemNumButton=(Button) findViewById(R.id.ItemNumButton);

        addMenuBtn=(Button) findViewById(R.id.addMenuBtn);

        descLayout = (LinearLayout) findViewById(R.id.desc_layout);
        priceLayout=(LinearLayout) findViewById(R.id.price_layout);
        lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,    LinearLayout.LayoutParams.WRAP_CONTENT);

        addMenuBtn.setClickable(false);
        retrieve_sharedArray();

    }


    //retrieve Menu if already saved
    public void retrieve_sharedArray()
    {
        addMenuBtn.setClickable(true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("itemNum"))
        {
            itemNum = preferences.getInt("itemNum", 0);
            descList = new String[itemNum];
            for(int i=0; i<itemNum; i++)
                descList[i]=preferences.getString("desc_" + i, null);
            priceList=new int[itemNum];
            for(int i=0; i<itemNum; i++)
                priceList[i]=preferences.getInt("price_" + i, 0);
            itemNumText.setText(Integer.toString(itemNum));
            if(viewGenerator()) {
                for (int l = 0; l < itemNum; l++) {
                    description[l].setText(descList[l]);
                    price[l].setText(Integer.toString(priceList[l]));
                }
            }
        }
    }

    public boolean viewGenerator()
    {

        price=new EditText[itemNum];
        description=new EditText[itemNum];
        descLayout.removeAllViews();
        for(int l=0; l<itemNum; l++)
        {
            description[l] = new EditText(this);
            description[l].setTextSize(15);
            description[l].setLayoutParams(lp);
            description[l].setId(l);
            description[l].setHint("Description of Item " +(l + 1) );
            descLayout.addView(description[l]);
        }
        priceLayout.removeAllViews();
        for(int l=0; l<itemNum; l++)
        {
            price[l] = new EditText(this);
            price[l].setTextSize(15);
            price[l].setLayoutParams(lp);
            price[l].setId(l);
            price[l].setInputType(InputType.TYPE_CLASS_NUMBER );
            price[l].setHint("Price of Item " +(l + 1) );
            priceLayout.addView(price[l]);
        }
        return true;
    }

    public void menuItemSelected(View v){

        String text=itemNumText.getText().toString();
        if(text.isEmpty()==false) {
            itemNum = Integer.parseInt(text);
            viewGenerator();
        }
    }

    public void onClickNextButton(View v)
    {
        priceList=new int[itemNum];
        String priceTemp,descTemp;
        for(int l=0; l<itemNum; l++) {
            priceTemp=price[l].getText().toString();
            if(TextUtils.isEmpty(priceTemp))
            {

                price[l].setError("Please enter the price");
                return;
            }
            else
                priceList[l]=Integer.parseInt(priceTemp);
            Log.i("checkLog","item "+l +" : price "+priceList[l]);
        }

        descList=new String[itemNum];
        for(int l=0; l<itemNum; l++) {
            descTemp=description[l].getText().toString();
            if(TextUtils.isEmpty(descTemp))
            {
                description[l].setError("Please enter the details");
                return;
            }
            else
            descList[l]= descTemp;
            Log.i("checkLog","item "+l +" : Description "+descList[l]);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("itemNum", itemNum);
        for(int i=0;i<priceList.length; i++)
            editor.putInt("price_" + i, priceList[i]);

        for(int i=0;i<descList.length; i++)
            editor.putString("desc_" + i, descList[i]);
        editor.apply();
        Bundle b=new Bundle();
        b.putInt("itemNum",itemNum);
        b.putIntArray("priceList",priceList);
        b.putStringArray("descList",descList);
        Intent createIntent = new Intent(MenuCreatorActivity.this, ItemListActivity.class);
        createIntent.putExtras(b);
        startActivity(createIntent);
    }
}
