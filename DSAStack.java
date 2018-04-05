/*
FILE            : DSAStack.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class utilizes DSALinkedList to implement a stack, which
is a LIFO data structure. The stack is generic, hence it can
hold any single data type.

Notable public methods:
    - count(), Tells how many items in the stack
    - isEmty(), Return "true" if stack is empty
    - push(E value), put item in the stack
    - pop(): E, returns & remove the top item in the stack
    - top(): E, only returns the top item in teh stack
*/

import java.util.*;
import java.io.*;

class DSAStack <E> implements Serializable
{

    //CLASS FIELDS
    private DSALinkedList<E> ll;
    private int count;
    
    //CONSTRUCTORS
    public DSAStack()
    {
        ll = new DSALinkedList<E>();
        count = 0;
    }

    //PUBLIC METHODS 
    public int count(){
        return count;
    }

    public boolean isEmpty(){
        return count == 0;
    }

    public void push(E inValue){

        ll.insertFirst(inValue);
        count ++;
    }

    public E pop(){
        E topVal = ll.removeFirst();
        count --;
        return topVal;
    }

    public Object top(){
        Object topVal;

        if(isEmpty()){
            throw new IllegalArgumentException("Stack is empty");
        }
 
        topVal = ll.peekFirst();  

        return topVal;     
    }

    public Iterator<E> iterator()
    {
        return ll.iterator();
    }

}
