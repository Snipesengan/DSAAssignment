/*
FILE            : State.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :


This class extends region, which holds general infomation about a place.
More specifically, it holds infomation about a country such as:
    - The name of the country its under
    - Locations 

The locations are stored using an ADT's, Binary search tree. This decision was
made because of the following reasons:
    - Since we do not know how many states there are in the input file
      ,BJTs allows us to dynamically create more space to store infomation
      compared to arrays.
    - Linked list also allow dynamic memory creation. However its O(N) access
      time is inferior to BJTs O(log(N)). Although BJTs have slower insertion 
      time than linked list, this trade off is appropriate as the application
      infers the retrieval of states/location more frequent than inserting
      them.
    - The location's name will be used as the key for the BJTs since it is unique

*/

import java.io.*;
import java.util.*;

public class State extends Region implements Serializable
{

    //CLASS FIELDS
    private int locationCount;
    private DSABinarySearchTree<Location> location;
    private String country;

    //CONSTRUCTORS
    public State()
    {
        super("unnamed", "nil", 0.0, "km^2", 0, "unknown");
        setCountry("unknown");
        location = new DSABinarySearchTree<Location>();
        locationCount = 0;
    }


   public State(String inName, String inShortName, double inArea, String inAreaUnit,  int inPop, String inPopref, String inCountry)
   {
        super(inName, inShortName, inArea, inAreaUnit, inPop, inPopref);
        setCountry(inCountry);
        locationCount = 0;
        location = new DSABinarySearchTree<Location>();
   }

    //Mutators
    public void addLocation(Location inLocation)
    {
        
        location.insert(inLocation.getName(), inLocation);
        locationCount ++;
    }

    public void setCountry(String inCountry)
    {
        if(validateString(inCountry) == false)
            throw new IllegalArgumentException("Country must not be blank");

        country = inCountry;
    }


    //Accessors
    public String getCountry()
    {
        return country;
    }

    //PUBLIC METHODS
    public Location getLocation(String locationName)
    {  
        return location.find(locationName);
    }

    public DSAQueue<Location> getLocationQueue()
    {
        return location.traverse(1);
    }

    public int getLocationCount()
    {
        return locationCount;
    }

    //TO STRING
    public String toString()
    {
        String str = "STATE - " +  super.toString() + ", COUNTRY: " + country + "\n";
        DSAQueue<Location> queue = location.traverse(1);
        Location l;

        while(!queue.isEmpty())
        {
            l = queue.dequeue();
            str += l.toString() + "\n";
        }

        return str;
    }

    //PRIVATE METHODS
    private boolean validateString(String inString)
    {
        boolean same = true;
        if(inString.equals("") || inString == null)
            same = false;

        return same;
    } 
}    
