package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private static final int NUM = 10; 
    private int length;
    
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(NUM);
        length = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    public V get(K key) {
        int code = getCode(key, chains.length);
        
        // If dictionary retrieving from is null or it doesnt contain the key, throw exception.
        if (chains[code] == null || !chains[code].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[code].get(key);
    }

    public V remove(K key) {
        int hash = getCode(key, chains.length);
        
        if (chains[hash] == null || !chains[hash].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V result = chains[hash].get(key);
        chains[hash].remove(key);
        length--;
        return result;
    }

    @Override
    public boolean containsKey(K key) {
        int hash = getCode(key, chains.length);
        return chains[hash] != null && chains[hash].containsKey(key);
    }

    @Override
    public int size() {
        return length;
    }
    
    private int getCode(K key, int modVal) {
        int hash = 0;
        if (key != null) {
            hash = key.hashCode() % modVal;
        }
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }

    
    public void put(K key, V value) {
        // Need to check if the key is null before we call hashcode on it
        int code = getCode(key, chains.length);
        
        if (chains[code] == null) {
            chains[code] = new ArrayDictionary<K, V>();
        }
        if (!chains[code].containsKey(key)) {
            length++;
        }
        chains[code].put(key, value);
        if (length / chains.length >= 1) {
            resize();
        }
    }
    
    private void resize() {
        IDictionary<K, V>[] replace = makeArrayOfChains(chains.length * 2); // Replacement array, doubled size
        for (int i = 0; i < chains.length; i++) { // loop through all indices of chains
            IDictionary<K, V> curr = chains[i]; // Isolated dictionary at ith index of chains
            if (curr != null) { // If that dictionary is null, do nothing. Otherwise: 
                for (KVPair<K, V> key : curr) { // Iterate over pairs, add them to new array.
                    int code = getCode(key.getKey(), replace.length);
                    
                    if (replace[code] == null) { // if dictionary at specified location is null, 
                        replace[code] = new ArrayDictionary<K, V>(); // create a new dictionary there
                    }
                    replace[code].put(key.getKey(), key.getValue()); // put the pair in! 
                }
            }
        }
        chains = replace;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
<<<<<<< HEAD
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
=======
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
>>>>>>> e9154deef026c8fbe3e89b0544561f18888df89f
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int currBucket;
        private Iterator<KVPair<K, V>> iter;
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            currBucket = 0; 
        }
        
        // Returns the next bucket after the currently observed one that has objects.
        private int nextNonEmptyIndex(int thisBuck) {
            for (int i = thisBuck + 1; i < chains.length; i++) {
                if (chains[i] != null) {
                    return i;
                }
             }
            return -1;
        }

        public boolean hasNext() {
            if (chains[currBucket] == null) {
                int possNewIndex = nextNonEmptyIndex(currBucket);
                if (possNewIndex == -1) { 
                    return false;
                }
                currBucket = possNewIndex;
                iter = chains[currBucket].iterator();
            }
  
            if (iter == null) { // Iterator not created yet
                if (chains[currBucket] == null) { // check if current settled dict is null
                    currBucket = nextNonEmptyIndex(currBucket);
                }
                if (currBucket == -1) {
                    return false;
                }
                iter = chains[currBucket].iterator();
            } 
            if (!iter.hasNext()) {
                int possNewIndex = nextNonEmptyIndex(currBucket);
                if (possNewIndex == -1) {
                    return false; 
                }
                currBucket = possNewIndex;
                iter = chains[currBucket].iterator();
            }
            
            return true;
        }

        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
                        
            return iter.next();
        }
    }
}