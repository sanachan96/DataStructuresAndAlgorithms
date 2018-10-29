package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    public static final int REF = 500;
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < REF; i++) {
            list.add(i);
        }
        
        int k = 5;
        IList<Integer> top = Searcher.topKSort(k, list);
        assertEquals(k, top.size());
        int ind = 0;
        for (int item : top) {
            assertEquals(REF - k + ind, item);
            ind++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testSmallerThanKAndEqualToK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < REF; i++) {
            list.add(i);
        }
        
        int k = REF + 10;
        IList<Integer> top = Searcher.topKSort(k, list);
        assertEquals(REF, top.size());
        int ind = 0;
        for (int item : top) {
            assertEquals(ind, item);
            ind++;
        }
        
        IList<Integer> top2 = Searcher.topKSort(list.size(), list);
        assertEquals(list.size(), top2.size());
        ind = 0;
        for (int item : top2) {
            assertEquals(ind, item);
            ind++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testAllSameValue() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < REF; i++) {
            int num = (int) (Math.random() * 100);
            list.add(num);
            temp.add(num);
        }
        Collections.sort(temp);
        
        int k = 50;
        IList<Integer> top = Searcher.topKSort(k, list);
        int ind = 0;
        for (int item : top) {
            assertEquals(temp.get(REF - k + ind), item);
            ind++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testRandomValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> temp = new ArrayList<>();
        
        for (int i = 0; i < REF; i++) {
            int num = (int) (Math.random() * 100);
            list.add(num);
            temp.add(num);
        }
        Collections.sort(temp);
        
        for (int k = 0; k < REF; k+=10) {
            IList<Integer> top = Searcher.topKSort(k, list);
            assertEquals(k, top.size());
            int i = 0;
            for (int item : top) {
                assertEquals(temp.get(REF - k + i), item);
                i++;
            }
        }
    }
    
    @Test(timeout=SECOND)
    public void testAllNegative() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < REF; i++) {
            int num = (int) (Math.random() * (-100));
            list.add(num);
            temp.add(num);
        }
        Collections.sort(temp);
        
        int k = 300;
        IList<Integer> top = Searcher.topKSort(k, list);
        assertEquals(k, top.size());
        int i = 0;
        for (int item : top) {
            assertEquals(temp.get(REF - k + i), item);
            i++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testCharacter() {
        IList<Character> list = new DoubleLinkedList<>();
        List<Character> temp = new ArrayList<>();
        for (int i = 0; i < REF; i++) {
            char r = (char) ((int) (Math.random() * 25 + 65));
            list.add(r);
            temp.add(r);
        } 
        Collections.sort(temp);
        
        for (int i = 0; i < REF; i+=20) {
            IList<Character> top = Searcher.topKSort(i,  list);
            int ind = REF - i;
            for (char c : top) {
                assertEquals(temp.get(ind), c);
                ind++;
            }
        }
    }
    
    @Test(timeout=SECOND)
    public void testString() {
        IList<String> list = new DoubleLinkedList<>();
        List<String> temp = new ArrayList<>();
        
        for (int i = 0; i < REF; i++) {
            String result = "";
            int stringSize = (int) (Math.random() * 10 + 1);
            for (int j = 0; j < stringSize; j++) {
                char c = (char) ((int) (Math.random() * 25 + 65));
                result += c;
            }
            list.add(result);
            temp.add(result);
        }
        Collections.sort(temp);
        
        int k = 342;
        IList<String> top = Searcher.topKSort(k, list);
        int i = 0;
        for (String item : top) {
            assertEquals(temp.get(REF - k + i), item);
            i++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testDouble() {
        IList<Double> list = new DoubleLinkedList<>();
        List<Double> temp = new ArrayList<>();
        
        for (int i = 0; i < REF; i++) {
            Double num = Math.random() * 100;
            list.add(num);
            temp.add(num);
        }
        Collections.sort(temp);
        
        int k = 284;
        IList<Double> top = Searcher.topKSort(k, list);
        int i = 0;
        for (Double item : top) {
            assertEquals(temp.get(REF - k + i), item);
            i++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testIllArgException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < REF; i++) { 
            list.add(i);
        }
        
        try {
            IList<Integer> top = Searcher.topKSort(-1, list);
            fail("Did not catch call with negative k value");
        } catch (IllegalArgumentException e) {
            // Do nothing.
        }
        
        list.add(null);
        try {
            IList<Integer> top = Searcher.topKSort(1, list);
            fail("Did not catch null entry heap");
        } catch (IllegalArgumentException e) {
            // Do nothing.
        }
    }
    
    @Test(timeout=SECOND) 
    public void testListUnchanged() {
        IList<Double> list = new DoubleLinkedList<>();
        List<Double> copy = new ArrayList<>();
        for (int i = 0; i < REF; i++) { 
            double random = Math.random() * 1000;
            list.add(random);
            copy.add(random);
        }
        
        IList<Double> top = Searcher.topKSort(100, list);
        
        int ind = 0;
        for (Double d : list) {
            assertEquals(copy.get(ind), d);
            ind++;
        }
    }
    
    
}
