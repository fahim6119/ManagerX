package arefin.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.batfia.arefin.ManagerX.R;

import java.util.ArrayList;

import arefin.Database.Attendee;
import arefin.Database.AttendeeDB;
import arefin.app;

public class SuggestionActivity extends AppCompatActivity
{
    ArrayList<String> attendeeNames;
    GridLayout.LayoutParams lp;
    GridLayout gridLayout;
    ToggleButton[][] buttonArray;
    int eventID;
    int rowCount=10;
    int columnCount=3;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        eventID= app.currentEventID;
        context=getBaseContext();
        attendeeNames=new ArrayList<>();
        viewGenerator();
    }

    public void onClickCreateButton(View v)
    {
        for(int i=0;i<attendeeNames.size();i++)
        {
            Attendee attendee = new Attendee(eventID, attendeeNames.get(i));
            int serial = AttendeeDB.insertAttendee(attendee);
        }
        Intent createIntent = new Intent(SuggestionActivity.this, AttendanceActivity.class);
        startActivity(createIntent);
    }

    public void viewGenerator()
    {
        ArrayList<Attendee> currentList=AttendeeDB.getAttendeesByEvent(eventID);
        ArrayList<String> baseList=new ArrayList<>();
        baseList.addAll(AttendeeDB.getAllAttendees());
        for(int i=0;i<currentList.size();i++)
        {
            baseList.remove(currentList.get(i).name);
        }
        int totalBtns=baseList.size();
        int btnCount=0;
        rowCount=(int)Math.ceil(totalBtns/3.0);
        buttonArray= new ToggleButton[rowCount][columnCount];
        TableLayout table = (TableLayout)findViewById(R.id.suggestionLayout);
        for (int row = 0; row < rowCount && btnCount<totalBtns; row++)
        {
            TableRow currentRow = new TableRow(context);
            for (int button = 0; button < columnCount && btnCount<totalBtns; button++,btnCount++) {
                ToggleButton currentButton = new ToggleButton(context);
                // you could initialize them here
                //currentButton.setOnClickListener(listener);
                // you can store them
                currentButton.setText(baseList.get(btnCount));
                currentButton.setTextOn(baseList.get(btnCount));
                currentButton.setTextOff(baseList.get(btnCount));
                currentButton.setTextColor(getResources().getColor(R.color.mainText));

                currentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            String text=buttonView.getText().toString();
                            attendeeNames.add(text);
                        } else {
                            String text=buttonView.getText().toString();
                            attendeeNames.remove(text);
                        }
                    }
                });
                buttonArray[row][button] = currentButton;
                // and you have to add them to the TableRow
                currentRow.addView(currentButton);
            }
            // a new row has been constructed -> add to table
            table.addView(currentRow);
        }
    }



}


