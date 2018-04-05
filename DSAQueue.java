/*
FILE            : DSAStack.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class utilizes DSALinkedList to implement a queue, which
is a FIFO data structure. The queue is generic, hence it can
hold any single data type.

Notable public methods:
    - count(), Tells how many items in the stack
    - isEmty(), Return "true" if stack is empty
    - enqueue(E value), put item in the queue
    - dequeue(): E, returns & remove the top item in the queue
    - peek(): E, only returns in front of the queue
*/

import java.util.*;
import java.io.*;

class DSAQueue <E> implements  Serializable
{
    //Unlike stack, queue shrinks on one side (the end)
    //and grow on the other (the front).
    //

    //CLASS FIELDS
    private DSALinkedList<E> ll; //The values in the queue
    private int count; //How many elements in the queue
    
    //CONSUTRUCTOR
    public DSAQueue()
    {
        ll = new DSALinkedList<E>();
        count = 0;
    }
    
    //PUBLIC METHODS
    public void enqueue(E inValue)// add item to the queue
    {

        ll.insertLast(inValue);
        count++;                
    }//enqueue()

    public E dequeue()// remove item from the queue by chaning the front index
    {
        E outValue = ll.removeFirst();
        count --;
 
        return outValue;
    }//dequeue()
        
    public E peek()// check the front item, but don't take it off
    {
        if(isEmpty())
            throw new IllegalArgumentException("Queue is empty");

        return ll.peekFirst();
    }//peek()

    public boolean isEmpty()//check if the queue is empty
    {
        return count == 0;

    }//isEmpty()  

    public int count()// number of elements in the queue
    {
        return count;
    }//count()

    public Iterator<E> iterator()
    {
        return ll.iterator();
    }

}
