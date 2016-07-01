package com.example.asus1.menuList;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuCreatorActivity extends AppCompatActivity {

    EditText itemNumText;
    Button itemNumButton;
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

        itemNumText=(EditText) findViewById(R.id.itemNumText);
        itemNumButton=(Button) findViewById(R.id.ItemNumButton);

        descLayout = (LinearLayout) findViewById(R.id.desc_layout);
        priceLayout=(LinearLayout) findViewById(R.id.price_layout);
        lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,    LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void menuItemSelected(View v){

        itemNum=Integer.parseInt(itemNumText.getText().toString());
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
    }

    public void onClickNextButton(View v)
    {
        priceList=new int[itemNum];
        for(int l=0; l<itemNum; l++) {
            priceList[l]=Integer.parseInt(price[l].getText().toString());
            Log.i("fahim","item "+l +" : price "+priceList[l]);
        }

        descList=new String[itemNum];
        for(int l=0; l<itemNum; l++) {
            descList[l]= description[l].getText().toString();
            Log.i("fahim","item "+l +" : Description "+descList[l]);
        }

        //Intent createIntent = new Intent(MenuCreatorActivity.this, ItemListActivity.class);
        //startActivity(createIntent);
    }
}
