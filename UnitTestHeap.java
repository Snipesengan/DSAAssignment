/*
FILE            : UnitTestHeap.java
AUTHOR          : Nhan Dao
REFEERENCES     : Connor Beardsmore - 15504319 (For providing such a lovely testharness template)
COMMENTS        :
*/

import java.io.*;
import java.util.*;

public class UnitTestHeap
{
	public static void main(String args[])
	{
        // VARIABLE DECLARATIONS
        final int DEFAULT_SIZE = 5;

        int iNumPassed = 0;
        int iNumTests = 0;
        DSAHeap heap = null;
        Iterator i;
        String sTestString;
        Object nodeValue;

//---------------------------------------------------------------------------

        System.out.println("\n\nTesting Normal Conditions - Constructor");
        System.out.println("=======================================");

        // TEST 1 : CONSTRUCTOR
        try {
            iNumTests++;
            heap = new DSAHeap(DEFAULT_SIZE);
            System.out.print("Testing creation of DSAHeap (isEmpty()): ");
            if (heap.isEmpty() == false)
                throw new IllegalArgumentException("isEmpty() should be true");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

//---------------------------------------------------------------------------

        System.out.println("\nTest inserting and removing");
        System.out.println("==========================================================");

        // TEST 2 : INSERT 
        try {
            iNumTests++;
            System.out.print("Testing insertFirst(): ");
            heap.add(3,"abc");
            heap.add(2,"jkl");
            heap.add(1,"xyz");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 3 : REMOVE 
        try {
            iNumTests++;
            System.out.print("Testing remove(): ");
            sTestString = (String)heap.remove();
            if (sTestString != "xyz")
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String)heap.remove();
            if (sTestString != "jkl")
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String)heap.remove();
            if (sTestString != "abc")
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 4 : IS EMPTY
        try {
            iNumTests++;
            System.out.print("Testing isEmpty(): ");
            sTestString = (String)heap.remove();
            System.out.println("FAILED");
        } catch(Exception e) { iNumPassed++; System.out.println("passed"); }

//---------------------------------------------------------------------------

        // TEST 5: HEAP SORT
        try {

            iNumTests++;
            System.out.print("Testing heapSort(): ");
            heap = new DSAHeap(DEFAULT_SIZE);
            DSAHeapEntry[] entry = new DSAHeapEntry[DEFAULT_SIZE];
            entry[0] = new DSAHeapEntry(2,"abc");
            entry[1] = new DSAHeapEntry(5,"def");
            entry[2] = new DSAHeapEntry(1,"ghi");
            entry[3] = new DSAHeapEntry(8,"jkl");
            entry[4] = new DSAHeapEntry(6,"mno");
            heap.heapSort(entry,entry.length);


            if(!entry[0].value.equals("ghi"))
                throw new IllegalArgumentException("FAILED.");
            if(!entry[1].value.equals("abc"))
                throw new IllegalArgumentException("FAILED.");
            if(!entry[2].value.equals("def"))
                throw new IllegalArgumentException("FAILED.");
            if(!entry[3].value.equals("mno"))
                throw new IllegalArgumentException("FAILED.");
            if(!entry[4].value.equals("jkl"))
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        }
        catch(Exception e) { System.out.println("FAILED"); throw e;}
            
//---------------------------------------------------------------------------

        // PRINT TEST SUMMARY
        System.out.print("\nNumber PASSED: " + iNumPassed + "/" + iNumTests);
        System.out.print(" -> " + (int)((double)iNumPassed/iNumTests*100) + "%\n");
    }
//---------------------------------------------------------------------------
}
