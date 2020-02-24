import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class TreeTest {

    @Test(timeout=1000)
    public void testHasNext() {
        BinaryTree<Integer> root = new BinaryTree<>(1);

        Iterator<Integer> iter = root.iterator();
        assertTrue(iter.hasNext());
        iter.next();
        assertFalse(iter.hasNext());
    }

    @Test(timeout=1000)
    public void testNext() {
        BinaryTree<Integer> root = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(2);
        BinaryTree<Integer> leftLeft = new BinaryTree<>(3);

        root.setLeft(left);
        left.setLeft(leftLeft);

        Iterator<Integer> iter = root.iterator();
        for (int i = 1; i <= 3; i++) {
            assertEquals(i, (int)iter.next());
        }
    }

    @Test(timeout=1000)
    public void testNext2() {
        BinaryTree<Integer> root = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(2);
        BinaryTree<Integer> right = new BinaryTree<>(3);

        root.setLeft(left);
        root.setRight(right);

        Iterator<Integer> iter = root.iterator();
        for (int i = 1; i <= 3; i++) {
            assertEquals(i, (int)iter.next());
        }
    }

    @Test(timeout=1000)
    public void testNext3() {
        BinaryTree<Integer> root = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(2);
        BinaryTree<Integer> right = new BinaryTree<>(4);
        BinaryTree<Integer> leftLeft = new BinaryTree<>(3);

        root.setLeft(left);
        root.setRight(right);
        left.setLeft(leftLeft);

        Iterator<Integer> iter = root.iterator();
        for (int i = 1; i <= 4; i++) {
            assertEquals(i, (int)iter.next());
        }
    }

//    @Test(timeout=1000)
//    public void testNext4() {
//        StandardTree<Integer> root = new StandardTree<>(1);
//        StandardTree<Integer> child1 = new StandardTree<>(2);
//        StandardTree<Integer> child2 = new StandardTree<>(4);
//        StandardTree<Integer> child3 = new StandardTree<>(6);
//        StandardTree<Integer> child1Child1 = new StandardTree<>(3);
//        StandardTree<Integer> child2Child1 = new StandardTree<>(5);
//        StandardTree<Integer> child3Child1 = new StandardTree<>(7);
//
//        root.addChild(child1);
//        root.addChild(child2);
//        root.addChild(child3);
//        child1.addChild(child1Child1);
//        child2.addChild(child2Child1);
//        child3.addChild(child3Child1);
//
//        Iterator<Integer> iter = root.iterator();
//        for (int i = 1; i <= 7; i++) {
//            assertEquals(i, (int)iter.next());
//        }
//    }


    @Test(timeout=1000)
    public void testNext5() {
        BinaryTree<Integer> root = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(2);
        BinaryTree<Integer> right = new BinaryTree<>(5);
        BinaryTree<Integer> leftL = new BinaryTree<>(3);
        BinaryTree<Integer> leftR = new BinaryTree<>(4);
        BinaryTree<Integer> rightL = new BinaryTree<>(6);
        BinaryTree<Integer> rightR = new BinaryTree<>(7);

        root.setLeft(left);
        root.setRight(right);
        left.setLeft(leftL);
        left.setRight(leftR);
        right.setLeft(rightL);
        right.setRight(rightR);

        Iterator<Integer> iter = root.iterator();
        for (int i = 1; i <= 7; i++) {
            assertEquals(i, (int)iter.next());
        }
    }

    @Test(timeout=1000)
    public void testIsBST() {
        BinaryTree<Integer> tree = new BinaryTree<>(2);
        BinaryTree<Integer> left = new BinaryTree<>(1);
        BinaryTree<Integer> right = new BinaryTree<>(3);

        tree.setLeft(left);
        tree.setRight(right);
        left.setLeft(new BinaryTree<>(null));
        left.setRight(new BinaryTree<>(null));
        right.setLeft(new BinaryTree<>(null));
        right.setRight(new BinaryTree<>(null));

        assertTrue(BinaryTree.isBST(tree));
    }

    @Test(timeout=1000)
    public void testIsBST2() {
        BinaryTree<Integer> tree = new BinaryTree<>(6);
        BinaryTree<Integer> left = new BinaryTree<>(2);
        BinaryTree<Integer> right = new BinaryTree<>(9);
        BinaryTree<Integer> leftL = new BinaryTree<>(1);
        BinaryTree<Integer> leftR = new BinaryTree<>(4);
        BinaryTree<Integer> rightL = new BinaryTree<>(8);


        tree.setLeft(left);
        tree.setRight(right);
        left.setLeft(leftL);
        left.setRight(leftR);
        right.setLeft(rightL);
        right.setRight(new BinaryTree<>(null));

        leftL.setLeft(new BinaryTree<>(null));
        leftL.setRight(new BinaryTree<>(null));
        leftR.setLeft(new BinaryTree<>(null));
        leftR.setRight(new BinaryTree<>(null));
        rightL.setLeft(new BinaryTree<>(null));
        rightL.setRight(new BinaryTree<>(null));

        assertTrue(BinaryTree.isBST(tree));
    }

    @Test(timeout=1000)
    public void testIsBST3() {
        BinaryTree<Integer> tree = new BinaryTree<>(null);
        assertTrue(BinaryTree.isBST(tree));
    }

    @Test(timeout=1000)
    public void testIsBST4() {
        BinaryTree<Integer> tree = new BinaryTree<>(1);
        assertFalse(BinaryTree.isBST(tree));
    }

    @Test(timeout=1000)
    public void testIsBST5() {
        BinaryTree<Integer> tree = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(null);
        BinaryTree<Integer> right = new BinaryTree<>(null);
        tree.setLeft(left);
        tree.setRight(right);
        assertTrue(BinaryTree.isBST(tree));
    }


    @Test(timeout=1000)
    public void testIsBST6() {
        BinaryTree<Integer> tree = new BinaryTree<>(1);
        BinaryTree<Integer> left = new BinaryTree<>(1);
        BinaryTree<Integer> right = new BinaryTree<>(null);
        tree.setLeft(left);
        tree.setRight(right);
        assertTrue(BinaryTree.isBST(tree));
    }
}
