package am2.navigation;

import java.util.ArrayList;

public class MinHeap
{        
	private int count;
    private int capacity;
    private BreadCrumb temp;
    private BreadCrumb mheap;
    private BreadCrumb[] array;
    private BreadCrumb[] tempArray;        

    public int Count()
    {
        return this.count;
    }

    public MinHeap() {  this(16); }

    public MinHeap(int capacity)
    {
        this.count = 0;
        this.capacity = capacity;
        array = new BreadCrumb[capacity];
    }

    public void BuildHead()
    {
        int position;
        for (position = (this.count - 1) >> 1; position >= 0; position--)
        {
            this.MinHeapify(position);
        }
    }

    public void Add(BreadCrumb item)
    {            
        this.count++;
        if (this.count > this.capacity)
        {
            DoubleArray();
        }
        this.array[this.count - 1] = item;
        int position = this.count - 1;

        int parentPosition = ((position - 1) >> 1);

        while (position > 0 && array[parentPosition].compareTo(array[position]) > 0)
        {
            temp = this.array[position];
            this.array[position] = this.array[parentPosition];
            this.array[parentPosition] = temp;
            position = parentPosition;
            parentPosition = ((position - 1) >> 1);
        }
    }        

    private void DoubleArray()
    {
        this.capacity <<= 1;
        tempArray = new BreadCrumb[this.capacity];
        CopyArray(this.array, tempArray);
        this.array = tempArray;
    }

    private static void CopyArray(BreadCrumb[] source, BreadCrumb[] destination)
    {
        int index;
        for (index = 0; index < source.length; index++)
        {
            destination[index] = source[index];
        }
    }

    public BreadCrumb Peek() throws Exception
    {
        if (this.count == 0)
        {
            throw new Exception("Heap is empty");
        }
        return this.array[0];
    }


    public BreadCrumb ExtractFirst() throws Exception
    {
        if (this.count == 0)
        {
            throw new Exception("Heap is empty");
        }
        temp = this.array[0];            
        this.array[0] = this.array[this.count - 1];
        this.count--;
        this.MinHeapify(0);
        return temp;
    }

    private void MinHeapify(int position)
    {
        do
        {
        	//get the array indices to the left and to the right of "position"
        	//left == position*2 + 1
        	//right == position*2 + 2, or left + 1
        	//the overlap should be there.
            int left = ((position << 1) + 1); //same as doing (position*2 + 1), but operationally significantly faster
            int right = left + 1;
            int minPosition;

            //get the minimum position between the left and the middle
            if (left < count && array[left].compareTo(array[position]) < 0)
            {
                minPosition = left;
            }
            else
            {
                minPosition = position;
            }

            //get the minimum position between the right and the minimum from the previous check
            if (right < count && array[right].compareTo(array[minPosition]) < 0)
            {
                minPosition = right;
            }

            //switch the position of the minimum and specified indices in the array if the minimum position is not the one passed in.
            if (minPosition != position)
            {
                mheap = this.array[position];
                this.array[position] = this.array[minPosition];
                this.array[minPosition] = mheap;
                position = minPosition;
            }
            else
            {
                return;
            }

        } while (true);
    }
}
