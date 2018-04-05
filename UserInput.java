/*
FILE            : UserInput.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class takes user input from the terminal and converted to the appropriate data type such as
integers, double, string, boolean.

It deals with exceptions that are associated with using the Scanner method that parse and convert
terminal input. One notable functionality is that it can assert integer/real input to be within a
certain range. As an convient, it can take in a prompt for the user in the form of a string.
*/

import java.util.*;

class UserInput
{
        /*********************************************************************************
         * PURPOSE : Output a prompt to the user to request an integer input             *
         * DATE    : 21/03/17                                                            *
         * IMPORT  : A prompt for the user in a form of  string                          *
         * EXPORT  : An integer                                                          *
         *********************************************************************************/

	public static int integerInput(int lowerLimit, int upperLimit, String prompt)
    {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        int integer = -1;

        while(!success)
        {
            try
            {
                System.out.print(prompt);
                integer = scanner.nextInt();

                while((integer < lowerLimit) || (integer > upperLimit))
                {
                    System.out.println("Please enter values between " + lowerLimit + " and " + upperLimit);
                    System.out.print(prompt);
                    integer = scanner.nextInt();
                }

                success = true;
            } catch(InputMismatchException e) {System.out.println("Please enter an integer!");}
            scanner.nextLine();
            
        }
       
        return integer;
    }

        /*********************************************************************************
         * PURPOSE : Output a prompt to the user to request a real input                 *
         * DATE    : 21/03/17                                                            *
         * IMPORT  : A prompt for the user in a form of  string                          *
         * EXPORT  : A real (double)                                                     *
         *********************************************************************************/

    public static double realInput(double lowerLimit, double upperLimit, String prompt)
    {
        Scanner scanner = new Scanner(System.in);
        double real = -1.0;
        boolean success = false;

        while(!success)
        {
            try
            {
                System.out.print(prompt);
                real = scanner.nextDouble();

                while((real < lowerLimit) || (real > upperLimit))
                {
                   System.out.println("Please enter values between " + lowerLimit + " and " + upperLimit);
                   System.out.print(prompt);
                   real = scanner.nextDouble();
                }

                success = true;
            } catch(InputMismatchException e) { System.out.println("Please enter a real!");}
            scanner.nextLine();
        }

        return real;
    }   

        /*********************************************************************************
         * PURPOSE : Output a prompt to the user, and take a user input                  *
         * DATE    : 21/03/17                                                            *
         * IMPORT  : A prompt for a user in the form of a string                         *
         * EXPORT  : A char                                                              *
         *********************************************************************************/

	public static String stringInput(String prompt)
	{
	    Scanner scanner = new Scanner(System.in);

	    System.out.print(prompt);
	    String string = scanner.nextLine();

	    return string;
	}

        /*********************************************************************************
         * PURPOSE : Output a prompt to the user, and take a user input                  *
         * DATE    : 21/09/17                                                            *
         * IMPORT  : A prompt for a user in the form of a string                         *
         * EXPORT  : A char                                                              *
         *********************************************************************************/

    public static boolean boolInput(String prompt)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print(prompt + "-y/n :> ");
        boolean r = false;
        boolean success = false;

        while(!success)
        {
            try
            {
                char chr = sc.nextLine().toLowerCase().charAt(0);

                while(chr != 'y' && chr != 'n')
                {
                    System.out.print("Please enter 'y' or 'n':> ");
                    chr = sc.nextLine().toLowerCase().charAt(0);
                }

                switch(chr)
                {
                    case 'y':
                        r = true;
                    break;

                    case 'n':
                        r = false;
                    break;
                }
                success = true;

            }
            catch(StringIndexOutOfBoundsException e )
            {
                System.out.print("Please enter 'y' or 'n':> ");
            }
        }
        
        return r;
    }

}