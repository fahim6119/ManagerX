package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Order
{
    public static final int SERVED=1;
    public static final int UNSERVED=0;

    public static final String TABLE="Order_table";
    int serial,itemID,served, quantity,attendeeID,eventID;
    public Order(int eventID,int itemID,int attendeeID,int served, int quantity)
    {
        this.eventID=eventID;
        this.itemID=itemID;
        this.attendeeID=attendeeID;
        this.served=served;
        this.quantity=quantity;
    }

    public Order()
    {

    }

    public void setServed()
    {
        served=SERVED;
    }

    public boolean ifServed()
    {
        if(served==SERVED)
            return true;
        else
            return false;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
