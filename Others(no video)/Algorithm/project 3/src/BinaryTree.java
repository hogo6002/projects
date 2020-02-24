import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A binary tree, where each node contains at most two children
 * Each root node contains a value and references to its left and right children (if they exist)
 *
 * @param <E> the type of the tree's elements
 */
public class BinaryTree<E> implements Tree<E> {
    private E root; // the element sitting at this tree's root node
    private BinaryTree<E> left; // the left child (subtree)
    private BinaryTree<E> right; // the right child (subtree)

    /**
     * Constructs a new binary tree with a single node
     *
     * @param root the element to store at this tree's root
     */
    public BinaryTree(E root) {
        this.root = root;
        this.left = null;
        this.right = null;
    }

    @Override
    public int size() {
        return 1 + (left != null ? left.size() : 0) + (right != null ? right.size() : 0);
    }

    @Override
    public E getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public List<Tree<E>> getChildren() {
        List<Tree<E>> children = new ArrayList<>();
        if (left != null) {
            children.add(left);
        }
        if (right != null) {
            children.add(right);
        }
        return children;
    }

    @Override
    public boolean contains(E elem) {
        if (root.equals(elem)) {
            return true;
        }
        return (left != null && left.contains(elem)) || (right != null && right.contains(elem));
    }

    @Override
    public Iterator<E> iterator() {
        return new TreeIterator<>(this);
    }

    /**
     * Sets the left child of this tree's root to the given subtree
     * Any existing left child will be overridden
     *
     * @param left the new left child
     */
    public void setLeft(BinaryTree<E> left) {
        this.left = left;
    }

    /**
     * Sets the right child of this tree's root to the given subtree
     * Any existing right child will be overridden
     *
     * @param right the new right child
     */
    public void setRight(BinaryTree<E> right) {
        this.right = right;
    }

    /**
     * @return the left child subtree of this tree's root node
     */
    public BinaryTree<E> getLeft() {
        return left;
    }

    /**
     * @return the right child subtree of this tree's root node
     */
    public BinaryTree<E> getRight() {
        return right;
    }

    /**
     * Determines whether the parameter tree is a binary search tree or not
     * This is determined by the definition of a binary search tree provided in the lectures
     *
     * @param tree the tree to check
     * @return true if this tree is a BST, otherwise false
     */
    public static <T extends Comparable<T>> boolean isBST(BinaryTree<T> tree) {
        BinaryTree<T> root = tree;
        List<BinaryTree<T>> orderedList = new ArrayList();
        List<BinaryTree<T>> parents = new ArrayList();
        parents.add(root);
        if (tree.getChildren().isEmpty()) {
            return tree.getRoot() == null;
        }
        boolean flag = false;
        while (true) {
            if (root.getLeft() != null) {
                if (root.getLeft().isLeaf()) {
                    flag = true;
                } else {
                    root = root.getLeft();
                    parents.add(root);
                }
            }
            if (flag){
                if (root.getLeft() != null) {
                    orderedList.add(root.getLeft());
                }

                if (parents.isEmpty()) {
                    break;
                }
                root = parents.remove(parents.size() - 1);
                orderedList.add(root);
                if (root.getRight() != null) {
                    orderedList.add(root.getRight());
                }
                if (parents.isEmpty()) {
                    break;
                }
                root = parents.remove(parents.size() - 1);
                orderedList.add(root);
                root = root.getRight();
                parents.add(root);
            }
        }

        for (int i = 1; i < orderedList.size() - 2; i = i + 2) {
            if (orderedList.get(i + 2).getRoot().compareTo(orderedList.get(i).getRoot()) < 0) {
                return false;
            }
            if (orderedList.get(i - 1).getRoot() != null) {
                return false;
            }
        }
        if (orderedList.get(orderedList.size() - 1).getRoot() != null) {
            return false;
        }
        return true;
    }
}
