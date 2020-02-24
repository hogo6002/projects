import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * An iterator for the tree ADT that performs a preorder traversal
 */
public class TreeIterator<E> implements Iterator<E> {
    private Tree<E> root;
    Stack<BinaryTree<E>> stack;

    /**
     * Constructs a new tree iterator from the root of the tree to iterate over
     *
     * You are welcome to modify this constructor but cannot change its signature
     * This method should have O(1) time complexity
     */
    public TreeIterator(BinaryTree<E> root) {
        this.root = root;
        if (root != null) {
            stack.push(root);
        }
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }


        BinaryTree<E> res = stack.pop(); // retrieve and remove the head of queue
        if (res.getRight() != null) stack.push(res.getRight());
        if (res.getLeft() != null) stack.push(res.getLeft());

        return res.getRoot();

    }


//    Tree<E> temp = next;
//        if (changedParent) {
//            children = root.getChildren();
//        }
//        changedParent = true;
//        if (!parentStack.isEmpty()) {
//            root = parentStack.peek();
//            if (!children.isEmpty()) {
//                if (!(children.size() >= i + 1)) {
//                    i = 0;
//                }
//                next = children.remove(i);
//                i = 0;
//                if (next.isLeaf()) {
//                    if (!children.isEmpty()) {
//                        changedParent = false;
//                    } else {
//                        parentStack.pop();
//                        if (!parentStack.isEmpty()) {
//                            root = parentStack.peek();
//                        }
//                        changedParent = true;
//                        Stack<Tree<E>> temps = new Stack<>();
//                        temps.addAll(visitedParent);
//                        int length = visitedParent.size();
//                        for (int n = 0; n < length; n++) {
//                            if (temps.pop() == root) {
//                                i++;
//                            }
//                        }
//                    }
//                    return temp.getRoot();
//                } else {
//                    visitedParent.push(root);
//                    root = next;
//                    parentStack.push(next);
//                }
//            } else {
//                parentStack.pop();
//                if (!parentStack.isEmpty() && !parentStack.peek().getChildren().isEmpty()) {
//                    root = parentStack.peek();
//                    Stack<Tree<E>> temps = new Stack<>();
//                    temps.addAll(visitedParent);
//                    int length = visitedParent.size();
//                    for (int n = 0; n < length; n++) {
//                        if (temps.pop() == root) {
//                            i++;
//                        }
//                    }
//                    next = root.getChildren().get(i);
//                    i = 0;
//                    parentStack.push(next);
//                } else {
//                    next = null;
//                }
//            }
//        } else {
//            next = null;
//        }
//        return temp.getRoot();
}
