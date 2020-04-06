package LP3.amp190005;

import java.util.Random;
import java.util.TreeSet;

/**
 * Testing for Java's HashSet and Cuckoo Hashing
 * 
 * @author Ayesha Gurnani      ang170003
 * @author Ishan Shah     ixs180019
 * @version 1.0
 * @since 2020-03-20
 */
public class TestFile {
    /**
     * Main method to test add, remove and contains on millions of input in Hashset and Cuckoo hashing
     * @param args No of keys, load factor and no of trials
     */
    public static void main(String[] args){

        int keys = 2000000; // No of keys
        if (args.length > 1) {
            keys = Integer.parseInt(args[0]);
        }

        int trials = 1; //No of trials
        if (args.length > 0) {
            trials = Integer.parseInt(args[2]);
        }
        

        Random random = new Random(); // to generate random numbers
        Integer[] arr = new Integer[keys]; // Array to store numbers
        SkipList<Integer> skp;
        TreeSet<Integer> treeSet;
        RedBlackTree<Integer> rbt;

        for (int x = 0; x < trials; x++) {
            System.out.println("\nTRIAL: " + x+1);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = random.nextInt(keys);
            }

            // Test for Java TreeSet
            System.out.println("Java TreeSet Performance:");
            Timer timer = new Timer();
            treeSet = new TreeSet<Integer>();
            for(int i = 0; i < arr.length; i++){
                treeSet.add(arr[i]);
                treeSet.contains(arr[i]);
            }
            for(int i = 0; i < arr.length; i++){
                treeSet.remove(arr[i]);
            }
            timer.end();
            System.out.println(timer);


            // Test for SkipList
            System.out.println("Skip List Performance:");
            timer = new Timer();
            skp = new SkipList<Integer>();
            for(int i = 0; i < arr.length; i++){
                skp.add(arr[i]);
                skp.contains(arr[i]);
            }
            for(int i = 0; i < arr.length; i++){
                skp.remove(arr[i]);
            }
            timer.end();
            System.out.println(timer);

            // Test for Red-Black Tree
            System.out.println("Red-Black Tree Performance:");
            timer = new Timer();
            rbt = new RedBlackTree<>();
            for(int i = 0; i < arr.length; i++){
                rbt.add(arr[i]);
                rbt.contains(arr[i]);
            }
            for(int i = 0; i < arr.length; i++){
                rbt.remove(arr[i]);
            }
            rbt.verifyRBT();
            timer.end();
            System.out.println(timer);
        }
    }
}
