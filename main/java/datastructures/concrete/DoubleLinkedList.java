package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    /**
     * Adds the given item to the *end* of this DoubleLinkedList.
     */
    public void add(T item) {
        if (size == 0) {
            Node<T> first = new Node<T>(item);
            front = first;
            back = first;
        }
        else {
            back.next = new Node<T>(back, item, null);
            back = back.next;
        }
        size++;
    }

    /**
     * Removes and returns the item from the *end* of this IList.
     *
     * @throws EmptyContainerException if the container is empty and there is no element to remove.
     */
    public T remove() {
        
        if (size == 0) {
            throw new EmptyContainerException();
        }
        
        T cont = back.data;
        
        if (size == 1) {
            front = null;
            back = null;
        }
        else {
            back = back.prev;
            back.next = null; 
        }
        
        size--;
        return cont;
    }
    
    /* 
     * Returns the Node at the specified index
     */
    private Node<T> helpFindIndex(int index) {
        
        // traverse from front
        if ((size - 1)/2 >= index) {
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        else {
            Node<T> current = back;
            for (int i = 0; i < size - 1 - index; i++) {
                current = current.prev;
            }
            return current;
        }
    }
    
    /**
     * Returns the item located at the given index.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        
        if (index == 0) {
            return front.data;
        }
        else if (index == size - 1) {
            return back.data;
        }
        else {
            Node<T> atIndex = helpFindIndex(index);
            return atIndex.data;
        }
    }

    /**
     * Overwrites the element located at the given index with the new item.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) { 
            throw new IndexOutOfBoundsException();
        }
        if (this.size == 1) {
            front = new Node<T>(item);
            back = front;
        }
        else if (index == 0) {
            front = new Node<T>(null, item, front.next);
            front.next.prev = front;
        }
        else if (index == this.size - 1) {
            back = new Node<T>(back.prev, item, null);
            back.prev.next = back;
        } 
        else {
            Node<T> current = helpFindIndex(index);
            Node<T> freshNode = new Node<T>(current.prev, item, current.next);
            current.prev.next = freshNode;
            current.next.prev = freshNode;
        }
    }

    /**
     * Inserts the given item at the given index. If there already exists an element
     * at that index, shift over that element and any subsequent elements one index
     * higher.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
     */
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (this.size() == 0) {
            front = new Node<T>(null, item, null);
            back = front;
        }
        else if (index == 0) { 
            front = new Node<T>(null, item, front);
            front.next.prev = front;
        }
        else if (index == this.size()) {
            back = new Node<T>(back, item, null);
            back.prev.next = back;
        }
        else {
            Node<T> pointer = helpFindIndex(index);
            Node<T> insertion = new Node<T>(pointer.prev, item, pointer);
            pointer.prev = insertion;
            insertion.prev.next = insertion;
        }
        size++;
    }

    /**
     * Deletes the item at the given index. If there are any elements located at a higher
     * index, shift them all down by one.
     *
     * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
     */
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> pointer = helpFindIndex(index);
        if (this.size() == 1) {
            front = null;
            back = null;
        } 
        else if (index == 0) {
            front = front.next;
            front.prev = null;
        } 
        else if (index == this.size() - 1) {
            back = back.prev;
            back.next = null;
        }
        else {
            pointer.prev.next = pointer.next;
            pointer.next.prev = pointer.prev;
        }
        
        size--;
        return pointer.data;
    }

    /**
     * Returns the index corresponding to the first occurrence of the given item
     * in the list.
     *
     * If the item does not exist in the list, return -1.
     */
    public int indexOf(T item) {
        Node<T> current = front;
        int index = 0;
        while (current != null) {
            if (current.data == null && item == null) {
                return index;
            }
            if (current.data.equals(item)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /*
     * Returns the size of the DoubleLinkedList
     */
    public int size() {
        return size;
    }

    /**
     * Returns 'true' if this container contains the given element, and 'false' otherwise.
     */
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null; 
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            else {
                Node<T> contents = current;
                current = current.next;
                return contents.data;
            }
        }
    }
}
