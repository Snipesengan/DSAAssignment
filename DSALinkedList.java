/*
FILE            : LinkedList.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This is an implementation of a double ended, doubly linked list. The list
can hold any single type of data because its been made generic. Here are 
some notable public methods:
    - insertFirst(E inValue): void
    - insertLast(E inValue): void
    - peekFirst(): E
    - peekLast(): E
    - removeFirst(): E
    - removeLast(): E
    - isEmpty(): boolean 
*/

import java.util.*;
import java.io.*;


class DSALinkedList <E> implements Iterable <E>, Serializable
{
    //CLASS FIELDS
    private DSAListNode<E> head;
    private DSAListNode<E> tail;
    private int count;

    //CONSTRUCTORS
    public DSALinkedList()
    {
        head = null;
        tail = null;
        count = 0;
    } 

    //PUBLIC METHODS
    public boolean isEmpty(){
        return (head == null) || (tail == null);

    }//isEmpty()

    public void insertFirst(E inValue){
        DSAListNode<E> newNode = new DSAListNode<E>(inValue);
 
        if(isEmpty()){
            head = newNode;
            tail = newNode;
        }
        else{
            head.setPrev(newNode);
            newNode.setNext(head);
            head = newNode;
        }
        count++;
 
    }//insertFirst()

    public void insertLast(E inValue){
        DSAListNode<E> newNode = new DSAListNode<E>(inValue);
 
        if(isEmpty()){
            head = newNode;
            tail = newNode;
        }
        else{
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        count++;

    }//insertLast()

    public E peekFirst(){
        if(isEmpty())
            throw new IllegalArgumentException ("List is empty");

        return head.getValue();
    }//peekFirst()

    public E peekLast(){
        if(isEmpty())
            throw new IllegalArgumentException ("List is empty");
     
        return tail.getValue();
    }//peekLast()
    
    public E removeFirst(){
        if(isEmpty())
            throw new IllegalArgumentException ("List is empty");       
     
        E obj = head.getValue();
        head = head.getNext();
        if(head != null)
            head.setPrev(null);
        else
            tail = null;

        count--;

        return obj;

    }//removeFirst()


    public E removeLast(){
        if(isEmpty())
           throw new IllegalArgumentException ("List is empty");

        E obj = tail.getValue();
        tail = tail.getPrev();
        if(tail != null)
            tail.setNext(null);
        else
            head = null;

        count--;

        return obj;   

    }//removeLast()

    public int count()
    {
        return count;
    }

    public Iterator<E> iterator(){
        return new DSALinkedListIterator<E>(this);
    }


    private class DSAListNode<E>
    {
        //Class fields
        private E m_value;
        private DSAListNode<E> m_next;
        private DSAListNode<E> m_prev;

        public DSAListNode(E inValue){
            m_value = inValue;
            m_next = null;
            m_prev = null;
        }

        public void setValue(E inValue){
            m_value = inValue;
        }
  
        public void setNext(DSAListNode<E> in_m_next){
            m_next = in_m_next;
        }

        public void setPrev(DSAListNode<E> in_m_prev){
            m_prev = in_m_prev;
        }    

        public E getValue(){ return m_value; }

        public DSAListNode<E> getNext(){ return m_next; }
        
        public DSAListNode<E> getPrev(){ return m_prev; }

    }

    private class DSALinkedListIterator<E> implements Iterator<E> {
        private DSALinkedList<E>.DSAListNode<E> iterNext; //This is the "Cursor" to keep track at where we are in the list
        
        //Constructor
        public DSALinkedListIterator(DSALinkedList<E> theList){
            iterNext = theList.head;
        } 

        //Iterator interface implementation
        public boolean hasNext(){ return (iterNext != null); }
        public E next(){
            E value;
            if(iterNext == null)
                value = null;
            else{
                value = iterNext.getValue();
                iterNext = iterNext.getNext();
            }
            return value;
        }

        public void remove() { throw new UnsupportedOperationException("Not Supported"); }
    }  

} 
