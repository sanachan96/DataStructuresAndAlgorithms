package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    private static final int NUM_CHILDREN = 4;
    private T[] heap;
    private int length;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(10);
        this.length = 0;
    }
    
    private void resize() {
        T[] replace = makeArrayOfT(length * 2);
        for (int i = 0; i < length; i++) {
            replace[i] = heap[i];
        }
        heap = replace;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    public T removeMin() {
        if (length == 0) {
            throw new EmptyContainerException();
        }
        T item = heap[length - 1];
        T min = heap[0];
        heap[0] = item;
        boolean done = false; // false when not done, true when done
        int ind = 0;
        
        while (length > ind * NUM_CHILDREN + 1 && !done) { // (size > ind * NUM_CHILDREN + 1) has 1 or more children
            int minInd = ind * NUM_CHILDREN + 1;
            T minVal = heap[minInd];
            for (int i = 1; i < NUM_CHILDREN; i++) {
                int childInd = ind * NUM_CHILDREN + i + 1;
                
                if (childInd < length && minVal.compareTo(heap[childInd]) > 0) {
                    minVal = heap[childInd];
                    minInd = childInd;
                }
            }
            if (item.compareTo(minVal) > 0) {
                heap[minInd] = item;
                heap[ind] = minVal;
                ind = minInd;
            } else {
                done = true;
            }
        }
        
        length--;
        return min;
    }

    public T peekMin() {
        if (length == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (length == heap.length) {
            resize();
        }
        int ind = length;
        heap[ind] = item;
        
        percolateUp(ind);       
        length++;
    }

    public int size() {
        return length;
    }

    @Override
    public void remove(T item) {
        //throw new UnsupportedOperationException();
        int ind = 0;
        while (heap[ind] != item) {
            ind++;
        }
        heap[ind] = heap[length - 1];
        percolateUp(ind);
        heap[length - 1] = null;
        length--;
    }
    
    private void percolateUp(int ind) {
        while (ind != 0 && 0 > heap[ind].compareTo(heap[(ind-1)/NUM_CHILDREN])) {
            T temp = heap[ind]; 
            heap[ind] = heap[(ind-1)/NUM_CHILDREN];
            heap[(ind-1)/NUM_CHILDREN] = temp;
            ind = (ind-1)/NUM_CHILDREN;
        }  
    }
}
