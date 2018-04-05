//
//File   : UnitTestStack.java
//Name   : Nhan Dao
//Unit   : DSA
//Purpose: Test DSAStack.java
//


class UnitTestStack
{
    public static void main(String args[])
    {
        int iNumpassed = 0;
        int iNumTests = 0;
        DSAStack stack;

        //Should not throw Exceptions
        System.out.println("\n");
        System.out.println("Testing Normal Conditions -- Constructor");
        System.out.println("=======================================");
        try{
            iNumTests++;
            System.out.print("Testing the default creation of DSAStack: " );
            stack = new DSAStack();
            iNumpassed++;
            System.out.println("passed");
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing count: ");
            if(stack.count() != 0){
                throw new IllegalArgumentException ("FAILED");
            }
            iNumpassed++;
            System.out.println("passed");
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing isEmpty: ");
            if(!stack.isEmpty()){
                throw new IllegalArgumentException("FAILED");
            }       
            iNumpassed++;
            System.out.println("passed");
            /*------------------------------------------------------------------*/
        }catch(IllegalArgumentException e){
            System.out.println("Illegal Argument: " + e.getMessage());

        }catch(Exception e){
            System.out.println("FAILED");
        }

        System.out.println("\n");
        System.out.println("Testing Normal Conditions -- Stack methods");
        System.out.println("==========================================");
        try{
            
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing the alternate creation of DSAStack: ");
            stack = new DSAStack();
            iNumpassed++;
            System.out.println("passed");

            /*------------------------------------------------------------------*/
            //Test case 1: Adding 3 test objects to the stack. Then checking the correct object are in their correct
            //             position by pop()'ing one object at a time
            iNumTests++;
            System.out.print("Testing case 2: ");
            try{
                Object obj1 = new Object();
                Object obj2 = new Object();
                Object obj3 = new Object();

                stack = new DSAStack();
                stack.push(obj1);
                stack.push(obj2);
                stack.push(obj3);

                for(int i = 1; i <= 3; i++){//Itterate three time, each time pop()'ing the stack and checking if 
                    switch(i)              //its the expected object
                    {
                    case 1:
                       if(stack.pop() != obj3)
                           throw new IllegalArgumentException("The 3rd object is at an incorrect postion");
                       break;
                    case 2:
                        if(stack.pop() != obj2)
                           throw new IllegalArgumentException("The 2nd object is at an incorrect position");    
                       break;
                    case 3:
                       if(stack.pop() != obj1)
                           throw new IllegalArgumentException("The 1st object is at an incorret postion");                                  
                       break;
                    }
                }
           }catch(IllegalArgumentException e){
               throw new IllegalArgumentException("FAILED, " + e.getMessage());
           }
           iNumpassed++;
           System.out.println("passed");
           
        }catch(IllegalArgumentException e){
            System.out.println("Illegal Argument: " + e.getMessage());
        }catch(Exception e){
            System.out.println("FAILED");
        }//End of normal conditions testing

        //Testing with error conditions (SHOULD throw exceptions)
        System.out.println("\n");
        System.out.println("Testing Error Conditions - Stack methods");
        System.out.println("========================================");

        try{
            iNumTests++;
            stack = new DSAStack();
            System.out.print("Testing pop() when stack is empty: ");
            Object obj = stack.pop();
            System.out.println("FAILED");
        }
        catch(Exception e){iNumpassed++;System.out.println("passed");}            
        
    }//main()
    
}
