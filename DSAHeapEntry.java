public class DSAHeapEntry
{
    public int priority;
    public Object value;

    public DSAHeapEntry(int inPriority, Object inValue)
    {   
        priority = inPriority;
        value = inValue;
    }
}