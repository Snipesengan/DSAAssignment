/*
FILE            : FileIO.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class has two purpose. First being that it can read in a location file
, create a Country object from the file and then return int. Secondly, it
will read a distance file and generate a DSAGraph object.

Here are some design decisions, these are reflected in the code through
the order in which data is retrieved and process.

    - File should only be read once as opening/reading file takes a long time.

    - This class should handle all the FileIO Exception, instead of throwing it
      to the calling function

    - All method should be static, because an instance of an object of this class 
      is unecessary.

    - A single, or multiple, incorrect line does not need for the file reading 
      operation to be aborted. It is logged and printed to the user. Hence,
      it is essential to distiguish whether the error caused by a single line
      is fatal or not. 

    - The data parsed from each line is immediately processed into the
      Country object (for locations file) or DSAGraph object (for distance file).
      This minimizes memory the program uses because the file data does not need to 
      be cached.

      Note that this is only possible because the Data structure used to store
      the data can dynamically grow and shrink - meaning we do not need to figure out
      how much information is contained in the file before we create the Object they
      are stored in.

    - The location file must be read in first before the distance file can be read in.
      Since distance file only contains the name of the locations, we must use Country
      object to obtain the actual reference to the Location object to store in the graph.

The file given is in the form of a CSV file. Unlike binary files, where the structure is
rigid, CSV files requires some assumptions to be made. These includes:

    - Location file contains infomation about countries, state, and location. Since the 
      aforementioned design decision is to simultaneously parse the information
      as each line are read and put them into the Country object; the assumption that
      the line containing state information will come after the line which contains
      country information must be made. The same can be said for locations and states.
      I.E State infomation will appear first in the file before the location information.
      This problem can only be dealt with by caching the file's data, which contradicts 
      the design decision mentioned above.

    - The number of fields in each line of the files are consistent; this does not
      mean that the assumption that information contain in each field is correct was made - 
      infact this problem is dealt with by the program and logged to the user.

The format of the file heavily influence the data structures and ADT's used to store the 
information in the file. Below are reasons why certain data structures/ADTs were chosen
(These reason may already been stated in the class they're implemented in): 

    - Binary search tree was used in Country/State because there were many instances
      where the a state or location was needed to be retrieved. BSTs can also dynamically
      grow and shrinks.

    - It is assumed that location names are unique for each state. Hence the label for
      the graph would have to atleast have the state name appended to the location name.

*/


import java.io.*;
import java.util.*;

class FileIO
{
    public static final int COUNTRY_FIELDS_COUNT = 6;
    public static final int STATE_FIELDS_COUNT = 7;
    public static final int LOCATION_FIELDS_COUNT = 5;
    public static final int DISTANCE_FIELDS_COUNT = 10;

    /*
        PURPOSE : Construct a country object using a file name 
        IMPORT  : the name of the file (String)
        EXPORT  : country object
    */
    public static Country readLocationFile(String path)
    {
        
        FileInputStream fileStrm = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        int numline = 0;
        String str;
        char region; //Stores the first letter of a line which tells if its a country/state/locations
        Country country = null; 
        State state = null;
        Location location = null;
        
        try
        {
            fileStrm = new FileInputStream(path);
            rdr = new InputStreamReader(fileStrm);
            bufRdr = new BufferedReader(rdr);

            str = bufRdr.readLine();
            numline ++;

            while(str != null) //While theres line left to read
            {
                try
                {
                    region = str.charAt(0); //Finds the first character of the line

                    if(region == 'C') //If line is Country
                    {  
                        if(country != null)
                            throw new IOException("Country has already been parsed");

                        country = parseCountry(str);
                    }
                    else if(region == 'S')//If line is State
                    {
                        state = parseState(str);

                        if(!state.getCountry().equals(country.getName()) && !state.getCountry().equals(country.getShortName()))//Check if the state belongs to the country
                            throw new IllegalArgumentException(state.getName() + " does not belong to the parsed country");

                        country.addState(state); //add the state to country object, it will also assert that the state name is unique

                    }
                    else if(region == 'L')//If line is Location
                    {

                        location = parseLocation(str);

                        if(!location.getCountryName().equals(country.getName())&& !location.getCountryName().equals(country.getShortName()))//Check if the location belongs to the country 
                            throw new IllegalArgumentException(location.getName() + " does not belong to the parsed country");

                        country.getState(location.getStateName()).addLocation(location); //get the state in country and add the location to that state
                        //Note the reason we only have to check whether the location belong to the country and not if it also belongs to the state is because
                        //country.getState(location.getStateName()) will assert that.

                    } 
                    else
                    {
                        throw new IllegalArgumentException("Invalid location type, can only be COUNTRY, STATE, LOCATION");
                    }


                }
                catch(IllegalArgumentException | IOException | NoSuchElementException | StringIndexOutOfBoundsException e)
                {
                    System.out.println("Line :" + numline + " could not be read in - " + e.getMessage());
                }

                str = bufRdr.readLine(); //read next line
                numline++;
            }//while(!EOF)

        }
        catch(IOException e)
        {
            System.out.println("Could not open the file: " + e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Unexpected error has occured...");
        }
        finally
        {
            try{fileStrm.close();}catch(IOException e){}; //close the file
        }

        return country;

    }

    /*
        PURPOSE : Construct a DSAGraph object using a file and a Country object
        IMPORT  : the name of the file (String), a Country object
        EXPORT  : DSAGraph<Location>
    */
    public static DSAGraph<Location> readDistanceFile(String path, Country country)
    {
        DSAGraph<Location> graph = new DSAGraph<Location>();
        FileInputStream fileStrm = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        int numline = 0;
        String str;
        Location source;
        Location dest;
        String source_label;
        String dest_label;

        String[] tok1;
        String[] tok2;
        String[] tok3;

        String transportType;
        double distance;
        int peakTime;
        int time;

        try
        {
            fileStrm = new FileInputStream(path);
            rdr = new InputStreamReader(fileStrm);
            bufRdr = new BufferedReader(rdr);

            str = bufRdr.readLine();
            numline ++;

            while(str != null)
            {
                try
                {
                    tok1 = str.split(",");
                    tok2 = tok1[8].split(":");

                    if(tok1.length > DISTANCE_FIELDS_COUNT)//Check if theres enough parameters
                        throw new IllegalArgumentException("Invalid number of parameters");

                    source = country.getState(tok1[1]).getLocation(tok1[0]); //Get the location 1
                    dest = country.getState(tok1[4]).getLocation(tok1[3]); //Get location 2
                    source_label = graph.generateLabel(source); //Generate the label for that location in the graph.
                    dest_label = graph.generateLabel(dest); //The label will be unique.

                    transportType = tok1[6];
                    distance = Double.parseDouble(tok1[7]);
                    time = Integer.parseInt(tok2[0])*3600 + Integer.parseInt(tok2[1])*60;

                    try //The following tries to retrieve the peak time
                    {   
                        tok2 = tok1[9].split("=");
                        tok3 = tok2[1].split(":");
                        peakTime = Integer.parseInt(tok3[0])*3600 + Integer.parseInt(tok3[1])*60;

                    } catch(ArrayIndexOutOfBoundsException e) { //This exception will be thrown if the peak time parameters are empty
                        peakTime = time; //set the peak time equals time
                    }

                    try{ graph.newNode(source_label, source); }catch(IllegalArgumentException e){} //These try statement are because
                    try{ graph.newNode(dest_label, dest); }catch(IllegalArgumentException e){} //the graph will throw an exception if 
                                                                                               //the nodes already exists in the graph.
                    graph.addEdge(source_label,dest_label,transportType,time,distance,peakTime);

                }
                catch(IllegalArgumentException | NoSuchElementException e)
                {
                    System.out.println("Line :" + numline + " could not be read in - " + e.getMessage());
                }

                str = bufRdr.readLine();
                numline++;
            }

        }
        catch(IOException e)
        {
            System.out.println("Could not open the file: " + e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Unexpected error has occured...");
        }
        finally
        {
            try{fileStrm.close();}catch(IOException e){};
        }

        return graph;
    }

    /*
        PURPOSE : Create a country from a  string
        IMPORT  : String containing information about a country
        EXPORT  : Country
    */
    private static Country parseCountry(String str) throws IOException
    {
        String[] tmp;
        Country country = new Country();
        String[] field = new String[COUNTRY_FIELDS_COUNT];

        field[0] = country.getName();
        field[1] = country.getShortName();
        field[2] = country.getLang();
        field[3] = Double.toString(country.getArea());
        field[4] = Integer.toString(country.getPopulation());
        field[5] = country.getPopRef();

        tmp = str.split(":");
        validateLine(tmp,"C");

        if(tmp.length != COUNTRY_FIELDS_COUNT + 1)
            throw new IOException("Error parsing the country, invalid number of fields, got: " + tmp.length);

        for(int i = 1; i < tmp.length; i++)
        {
            String[] tmp1 = tmp[i].split("=");

            if(tmp1.length == 2)
                field[i - 1] = tmp1[1];
        }

        country.setName(field[0]);
        country.setShortName(field[1]);
        country.setLang(field[2]);
        country.setArea(Double.parseDouble(field[3]));
        country.setPopulation(Integer.parseInt(field[4]));
        country.setPopRef(field[5]);

        return country;
    }

    /*
        PURPOSE : Create a state from a string
        IMPORT  : String containing information about the state
        EXPORT  : State
    */
    private static State parseState(String str) throws IOException
    {
        String[] tmp;
        State state = new State();
        String[] field = new String[STATE_FIELDS_COUNT];

        field[0] = state.getName();
        field[1] = state.getCountry();
        field[2] = state.getShortName();
        field[3] = Double.toString(state.getArea());
        field[4] = state.getAreaUnit();
        field[5] = Integer.toString(state.getPopulation());
        field[6] = state.getPopRef();

        tmp = str.split(":");
        validateLine(tmp,"S");

        if(tmp.length != STATE_FIELDS_COUNT + 1)
            throw new IOException("Error parsing the state, invalid number of fields, got: " + tmp.length);

        for(int i = 1; i < tmp.length; i++)
        {
            String[] tmp1 = tmp[i].split("=");

            if(tmp1.length == 2)
                field[i - 1] = tmp1[1];
        }

        state.setName(field[0]);
        state.setCountry(field[1]);
        state.setShortName(field[2]);
        state.setArea(Double.parseDouble(field[3]));
        state.setAreaUnit(field[4]);
        state.setPopulation(Integer.parseInt(field[5]));
        state.setPopRef(field[6]);

        return state; 
    }

    /*
        PURPOSE : Create a location
        IMPORT  : String containing information about a location
        EXPORT  : Location
    */
    private static Location parseLocation(String str) throws IOException
    {
        String[] tmp;
        Location location = new Location("null","null","null","null");
        String[] field = new String[LOCATION_FIELDS_COUNT];

        field[0] = location.getName();
        field[1] = location.getStateName();
        field[2] = location.getCountryName();
        field[3] = location.getPos();
        field[4] = location.getDescription();

        tmp = str.split(":");
        validateLine(tmp,"L");

        if(tmp.length != LOCATION_FIELDS_COUNT + 1)
            throw new IOException("Error parsing the location, invalid number of fields, got: " + tmp.length);

        for(int i = 1; i < tmp.length; i++)
        {
            String[] tmp1 = tmp[i].split("=");

            if(tmp1.length == 2)
                field[i - 1] = tmp1[1];
        }

        location.setName(field[0]);
        location.setStateName(field[1]);
        location.setCountryName(field[2]);
        location.setPos(field[3]);
        location.setDescription(field[4]);

        return location; 
    }

    /*
        PURPOSE : This method throws an exception if a line doesn't have the write parameter name
        IMPORT  : Array of parameter in the location file
        EXPORT  : nothing
    */
    private static void validateLine(String[] line, String region) throws IOException
    {
        boolean valid = true;
        String str = "";
        switch(region)
        {
            case "C":
            {
                int i = 0;
                String s = null;
                while(i < line.length)
                {
                    String[] tmp = line[i].split("=");
                    switch(i)
                    {
                        case 0:
                            s = "COUNTRY";
                            break;
                        case 1:
                            s = "NAME";
                            break;
                        case 2:
                            s = "SHORTNAME";
                            break;
                        case 3:
                            s = "LANGUAGE";
                            break;
                        case 4:
                            s = "AREA";
                            break;
                        case 5:
                            s = "POPULATION";
                            break;
                        case 6:
                            s = "POPREF";
                            break;
                    }
           
                    if(!tmp[0].equals(s))
                    {
                         valid = false;
                         str += ("Expected: \"" + s + "\".But got \"" + tmp[0] + "\";");
                    }

                    i++;
                }         
            } break;
            case "S":
            {
                int i = 0;
                String s = null;
                while(i < line.length)
                {
                    String[] tmp = line[i].split("=");
                    switch(i)
                    {
                        case 0:
                            s = "STATE";
                            break;
                        case 1:
                            s = "NAME";
                            break;
                        case 2:
                            s = "COUNTRY";
                            break;
                        case 3:
                            s = "SHORTNAME";
                            break;
                        case 4:
                            s = "AREA";
                            break;
                        case 5:
                            s = "AREAUNIT";
                            break;
                        case 6:
                            s = "POPULATION";
                            break;
                        case 7:
                            s = "POPREF";
                            break;
                    }

                    if(!tmp[0].equals(s))
                    { 
                        str += ("Expected: \"" + s + "\".But got \"" + tmp[0] + "\";");
                        valid = false;
                    }

                    i++;
                }
            } break;
            case "L":
            {
                int i = 0;
                String s = null;
                while(i < line.length)
                {
                    String[] tmp = line[i].split("=");
                    switch(i)
                    {
                        case 0:
                            s = "LOCATION";
                            break;
                        case 1:
                            s = "NAME";
                            break;
                        case 2:
                            s = "STATE";
                            break;
                        case 3:
                            s = "COUNTRY";
                            break;
                        case 4:
                            s = "COORDS";
                            break;
                        case 5:
                            s = "DESCRIPTION";
                            break;
                    }

                   if(!tmp[0].equals(s))
                   {
                        str += ("Expected: \"" + s + "\".But got \"" + tmp[0] + "\";");
                        valid = false;
                   }
                    i++;
                }
            }break;
        }

        if(!valid)
            throw new IOException(str);                                   
    }

}
    
        

