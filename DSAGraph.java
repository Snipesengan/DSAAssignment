/*
FILE            : FileIO.java
AUTHOR          : Nhan Dao
REFEERENCES     : N/A
COMMENTS        :

This class mains purpose is to generate a Undirected, simple graph
of locations. The graph will allows calculation of the shortest distance,
shortest time to get from one node to another node. The graph will also
allows calculation of nearby nodes from a given node in a given radius.

An important factor that determines how fast these calculation are made
is what type of data stucture/ADTs information are stored in. The graph
holds 2 important information.

    - A list of nodes in the graph. The nodes themselves are a private class
      which we will dicuss later.

    - A list of edges. They also have their on private class.

The nodes are stored using a Linked-list. This decision was made because
frequent iterations through a linked-list is required. Linked-list allows
iteration through each node at a time complexity of O(N), N ~ number of nodes.
This is the fastest time complexity possible. The reason we need to iterate 
through the node list will be outlined later, but it is mainly because we do
not know before hand which nodes in the graph we need to visit. Arrays are not
sutiable for this application because it cannot dynamically grow or shrink since
we want to be able add more nodes.

The edges are stored in a Binary Search tree, this is because edges contain
information about the two nodes that it connects. Since every edge will be unique 
in terms of the two nodes that it connects, we can append the nodes label to form 
a key for that edge. The path finding algorithm will always retrive the information
about the nodes first before needing to know the edge value. Hence BSTs will allow
retrieval of the edge faster ,O(log(N)), if we know the two nodes that it connects
before hand. Linked list are slower at finding at an time complexity of O(N). 

What dijkstra does:
    In this application of dijkstra's path finding algorithm, we update all the tentative
    value of a node with respect to a starting node. The tentative value simply means the
    effort or cost to get to that node. A tentative value of Double.MAX_VALUE means that
    it is impossible to get to that node from the startin node.

-----------------------------------------------------------------------------------
The path finding algorithm used was inspired by the youtube video:
https://www.youtube.com/watch?v=GazC3A4OQTE&t=482s

Notable mentions:
    Ryan Pyrc (For helping with comfirimg the fastest/shortest route)

*/
import java.util.*;
import java.io.*;

public class DSAGraph<E> implements Serializable
{

    //CLASS FIELDS
    private DSALinkedList<DSAGraphNode<E>> nodeList;
    private DSABinarySearchTree<DSAGraphEdge> edgeList;
    private int numEdge;
    private int numNode;

    public DSAGraph()
    {
        nodeList = new DSALinkedList<DSAGraphNode<E>>();
        edgeList = new DSABinarySearchTree<DSAGraphEdge>();
        numNode = 0;
        numEdge = 0;
    }

    //PUBLIC METHODS
    public E getValue(String label)
    {
        return getNode(label).getValue();
    }

   /*
        PURPOSE   : Generate a new node in the graph
        IMPORT    : A label (String), value the node will store
        EXPORT    : nothing
        Assertions: The node label must be unique 
    */
    public void newNode(String label, E value)
    {
        DSAGraphNode<E> node = null;
        Iterator<DSAGraphNode<E>> i = nodeList.iterator();

        while(i.hasNext()) { // checks whether the key already exists
            node = i.next();
            if(node.getLabel().equals(label))
                throw new IllegalArgumentException("A node with the label \"" + label + "\" already exists");

        }

        nodeList.insertLast(new DSAGraphNode<E>(label,value)); //Insert the new node to the node list
        numNode++;       
    }

    /*
        PURPOSE   : Update the impairment of an edge
        IMPORT    : label of node 1, label of node 2, the transport mode, impairment value (0 - 100)
        EXPORT    : nothing
    */
    public void updateImpairment(String src, String dest, String transMode, int impairment)
    {
        DSAGraphEdge edge = getEdge(src,dest);
        edge.updateImpairment(transMode, impairment);
    }


    /*
        PURPOSE   : Add a new edge to the graph
        IMPORT    : Labels of the two node, the transport mode, time it takes, distance, and the peak time
        EXPORT    : nothing
    */
    public void addEdge(String srcLabel, String destLabel, String travelMode, int time, double displacement, int peakTime)
    {
        //Find the two nodes by looking for the label
        DSAGraphNode<E> srcNode = getNode(srcLabel);
        DSAGraphNode<E> destNode = getNode(destLabel);
        DSAEdgeCost cost = new DSAEdgeCost(travelMode, displacement, time, peakTime);

        if(!srcNode.isAdj(destNode)) //if nodes aren't adjacent to each other
        {
            //Add both node to each other adj-list because its an undirected graph
            srcNode.insertAdj(destNode);
            destNode.insertAdj(srcNode);

            //then insert the new edge into the BST
            edgeList.insert(srcLabel + "," + destLabel, new DSAGraphEdge(srcNode, destNode, cost));
            numEdge++;
        }
        else //if nodes is adjacent to each other already
        {
            //We must update the cost field of the edge between the two nodes
            DSAGraphEdge edge = getEdge(srcLabel, destLabel); //retrieve the edge using the label
            edge.addCost(cost); //add the cost to that edge
        } 
    }

    /*
        PURPOSE   : Because we are storing location 
        IMPORT    : Labels of the two node, the transport mode, time it takes, distance, and the peak time
        EXPORT    : nothing
        * NOTE - this might be a bad method because it create unecessary association between DSAGraph and
                 Location. Although i'm not sure... 
    */
    public String generateLabel(Location location)
    {
        String label = location.getName() + "," + location.getStateName();

        return label;
    }

    public void displayAdjList() //Just prints the adjacentcy list of every node
    {
        Iterator<DSAGraphNode<E>> i = nodeList.iterator();
        DSAGraphNode<E> node;

        while(i.hasNext()) {
            node = i.next();
            System.out.println(node.toString());
        }
    }

    public void displayEdgeList() //prints all the edges
    {
        DSAQueue<DSAGraphEdge> q = edgeList.traverse(1); //get a queue of edges by traversing the BST
        DSAGraphEdge edge;

        while(!q.isEmpty()) {
            edge = q.dequeue();
            System.out.println(edge.toString());
        }
    }


    /*
        PURPOSE   : Finds all the node within a given node in a given radius. Then print them to the user
        IMPORT    : The source node, a radius 
        EXPORT    : nothing
    */
    public void nearbySearch(String source, double radius)
    {
        updateTentative(source, true, null, false); //update the tentative value for all the nodes with respect to the source node
                                                    //The import paremeters means, calculate shortest distance, any transport mode,
                                                    //and no peak time.

        //Once the tentative value is updated. All thats left to do is iterate through the list of nodes and find out which one has
        //their tentative value set to less of the radius.

        Iterator<DSAGraphNode<E>> i = nodeList.iterator();

        System.out.println("Nearby locations " + radius + " km" + " from " + source + ".");
        while(i.hasNext())
        {
            DSAGraphNode<E> node = i.next();
            double distance = node.getTentative();

            if(distance <= radius && distance > 0)
            {
                System.out.println(node.getLabel() + ", " + distance + " km.");
            }
        }
    }

    /*
        PURPOSE   : Finds how to get from one node to another
        IMPORT    : Starting and destination node, and settings for the path finding algorithim.
        EXPORT    : nothing
    */
    public void travelSearch(String source, String dest, boolean shortDist, String transportMode, boolean peak, boolean verbose)
    {
        //shortDist is a boolean which tells the path finding to look for the shortest path or fastest time.
        //true means finds shortest path, false means find the fastest path

        //tranpsort mode is self explanatory

        //peak is a boolean which tells if its peak time or not. True means its peak time.

        //Verbose is a boolean which tell whether to print detailed information or not.

        updateTentative(source, shortDist, transportMode, peak);
        printTravelPath(getNode(source), getNode(dest), 0.0, 0, peak, verbose); //printTravelPath is a recursive method to backtrack from
                                                                                //the destination node to the starting node and prints the
                                                                                //detail. It is explained below.
    }

    //PRIVATE METHODS
    /*
        PURPOSE   : This method implements Dijkstra path finding to update the tentive value of all the nodes
        IMPORT    : Starting and destination node, and settings for the path finding algorithim.
        EXPORT    : nothing
    */
    private void updateTentative(String source, boolean shortDist, String transportMode, boolean peak)
    {
        resetVisited();
        DSAHeap heap = new DSAHeap(nodeList.count());//initialize the heap with enough space to store all nodes
        DSAGraphNode<E> node,nextNode; //node is the current node visted, nextNode is the next node to be visited
        Iterator<DSAGraphNode<E>> i;
        double cost, tentative;

        node = getNode(source);
        node.setTentative(0.0);//make the tenative distance of the starting node 0
        heap.add((int) (node.getTentative() * 100), node);//add it to the heap

        while(!heap.isEmpty())
        {
            node = (DSAGraphNode<E>) heap.remove();
            i = node.iterator();
            node.setVisited(true);            

            while(i.hasNext())
            {
                nextNode = i.next();
                if(!nextNode.visited())//if the node haven't been visited
                {
                    if(transportMode == null) //if theres no specific transport mode
                    {
                        if(shortDist) //if the user wants the shortest distance
                        {
                            cost = getEdge(node.getLabel(), nextNode.getLabel()).getShortestDistance();
                        }
                        else //if the user wants the shortest time
                        {
                            cost = (double) getEdge(node.getLabel(), nextNode.getLabel()).getShortestTime(peak); 
                        }
                    }
                    else //if theres a tranpsport mode the user wants
                    {
                        cost = getEdge(node.getLabel(), nextNode.getLabel()).getCost(transportMode);
                    }
                   
                    tentative = node.getTentative() + cost; //calculate the tentative value for the next node

                    if(tentative < nextNode.getTentative()) //if the tenative value is smaller
                    {
                        nextNode.setTentative(tentative);//set the tentaive value for the next node
                        nextNode.setPrev(node);//update the next node previous node referenece to the current node.
                        heap.add((int) (tentative * 100), nextNode);//add that node to the heap
                    }
                }
            }
        }
    }

    /*
        PURPOSE   : This method uses backtracking to print the path of travel
        IMPORT    : Starting and current node, and settings for the path finding algorithim.
        EXPORT    : nothing
    */
    private void printTravelPath(DSAGraphNode<E> start, DSAGraphNode<E> curr, double dist, int time, boolean peak, boolean verbose)
    {
        DSAGraphNode<E> prev = curr.getPrev();
        DSAGraphEdge edge;
        DSAEdgeCost cost;

        if(prev == null) //if the previous pointer of a node equals null, which means that it was never updated by updateTentative(). Hence it
                         //couldn't be reached from our starting node
            throw new IllegalArgumentException("No such path found");

        edge = getEdge(prev.getLabel(),curr.getLabel()); //get the edge
        cost = edge.getUsedCost(); //get the last used cost of that edge, the most recent cost value to calculate the tentative value.
        dist += cost.getDistance();

        if(peak)
        { time += cost.getPeakTime(); }
        else
        { time += cost.getTime(); }
        

        if(prev != start)//recurse only if the current node previous node reference is not the starting node
        {
            printTravelPath(start,prev,dist,time, peak, verbose);
        }
        else
        {   
            System.out.println("Total distance:   " + dist + "km");
            System.out.println("Total time    :   " + time/3600 + ":" + (time/60 - ((time/3600)*60)));
            System.out.println();
        }

        if(verbose) //if verbose, prints the location information
        {
            System.out.println("FROM | " +prev.getValue().toString());
            System.out.println("TO   | " + curr.getValue().toString());
        }
        else //else just print the label for the two node
        {   
            System.out.println("FROM | " + prev.getLabel());
            System.out.println("TO   | " + curr.getLabel());
        }

        System.out.println("COST | " + cost.toString()); //print the cost
        System.out.println();
    }

    /*
        PURPOSE   : Get a node using the label its under
        IMPORT    : label (String)
        EXPORT    : DSAGrahNode<E>
    */
    private DSAGraphNode<E> getNode(String nodeLabel)
    {
        //basically iterates through the nodeList and find the
        //node with the matching label
        //Throws and exception if the node is not found.

        Iterator<DSAGraphNode<E>> i = nodeList.iterator();
        DSAGraphNode<E> node = i.next();

        while (node != null && !node.getLabel().equals(nodeLabel)) {
            node = i.next();
        }

        if(node == null)
            throw new NoSuchElementException("Node \"" + nodeLabel + "\" not found!");

        return node;
    }

     /*
        PURPOSE   : Get an edge
        IMPORT    : labels of the two nodes that makes the edge
        EXPORT    : DSAGraphEdge
    */
    private DSAGraphEdge getEdge(String srcLabel, String destLabel)
    {
        DSAGraphEdge edge = null;

        //Because the edges are stored in Binary Search Tree
        //we can use the built in find() method. Because there
        //are two possibility of a label when the edge is created.
        //Eg: "Curtin,Sydney" OR "Sydney,Curtin"
        //If we cant find it with the first label, we must try the
        //second label.

        try 
        {
            edge = edgeList.find(srcLabel + "," + destLabel);
        }
        catch(NoSuchElementException e)
        {
            edge = edgeList.find(destLabel + "," + srcLabel);
        }

        return edge;
    }


    /*
        PURPOSE   : Reset all the fields of all nodes that may have been updated by the path finding algorithm
        IMPORT    : nothing
        EXPORT    : nothin
    */
    private void resetVisited()
    {
        Iterator<DSAGraphNode<E>> i = nodeList.iterator();
        DSAGraphNode<E> curr;

        while(i.hasNext()) {
            curr = i.next();
            curr.setVisited(false); //unvisit the node
            curr.setPrev(null);//set the node previous reference to null.
            curr.setTentative(Double.MAX_VALUE);//set the tentative value to max
        }
    }

    /*
    The following inner class DSAGraphNode<E> holds information about a node.
    Node contains one generic so it can hold any one piece of data. It is identified
    by a label in the form of a string. Here are the description of all the class fields:

        - label (String): Is how the node is going to be identified. (must be unique)

        - value (E generic): Information that the node holds.

        - adjList (DSALinkedList<DSAGraphNode<E>>): This is the adjacency list of the node
          .It represents all the node that is adjacent to itself. Linked List was used to
          store this information because often we would need to iterate through the adjacency
           list. Linked - list can also grow and shrinks therefore it is an approrpiate data structure
           as we do not know before hand how many nodes is adjacent.

        - visited is boolean which tells whether the node has been visited. Used in the pathfinding algorithm.

        - numAdj tells how many adjacent nodes there are.

        - prev (DSAGraphNode<E>): this value is set by the path finding algorithm. It represents the node which
          was used to get to this node. It is used to back track from the destination node to the starting node
          in order to retrieve the path.

        - tentative (double): is a value used by the path finding algorithm. This value was explained earlier.

    
    */

    private class DSAGraphNode<E> implements Serializable
    {
        private String label;
        private E value;
        private DSALinkedList<DSAGraphNode<E>> adjList;
        private boolean visited;
        private int numAdj;
        private DSAGraphNode<E> prev;
        private double tentative; //the d stands for "dijkstra"

        public DSAGraphNode(String inLabel, E inValue)
        {
            value = inValue;
            label = inLabel;
            numAdj = 0;
            adjList = new DSALinkedList<DSAGraphNode<E>>();
            visited = false;
            tentative = Double.MAX_VALUE; //represents infinity
            prev = null;
        }

        public String getLabel() { return label; }

        public E getValue() { return value; }

        public boolean visited() { return visited; }

        public void setVisited(boolean x) { visited = x; }

        public void setPrev(DSAGraphNode<E> inPrev) { prev = inPrev; }

        public DSAGraphNode<E> getPrev(){ return prev; }

        public int getNumAdj() { return numAdj; }

        public double getTentative(){ return tentative; }

        public void setTentative(double in) { tentative = in; }

        public void insertAdj(DSAGraphNode<E> node)
        {
            
            adjList.insertLast(node);
            numAdj++;
        }
        
        //Output the adjacentcy list
        public String toString()
        {
            String str = label + " --> ";
            Iterator<DSAGraphNode<E>> i = adjList.iterator();
            DSAGraphNode<E> curr;

            while(i.hasNext()) {
                curr = i.next();
                str += curr.getLabel() + "|";
            }

            return str;
        }


        public boolean isAdj(DSAGraphNode<E> node)
        {
            Iterator<DSAGraphNode<E>> i = adjList.iterator();
            Boolean isAdj = false;
            DSAGraphNode curr;

            while(i.hasNext() && isAdj == false) {
                curr = i.next();
                if(curr.getLabel().equals(node.getLabel()))
                    isAdj = true;
            }

            return isAdj;
        }

        public Iterator<DSAGraphNode<E>> iterator() { return adjList.iterator(); }        
    }

    /*
    DSAGraphEdge hold information about an edge. An edge is identified by its reference to the two
    nodes that connects to form the edge. THe most important information that the edge holds is the weight,
    or cost of the edge.

    Since there are many costs in an edge, a Linked-list was used to store it. A linked list was chosen because
    it allows iteration through the cost list to find the appropriate cost value.

    Cost value are stored using DSAEdgeCost object, which contains more specific information about the cost.
    */

    private class DSAGraphEdge
    {
        private DSAGraphNode from;
        private DSAGraphNode to;
        private DSALinkedList<DSAEdgeCost> costList;
        private DSAEdgeCost usedCost; //Used cost tells the most recent cost value that was exported.

        public DSAGraphEdge(DSAGraphNode inFrom, DSAGraphNode inTo, DSAEdgeCost cost)
        {
            from = inFrom;
            to = inTo;
            costList = new DSALinkedList<DSAEdgeCost>();
            addCost(cost);
            usedCost = null;
        }

        /*
            PURPOSE   : Add a new cost value to the edge
            IMPORT    : cost (DSAEdgeCost)
            EXPORT    : Nothing
            Assertion : The transport type for each cost in the cost list is unique
        */
        public void addCost(DSAEdgeCost inCost)
        {
            Iterator<DSAEdgeCost> i = costList.iterator();

            while(i.hasNext())
            {
                if(inCost.getTransportMode().equals(i.next().getTransportMode()))
                    throw new IllegalArgumentException("Error. Multiple definition for the same transport mode");
            }

            costList.insertLast(inCost);
        }


        /*
            PURPOSE   : Retrieve a cost in the form of a tentative value for a specific transport mode
            IMPORT    : transportMode(String)
            EXPORT    : tentative value (double)
        */
        public double getCost(String transportMode)
        {
            Iterator<DSAEdgeCost> i = costList.iterator();
            boolean found = false;
            double tentative = Double.MAX_VALUE; //sets the tentative value to max

            while(i.hasNext() && found == false) //iterate through the cost list and finds a cost with the same transport mode
            {
                DSAEdgeCost cost = i.next();
                if(cost.getTransportMode().equals(transportMode))
                {
                    found = true;
                    tentative = cost.getDistance();
                    usedCost = cost;
                }
            }

            return tentative;
        }

        /*
            PURPOSE   : Goes through the cost list and finds which cost contains the smallest distance
            IMPORT    : nothing
            EXPORT    : tentative value (double)
        */
        public double getShortestDistance()
        {
            Iterator<DSAEdgeCost> i = costList.iterator();
            DSAEdgeCost minCost = i.next();

            while(i.hasNext())
            {
                DSAEdgeCost cost = i.next();
                if(cost.getDistance() < minCost.getDistance())
                {
                    minCost = cost;
                }
            }

            usedCost = minCost;
            return minCost.getDistance();
        }

        /*
            PURPOSE   : Goes through the cost list and finds which cost contains the smallest time
            IMPORT    : peak (boolean), whether its peak time or not
            EXPORT    : tentative value (double)
        */
        public int getShortestTime(boolean peak)
        {
            Iterator<DSAEdgeCost> i = costList.iterator();
            DSAEdgeCost minCost = i.next();
            int time;

            if(peak)
            { time = minCost.getPeakTime(); }
            else
            { time = minCost.getTime(); }

            while(i.hasNext())
            {
                DSAEdgeCost cost = i.next();
                if(!peak)//if we dont want to find the peak time
                {
                    if(cost.getTime() < minCost.getTime())
                    {
                        minCost = cost;
                        time = minCost.getTime();
                    }
                }
                else //if we want to find the peak time
                {
                  if(cost.getPeakTime() < minCost.getPeakTime())
                    {
                        minCost = cost;
                        time = minCost.getPeakTime();
                    }  
                }
            }

            usedCost = minCost;
            return minCost.getTime();
        }


        /*
            PURPOSE   : Goes through the cost list and finds one with the matching transport mode and then impair it
            IMPORT    : transportMode(String), impairment(int)
            EXPORT    : nothing
        */
        public void updateImpairment(String transportMode, int impairment)
        {
            Iterator<DSAEdgeCost> i = costList.iterator();
            boolean found = false;
            DSAEdgeCost cost = null;

            while(i.hasNext() && found == false)
            {
                cost = i.next();
                if(cost.getTransportMode().equals(transportMode))
                    found = true;
            }

            if(found == false)
                throw new IllegalArgumentException("Transport mode \"" + transportMode + "\" does not exists");

            cost.setImpairment(impairment);
        }

        public DSAEdgeCost getUsedCost() { return usedCost; }

        public DSAGraphNode getFrom() { return from; }

        public DSAGraphNode getTo() { return to; }

        public String toString()
        {
            String str = "";
            str += "\"" + from.getLabel() + "\" <--> ";
            str += "\"" + to.getLabel() + "\"";

            return str;
        }

    }

}
