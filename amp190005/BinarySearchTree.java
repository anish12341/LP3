package amp190005;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/**
 * Implementation of Binary Search Tree.
 *
 * @author Courtney Erbes cte150030
 * @author Henil Doshi hxd180025
 * @version 1.0
 * @since 2020-02-23
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {
	/**
	 * Class to represent an entry in a BST
	 */
	static class Entry<T> {
		T element; // value of entry
		Entry<T> left, right; // left and right children of entry

		public Entry(T x) {
			this.element = x;
		}

		/**
		 * Constructor to create entry in BST
		 * 
		 * @param x     The value of the new entry
		 * @param left  The left child of the new entry
		 * @param right The right child of the new entry
		 */
		public Entry(T x, Entry<T> left, Entry<T> right) {
			this.element = x;
			this.left = left;
			this.right = right;
		}
	}

	Entry<T> root; // root of BST
	int size; // size of BST
	Stack<Entry<T>> parents = new Stack(); // path from parent to current node
	Entry<T> splicedChild;
	String direction;

	/**
	 * Constructor for basic, empty BST
	 */
	public BinarySearchTree() {
		root = null;
		size = 0;
	}

	/**
	 * This method checks if element(x) is contained in tree or not
	 * 
	 * @param x Element which is being searched
	 * @return boolean true if element is found, else false
	 */
	public boolean contains(T x) {
		Entry<T> ent = find(x);
		if (ent == null || x.compareTo(ent.element) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * This method finds the element(x) in the tree if it exists. This method is
	 * used by other methods in this class.
	 * 
	 * @param x Element which is to be found
	 * @return Entry<T> node of tree if element found, else return entry where
	 *         failed
	 */
	private Entry<T> find(T x) {
		parents = new Stack();
		parents.push(null);
		Entry<T> result = findHelper(x, root);
		return result;
	}

	/**
	 * Helper method (used by find method) to find an element
	 * 
	 * @param x   Element which is being compared to
	 * @param ent Element which is being searched
	 * @return Entry<T> node of tree if element found, else return entry where
	 *         failed
	 */
	private Entry<T> findHelper(T x, Entry<T> ent) {
		 
		if (ent == null || x.compareTo(ent.element) == 0) {
			return ent;
		}

		if (ent.right == null && ent.left == null) {
			return ent;
		}
		while (true) {
			if (x.compareTo(ent.element) == 0) {
				break;
			} else if (x.compareTo(ent.element) < 0) {
				if (ent.left == null || ent.left.element == null) {
					break;
				}
				parents.push(ent);
				ent = ent.left;
			} else if (ent.right == null || ent.right.element == null) {
				break;
			} else {
				parents.push(ent);
				ent = ent.right;
			}
		}

		return ent;
	}

	/**
	 * This method checks if there is an element that is equal to x in the tree.
	 * Element in tree that is equal to x is returned, null otherwise.
	 * 
	 * @param x Element which is being compared to
	 * @return T Element in tree that is equal to x is returned, null otherwise
	 */
	public T get(T x) {
		Entry<T> ent = find(x);
		if (ent != null && x.compareTo(ent.element) == 0) {
			return ent.element;
		}
		return null;
	}

	/**
	 * This method adds element(x) to the tree. If tree contains a node with same
	 * key, don't add the duplicate element. Returns true if x is a new element
	 * added to tree, else false
	 * 
	 * @param x Element which we want to add in the tree
	 * @return boolean returns true if element is added to tree, else returns false
	 */
	public boolean add(T x) {
		if (size == 0) {
			root = new Entry<>(x, null, null);
			size++;
			return true;
		}
		Entry<T> ent = find(x);

		if (x.compareTo(ent.element) == 0) {
			return false;
		}
		parents.push(ent);

		if (x.compareTo(ent.element) < 0) {
			ent.left = new Entry(x, null, null);
		} else {
			ent.right = new Entry(x, null, null);
		}
		size++;
		return true;
	}

	/**
	 * This method removes the element(x) from the tree. Returns x if found,
	 * otherwise returns null
	 * 
	 * @param x Element which is going to be removed
	 * @return T element if found, else return null
	 */
	public Entry<T> remove(T x) {
		if (size == 0) {
			return null;
		}
		Entry<T> ent = find(x);

		if (x.compareTo(ent.element) != 0) {
			return null;
        }
        if (ent.left.element == null && ent.right.element == null) {
            this.splicedChild = null;
            splice(ent);
            size--;
            return ent;
        } else if (ent.left.element == null || ent.right.element == null) {
			if (ent.left.element == null) {
				this.splicedChild = ent.right;
			} else if (ent.right.element == null) {
				this.splicedChild = ent.left;
			}
			splice(ent);
			size--;
			return ent;
		} else {
			parents.push(ent);
			Entry<T> minRight = findHelper(x, ent.right);
			ent.element = minRight.element;
			this.splicedChild = minRight;
			splice(minRight);
			size--;
			return ent;
		}
	}

	/**
	 * Helper method used by remove(T x) method Precondition: ent has at most one
	 * child and stack has path from root to parent of ent
	 * 
	 * @param ent
	 * @return nothing
	 */
	private void splice(Entry<T> ent) {
		Entry<T> parent = parents.peek();
		Entry<T> child = ent.left.element == null ? ent.right : ent.left;
		/*
		 * System.out.println("Parent(splice): " + parent.element);
		 * System.out.println("Child(splice): " + child.element);
		 */
		
		
		if (parent == null) {
			root = child;
		} else if (parent.left == ent) {
			parent.left = child;
			direction = "left";
		} else {
			parent.right = child;
			direction = "right";
		}
	}

	/**
	 * This method returns splicedChild of removed Entry (Node)
	 * 
	 * @return Entry<T> splicedChild
	 */
	public Entry<T> getsplicedChild() {
		return this.splicedChild;
	}

	public String getDirection() {
		return this.direction;
	}

	/**
	 * This method returns minimum element of tree. It returns element if found,
	 * otherwise returns null (tree is empty).
	 * 
	 * @return T minimum element
	 */
	public T min() {
		if (root == null) {
			return null;
		}

		Entry<T> current = root;

		while (current.left != null) {
			current = current.left;
		}

		return current.element;
	}

	/**
	 * This method returns maximum element of tree. It returns element if found,
	 * otherwise returns null (tree is empty).
	 * 
	 * @return T maximum element
	 */
	public T max() {
		if (root == null) {
			return null;
		}

		Entry<T> current = root;

		while (current.right != null) {
			current = current.right;
		}

		return current.element;
	}

	/**
	 * This method creates an array with the elements using in-order traversal of
	 * tree
	 * 
	 * @return Comparable[] array with elements of tree
	 */
	public Comparable[] toArray() {
		if (root == null) {
			return new Comparable[0];
		}

		Comparable[] arr = new Comparable[size];
		Stack<Entry<T>> s = new Stack();
		Entry<T> current = root;
		int i = 0;

		// traverse the tree
		while (current != null || s.size() > 0) {
			while (current != null) {
				s.push(current);
				current = current.left;
			}
			current = s.pop();
			arr[i++] = current.element;
			current = current.right;
		}
		return arr;
	}

	/**
	 * Optional problem 2: Iterate elements in sorted order of keys Solve this
	 * problem without creating an array using in-order traversal (toArray()).
	 */
	public Iterator<T> iterator() {
		return null;
	}

	/**
	 * This the main method through which you can add, remove and print the elements
	 * of tree
	 * 
	 * @param args not used
	 * @return nothing
	 */

	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc;
		/*
		 * if (args.length > 0) { File file = new File(args[0]); sc = new Scanner(file);
		 * } else { sc = new Scanner(System.in); }
		 */
		File file = new File("G:\\Study\\Data Structures\\src\\amp190005\\sk-t01.txt");
		sc = new Scanner(file);

		String operation = "";
		long operand = 0;
		int modValue = 999983;
		long result = 0;
//		RedBlackTree<Long>  = new RedBlackTree<>();
		BinarySearchTree<Long> redBlackTree = new BinarySearchTree<>();
		// Initialize the timer
		Timer timer = new Timer();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
			case "Add": {
				operand = sc.nextLong();
				System.out.println("Add element: " + operand);
				if (redBlackTree.add(operand)) {
					result = (result + 1) % modValue;
				}
				break;
			}
			case "Remove": {
				operand = sc.nextLong();
				System.out.println("Remove element:" + operand);
				if (redBlackTree.remove(operand) != null) {
					result = (result + 1) % modValue;
				}
				break;
			}
			case "Contains": {
				operand = sc.nextLong();
				if (redBlackTree.contains(operand)) {
					result = (result + 1) % modValue;
					System.out.println("Tree contains:" + operand);
				} else {
					System.out.println("Tree DOES'T CONTAIN " + operand);
				}
				break;
			}

			/*
			 * case "Validate":{ boolean valid = redBlackTree.verifyRBT(); if(!valid)
			 * System.out.println("Invalid RBT"); else{ System.out.println("Valid RBT"); }
			 * break; }
			 */
			}
		}

		// End Time
		timer.end();

		System.out.println("Result:" + result);
		System.out.println(timer);
	}
}

/*
 * public static void main(String[] args) { BinarySearchTree<Integer> t = new
 * BinarySearchTree<>(); Scanner in = new Scanner(System.in);
 * while(in.hasNext()) { int x = in.nextInt(); if(x > 0) {
 * System.out.print("Add " + x + " : "); System.out.println(); t.add(x);
 * t.printTree(); } else if(x < 0) { System.out.print("Remove " + x + " : ");
 * t.remove(-x); t.printTree(); } else { Comparable[] arr = t.toArray();
 * System.out.print("Final: "); for(int i=0; i<t.size; i++) {
 * System.out.print(arr[i] + " "); } System.out.println(); return; } } }
 * 
 * public void printParents() { System.out.print("Parent's length: " +
 * parents.size() + " "); for (int i = 1; i < parents.size(); i++) {
 * System.out.print(parents.get(i).element + " -"); } // if (parents.peek() !=
 * null) // System.out.println("Top: " + parents.peek().element);
 * System.out.println(); }
 *//**
	 * This method prints tree size and then inorder traversal of tree
	 * 
	 * @return nothing
	 */
/*
 * public void printTree() { System.out.print("[" + size + "]");
 * printTree(root); System.out.println(); }
 * 
 *//**
	 * This is the helper method used by printTree() method for inorder traversal of
	 * tree
	 * 
	 * @param node root of the tree
	 * @return nothing
	 *//*
		 * void printTree(Entry<T> node) { if(node != null) { printTree(node.left);
		 * System.out.print(" " + node.element); printTree(node.right); } } }
		 */
/*
 * Sample input: 1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0
 * 
 * Output: Add 1 : [1] 1 Add 3 : [2] 1 3 Add 5 : [3] 1 3 5 Add 7 : [4] 1 3 5 7
 * Add 9 : [5] 1 3 5 7 9 Add 2 : [6] 1 2 3 5 7 9 Add 4 : [7] 1 2 3 4 5 7 9 Add 6
 * : [8] 1 2 3 4 5 6 7 9 Add 8 : [9] 1 2 3 4 5 6 7 8 9 Add 10 : [10] 1 2 3 4 5 6
 * 7 8 9 10 Remove -3 : [9] 1 2 4 5 6 7 8 9 10 Remove -6 : [8] 1 2 4 5 7 8 9 10
 * Remove -3 : [8] 1 2 4 5 7 8 9 10 Final: 1 2 4 5 7 8 9 10
 * 
 */
