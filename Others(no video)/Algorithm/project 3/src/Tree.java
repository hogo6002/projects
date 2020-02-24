import java.util.List;

/**
 * Tree ADT used in the HW3 tree classes
 *
 * @author Henry O'Brien
 * @param <E> the type of the tree's elements
 */
public interface Tree<E> extends Iterable<E> {

    /**
     * @return the number of nodes in this tree
     */
    int size();

    /**
     * @return the element sitting at the root of this tree
     */
    E getRoot();

    /**
     * @return whether the tree is a leaf (a single node with no children) or not
     */
    boolean isLeaf();

    /**
     * @return the children of this tree's root node in the order they appear from left to right
     */
    List<Tree<E>> getChildren();

    /**
     * Determines if the given elem is in this tree
     *
     * @param elem the element to look for
     * @return true if elem is in this tree, otherwise false
     */
    boolean contains(E elem);
}
