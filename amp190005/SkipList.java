/* Starter code for LP3 */

// Change this to netid of any member of team
package LP3.amp190005;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int maxLevel = 32;
    Entry head, tail;
    int size;
    Entry[] pred;

    static class Entry<E>{
        E element;
        Entry[] next;
        Entry prev;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            // add more code if needed
        }

        public E getElement() {
            return element;
        }

        int height() {
            return next.length;
        }
    }

    // Constructor
    public SkipList() {
        head = new Entry(null, maxLevel + 1);
        tail = new Entry(null, maxLevel + 1);
        setNextTail(head, tail);
    }

    public void setNextTail(Entry head, Entry tail) {
        for (int i=0; i<head.height(); i++) {
            head.next[i] = tail;
        }
    }

    public void findPred(T x) {
        Entry curr = head;
        pred = new Entry[curr.height()];
        for (int i = curr.height()-1 ; i >= 0 ; i--) {
            while ((curr.next[i].getElement() != null) && (((T)curr.next[i].getElement()).compareTo(x) < 0)) {
                curr = curr.next[i];
            }
            pred[i] = curr;
        }
        return;
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
        if (contains(x)) return false;
        // height
        int height = chooseHeight();
        Entry entry = new Entry(x, height);
        for (int i=0 ; i < height ; i++) {
          entry.next[i] = pred[i].next[i];
          pred[i].next[i] = entry;
        }
        size++;
        return false;
    }

    public int chooseHeight() {
        Random r = new Random();
        int height = 1 + Integer.numberOfLeadingZeros(r.nextInt());
        return Math.min(height, maxLevel);
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        return null;
    }

    // Does list contain x?
    public boolean contains(T x) {
        findPred(x);
        return ((pred[0].next[0].getElement() != null) && (((T)pred[0].next[0].getElement()).compareTo(x) == 0));
    }

    // Return first element of list
    public T first() {
        return null;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        return null;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
        return null;
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        return null;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n).
    public T getLog(int n) {
        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return null;
    }

    // Return last element of list
    public T last() {
        return null;
    }


    // Not a standard operation in skip lists.
    public void rebuild() {

    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
    	if(!contains(x))
    		return null;
    	Entry entry = pred[0].next[0];
    	int height = entry.height();
    	for(int i=0; i<height; i++) {
    		pred[i].next[i] = entry.next[i];
    	}
    	size--;
        return x;
    }

    // Return the number of elements in the list
    public int size() {
        return size;
    }

    public void printSkipList() {
        for (int i=0; i < head.height(); i++) {
            Entry p = head;
            System.out.print("Level " + i + ": ");
            while (p != null) {
                System.out.print(p.getElement() + " -> ");
                p = p.next[i];
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Long returnValue = null;
        SkipList<Long> skipList = new SkipList<>();
        // Initialize the timer
        Timer timer = new Timer();

        System.out.println("Available operations");
        System.out.println("Add");
        System.out.println("Ceiling");
        System.out.println("First");
        System.out.println("Get");
        System.out.println("Last");
        System.out.println("Floor");
        System.out.println("Remove");
        System.out.println("Contains");
        System.out.println("Print");
        System.out.println("Size");
        System.out.println("Isempty");

        while (!((operation = sc.next()).equals("End"))) {
                switch (operation) {
                case "Add": {
                    operand = sc.nextLong();
                    if(skipList.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    skipList.printSkipList();
                    break;
                }
                case "Ceiling": {
                    operand = sc.nextLong();
                    returnValue = skipList.ceiling(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "First": {
                    returnValue = skipList.first();
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Get": {
                    int intOperand = sc.nextInt();
                    returnValue = skipList.get(intOperand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Last": {
                    returnValue = skipList.last();
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Floor": {
                    operand = sc.nextLong();
                    returnValue = skipList.floor(operand);
                    if (returnValue != null) {
                        result = (result + returnValue) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextLong();
                    if (skipList.remove(operand) != null) {
                        result = (result + 1) % modValue;
                        System.out.println("Removed Entry: "+operand);
                    }
                    else
                    	System.out.println(operand+" is not present in the Skiplist");
                    skipList.printSkipList();
                    break;
                }
                case "Contains":{
                    operand = sc.nextLong();
                    if (skipList.contains(operand)) {
                        result = (result + 1) % modValue;
                        System.out.println("Yes I am here");
                    } else {
                        System.out.println("No I am not");
                    }
                    break;
                }
                case "Print": {
                    skipList.printSkipList();
                    break;
                }
                case "Size": {
                    System.out.println("Size: " + skipList.size());
                    break;
                }
                case "Isempty": {
                    System.out.println(skipList.isEmpty());
                }
            }
        }

        // End Time
        timer.end();
        System.out.println(result);
        System.out.println(timer);
    }
}