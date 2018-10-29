package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    public IPriorityQueue<Integer> makeBasicIntHeap() {
        IPriorityQueue<Integer> test = new ArrayHeap<>();
        for (int i = 1; i <= 5; i++) {
            test.insert(i);
        }
        return test;
    }
    
    public IPriorityQueue<String> makeBasicStringHeap() {
        IPriorityQueue<String> test = new ArrayHeap<>();
        for (int i = 1; i <= 5; i++) {
            test.insert(i + "");
        }
        return test;
    }
    
    @Test(timeout=SECOND)
    public void testRandomEntries() {
        IPriorityQueue<Double> test = new ArrayHeap<Double>();
        List<Double> entries = new ArrayList<Double>();
        for (int i = 0; i < 100; i++) {
            Double entry = 1000 * Math.random();
            entries.add(entry);
            test.insert(entry);
        }
        Collections.sort(entries);
        for (int i = 0; i < 100; i++) {
            assertEquals(entries.get(i), test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
        heap.removeMin();
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND) 
    public void testIntPeekRemoveMin() {
        IPriorityQueue<Integer> test = makeBasicIntHeap();
        for (int i = 1; i <= 5; i++) {
            assertEquals(i, test.peekMin());
            assertEquals(i, test.removeMin());
        }
        try {
            test.peekMin();
            fail("Did not catch call on empty heap");
        }
        catch (EmptyContainerException e) {
            // Do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testIntInsert() {
        IPriorityQueue<Integer> test = new ArrayHeap<Integer>();
        for (int i = 10; i >= 0; i--) {
            test.insert(i);
        }
        for (int i = 0; i <= 10; i++) {
            assertEquals(11-i, test.size());
            assertEquals(i, test.peekMin());
            assertEquals(i, test.removeMin());
        }
        try {
            test.peekMin();
            fail("Did not catch call on empty heap");
        }
        catch (EmptyContainerException e) {
            // Do nothing
        }
    }
    
    public void testStringRemoveMin() {
        IPriorityQueue<String> test = makeBasicStringHeap();
        for (int i = 1; i <= 5; i++) {
            assertEquals(6 - i, test.size());
            assertEquals(i + "", test.peekMin());
            assertEquals(i + "", test.removeMin());
        }
        try {
            test.removeMin();
            fail("Did not catch call on empty heap");
        }
        catch (EmptyContainerException e) {
            // Do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testStringInsert() {
        IPriorityQueue<String> test = new ArrayHeap<String>();
        for (int i = 9; i >= 0; i--) {
            test.insert(i + "");
        }
       
        for (int i = 0; i < 10; i++) {
            assertEquals(10 - i, test.size());
            assertEquals(i + "", test.peekMin());
            assertEquals(i + "", test.removeMin());
        }
        
        try {
            test.peekMin();
            fail("Did not catch call on empty heap");
        } catch (EmptyContainerException ex) {
            // do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testNullInsert() {
        IPriorityQueue<String> test = new ArrayHeap<String>();
        try {
            test.insert(null);
            fail("Did not catch null entry");
        }
        catch(IllegalArgumentException e) {
            // do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testLarge() {
        IPriorityQueue<Integer> test = new ArrayHeap<Integer>();
        int large = 500;
        for (int i = large; i > 0; i--) {
            test.insert(i);
            assertEquals(i, test.peekMin());
        }
        assertEquals(large, test.size());
        for (int i = 1; i <= large; i++) {
            assertEquals(i, test.peekMin());
            assertEquals(i, test.removeMin());   
        }
        assertEquals(test.isEmpty(), true);
    }
    
    @Test(timeout=SECOND)
    public void testRandomNegativeEntries() {
        IPriorityQueue<Double> test = new ArrayHeap<Double>();
        List<Double> entries = new ArrayList<Double>();
        for (int i = 0; i < 100; i++) {
            Double entry = 1000 * Math.random() - 500;
            entries.add(entry);
            test.insert(entry);
        }
        Collections.sort(entries);
        for (int i = 0; i < 100; i++) {
            assertEquals(entries.get(i), test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testSimilarEntries() {
        IPriorityQueue<Integer> test = new ArrayHeap<Integer>();
        for (int i = 0; i < 100; i++) {
            test.insert(0);
            assertEquals(0, test.peekMin());
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(100 - i, test.size());
            assertEquals(0, test.removeMin());
        }
        assertEquals(0, test.size());
    }
    
    @Test(timeout=SECOND) 
    public void testPeekRemoveSame() {
        IPriorityQueue<Integer> test = makeBasicIntHeap();
        for (int i = 1; i <= 5; i++) {
            assertEquals(test.peekMin(), test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemove() {
        IPriorityQueue<Integer> test = makeBasicIntHeap();
        test.remove(2);
        assertEquals(test.peekMin(), 1);
        assertEquals(test.removeMin(), 1);
        assertEquals(test.peekMin(), 3);
        assertEquals(test.removeMin(), 3);
        assertEquals(test.peekMin(), 4);
        assertEquals(test.removeMin(), 4);
        assertEquals(test.peekMin(), 5);
        assertEquals(test.removeMin(), 5);
    }
    
    @Test(timeout=SECOND) 
    public void testEmptyExceptions() {
        IPriorityQueue<Integer> test = new ArrayHeap<>();
        
        try {
            test.peekMin();
            fail("Did not catch call on empty heap");
        }
        catch (EmptyContainerException e) {
            // Do nothing
        }
        try {
            test.removeMin();
            fail("Did not catch call on empty heap");
        }
        catch (EmptyContainerException e) {
            // Do nothing
        }
    }
}
