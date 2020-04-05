package LP3.amp190005;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author vedant, vishal
 *
 * @param <T> Generic Type - can refer any Object
 */
public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	static class Entry<T> extends BinarySearchTree.Entry<T> {
		boolean color;

		// default color always inserted as RED
		Entry(T x, Entry<T> left, Entry<T> right) {
			super(x, left, right);
			color = RED;
		}

		Entry(T x) {
			super(x);
			color = BLACK;
		}

		boolean isRed() {
			return color == RED;
		}
		boolean isBlack() {
			return color == BLACK;
		}

		void setColor(boolean color) {
			this.color = color;
		}

		boolean isLeafNode(){
			if(left.element == null && right.element == null)
				return true;
			return false;
		}
	}

	Entry<T> NIL;
	RedBlackTree() {
		NIL = new Entry<>(null,null,null);
		NIL.color = BLACK;
		root = NIL;
	}

	/**
	 * Add x to tree. If tree contains a node with same key, replace element by x.
	 * Node is added using super function and the color is fixed here
	 * @return true if x is a new element added to tree and false otherwise
	 */
	public boolean add(T x) {
		boolean add = super.add(x);
		printParents();
		// RedBlackTree.Entry<T> curr = new Entry(bstCurr.element, NIL, NIL);
		if (add) {
			BinarySearchTree.Entry<T> curr = getCurrent(x);
			// initializing root node
			if (size == 1) {
				root = curr;
			} else {
				((Entry<T>)curr).setColor(RED);
				insertFix((Entry<T>) curr);
				((Entry<T>) root).color = BLACK;
			}
		}
		return add;
	}

	private void insertFix(Entry<T> curr) {
		Entry<T> parent = getParent(curr);
		while (curr != root && parent != null && parent.color != BLACK && curr.color != BLACK) {
			if (isLeftChild(parent, null)) {
				Entry<T> uncle = getUncle(curr, parent);
				if (uncle.element != null && uncle.color == RED) {
					parent.setColor(BLACK);
					uncle.setColor(BLACK);
					curr = getParent(parent);
					if (curr != null) {
						curr.setColor(RED);
						parent = getParent(curr);
					}
				} else {
					if (isRightChild(curr, parent)) {
						rotateLeft(parent);
						Entry<T> temp = curr;
						curr = parent;
						parent = temp;
					}
					if (parent != null) {
						parent.setColor(BLACK);
						Entry<T> grandParent = getParent(parent);
						if (grandParent != null) {
							grandParent.setColor(RED);
							rotateRight(grandParent);
						}
						curr = parent;
						parent = getParent(curr);
					}
				}
			} else {
				Entry<T> uncle = getUncle(curr, parent);
				if (uncle.element != null && uncle.color == RED) {
					parent.setColor(BLACK);
					uncle.setColor(BLACK);
					curr = getParent(parent);
					if (curr != null) {
						curr.setColor(RED);
						parent = getParent(curr);
					}
				} else {
					if (isLeftChild(curr, parent)) {
						rotateRight(parent);
						Entry<T> temp = curr;
						curr = parent;
						parent = temp;
					}
					if (parent != null) {
						parent.setColor(BLACK);
						Entry<T> grandParent = getParent(parent);

						if (grandParent != null) {
							grandParent.setColor(RED);
							rotateLeft(grandParent);
						}
						curr = parent;
						parent = getParent(curr);
					}
				}
			}
		}
	}



	/**
	 * To determine whether the current element is a left child of it's parent
	 * @param curr
	 * @return
	 */
	public boolean isLeftChild(Entry<T> curr, Entry<T> fromParent) {
		if (fromParent == null) {
			Entry<T> parent = (Entry<T>)parents.peek();
			if (parent != null && parent.left == curr) {
				return true;
			}
			return false;
		} else {
			if (fromParent.left == curr) {
				return true;
			}
			return false;
		}
	}

	/**
	 * To determine whether the current element is a right child of it's parent
	 * @param curr
	 * @return
	 */
	public boolean isRightChild(Entry<T> curr, Entry<T> fromParent) {
		if (fromParent == null) {
			Entry<T> parent = (Entry<T>)parents.peek();
			if (parent.right == curr) {
				return true;
			}
			return false;
		} else {
			if (fromParent.right == curr) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Method to get parent of curr
	 * @param curr Current node
	 */
	public Entry<T> getParent(Entry<T> curr) {
		return ((Entry<T>)parents.pop());
	}

	/**
	 * Method to get parent of curr
	 * @param curr Current node
	 */
	public Entry<T> getUncle(Entry<T> curr, Entry<T> parent) {
		Entry<T> grandParent = (Entry<T>) parents.peek();
		if (grandParent != null) {
			if (grandParent.left == parent)
				return (Entry<T>) grandParent.right;
			else if (grandParent.right == parent)
				return (Entry<T>) grandParent.left;
			else
				return null;
		}
		return null;
	}

	/**
	 * Method to get recently added element
	 * @param x Recently added element
	 */
	public RedBlackTree.Entry<T> getCurrent(T x) {
		if (parents.size() > 0) {
			Entry<T> parent = ((Entry<T>)parents.peek());
			convertToRBT(parent);
			if (parent != null) {
				if (parent.left != null && parent.left.element == x) {
					return (Entry<T>)parent.left;
				} else if (parent.right != null && parent.right.element == x) {
					return (Entry<T>)parent.right;
				}
			}
		}
		// return (RedBlackTree.Entry<T>)root;
		RedBlackTree.Entry<T> newRoot = new Entry<T>(x, NIL, NIL);
		newRoot.setColor(BLACK);
		return newRoot;
	}

	/**
	 * Convert BST.Entry to RBT.Entry
	 * @param curr Object of BST.entry
	 * @return Object of RBT.Entry
	 */
	public void convertToRBT(BinarySearchTree.Entry<T> curr) {
		if (curr.left.element != null) {
			BinarySearchTree.Entry<T> temp = curr.left;
			curr.left = new RedBlackTree.Entry<T>(temp.element, NIL, NIL);
		}
		else{
			curr.left = NIL;
		}
		if (curr.right.element != null) {
			BinarySearchTree.Entry<T> temp = curr.right;
			curr.right = new RedBlackTree.Entry<T>(temp.element, NIL, NIL);
		}
		else {
			curr.right = NIL;
		}
	}

	/**
	 * rotation of tree to the left around parentNode
	 * 		p				x
	 * 		 \		=> 	   /
	 * 		  \    		  /
	 * 		   x		 p
	 * @param parentNode - p as in above fig
	 */
	private void rotateLeft(Entry<T> parentNode) {
		Entry<T> temp = (Entry<T>) parentNode.right; // temp = p address
		parentNode.right = temp.left;
		temp.left = parentNode;
		if (parents.peek() != null && parents.peek().left == parentNode) {
			parents.peek().left = temp;
		} else if (parents.peek() != null && parents.peek().right == parentNode) {
			parents.peek().right = temp;
		} else {
			root = temp;
		}
	}

	/**
	 * rotation of tree to the right around parentNode
	 * 		p				x
	 * 	   /		=>		 \
	 * 	  /					  \
	 *   x					   p
	 * @param parentNode - p as in above fig
	 */
	private void rotateRight(Entry<T> parentNode) {
		Entry<T> temp = (Entry<T>) parentNode.left; // = node // temp = p address
		parentNode.left = temp.right;
		temp.right = parentNode;
		if (parents.peek() != null && parents.peek().left == parentNode) {
			parents.peek().left = temp;
		} else if (parents.peek() != null && parents.peek().right == parentNode) {
			parents.peek().right = temp;
		} else {
			root = temp;
		}
	}


	public Entry<T> remove(T x) {
		Entry<T> removed = (Entry<T>)super.remove(x);
		Entry<T> cursor = null;
		System.out.println("After removing:");
		print();
		Entry<T> splicedChild = (Entry<T>) super.getsplicedChild();

		if(splicedChild.element != null) {
			cursor = (Entry<T>) super.find(splicedChild.element);
		}
		if (removed.isLeafNode() && removed.color == RED)
			return removed;

		else if(removed.color == BLACK){
			System.out.println("Cursor: " + cursor.element);
			fixUp(cursor);
			return removed;
		}
		return null;
	}

	public void fixUp(Entry<T> cursor) {
		Entry<T> sibling;
		Entry<T> siblingL = null;
		Entry<T> siblingR = null;
		Entry<T> parent = getParent(cursor);
		System.out.println("Parent(fixUp): " + parent.element);

		if(cursor == root) {
			System.out.println("Cursor is root ");
			cursor.color = BLACK;
			return;
		}


		while (cursor != root && cursor.color == BLACK) {

//			Cursor is left child
			if (parent.left == cursor) {
				System.out.println("Cursor is left child");
				sibling = (Entry<T>) parent.right;
				siblingL = (Entry<T>) sibling.left;
				siblingR = (Entry<T>) sibling.right;

				if (sibling != null) {
					System.out.println("Sibling: " + sibling.element);
				}

//				case 1: sibling color is red
				if (sibling.color == RED) {
					System.out.println("Case 1: Sibling color is red");
					sibling.color = BLACK;
					parent.color = RED;
					rotateLeft(parent);
				}

//				case 2*: both children of sibling is black
				if (sibling.color == BLACK && siblingL.color == BLACK && siblingR.color == BLACK) {
					System.out.println("Case 2: both children of sibling is black");
					sibling.color = RED;
					cursor = parent;
				}

//				case 3: right child of sibling is black
				else {
					System.out.println("Case 3: right child of sibling is black");
					if (siblingR.color == BLACK) {
						siblingL.color = BLACK;
						sibling.color = RED;
						rotateRight(sibling);
					}


					System.out.println("Case 4");
//					case 4:
					siblingR.color = BLACK;
					sibling.color = parent.color;
					parent.color = BLACK;
					rotateLeft(parent);
					cursor = (Entry<T>) this.root;
				}
			}

//			cursor is right child
			else {
				System.out.println("Cursor is right child");
				sibling = (Entry<T>) parent.left;
				siblingL = (Entry<T>) sibling.left;
				siblingR = (Entry<T>) sibling.right;

//				case 1: Sibling color is red
				if (sibling.color == RED) {
					System.out.println("Case 1: Sibling color is red");
					sibling.color = BLACK;
					parent.color = RED;
					rotateRight(parent);
				}

//				case 2:  both children of sibling is black
				if (sibling.color == BLACK && siblingL.color == BLACK && siblingR.color == BLACK) {
					System.out.println("Case 2: both children of sibling is black");
					sibling.color = RED;
					cursor = parent;
				}

//				case 3: left child of sibling is black
				else {
					System.out.println("Case 3: left child of sibling is black");
					if (siblingL.color == BLACK) {
						siblingR.color = BLACK;
						sibling.color = RED;
						rotateLeft(sibling);
					}
						//case 4
					System.out.println("Case 4");
					siblingL.color = BLACK;
					sibling.color = parent.color;
					parent.color = BLACK;
					rotateRight(parent);
					cursor = (Entry<T>) this.root;

				}
			}
		}

		if(cursor.color == RED){
			cursor.color = BLACK;
		}
	}


	/**
	 * overrides BST printTree method
	 */
	public void printTree() {
		System.out.print("[" + size + "]");
		this.printTree((Entry<T>) root);
		System.out.println();
	}

	// In order traversal of RBTree
	public void printTree(Entry<T> node) {
		if (node != null) {
			// System.out.print("Type: " + node.getClass() + " ");
			printTree((Entry<T>) node.left);
			String s = node.color == BLACK ? (String) "B" : "R";
			if(node == NIL)
				System.out.print(" " + "NIL" + "" + s);
			else
				System.out.print(" " + node.element + "" + s);
			printTree((Entry<T>) node.right);
		}
	}

	public boolean isRootBlack(){
		if(root == NIL)
			return true;
		if(((Entry<T>)root).color == RED)
			return false;
		else
			return true;
	}

	public boolean validateNodes(Entry<T> node){

		Entry<T> leftNode= (Entry<T>) node.left;
		Entry<T> rightNode = (Entry<T>) node.right;

		if (node.isLeafNode()) {
			if(leftNode.color == RED  || leftNode.color == RED) {
				// Leafs should not be red
				System.out.println("NIL should not be red");
				return false;
			} else
				return true;
		}

		if (node.color == RED) {
			// You should not have two red nodes in a row

			if (leftNode.color == RED) {
				System.out.println("Adjacent red left nodes: "+ node.element + " and " + leftNode.element);
				return false;
			}
			if (rightNode.color == RED) {
				System.out.println("Adjacent red right nodes: "+ node.element + " and " + rightNode.element);
				return false;
			}
		}

		if (leftNode.element != null && !leftNode.isLeafNode()) {
			// Check BST property
			boolean leftCheck = leftNode.element.compareTo(node.element) <= 0;
			if (!leftCheck){
				System.out.println("Does not satisfy BST property for LEFT NODE " + leftNode.element);
				return false;
			}

			// Check red-black property
			leftCheck = this.validateNodes(leftNode);
			if (!leftCheck){
				System.out.println("Does not satisfy RBT property for LEFT NODE " + leftNode.element);
				return false;
			}
		}

		if (rightNode.element != null  && !rightNode.isLeafNode()) {
			// Check BST property
			boolean rightCheck = rightNode.element.compareTo(node.element) > 0;
			if (!rightCheck){
				System.out.println("Does not satisfy BST property for RIGHT NODE " + rightNode.element);
				return false;
			}
			// Check red-black property

			rightCheck = this.validateNodes(rightNode);
			if (!rightCheck) {
				System.out.println("Does not satisfy RBT property for RIGHT NODE " + rightNode.element);
				return false;
			}
		}
		return true;
	}

	class Height {
		int height = 0;
	}

	boolean isBalanced(Entry<T> root, Height height)
	{
		/* If tree is empty then return true */
		if (root.element == null) {
			height.height = 0;
			return true;
		}

		/* Get heights of left and right sub trees */
		Height lheight = new Height(), rheight = new Height();
		boolean l = isBalanced((Entry<T>) root.left, lheight);
		boolean r = isBalanced((Entry<T>) root.right, rheight);
		int lh = lheight.height, rh = rheight.height;

        /* Height of current node is max of heights of
           left and right subtrees plus 1*/
		height.height = (lh > rh ? lh : rh) + 1;

        /* If difference between heights of left and right
           subtrees is more than 2 then this node is not balanced
           so return 0 */
		if ((lh - rh >= 3) || (rh - lh >= 3))
			return false;

        /* If this node is balanced and left and right subtrees
           are balanced then return true */
		else
			return l && r;
	}

	/*public int computeHeight(Entry<T> node){
		if(node == NIL){
			return 0;
		}

		int leftHeight = computeHeight((Entry<T>) node.left);
		int rightHeight = computeHeight((Entry<T>) node.right);
		int add = node.color == BLACK ? 1 : 0;
		// The current subtree is not a red black tree if and only if
		// one or more of current node's children is a root of an invalid tree
		// or they contain different number of black nodes on a path to a null node.
		if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight)
			return -1;
		else
			return leftHeight + add;
	}

	public boolean isBlackHeightValid(Entry<T> root){
		return computeHeight(root) != -1;
	}*/

	public boolean verifyRBT(){
		if(!isRootBlack()) {
			System.out.println("Root is not black");
			return false;
		}
		else if(!validateNodes((Entry<T>) root)) {
			System.out.println("Not a valid RBT");
			return false;
		}
		else if(!isBalanced((Entry<T>) root, new Height())){
			System.out.println("Invalid height");
			return false;
		}
		else {
			System.out.println("Valid RBT");
			return true;
		}
	}

	public void print()
	{
		List<List<String>> lines = new ArrayList<List<String>>();

		List<Entry<T>> level = new ArrayList<Entry<T>>();
		List<Entry<T>> next = new ArrayList<Entry<T>>();

		level.add((Entry<T>) root);
		int nn = 1;

		int widest = 0;

		while (nn != 0) {
			List<String> line = new ArrayList<String>();

			nn = 0;

			for (Entry<T> n : level) {
				if (n == null) {
					line.add("null");
					next.add(null);
					next.add(null);
				}
				else if(n == NIL){
					line.add("NIL");
					next.add((Entry<T>) NIL.element);
					next.add((Entry<T>) NIL.element);
				}
				else {
					String color = n.color == BLACK ? (String) "B" : "R";
					String aa = String.valueOf(n.element) + color;
					line.add(aa);
					if (aa.length() > widest) widest = aa.length();

					next.add((Entry<T>) n.left);
					next.add((Entry<T>) n.right);

					if (n.left!= NIL) nn++;
					if (n.right != NIL) nn++;
				}
			}

			if (widest % 2 == 1) widest++;

			lines.add(line);

			List<Entry<T>> tmp = level;
			level = next;
			next = tmp;
			next.clear();
		}

		int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
		for (int i = 0; i < lines.size(); i++) {
			List<String> line = lines.get(i);
			int hpw = (int) Math.floor(perpiece / 2f) - 1;

			if (i > 0) {
				for (int j = 0; j < line.size(); j++) {

					// split node
					char c = ' ';
					if (j % 2 == 1) {
						if (line.get(j - 1) != null) {
							c = (line.get(j) != null) ? '┴' : '┘';
						} else {
							if (j < line.size() && line.get(j) != null) c = '└';
						}
					}
					System.out.print(c);

					// lines and spaces
					if (line.get(j) == null) {
						for (int k = 0; k < perpiece - 1; k++) {
							System.out.print(" ");
						}
					} else {

						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? " " : "─");
						}
						System.out.print(j % 2 == 0 ? "┌" : "┐");
						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? "─" : " ");
						}
					}
				}
				System.out.println();
			}

			// print line of numbers
			for (int j = 0; j < line.size(); j++) {

				String f = line.get(j);
				if (f == null) f = "";
				int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
				int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

				// a number
				for (int k = 0; k < gap1; k++) {
					System.out.print(" ");
				}
				System.out.print(f);
				for (int k = 0; k < gap2; k++) {
					System.out.print(" ");
				}
			}
			System.out.println();

			perpiece /= 2;
		}
	}

	/**
	 * Sample Input 10 85 15 70 20 60 30 50 65 80 90 40 5 55 -60 -15 -85 -70 -90 -20
	 *
	 10
	 Add 10 : [1] 10B
	 85
	 Add 85 : [2] 10B 85R
	 15
	 Add 15 : [3] 10R 15B 85R
	 70
	 Add 70 : [4] 10B 15B 70R 85B
	 20
	 Add 20 : [5] 10B 15B 20R 70B 85R
	 60
	 Add 60 : [6] 10B 15B 20B 60R 70R 85B
	 30
	 Add 30 : [7] 10B 15B 20R 30B 60R 70R 85B
	 50
	 Add 50 : [8] 10B 15R 20B 30B 50R 60B 70R 85B
	 65
	 Add 65 : [9] 10B 15R 20B 30B 50R 60B 65R 70R 85B
	 80
	 Add 80 : [10] 10B 15R 20B 30B 50R 60B 65R 70R 80R 85B
	 90
	 Add 90 : [11] 10B 15R 20B 30B 50R 60B 65R 70R 80R 85B 90R
	 40
	 Add 40 : [12] 10B 15B 20B 30B 40R 50B 60R 65B 70B 80R 85B 90R
	 5
	 Add 5 : [13] 5R 10B 15B 20B 30B 40R 50B 60R 65B 70B 80R 85B 90R
	 55
	 Add 55 : [14] 5R 10B 15B 20B 30B 40R 50B 55R 60R 65B 70B 80R 85B 90R
	 -60
	 Remove -60 : [13] 5R 10B 15B 20B 30B 40B 50R 55R 65B 70B 80R 85B 90R
	 -15
	 Remove -15 : [12] 5B 10B 20B 30B 40B 50R 55R 65B 70B 80R 85B 90R
	 -85
	 Remove -85 : [11] 5B 10B 20B 30B 40B 50R 55R 65B 70B 80R 90B
	 -70
	 Remove -70 : [10] 5B 10B 20B 30B 40B 50R 55R 65B 80B 90B
	 -90
	 Remove -90 : [9] 5B 10B 20B 30B 40B 50B 55B 65R 80B
	 -20
	 Remove -20 : [8] 5R 10B 30B 40B 50B 55B 65B 80B
	 * @param args
	 */
	public static void main(String[] args) {

		RedBlackTree<Integer> t = new RedBlackTree<>();
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int x = in.nextInt();
			if (x > 0) {
				System.out.println("Add " + x + " : ");
				t.add(x);
				System.out.print("Root " + t.root.element + " : ");
				t.printTree();
				t.print();
				t.verifyRBT();
			} else if (x < 0) {
				System.out.print("Remove " + -x + " : ");
				t.remove(-x);
				System.out.println("Direction: "+t.direction);
				t.printTree();
				t.print();
				t.verifyRBT();
			} else {
				Comparable[] arr = t.toArray();
				System.out.print("Final: ");
				for (int i = 0; i < t.size; i++) {
					System.out.print(arr[i] + " ");
				}
				System.out.println();
				return;
			}
		}

	}

	public void printParents() {
		System.out.print("Parent's length: " + parents.size() + " ");
		for (int i = 1; i < parents.size(); i++) {
			System.out.print(parents.get(i).element + " -");
		}
		// if (parents.peek() != null)
		//     System.out.println("Top: " + parents.peek().element);
		System.out.println();
	}
}
