//
//File   : UnitTestBinarySearchTree.java
//Author : Nhan Dao
//Unit   : DSA
//Purpose: To test DSABinarySearchTree.java
//

import java.io.*;
import java.util.*;

class UnitTestBinarySearchTree
{
    public static void main(String[] args){
        //Variables declartion
        int iNumPassed = 0;
        int iNumTests = 0;
        DSABinarySearchTree<String> bt = null;
        String sTestString;
        String[] nodeValue;
        String[] nodeKey;
        int lineNum;
        FileInputStream fileStrm = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;


//--------------------------------------------------------------------------

        System.out.println("\n\nTesting Normal Conditions - Constructor");
        System.out.println("=================================================");

        //  TEST 1  : CONSTRUCTOR
        try {
            iNumTests++;
            bt = new DSABinarySearchTree<String>();
            System.out.print("Testing creation of DSABinarySearchTree: ");

            //Asserting height
            if(bt.height() != -1)
               throw new IllegalArgumentException("Incorrect tree height | Height = " + bt.height()
                                                    + "| Expected height = -1");

            iNumPassed++;
            System.out.println("passed");
        }
        catch(Exception e){
            System.out.println("FAILED! " + e.getMessage());
        }

//--------------------------------------------------------------------------

        System.out.println("\n\nTest tree using RandomNames.csv");
        System.out.println("=================================================");

        try{
            bt = new DSABinarySearchTree<String>();
            fileStrm = new FileInputStream("RandomNames.csv");
            rdr = new InputStreamReader(fileStrm);
            bufRdr = new BufferedReader(rdr);
            nodeValue = new String[6];
            nodeKey = new String[6];


            //Test 2  :  INSERT
            iNumTests++;
            System.out.print("Testing insert(): ");
            sTestString = bufRdr.readLine();
            for(int i = 0; i < 6; i++){
                String tmp[] = sTestString.split(",");
                nodeKey[i] = tmp[0];
                nodeValue[i] = tmp[1];

                bt.insert(nodeKey[i], nodeValue[i]); //Insert the value into the tree
                sTestString = bufRdr.readLine();
            }
            System.out.println("passed");


            //TEST 3  :  HEIGHT
            iNumTests++;
            System.out.print("Testing height(): ");
            if(bt.height() != 2)
                throw new IllegalArgumentException("Incorrect tree height | Height = " + bt.height()
                                                    + "| Expected height = 2");
            iNumPassed++;
            System.out.println("passed");


            //TEST 4  : FIND
            iNumTests++;
            System.out.print("Testing find(): ");
            for(int i = 0; i < 6; i++){
                if(!nodeValue[i].equals(bt.find(nodeKey[i]))){
                    String str;
                    str =  "Incorrect values at node. Node key = " + nodeKey[i];
                    str += "| Expected value = " + nodeValue[i];
                    str += "| Actual value = " + bt.find(nodeKey[i]); 

                    throw new IllegalArgumentException(str);
                }
            }
            iNumPassed++;
            System.out.println("passed");


            //TEST 5   : DELETE
            iNumTests++;
            System.out.print("Testing delete(): ");
            for(int i = 0; i < 6; i++){
                bt.delete(nodeKey[i]);
            }
            iNumPassed++;
            System.out.println("passed");


        }
        catch(IOException e){
            System.out.println("!! Error reading RandomNames.csv !!");
        }
        catch(Exception e){
            System.out.println("FAILED! " + e.getMessage());
        }
        finally{ try{fileStrm.close();} catch(IOException ex2) { }}
    }
}

