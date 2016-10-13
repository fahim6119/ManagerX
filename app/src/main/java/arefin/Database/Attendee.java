package arefin.Database;

import java.util.Comparator;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Attendee {
    public static final String TABLE="Attendee_table";

    public String name;
    public int serial,eventID;
    public double total,paid;
    public Attendee(int eventID,String name,double total,double paid)
    {
        this.eventID=eventID;
        this.name=name;
        this.total=total;
        this.paid=paid;
    }

    Attendee()
    {

    }


    public Attendee(int eventID,String name)
    {
        this.eventID=eventID;
        this.name=name;
        this.total=0;
        this.paid=0;
    }
    public double getDue()
    {
       return total-paid;
    }

    public void setAttendeeTotal(double total)
    {
        this.total=total;
    }

    public void setAttendeePaid(double paid)
    {
        this.paid=paid;
    }
}
