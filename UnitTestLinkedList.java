 /***************************************************************************
 *  FILE: TestLinkedList.java
 *  AUTHOR: Connor Beardsmore - 15504319
 *  UNIT: DSA120 Prac 8
 *  PURPOSE: Basic Test Harness For Linked List
 *  LAST MOD: 12/10/15
 *  REQUIRES: NONE
 ***************************************************************************/

import java.io.*;
import java.util.*;

public class UnitTestLinkedList
{
	public static void main(String args[])
	{
        // VARIABLE DECLARATIONS
        int iNumPassed = 0;
        int iNumTests = 0;
        DSALinkedList<String> ll = null;
        Iterator i;
        String sTestString;
        Object nodeValue;

//---------------------------------------------------------------------------

        System.out.println("\n\nTesting Normal Conditions - Constructor");
        System.out.println("=======================================");

        // TEST 1 : CONSTRUCTOR
        try {
            iNumTests++;
            ll = new DSALinkedList<String>();
            System.out.print("Testing creation of DSALinkedList (isEmpty()): ");
            if (ll.isEmpty() == false)
                throw new IllegalArgumentException("Head must be null.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

//---------------------------------------------------------------------------

        System.out.println("\nTest inserting first and removing first (stack behaviour)");
        System.out.println("==========================================================");

        // TEST 2 : INSERT FIRST
        try {
            iNumTests++;
            System.out.print("Testing insertFirst(): ");
            ll.insertFirst("abc");
            ll.insertFirst("jkl");
            ll.insertFirst("xyz");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 3 : PEEK LAST
        try {
            iNumTests++;
            System.out.print("Testing peekLast(): ");
            sTestString = (String)ll.peekLast();
            if (sTestString != "abc")
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed   ");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 4 : REMOVE FIRST
        try {
            iNumTests++;
            System.out.print("Testing removeFirst(): ");
            sTestString = (String)ll.removeFirst();
            if (sTestString != "xyz")
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String)ll.removeFirst();
            if (sTestString != "jkl")
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String)ll.removeFirst();
            if (sTestString != "abc")
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 5 : IS EMPTY
        try {
            iNumTests++;
            System.out.print("Testing isEmpty(): ");
            sTestString = (String)ll.removeFirst();
            System.out.println("FAILED");
        } catch(Exception e) { iNumPassed++; System.out.println("passed"); }

//---------------------------------------------------------------------------

        System.out.println("\nTest inserting last and removing first (queue behaviour)");
        System.out.println("========================================================");

        // TEST 6 : INSERT LAST()
        try {
            iNumTests++;
            System.out.print("Testing insertLast(): ");
            ll.insertLast("abc");
            ll.insertLast("jkl");
            ll.insertLast("xyz");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 7 : PEEK LAST
        try {
            iNumTests++;
            System.out.print("Testing peekFirst(): ");
            sTestString = (String)ll.peekFirst();
            if (sTestString != "abc")
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }

        // TEST 8 : REMOVE FIRST
        try {
            iNumTests++;
            System.out.print("Testing removeFirst(): ");
            sTestString = (String)ll.removeFirst();
            if (sTestString != "abc")
                throw new IllegalArgumentException("FAILED.");

            sTestString = (String)ll.removeFirst();
            if (sTestString != "jkl")
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String)ll.removeFirst();
            if (sTestString != "xyz")
               throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED");}

        // TEST 9 : IS EMPTY 2
        try {
            iNumTests++;
            System.out.print("Testing isEmpty(): ");
            sTestString = (String)ll.removeFirst();
            System.out.println("FAILED");
        } catch(Exception e) { iNumPassed++; System.out.println("passed"); }

        // TEST 10 : INSERT FIRST
        try {
            iNumTests++;
            System.out.print("Testing insertFirst()");
            ll.insertFirst("abc");
            ll.insertFirst("jkl");
            ll.insertFirst("xyz");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED"); }
        
//---------------------------------------------------------------------------
        
        System.out.println("\nTest iterator");
        System.out.println("========================================================");

        // TEST 11: ITERATOR
        try {
            iNumTests++;
            System.out.print("Testing iterator(): ");
            i = ll.iterator();
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED");}

        // TEST 12: HAS NEXT
        try {
            iNumTests++;
            i = ll.iterator();
            System.out.print("Testing hasNext()");
            if(!i.hasNext())     
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED");}

        // TEST 12: NEXT
        try {
            iNumTests++;
            i = ll.iterator();
            System.out.print("Testing next(): ");
            sTestString = (String) i.next();
            if(!sTestString.equals("xyz"))
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String) i.next();
            if(!sTestString.equals("jkl"))
                throw new IllegalArgumentException("FAILED.");
            sTestString = (String) i.next();
            if(!sTestString.equals("abc"))
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED");}

        // TEST 13: HAS NEXT 2
        try {
            iNumTests++;
            i = ll.iterator();
            i.next();
            i.next();
            i.next();
            System.out.print("Testing hasNext(): ");  
            if(i.hasNext())
                throw new IllegalArgumentException("FAILED.");
            iNumPassed++;
            System.out.println("passed");
        } catch(Exception e) { System.out.println("FAILED");}
            
//---------------------------------------------------------------------------

        // PRINT TEST SUMMARY
        System.out.print("\nNumber PASSED: " + iNumPassed + "/" + iNumTests);
        System.out.print(" -> " + (int)((double)iNumPassed/iNumTests*100) + "%\n");
    }
//---------------------------------------------------------------------------
}
