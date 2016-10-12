package arefin.Database;

/**
 * Created by Arefin on 12-Oct-16.
 */

public class Event
{
    public static final String TABLE="event";

    String name, place, timestamp;
    int serial;
    Event(String name,String place, String timeStamp)
    {
        this.name=name;
        this.place=place;
        this.timestamp=timeStamp;
    }

    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("Event Name : "+name+"\n");
        sb.append("Place : "+place+"\n");
        sb.append("TimeStamp : "+timestamp+"\n\n");
        return sb.toString();
    }
}
