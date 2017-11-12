

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Testing {

    private boolean assertListContains(List list, List otherList){
        for (int i = 0; i < list.size(); i++){
            if (!otherList.contains(list.get(i)) || !list.contains(otherList.get(i))){
                return false;
            }
        }
        return true;
    }

    @Test
    void assertListContainsTest(){
        assertTrue(assertListContains(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(6, 5, 4, 3, 2, 1)));
        assertFalse(assertListContains(Arrays.asList(1, 3, 4, 5, 6), Arrays.asList(6, 5, 4, 3, 2, 1)));
        assertFalse(assertListContains(Arrays.asList(1, 2, 4), Arrays.asList(6, 4, 2)));
        assertTrue(assertListContains(Arrays.asList(1, 2, 4), Arrays.asList(1, 4, 2)));
    }

    private boolean createTest(BinaryTree binaryTree, List expectedList){
        List list = new ArrayList();
        BinaryTree.BinaryTreeIterator binaryTreeIterator = (BinaryTree.BinaryTreeIterator) binaryTree.iterator();
        while (binaryTreeIterator.hasNext()) {
            list.add(binaryTreeIterator.next());
        }
        return assertListContains(list, expectedList);
    }

    @Test
    void iterator() {
        BinaryTree binaryTreeTest1 = new BinaryTree();
        binaryTreeTest1.add(5);
        binaryTreeTest1.add(2);
        binaryTreeTest1.add(7);
        binaryTreeTest1.add(1);
        binaryTreeTest1.add(4);
        binaryTreeTest1.add(0);
        binaryTreeTest1.add(3);
        assertTrue(createTest(binaryTreeTest1, Arrays.asList(5, 2, 1, 0, 4, 3, 7)));

        BinaryTree binaryTreeTest2 = new BinaryTree();
        binaryTreeTest2.add(0);
        binaryTreeTest2.add(1);
        binaryTreeTest2.add(2);
        binaryTreeTest2.add(3);
        assertTrue(createTest(binaryTreeTest2, Arrays.asList(0, 2, 1, 3)));
        assertFalse(createTest(binaryTreeTest2, Arrays.asList(2, 1, 3)));

        BinaryTree binaryTreeTest3 = new BinaryTree();
        binaryTreeTest3.add(48);
        binaryTreeTest3.add(52);
        binaryTreeTest3.add(41);
        binaryTreeTest3.add(39);
        binaryTreeTest3.add(47);
        binaryTreeTest3.add(49);
        binaryTreeTest3.add(54);
        assertTrue(createTest(binaryTreeTest3, Arrays.asList(39, 41, 47, 48, 49, 52, 54)));
        assertTrue(createTest(binaryTreeTest3, Arrays.asList(39, 41, 47, 48, 54, 52, 49)));
        assertFalse(createTest(binaryTreeTest3, Arrays.asList(41, 47, 48, 54, 52, 49)));
        assertFalse(createTest(binaryTreeTest3, Arrays.asList(13, 2231, 221132, 32112)));
    }

    @Test
    public void remove(){
        BinaryTree binaryTreeTest1 = new BinaryTree();
        binaryTreeTest1.add(5);
        binaryTreeTest1.add(2);
        binaryTreeTest1.add(7);
        binaryTreeTest1.add(1);
        binaryTreeTest1.add(4);
        binaryTreeTest1.add(0);
        binaryTreeTest1.add(3);
        binaryTreeTest1.remove(2);
        assertFalse(createTest(binaryTreeTest1, Arrays.asList(5, 2, 1, 0, 4, 3, 7)));
        assertTrue(createTest(binaryTreeTest1, Arrays.asList(5, 1, 0, 4, 3, 7)));

        BinaryTree binaryTreeTest2 = new BinaryTree();
        binaryTreeTest2.add(0);
        binaryTreeTest2.add(1);
        binaryTreeTest2.add(2);
        binaryTreeTest2.add(3);
        assertTrue(binaryTreeTest2.remove(0));
        assertFalse(createTest(binaryTreeTest2, Arrays.asList(0, 2, 1, 3)));
        assertTrue(createTest(binaryTreeTest2, Arrays.asList(2, 1, 3)));

        BinaryTree binaryTreeTest5 = new BinaryTree();
        binaryTreeTest5.add(48);
        binaryTreeTest5.add(41);
        binaryTreeTest5.add(39);
        binaryTreeTest5.add(47);
        assertFalse(binaryTreeTest5.remove(42));
    }
}