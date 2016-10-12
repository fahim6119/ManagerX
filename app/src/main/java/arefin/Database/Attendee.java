package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Attendee {
    public static final String TABLE="Attendee_table";

    String name;
    int serial;
    double total,paid;
    int eventID;
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
