package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Order
{
    public static final String TABLE="Order_table";
    int serial,item,served, quantity,attendeeID;
    String attendee;
    public Order(int item,String attendee,int served, int quantity)
    {
        this.item=item;
        this.attendee=attendee;
        this.served=served;
        this.quantity=quantity;
    }
}
