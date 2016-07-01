package com.example.asus1.menuList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    int amount;
    String name,place;
    Button amountEnter;
    EditText amountVal,nameVal,locVal;
    TextView amountMsg,nameMsg,locMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        amountMsg=(TextView)findViewById(R.id.amountMsg);
        amountVal=(EditText)findViewById(R.id.amountVal);
        locMsg=(TextView)findViewById(R.id.locMsg);
        locVal=(EditText)findViewById(R.id.locVal);
        nameMsg=(TextView)findViewById(R.id.nameMsg);
        nameVal=(EditText)findViewById(R.id.nameVal);
        amountEnter=(Button)findViewById(R.id.amountEnter);
        amount=100;
        name="@strings/database_name";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        amountEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(amountVal.getText().toString());
                name=nameVal.getText().toString();
                place=locVal.getText().toString();
                Toast.makeText(getBaseContext(), "For database "+ name+"Amount Set to "+amount,
                        Toast.LENGTH_LONG).show();
                editor.putString("name", name);
                editor.putInt("amount",amount);
                editor.putString("place",place);
                editor.apply();
                Intent createIntent = new Intent(CreateActivity.this, CollectorActivity.class);
                startActivity(createIntent);
                finish();
            }
        });


    }
}
