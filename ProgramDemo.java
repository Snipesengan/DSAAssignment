//
//File.  : ProgramDemo.java
//Author : Nhan Dao
//Unit   : DSA
//Purpose: To run the program
//

import java.util.*;
public class ProgramDemo
{
    private static Country country = null;
    private static DSAGraph graph = null;

    public static void main(String[] args)
    {
        boolean running = true;

        while(running)
        {
            clearScreen();
            System.out.println(menuString());
            int selection;

            selection = UserInput.integerInput(1, 6, "Choice:> ");

            switch(selection)
            {
                case 1: {
                    clearScreen();
                    travelSearch();
                    pause();
                                  
                } break;

                case 2: {
                    clearScreen();
                    try
                    {
                        locationSearch(country);
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                    }

                    pause();          
                } break;

                case 3: {
                    clearScreen();
                    nearbySearch();
                    pause();

                } break;

                case 4: {
                    clearScreen();
                    updateData();
                    pause();
                } break;

                case 5: {
                    clearScreen();
                    loadData();
                    pause();
                } break;  

                case 6:
                {
                    System.out.println("Program exiting...");
                    running = false;
                } break;
            }
        }
    }

    private static String menuString()
    {
        String str = "";

        str += "1 . Travel Search\n";
        str += "2 . Location Search\n";
        str += "3 . Nearby Search\n";
        str += "4 . Update data\n";
        str += "5 . Load data\n";
        str += "6 . Exit\n";

        return str;
    }

    private static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printBreak()
    {
        System.out.println("-----------------------------------------------");
    }

    private static void pause()
    {
        UserInput.stringInput("Press enter to continue...");
    }

    private static void updateData()
    {
        try
        {
            if(graph == null)
                throw new IllegalArgumentException("Error, No distance data has been read in.");

            graph.displayEdgeList();
            printBreak();
            String label1 = UserInput.stringInput("Enter label of location 1: ");
            String label2 = UserInput.stringInput("Enter label of location 2: ");
            String mode = UserInput.stringInput("Enter the transport mode you would like to update :");
            int imp = UserInput.integerInput(0,100, "Enter impairment 0-100: ");
            graph.updateImpairment(label1,label2,mode,imp);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void nearbySearch()
    {
        try
        {
            Location loc;
            double radius;
            String label;

            loc = locationSearch(country);
            printBreak();
            radius = UserInput.realInput(0,Double.MAX_VALUE, "Enter radius:> ");
            label = loc.getName() + "," + loc.getStateName();
            printBreak();
            graph.nearbySearch(label, radius);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void travelSearch()
    {
        Location start;
        Location finish;
        int selection = 0;

        while(selection != 4)
        {
            boolean verbose = false;
            boolean peak = false;
            String startLabel = null;
            String finishLabel = null;
            boolean shortDist = false;
            String transport = null;

            try
            {
                clearScreen();
                System.out.println("1. Find shortest path");
                System.out.println("2. Find the fastest path");
                System.out.println("3. Find path with desired travel mode");
                System.out.println("4. Back");

                selection = UserInput.integerInput(1,4,"Choice:> ");


                if(selection != 4)
                {
                    if(graph == null)
                        throw new IllegalArgumentException("Error. Please load distance data.");

                    printBreak();
                    System.out.println("START");
                    start = locationSearch(country);
                    printBreak();
                    System.out.println("FINISH");
                    finish = locationSearch(country);
                    printBreak();
                    startLabel = start.getName() + "," + start.getStateName();
                    finishLabel = finish.getName() + "," + finish.getStateName();
                }
                
                switch(selection)
                {
                    case 1:
                    {
                        shortDist = true;
                    } break;

                    case 2: //If the user wants the fastest path
                    {
                        peak = UserInput.boolInput("Peak time?");   
                    } break;

                    case 3: //If the user wants a specific mode of transport
                    {
                        transport = UserInput.stringInput("Enter mode of transport:> ");
                        peak = UserInput.boolInput("Peak time?");
                    } break;
                }

                if(selection != 4)
                {
                    verbose = UserInput.boolInput("Verbose travel detail?");
                    printBreak();
                    graph.travelSearch(startLabel,finishLabel, shortDist, transport, peak, verbose);
                    pause();
                }
            }
            catch(IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
                pause();
            }
            catch(NoSuchElementException e)
            {
                System.out.println("Graph does not have the start/finish location - " + e.getMessage());
                pause();
            }
        }
    }

    private static Location locationSearch(Country country)
    {
        DSALinkedList<Location> locationList;
        Location location = null;
        Location[] lArray;
        Iterator<Location> i;
        String subString;
        int count;
        boolean found = false;


        if(country == null)
        {
            throw new IllegalArgumentException("Cannot search for location, missing location data");
        }               
        else
        {
            locationList = country.getLocations();
            lArray = new Location[locationList.count()];

            while(!found)
            {
                count = 0;
                i = locationList.iterator();
                subString = UserInput.stringInput("Enter location:> ").toLowerCase();

                while(i.hasNext())
                {
                    location = i.next();
                    if(location.getName().toLowerCase().startsWith(subString))
                    {
                        System.out.println((count + 1) + ". " + location.getName());
                        lArray[count] = location;
                        count ++;    
                    }
                }

                if(count == 0) //No location matches the substring
                {
                    System.out.println("Found no location starting with \"" + subString + "\"");
                }
                else if (count == 1) //One location matches the substring
                {
                    location = lArray[0];
                    found = true;
                }
                else if(UserInput.boolInput("Is your location in the list?"))
                {
                    int idx = UserInput.integerInput(1, count, "Enter index of location:> ");
                    location = lArray[idx - 1];
                    found = true;
                }

            }//While location is not found

            System.out.println(location.toString());

        } //if country != null

        return location;
    }

    private static void loadData()
    {
        int selection = 0;

        while(selection != 3)
        {
            clearScreen();
            System.out.println("1. Load location data");
            System.out.println("2. Load distance data");
            System.out.println("3. Back");

            selection = UserInput.integerInput(1,3,"Choice:> ");
            switch(selection)
            {
                case 1:
                {
                    try
                    {
                        String fileName  = UserInput.stringInput("Enter file name: ");
                        country = FileIO.readLocationFile(fileName);
                        printBreak();
                        System.out.println(country.toString());
                        printBreak();
                        System.out.println("The country has been created successfully");

                    } catch(Exception e) {
                        System.out.println("Location data was not successfully loaded: " + e.getMessage());
                        pause();
                    }
                    pause();
                } break;

                case 2:
                { 
                    if(country == null)
                    {
                        System.out.println("Cannot read in distance file, missing location data");
                    }               
                    else
                    {
                        try
                        {
                            String fileName  = UserInput.stringInput("Enter file name: ");
                            graph = FileIO.readDistanceFile(fileName,country);
                            printBreak();
                            graph.displayEdgeList();
                            printBreak();
                            System.out.println("The distance data has been successfuly read");

                        } catch(Exception e) {
                            System.out.println("Distance data was not successfully loaded");
                            throw e;
                        }        
                    }
                    pause();
                } break;
            }

        }
    }
}