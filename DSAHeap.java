/*
FILE            : DSAHeap.java
AUTHOR          : Nhan Dao
REFEERENCES     : DSA lecture 7
COMMENTS        :

This class implement a min-heap, which is way to store value
based on their "priority". Items with higher priority are 
moved to the top of the heap. Hence one obvious application
is to a sorting algorithm, which goes by the name "heap sort"

This data structure has many applications, its functionality as
a priority queue can be used for Dijkstra's pathfinding algorithm.

This implementation of the heap uses an array, hence there are obvious
limitation and benefits. These are:
    - Array has fix size, hence there always is a maxiumum amount of space.
    - We can utilize simple arithmetic to index where each item should go
      based on their priority. *Look in DSA lecture 7.

Notable public methods:
    - isEmty(), Return "true" if heap is empty
    - add(int, Object), put item in the heap
    - remove: E, returns & remove the top item in the heap
    - heapSort(Entry[]): Takes an array of entries and sort them
*/

public class DSAHeap
{
    //CLASS FIELDS
	private DSAHeapEntry[] m_heap;
	private int m_count;

    //CONSTRUCTORS
	public DSAHeap(int maxSize)
	{
		m_heap = new DSAHeapEntry[maxSize];
		m_count = 0;
	}

    //PUBLIC METHODS
    public boolean isEmpty()
    {
        return m_count == 0;
    }

	public void add(int priority, Object value)
	{
        if(m_count == m_heap.length)
            throw new IllegalArgumentException("The heap is full");

        //First we will create a new entry
        DSAHeapEntry newEntry = new DSAHeapEntry(priority, value);
        m_heap[m_count] = newEntry; //put the new entry next node in the tree
        trickleUp(m_count, m_heap); //trickle up until its at the right position
        m_count++;
	}//add

	public Object remove()
	{
        if(m_count == 0)
            throw new IllegalArgumentException("The heap is empty");

        DSAHeapEntry deletedEntry = m_heap[0]; //get the entry at the root
        m_heap[0] = m_heap[m_count - 1];
        trickleDown(0,m_heap,m_count);
        m_count --;

        return deletedEntry.value;
	}//remove

	public void heapSort(DSAHeapEntry[] list, int numItems)
	{
        heapify(list, numItems);
        for(int i = numItems - 1; i > 0; i--)
        {
            DSAHeapEntry tmp = list[0];
            list[0] = list[i];
            list[i] = tmp;
            trickleDown(0, list, numItems);
        }
	}//heapSort

    public String toString()
    {
        String str = "[" + m_heap[0].value.toString() + ",";
        for(int i = 1; i < m_count - 1; i++)
        {
            str +=  " " + m_heap[i].value.toString() + "," ;
        }

        if(m_count > 1)
            str +=  " " + m_heap[m_count - 1].value.toString();

        return str += "]";
    }

    //PRIVATE METHODS
    private void heapify(DSAHeapEntry[] list, int numItems)
    {
        for(int i = numItems/2 - 1 ; i >= 0; i--)
        {
            trickleDown(i,list,numItems);
        }
    }

	private void trickleUp(int currIdx, DSAHeapEntry[] heapArray)
	{
		int parentIdx = (currIdx - 1)/2;

		if(currIdx > 0)
		{
			if(heapArray[currIdx].priority < heapArray[parentIdx].priority)
			{
				DSAHeapEntry tmp = heapArray[parentIdx];
                heapArray[parentIdx] = heapArray[currIdx];
                heapArray[currIdx] = tmp;
			}
            trickleUp(parentIdx, heapArray);
		}
	}

    private void trickleDown(int currIdx, DSAHeapEntry[] heapArray, int numItems)
    {
        int lChildIdx = currIdx * 2 + 1;
        int rChildIdx = lChildIdx + 1;

        if(lChildIdx < numItems) // there is a left child
        {
            int largeIdx = lChildIdx;
            if(rChildIdx < numItems) //there is a right child
            {
                if(heapArray[lChildIdx].priority > heapArray[rChildIdx].priority)
                    largeIdx = rChildIdx;
            }

            if(heapArray[largeIdx].priority < heapArray[currIdx].priority)//if 
            {
                DSAHeapEntry tmp = heapArray[largeIdx];
                heapArray[largeIdx] = heapArray[currIdx];
                heapArray[currIdx] = tmp;

                trickleDown(largeIdx, heapArray, numItems);
            }
        }
    }


}
