/*
FILE            : Location.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class holds infomation of specific to a location, such as:
    - name 
    - name of the state its in
    - name of the country its in
    - The postion (Long/Lat) in the form of a string
    - The description about the location

*/

import java.io.*;
import java.util.*;

    public class Location implements Serializable
    {
        //Class Field
        private String name;
        private String stateName;
        private String countryName;
        private String pos;
        private String description;
        

        public Location(String inName, String inState, String inPos, String inDescription)
        {
            setName(inName);
            setStateName(inState);
            setPos(inPos);
            setDescription(inDescription);
        }

        //Mutators
        public void setName(String inName)
        {
            if(validateString(inName) == false)
                throw new IllegalArgumentException("Invalid name: " + inName);

            name = inName;
        }

        public void setCountryName(String inCountry)
        {
            countryName = inCountry;
        }

        public void setPos( String inPos)
        {
             pos = inPos;
        }
        
        public void setDescription(String inDescription)
        {
            description = inDescription;
        }

        public void setStateName(String inState)
        {
            if(validateString(inState) == false)
                throw new IllegalArgumentException ("State must not be blank");

            stateName = inState;
        }

        //Accessors
        public String getName()
        {
            return name;
        }

        public String getStateName()
        {
            return stateName;
        }

        public String getPos()
        {
            return pos;
        }

        public String getDescription()
        {
            return description;
        }

        public String getCountryName()
        {
            return countryName;
        }
        
        //TO STRING
        public String toString()
        {
            return "LOCATION - " + "Name: " + name + ", State: " + stateName + ", Position: " + pos + ", Description: " + description;
        }

        //PRIVATE METHODS
        private boolean validateString(String inString)
        {
            boolean valid = true;
            if(inString == null || inString.equals(""))
                valid = false;
            
            return valid;
        }
    }    
