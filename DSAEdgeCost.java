//
//File   : DSAEdgeCost.java
//Author : Nhan Dao
//Unit   : DSA
//Purpose:  To store related information between two location
//

import java.util.*;

public class DSAEdgeCost
{
    private String transportMode;
    private double distance;
    private int time;
    private int peakTime;
    private int impairment;

    public DSAEdgeCost(String inTransportMode, double inDistance, int inTime, int inPeakTime)
    {
        transportMode = inTransportMode;
        distance = inDistance;
        time = inTime;
        peakTime = inPeakTime;
        impairment = 0;
    }

    //Accessors and Mutator

    public String getTransportMode()
    {
        return transportMode;
    }

    public void setTransportMode(String inStr)
    {
        transportMode = inStr;
    }

    public double getDistance()
    {
        return calcCost(distance);
    }

    public void setDistance(double inDistance)
    {
        if(inDistance < 0)
            throw new IllegalArgumentException("Distance value of an edge cannot be less than 0");

        distance = inDistance;
    }

    public int getTime()
    {
        return (int) calcCost((double) time);
    }

    public void setTime(int inTime)
    {
        if(inTime < 0)
            throw new IllegalArgumentException("Time value of an edge cannot be less than 0");

        time = inTime;
    }

    public int getPeakTime()
    {
        return (int) calcCost((double) peakTime);
    }

    public void setPeakTime(int inPeakTime)
    {
        if(inPeakTime < 0)
            throw new IllegalArgumentException("Time value of an edge cannot be less than 0");

        peakTime = inPeakTime;
    }

    public void setImpairment(int inImpairment)
    {
        if(inImpairment < 0 || inImpairment > 100)
            throw new IllegalArgumentException("Invalid impairment, can only 0-100.");

        impairment = inImpairment;
    }

    public double calcCost(double cost)
    {
        double result;

        if(impairment == 100)
            result = Double.MAX_VALUE;
        else
            result = cost / (1 - (double) impairment/100);

        return result;
    }

    public String toString()
    {
        String str = "";
        int hour = time/3600;
        int minutes = time/60 - hour*60;
        int pHour = peakTime/3600;
        int pMinutes = peakTime/60 - pHour * 60;
        str += transportMode + "," + distance +" km," + hour + ":" + minutes;

        if(peakTime != time)
            str += ",peak=" + pHour + ":" + pMinutes;

        if(impairment > 0)
            str += ".This route is impaired by" + impairment ;

        return str;
    }
    
}
