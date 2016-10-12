package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Attendee {
    public static final String TABLE="Attendee_table";

    String name;
    int total,paid,serial;
    public Attendee(String name,int total,int paid)
    {
        this.name=name;
        this.total=total;
        this.paid=paid;
    }

    public int getDue()
    {
       return total-paid;
    }

}
