package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    
    // Map to point each index to the T item.
    private IDictionary<T, Integer> map = new ChainedHashDictionary<T, Integer>();
    // Integer to keep track of the size and the index.
    private int size;
    
    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        pointers = new int[10]; // need to resize when there's not enough space?
        size = 0;
    }
                                                            
    @Override
    public void makeSet(T item) {
        if (map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int length = pointers.length;
        if (length == size) {
            resize(length);
        }
        map.put(item, size);
        pointers[size] = -1; // always height of 0 = -1
        size++;
    }
    
    private void resize(int len) {
        int[] replace = new int[len * 2];
        for (int i = 0; i < len; i++) {
            replace[i] = pointers[i];
        }
        pointers = replace;
    }

    @Override
    public int findSet(T item) {
        // should probably be a recursive method 
        if (!map.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return findSetHelper(map.get(item));
    }
    
    private int findSetHelper(int index) {
        if (pointers[index] < 0) {
            return index;
        } else {
            int next = pointers[index];
            int root = findSetHelper(next);
            pointers[index] = root;
            return root;
        }
    }
    
    @Override
    public void union(T item1, T item2) {
        if (!map.containsKey(item1) || !map.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        // finds the contents of the array
        int first = findSet(item1);
        int second = findSet(item2);
        if (first == second) {
            throw new IllegalArgumentException();
        }

        int root = 0;
        int child = 0;
        // Since they're both negative, the taller height one is going to be LESS THAN the shorter height one,
        // the "smaller" number is going to be root
        if (pointers[first] > pointers[second]) {   
            root = second;
            child = first;
        } else { // if same rank, we chose this one to be default
            root = first;
            child = second;
            // if same rank, we increase the rank, -- because they are negative numbers
            if (pointers[first] == pointers[second]) {
                pointers[root]--;
            }
        }       
        pointers[child] = root;
    }
}
