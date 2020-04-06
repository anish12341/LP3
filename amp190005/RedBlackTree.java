
package LP3.amp190005;

import java.util.Scanner;

public class RedBlackTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {
    private static final boolean BLACK = false;
    private static final boolean RED = true;

    static class Entry<T> extends BinarySearchTree.Entry<T> {
        boolean color;
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            color = RED;
        }

//        boolean isRed() {
//	    return color == RED;
//        }
//
//        boolean isBlack() {
//	    return color == BLACK;
//        }

    }

    public RedBlackTree() {
	    super();
    }

    @Override
    public boolean add(T x) {
        boolean ans = super.add(new Entry<>(x, null,null));
        if(ans) {

            Entry<T> current = (Entry<T>) this.parents.pop();
            Entry<T> p = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
            Entry<T> gp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
            current.color = RED;

            while ((current != this.root) && (getColor(p) != BLACK)) {
                if(getLeftChild(gp) == p) {

                    boolean UncleColor = getColor(getRightChild(gp));

                    if(UncleColor == RED) {

                        p.color = BLACK;
                        ((Entry<T>)gp.right).color = BLACK;
                        current = gp;
                        current.color = RED;
                        p = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
                        gp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();

                    } else {

                        if (getRightChild(p) == current) {

                            Entry<T> temp = current;
                            current = p;
                            p = temp;
                            rotateLeft(current, gp);

                        }

                        p.color = BLACK;
                        gp.color = RED;
                        Entry<T> gGParent = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
                        rotateRight(gp, gGParent);
                        current = p;
                        p = gGParent;
                        gp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();

                    }
                } else {

                    boolean colorOfUncle = getColor(getLeftChild(gp));

                    if (colorOfUncle == RED) {
                        p.color = BLACK;
                        ((Entry<T>) gp.left).color = BLACK;
                        current = gp;
                        current.color = RED;
                        p = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
                        gp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();

                    } else {

                        if (p.left == current) {

                            Entry<T> temp = current;
                            current = p;
                            p = temp;
                            rotateRight(current, gp);

                        }

                        p.color = BLACK;
                        gp.color = RED;
                        Entry<T> ggp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();
                        rotateLeft(gp, ggp);
                        current = p;
                        p = ggp;
                        gp = this.parents.empty() ? null : (Entry<T>) this.parents.pop();

                    }
                }
                ((Entry<T>)this.root).color = BLACK;
            }
            ((Entry<T>)this.root).color = BLACK;
        }
        return ans;
    }

    private void rotateLeft(Entry<T> node, Entry<T> p) {

        Entry<T> succ = (Entry<T>) node.right;

        if(p != null) {
            if (p.left == node) {
                p.left = succ;
            } else {
                p.right = succ;
            }

        } else {
            this.root = succ;
        }

        Entry<T> temp = (Entry<T>) succ.left;
        succ.left = node;
        node.right = temp;
    }

    private void rotateRight(Entry<T> node, Entry<T> p) {
        Entry<T> succ = (Entry<T>) node.left;

        if(p != null) {

            if (p.left == node) {
                p.left = succ;
            } else {
                p.right = succ;
            }

        } else {
            this.root = succ;
        }

        Entry<T> temp = (Entry<T>) succ.right;
        succ.right = node;
        node.left = temp;
    }

    private boolean getColor(Entry<T> ent) {
        return ent == null ? BLACK : ent.color;
    }

    private Entry<T> getLeftChild(Entry<T> ent) {
        return ent == null ? null : (Entry<T>) ent.left;
    }

    private Entry<T> getRightChild(Entry<T> ent) {
        return ent == null ? null : (Entry<T>) ent.right;
    }

    private void setColor(Entry<T> ent, boolean colour) {
        if(ent != null) {
            ent.color = colour;
        }
    }

    @Override
    public T remove(T x) {

        T temp = super.remove(x);
        if(temp != null) {
            Entry<T> cursor = (Entry<T>) this.parents.pop();
            Entry<T> removed = (Entry<T>) this.parents.pop();
            if(removed.color == BLACK) {
                fixup(cursor);
            }
        }
        return temp;
    }

    private void fixup(Entry<T> cursor) {

        Entry<T> parent = (Entry<T>) this.parents.pop();

        while (cursor != this.root && getColor(cursor) == BLACK) {

            if(parent.left == cursor) {

                Entry<T> sibling = (Entry<T>)parent.right;

                if(sibling == null) {
                    break;
                }

                boolean colorOfSibling = getColor(sibling);

                if(colorOfSibling == RED) {
                    setColor(sibling,BLACK);
                    setColor(parent,RED);
                    Entry<T> gp = this.parents.empty() ? null : (Entry<T>)this.parents.peek();
                    rotateLeft(parent, gp);
                    this.parents.push(sibling);
                    sibling = (Entry<T>) parent.right;

                }
                else if(getColor(getLeftChild(sibling)) == BLACK && getColor(getRightChild(sibling)) == BLACK) {
                    setColor(sibling,RED);
                    cursor = parent;
                    parent = this.parents.empty() ? null : (Entry<T>) this.parents.pop();


                }
                else {

                    if(getColor(getRightChild(sibling)) == BLACK) {
                        setColor(getLeftChild(sibling),BLACK);
                        setColor(sibling,RED);
                        rotateRight(sibling,parent);
                        sibling = (Entry<T>) parent.right;
                    }
                    else {
                        setColor(getRightChild(sibling),BLACK);
                        setColor(sibling,getColor(parent));
                        setColor(parent,BLACK);
                        Entry<T> gp = this.parents.empty() ? null : (Entry<T>) this.parents.peek();
                        rotateLeft(parent, gp);
                        cursor = (Entry<T>) this.root;
                    }
                }
            } else {
                Entry<T> sibling = (Entry<T>)parent.left;
                if(sibling == null) {
                    break;
                }
                boolean colorOfSibling = getColor(sibling);
                if(colorOfSibling == RED) {
                    setColor(sibling,BLACK);
                    setColor(parent,RED);
                    Entry<T> gp = this.parents.empty() ? null : (Entry<T>)this.parents.peek();
                    rotateRight(parent, gp);
                    this.parents.push(sibling);
                    sibling = (Entry<T>) parent.left;
                }

                else if(getColor(getLeftChild(sibling)) == BLACK && getColor(getRightChild(sibling)) == BLACK) {
                    sibling.color = RED;
                    cursor = parent;
                    parent = this.parents.empty() ? null : (Entry<T>) this.parents.pop();

                }
                else {
                    if(getColor(getLeftChild(sibling)) == BLACK) {
                        ((Entry<T>)sibling.right).color = BLACK;
                        sibling.color = RED;
                        rotateLeft(sibling,parent);
                        sibling = (Entry<T>) parent.left;
                    }
                    else {
                        ((Entry<T>) sibling.left).color = BLACK;
                        sibling.color = parent.color;
                        parent.color = BLACK;
                        Entry<T> gp = this.parents.empty() ? null : (Entry<T>) this.parents.peek();
                        rotateRight(parent, gp);
                        cursor = (Entry<T>) this.root;
                    }
                }
            }
        }
        if(cursor != null) {
            cursor.color = BLACK;
        }
    }

    @Override
    public boolean contains(T x) {
        return super.contains(x);
    }

    /**
     * overrides BST printTree method
     * @param
     * @return
     */
    public void printTree() {
        System.out.print("[" + size + "]");
        this.printTree((Entry<T>) root);
        System.out.println();
    }

    /**
     * Inorder traversal of the tree
     * @param node, current node in the traversal
     * @return
     */
    public void printTree(Entry<T> node) {
        if (node != null) {
            // System.out.print("Type: " + node.getClass() + " ");
            printTree((Entry<T>) node.left);
            String s = node.color == BLACK ? (String) "B" : "R";
            /*if(node == NIL)
                System.out.print(" " + "NIL" + "" + s);
            else*/
                System.out.print(" " + node.element + "" + s);
            printTree((Entry<T>) node.right);
        }
    }

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
            } else if (x < 0) {
                System.out.print("Remove " + -x + " : ");
                t.remove(-x);
                t.printTree();
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

}

