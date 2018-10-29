package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    
    public ArrayDictionary() {
        pairs = makeArrayOfPairs(20);
        this.size = 0;
    }
    
    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    public V get(K key) {
        
        int index = keyIndex(key);
        
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return pairs[index].value;
    }

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    public void put(K key, V value) {
        int index = keyIndex(key);
        if (index == -1) { 
            if (this.size == pairs.length) {
                Pair<K, V>[] newArray = makeArrayOfPairs(this.size * 2);
                for (int i = 0; i < this.size; i++) {
                    newArray[i] = pairs[i];
                }   
                pairs = newArray;
            }
            pairs[this.size] = new Pair<>(key, value);
            this.size++;
        } 
        else { 
            pairs[index].value = value;
        }
        
    }

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */    
    public V remove(K key) {
        int index = keyIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        V content = pairs[index].value;
        for (int i = index; i < this.size - 1; i++) {
            pairs[i] = pairs[i + 1];
        }
        this.size--;
        return content;
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    public boolean containsKey(K key) {
        return keyIndex(key) != -1;
    }
    
    /* 
     * Returns index of key if present in ArrayDictionary, -1 otherwise.
     */
    private int keyIndex(K key) {
        for (int i = 0; i < this.size; i++) {
            if (key == pairs[i].key || (key != null && pairs[i].key != null && key.equals(pairs[i].key))) { 
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    public int size() {
        return this.size;
    }
    
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(pairs, 0);
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        
        private int currInd;
        private Pair<K, V>[] dict;
        
        public ArrayDictionaryIterator(Pair<K, V>[] dict, int currInd) {
            this.currInd = currInd;
            this.dict = dict;
        }
        
        public boolean hasNext() {
            return currInd < dict.length && dict[currInd] != null;
        }

        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> fresh = new KVPair<K, V>(dict[currInd].key, dict[currInd].value);
            currInd++;
            return fresh;
        }
        // ...
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
    
