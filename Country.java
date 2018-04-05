/*
FILE            : Country.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :


This class extends region, which holds general infomation about a place.
More specifically, it holds infomation about a country such as:
    - Spoken language
    - States

The states are stored using an ADT's, Binary search tree. This decision was
made because of the following reasons:
    - Since we do not know how many states there are in the input file
      ,BJTs allows us to dynamically create more space to store infomation
      compared to arrays.
    - Linked list also allow dynamic memory creation. However its O(N) access
      time is inferior to BJTs O(log(N)). Although BJTs have slower insertion 
      time than linked list, this trade off is appropriate as the application
      infers the retrieval of states/location more frequent than inserting
      them.
    - The state's name will be used as the key for the BJTs since it is unique

NOTE: Most algorithm used in this file are mundane, with the exception of 
getLocations(), which retrieves locations of all states stored in the country
and put them in a linked list. This functionality is implemented whenever
iteration overall all the locations is required.
*/

import java.util.*;
import java.io.*;

public class Country extends Region implements Serializable
{

    //CLASS FIELDS
    private DSABinarySearchTree<State> state;
    private String lang;
    private int stateCount;


    //COUNSTRUCTORS
    public Country()
    {
        super("unnamed", "nil", 0.0, "km^2", 0, "unknown");
        setLang("unknown");
        state = new DSABinarySearchTree<State>();
        stateCount = 0;
    }

    //MUTATORS
    public void setLang(String inLang)
    {
        if(validateString(inLang) == false)
            throw new IllegalArgumentException("Language must not be blank");

        lang = inLang;
    }

    public void addState(State inState)
    {
        state.insert(inState.getShortName(), inState);
        stateCount ++; 
    }

    //ACCESSORS
    public String getLang()
    {
        return lang;
    }

    public State getState(String name)
    {
        return state.find(name);
    }

    //PUBLIC METHODS
    /*
        PURPOSE :   This retrives all the locations within the each states in
                    the form of a linked list.  
        IMPORT  : None
        EXPORT  : DSALinkedList
    */
    public DSALinkedList<Location> getLocations()
    {
        /*
        This method utilizes the traverse() method in DSABinarySearchTree.java
        to retrive all the states in the form of a queue. Then each state will
        have its own binary search tree of locations. The same method is used
        to retrieve the locations in each state. Then all locations is put on
        a linked list.
        */

        DSALinkedList<Location> locations = new DSALinkedList<Location>();
        DSAQueue<State> stateQ = state.traverse(1);

        while(!stateQ.isEmpty())
        {
            State state = stateQ.dequeue();
            DSAQueue<Location> locationQ = state.getLocationQueue();
            while(!locationQ.isEmpty())
            {
                locations.insertLast(locationQ.dequeue());
            }
        }

        return locations;
    }

    public int getStateCount()
    {
        return stateCount;
    }

    //To String
    public String toString()
    {
        String str = "COUNTRY - " + super.toString() + ", Language: " + lang;
        DSAQueue<State> i = state.traverse(1);
        State s;

        while(!i.isEmpty())
        {
            s = i.dequeue();
            str += s.toString();
            str += "\n";
        }
        
        return str;
    }

    //PRIVATE METHODS
    /*
        PURPOSE : Check whether a string is valid 
        IMPORT  : String
        EXPORT  : boolean
    */
    private boolean validateString(String inString)
    {
        boolean same = true;
        if(inString.equals("") || inString == null)
            same = false;
        
        return same;
    }
}    
