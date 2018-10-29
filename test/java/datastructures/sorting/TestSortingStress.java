package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    private char randChar() {
        return (char) ((int) (Math.random() * 25 + 65));
    }

    @Test(timeout=10*SECOND)
    public void testLarge() {
        IPriorityQueue<Integer> test = new ArrayHeap<Integer>();
        List<Integer> copy = new ArrayList<>();
        int large = 1000000;
        for (int i = large; i > 0; i--) {
            test.insert(i);
            copy.add(i);
            assertEquals(i, test.peekMin());
        }
        Collections.sort(copy);
        assertEquals(large, test.size());
        for (int i = 0; i < large; i++) {
            assertEquals(copy.get(i), test.peekMin());
            assertEquals(copy.get(i), test.removeMin());
        }
    }

    @Test(timeout=10*SECOND)
    public void testWeirdOrder() {
        IPriorityQueue<Integer> test = new ArrayHeap<Integer>();

        for (int i = 0; i < 30000; i++) {
            for (int j = 100; j > 0; j--) {
                test.insert(j);
            }
        }
        assertEquals(3000000, test.size());
        for (int i = 1; i <= 100; i++) {
            for (int j = 0; j < 30000; j++) {
                assertEquals(i, test.peekMin());
                assertEquals(i, test.removeMin());
            }
        }
    }

    @Test(timeout=10*SECOND)
    public void testKSortLarge() {
        IList<Double> test = new DoubleLinkedList<Double>();
        List<Double> comp = new ArrayList<Double>();
        int listSize = 5000;
        for (int i = 0; i < listSize; i++) {
            Double entry = Math.random() * 1000 - 500;
            test.add(entry);
            comp.add(entry);
        }
        Collections.sort(comp);

        for (int k = 0; k <= listSize; k+=2) {
            IList<Double> topKList = Searcher.topKSort(k, test); // Gives top k elements sorted
            int ind = listSize-k;
            for (Double d : topKList) {
                assertEquals(comp.get(ind), d);
                ind++;
            }
        }
    }

    @Test(timeout=10*SECOND)
    public void testKSortLargeString() {
        IList<String> list = new DoubleLinkedList<>();
        List<String> temp = new ArrayList<>();
        int listSize = 5000;
        for (int i = 0; i < listSize; i++) {
            int stringLength = (int) (Math.random() * 20); // Random string size
            String entry = "";
            for (int j = 0; j < stringLength; j++) { // Fill new string entry with random character
                Character r = randChar();
                entry = entry + r;
            }
            list.add(entry);
            temp.add(entry);
        }

        Collections.sort(temp);

        for (int k = 0; k <= listSize; k+=2) {
            IList<String> topKList = Searcher.topKSort(k, list); // Gives top k elements sorted
            int ind = listSize-k;
            for (String d : topKList) {
                assertEquals(temp.get(ind), d);
                ind++;
            }
        }
    }

    @Test(timeout=10*SECOND)
    public void testIntKSortLarge() {
        IList<Integer> test = new DoubleLinkedList<Integer>();
        List<Integer> comp = new ArrayList<Integer>();
        int listSize = 5000;
        for (int i = 0; i < listSize; i++) {
            int entry = (int) (Math.random() * 1000 - 500);
            test.add(entry);
            comp.add(entry);
        }
        Collections.sort(comp);

        for (int k = 0; k <= listSize; k+=2) {
            IList<Integer> topKList = Searcher.topKSort(k, test); // Gives top k elements sorted
            int ind = listSize-k;
            for (int d : topKList) {
                assertEquals(comp.get(ind), d);
                ind++;
            }
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testSearcherTopKSort() {
        int big = 500000;

        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> test = new ArrayList<Integer>();
        for (int i = big; i > 0; i--) {
            list.add(i);
            test.add(i);
        }
        Collections.sort(test);

        int k = 100000;
        IList<Integer> top = Searcher.topKSort(k, list);
        assertEquals(k, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(test.get(big - k + i), top.get(i));
        }

        k = 50000;
        IList<Integer> top2 = Searcher.topKSort(k, list);
        assertEquals(k, top2.size());
        for (int i = 0; i < top2.size(); i++) {
            assertEquals(test.get(big - k + i), top2.get(i));
        }
    }
}
