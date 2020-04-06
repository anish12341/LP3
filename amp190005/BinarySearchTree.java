

package LP3.amp190005;

import java.util.Scanner;
import java.util.Stack;
import java.util.Iterator;

public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
	        this.left = left;
	        this.right = right;
        }
    }
    
    Entry<T> root;
    int size;
    Stack<Entry<T>> parents = new Stack<>();

    public BinarySearchTree() {
	    root = null;
	    size = 0;
    }

    private Entry<T> find(T x) {
        parents = new Stack<>();
        return this.find(this.root,x);
    }

    public boolean contains(T x) {
        if(this.get(x) != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public T get(T x) {
         Entry<T> temp = this.find(x);
         if(temp == null || x.compareTo(temp.element) != 0) {
             return null;
         } else {
             return temp.element;
         }
    }



    private Entry<T> find(Entry<T> ent,T x) {
        while(true) {
           if(ent == null || x.compareTo(ent.element) == 0) {
               break;
           }
           else if(x.compareTo(ent.element) < 0) {
               if(ent.left == null) {
                   break;
               } else {
                   this.parents.push(ent);
                   ent = ent.left;
               }
           } else {
               if(ent.right == null) {
                   break;
               } else {
                   this.parents.push(ent);
                   ent = ent.right;
               }
           }
        }
        this.parents.push(ent);
        return ent;
    }

    public boolean add(T x) {
        Entry<T> elem = new Entry<>(x,null, null);
        return add(elem);
    }

    public boolean add(Entry<T> ent) {

        if(this.size == 0) {

            this.root = ent;
            size++;
            this.parents = new Stack<>();
            this.parents.push(ent);
            return true;
        }
        else {

            Entry<T> temp = this.find(ent.element);
            if(ent.element.compareTo(temp.element) == 0) {
                return false;
            } else if(ent.element.compareTo(temp.element) < 0) {
                temp.left = ent;
                size++;
            } else {
                temp.right = ent;
                size++;
            }
        }
        this.parents.push(ent);
        return true;

    }

    public T remove(T x) {

        if(this.size == 0) {
            return null;
        }
        Entry<T> temp = find(x);
        if(temp.element.compareTo(x) != 0) {
            return null;
        }
        if(temp.left == null || temp.right == null) {
            this.splice(temp);
            this.size--;
        }
        else {
            Entry<T> minRight = this.find(temp.right,x);
            temp.element = minRight.element;
            this.splice(minRight);
            this.size--;
        }

        return x;
    }

    private void splice(Entry<T> t) {

        Entry<T> splicedChild = t.left== null ? t.right:t.left;

        if(t == this.root) {
            this.root = splicedChild;
        }
        else {

            Entry<T> temp = this.parents.pop();
            Entry<T> parent = this.parents.peek();
            this.parents.push(temp);
            if (parent.left == t) {
                parent.left = splicedChild;
            } else {
                parent.right = splicedChild;
            }
        }
        this.parents.push(splicedChild);
    }

    public T min() {
	    return null;
    }

    public T max() {
        return null;
    }

    public Comparable[] toArray() {
        Comparable[] data = new Comparable[size];
        Entry<T> temp = this.root;
        Stack<Entry<T>> stack = new Stack<>();
        int idx = 0;
        stack.push(temp);
        leftTraversal(stack,temp);
        while (!stack.isEmpty()) {
            data[idx++] = stack.peek().element;
            temp = stack.pop();
            if(temp.right != null) {
                temp = temp.right;
                stack.push(temp);
                leftTraversal(stack,temp);
            }
        }
	return data;
    }

    public void leftTraversal(Stack<Entry<T>> stack,Entry<T> ent) {
        while (ent.left != null) {
            stack.push(ent.left);
            ent = ent.left;
        }
    }

    /** Optional problem 2: Iterate elements in sorted order of keys
	Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
	return null;
    }

    // Optional problem 2.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem 2.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem 2.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem 2.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

// End of Optional problem 2

    public static void main(String[] args) {
	BinarySearchTree<Integer> t = new BinarySearchTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }           
        }


    }

    public void printTree() {
	System.out.print("[" + size + "]");
	printTree(root);
	System.out.println();
    }

    void printTree(Entry<T> node) {
	if(node != null) {
	    printTree(node.left);
	    System.out.print(" " + node.element);
	    printTree(node.right);
	}
    }

}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
