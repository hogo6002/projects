//import java.util.LinkedList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * A standard (non-binary) tree
// * Each root node contains a value and references to zero or more children
// *
// * @author Henry O'Brien
// * @param <E> the type of the tree's elements
// */
//public class StandardTree<E> implements Tree<E> {
//    private E root; // the element sitting at this tree's root node
//    private List<StandardTree<E>> children; // the child subtrees of this tree's root node
//
//    /**
//     * Constructs a new tree with a single node
//     *
//     * @param root the element to store at this tree's root
//     */
//    public StandardTree(E root) {
//        this.root = root;
//        this.children = new LinkedList<>();
//    }
//
//    @Override
//    public int size() {
//        int size = 1;
//        for (StandardTree<E> child : children) {
//            size += child.size();
//        }
//        return size;
//    }
//
//    @Override
//    public E getRoot() {
//        return root;
//    }
//
//    @Override
//    public boolean isLeaf() {
//        return children.size() == 0;
//    }
//
//    @Override
//    public List<Tree<E>> getChildren() {
//        return new LinkedList<>(children);
//    }
//
//    @Override
//    public boolean contains(E elem) {
//        if (root.equals(elem)) {
//            return true;
//        }
//        for (StandardTree<E> child : children) {
//            if (child.contains(elem)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
////    @Override
////    public Iterator<E> iterator() {
////        return new TreeIterator<>(this);
////    }
//
//    /**
//     * Adds a child to this tree's root node
//     *
//     * @param child the child subtree to add
//     */
//    public void addChild(StandardTree<E> child) {
//        children.add(child);
//    }
//
//    /**
//     * Removes the given subtree if it exists as an immediate child of this tree's root
//     *
//     * @param child the subtree to remove
//     * @return true on success, false if the subtree is not an immediate child of the tree's root
//     */
//    public boolean removeChild(StandardTree<E> child) {
//        return children.remove(child);
//    }
//}
