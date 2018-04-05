//
//File   : DSABinaryTreeSearch.java
//Author : Nhan Dao
//Unit   : DSA
//Purpose: To create a binary search tree
//

import java.util.*;
import java.io.*;

class DSABinarySearchTree<E> implements Serializable
{
    //Private TreeNode Class
    private class DSATreeNode<E> implements Serializable
    {
        //Class Fields
        private String m_key; 
        private E m_value;
        private DSATreeNode<E> m_leftChild;
        private DSATreeNode<E> m_rightChild;

        //Constructor
        public DSATreeNode(String inKey, E inVal){

            if (inKey == null)
                    throw new IllegalArgumentException("Key cannot be null");

            m_key = inKey;
            m_value = inVal;
            m_rightChild = null;
            m_leftChild = null;

        }

        //Accessor
        public String getKey(){
            return m_key;

        }

        public E getValue(){
            return m_value;
        }

        public DSATreeNode<E> getLeft(){
            return m_leftChild;
        }

        public DSATreeNode<E> getRight(){
            return m_rightChild;
        }

        //Mutator
        public void setLeft(DSATreeNode<E> newLeft){
            m_leftChild = newLeft;
        }

        public void setRight(DSATreeNode<E> newRight){
            m_rightChild = newRight;
        }
        
    } 

    private DSATreeNode<E> m_root; //The root node

    //Constructor
    public DSABinarySearchTree()
    {
        m_root = null; //Start with an empty tree
    }

    //Public methods 
    //-- These are wrapper methods that will call recursive functions of itself
    public E find (String key){
        return findRec(key, m_root);
    }

    public void insert(String key, E value){
        m_root = insertRec(key,value,m_root);
    }

    public void delete(String key){ //Hardest Shit
        m_root = deleteRec(key, m_root);
    }

    //toString & display
    public String toString(){
        return null;
    }

    public void display(){
        displayRec(m_root, 0);
    }

    //height (Shows how tall the tree is)
    public int height(){
        return heightRec(m_root);
    }

    public DSAQueue<E> traverse(int type)
    {
        DSAQueue<E> outQueue = new DSAQueue<E>();

        switch(type)
        {
            case 0:
                preOrder(outQueue,m_root);
                break;

            case 1:
                inOrder(outQueue,m_root);
                break;

            case 2:
                postOrder(outQueue, m_root);
                break;

            default:
                throw new IllegalArgumentException("Invalid traverse type");
        }

        return outQueue;
    }


    //Private methods
    //-- Recursive methods
    private E findRec(String key, DSATreeNode<E> cur){
        E val = null;

        if(cur == null)//Reaches the end of the tree without finding key
            throw new NoSuchElementException("Key " + key + " not found");
        else if (key.equals(cur.getKey())){ //if they key is found
            val = cur.getValue();
        }
        else if (key.compareTo(cur.getKey()) < 0){//if key < key at this node
            val = findRec(key,cur.getLeft());
        }
        else{//if key > key at this node
            val = findRec(key,cur.getRight());
        }

        return val;

    }

    private DSATreeNode<E> insertRec(String key, E data, DSATreeNode<E> cur){
        DSATreeNode<E> updateNode = cur;

        //Base case
        if(cur == null){
            updateNode = new DSATreeNode<E>(key,data);
            //*debug System.out.println("Inserted key: " + key + ", data: " + (String) data);
        }
        else if(key.equals(cur.getKey())){//If key equals current node.
            throw new IllegalArgumentException("key already exists");
        }
        else if (key.compareTo(cur.getKey()) < 0){//if key < key at this node
            cur.setLeft( insertRec(key, data, cur.getLeft()) );
        }
        else{//if key > key at this node
            cur.setRight( insertRec(key, data, cur.getRight()) ); 
        }
        

        return updateNode;
    }

    private int heightRec(DSATreeNode<E> cur){
        int htSoFar, iLeftHt, iRightHt;

        if(cur == null)
            htSoFar = -1; //Base case - no more along this branch, -1 to subtract
        else{
            iLeftHt = heightRec(cur.getLeft());
            iRightHt = heightRec(cur.getRight());

            // Get highest of left vs right branches
            if(iLeftHt > iRightHt)
                htSoFar = iLeftHt + 1;
            else
                htSoFar = iRightHt + 1;

        }

        return htSoFar;
    }

    private DSATreeNode<E> deleteRec(String key, DSATreeNode<E> cur){
        //like find() traverse down the tree until the key is found
        DSATreeNode<E> updateNode = cur;

        if(cur == null){
            throw new IllegalArgumentException("Node is not in the tree");//not in the tree
        }
        else if(key.equals(cur.getKey())){//found the key
            updateNode = deleteNode(key,cur);//the update node now replaces the deleted node
        }
        else if(key.compareTo(cur.getKey()) < 0){
            cur.setLeft(deleteRec(key,cur.getLeft()));
        }
        else{
            cur.setRight(deleteRec(key,cur.getRight()));
        }

        return updateNode;
    }

    private DSATreeNode<E> deleteNode(String key, DSATreeNode<E> delNode){
        DSATreeNode<E> updateNode = null;

        if(delNode.getLeft() == null && delNode.getRight() == null){//no child
            updateNode = null;
        }
        else if(delNode.getLeft() != null && delNode.getRight() == null){//one child - left
            updateNode = delNode.getLeft();
        }
        else if(delNode.getLeft() == null && delNode.getRight() != null){//one child - right
            updateNode = delNode.getRight();
        }
        else{//two children
            updateNode = promoteSuccessor(delNode.getRight());//promote successor finds 
            if(updateNode != delNode.getRight()){ // no cylces
                updateNode.setRight(delNode.getRight());
            }
            updateNode.setLeft(delNode.getLeft());
        }

        return updateNode;
    }

    private DSATreeNode<E> promoteSuccessor(DSATreeNode<E> cur){
        //finds the left most of the right subtree of cur node
        //This key is guarenteed to be bigger than any node to the left
        //of the deleted node. But smaller than every thing to the right
        //**see tree structure**

        DSATreeNode<E> successor = cur;
        
        if(cur.getLeft() != null){ //if there is a left child
            successor = promoteSuccessor(cur.getLeft()); //this recursive will unwind and update the links correctly
            if(successor == cur.getLeft()){ //parent of the successor
                cur.setLeft(successor.getRight()); //adopts the right child of the successor
            }
        }

        return successor;
    }

    private void displayRec(DSATreeNode<E> cur, int space){
        if (cur != null){
             // Increase distance between levels
            space += 5;
     
            // Process right child first
            displayRec(cur.getRight(), space);
     
            // Print current node after space
            // count
            System.out.println();
            for (int i = 5; i < space; i++)
                System.out.print(" ");

            System.out.print((String) cur.getKey());
     
            // Process left child
            displayRec(cur.getLeft(), space);
            System.out.println();

        }
       
    }

    private void inOrder(DSAQueue<E> queue, DSATreeNode<E> cur){
        if(cur != null){
            inOrder(queue, cur.getLeft()); //this will traverese to the left most child
            queue.enqueue(cur.getValue());
            inOrder(queue, cur.getRight());
        }
    }

    private void preOrder(DSAQueue<E> queue, DSATreeNode<E> cur){
        if(cur != null){
            queue.enqueue(cur.getValue());
            preOrder(queue,cur.getLeft()); //this will traverese to the left most child
            preOrder(queue,cur.getRight());
        }
    }

    private void postOrder(DSAQueue<E> queue, DSATreeNode<E> cur){
        if(cur != null){
            postOrder(queue,cur.getLeft());
            postOrder(queue,cur.getRight());
            queue.enqueue(cur.getValue());
        }
    }
}













