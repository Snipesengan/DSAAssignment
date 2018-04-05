//
//File   : UnitTestQueue.java
//Name   : Nhan Dao
//Unit   : DSA
//Purpose: Test DSAQueue.java
//


class UnitTestQueue
{
    public static void main(String args[])
    {
        int iNumpassed = 0;
        int iNumTests = 0;
        DSAQueue<Object> queue;
        //Should not throw Exceptions
        System.out.println("\n");
        System.out.println("Testing Normal Conditions -- Constructor");
        System.out.println("=======================================");
        try{
            iNumTests++;
            System.out.print("Testing the default creation of DSAQueue: " );
            queue = new DSAQueue<Object>();
            iNumpassed++;
            System.out.println("passed");
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing count: ");
            if(queue.count() != 0){
                throw new IllegalArgumentException ("FAILED");
            }
            iNumpassed++;
            System.out.println("passed");
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing isEmpty: ");
            if(!queue.isEmpty()){
                throw new IllegalArgumentException("FAILED");
            }       
            iNumpassed++;
            System.out.println("passed");
        }catch(IllegalArgumentException e){
            System.out.println("Illegal Argument: " + e.getMessage());

        }catch(Exception e){
            System.out.println("FAILED");
        }

        System.out.println("\n");
        System.out.println("Testing Normal Conditions -- queue methods");
        System.out.println("==========================================");
        try{
            int capacity = 20;
            
            /*------------------------------------------------------------------*/
            iNumTests++;
            System.out.print("Testing the alternate creation of DSAQueue: ");
            queue = new DSAQueue<Object>();
            iNumpassed++;
            System.out.println("passed");
            
            /*------------------------------------------------------------------*/
            //Test case 1: Start with a full queue, then dequeue 80% of it. Then enqueue 5 objects and ensuring that
            //             they dequeue in the correct order.
            //             Note that filling up the queue and dequeueling is to check that the implementation of a
            //             circular queue has been successful.
            iNumTests++;
            System.out.print("Testing case 1: ");
            try{
                int count = 0;
                Object obj1 = new Object();
                Object obj2 = new Object();
                Object obj3 = new Object();
                Object obj4 = new Object();
                Object obj5 = new Object();

                queue = new DSAQueue<Object>();

                queue.enqueue(obj1);
                queue.enqueue(obj2);
                queue.enqueue(obj3);
                queue.enqueue(obj4);
                queue.enqueue(obj5);

                while(!queue.isEmpty()){
                    
                    Object obj = queue.dequeue();
                    switch(count)                   
                    {
                        case 0:
                           if(obj != obj1)
                               throw new IllegalArgumentException("The 1st object is at an incorrect postion");
                           break;
                        case 1:
                            if(obj != obj2)
                               throw new IllegalArgumentException("The 2nd object is at an incorrect position");    
                           break;
                        case 2:
                           if(obj != obj3)
                               throw new IllegalArgumentException("The 3rd object is at an incorret postion");
                           break;
                        case 3:
                            if(obj != obj4)
                               throw new IllegalArgumentException("The 4th object is at an incorrect position");    
                           break;
                        case 4:
                           if(obj != obj5)
                               throw new IllegalArgumentException("The 5th object is at an incorret postion");
                           break;

                        default:
                            throw new IllegalArgumentException("The queue has more elements than it should");
                   }
                   count ++; 
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
        System.out.println("Testing Error Conditions - Queue methods");
        System.out.println("========================================");

        try{
            iNumTests++;
            queue = new DSAQueue<Object>();
            System.out.print("Testing dequeue when queue is empty: ");
            Object obj = queue.dequeue();
            System.out.println("FAILED");
        }
        catch(Exception e){iNumpassed++;System.out.println("passed");}            
        
    }//main()
    
}
