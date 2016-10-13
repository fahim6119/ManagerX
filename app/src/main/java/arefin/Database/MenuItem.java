package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class MenuItem
{
    final static String TABLE="menu";
    public int serial, eventID;
    public String description;
    public double price;
    public MenuItem(int event,String description,double price)
    {
        eventID=event;
        this.description=description;
        this.price=price;
    }
    MenuItem()
    {

    }
}
