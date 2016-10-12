package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class MenuItem
{
    final static String TABLE="menu";
    int serial, eventID;
    String description;
    double price;
    MenuItem(int event,String description,int price)
    {
        eventID=event;
        this.description=description;
        this.price=price;
    }
    MenuItem()
    {

    }
}
