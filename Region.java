/*
FILE            : Region.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This is an abstract class which holds general infomation about a
region. Classes that inherit this class are Country and State.
Class Fields includes:
    - name, the name of the region
    - shortName, the short name of the region
    - area, the geographical area
    - area unit, the SI unit in reference to the area
    - population, how many people lives in this region
    - popRef, is the source of the population data

*/
import java.io.*;

    public abstract class Region implements Serializable
    {
        //CLASS FIELDS
        private String name;
        private String shortName;
        private double area;
        private String areaUnit;
        private int population;
        private String popRef;

        
        public Region(String inName, String inShortName, double inArea, String inAreaUnit, int inPop, String inPopRef)
        {
            setName(inName);
            setShortName(inShortName);
            setArea(inArea);
            setAreaUnit(inAreaUnit);
            setPopulation(inPop);
            setPopRef(inPopRef);   
        }

        //Mutators
        public void setName(String inName)
        {
            if(validateString(inName) == false)
                throw new IllegalArgumentException("Name must not be blank");

            name = inName;
        }

        public void setShortName(String inShortName)
        {
            if(!validateString(inShortName))
                throw new IllegalArgumentException("Short name must not be blank");

            shortName = inShortName;
        }

        public void setArea(double inArea)
        {
            if(inArea < 0.00)
                throw new IllegalArgumentException("Invalid area");

            area = inArea;
        }

        public void setAreaUnit(String inAreaUnit)
        {
            if(validateString(inAreaUnit) == false)
                throw new IllegalArgumentException("Invalid area");

            areaUnit = inAreaUnit;
        }
        

        public void setPopulation(int inPop)
        {
            if(inPop < 0)
                throw new IllegalArgumentException("Invalid population");

            population = inPop;
        }

        public void setPopRef(String inPopRef)
        {
            if(validateString(inPopRef) == false)
                throw new IllegalArgumentException("Source of population data must not be blank");

            popRef = inPopRef;
        }


        //Accessors
        public String getName()
        {
            return name;
        }

        public String getShortName()
        {
            return shortName;
        }

        public double getArea()
        {
            return area;
        }

        public String getAreaUnit()
        {
            return areaUnit;
        }

        public int getPopulation()
        {
            return population;
        }

        public String getPopRef()
        {
            return popRef;
        }

        //To String
        public String toString()
        {
            return "Name: " + name + ", sName: " + shortName + ", Area: " + area + " " + areaUnit + ", Population: " + population + ", PopREF: " + popRef;
        }
 
        //Other private methods
        private boolean validateString(String inString)
        {
            boolean same = true;
            if(inString.equals("") || inString == null)
                same = false;
            
            return same;
        }
   }    
