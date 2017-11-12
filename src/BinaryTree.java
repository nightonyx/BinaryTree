import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private static class Node<T> {
        T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || root == null || (!contains(o))) return false;
        Node<T> node = find((T) o);
        Node<T> parent = findParent(node);
        if (node.left == node.right) {
            if (parent != null) {
                if (parent.left == node) parent.left = null;
                else parent.right = null;
            } else root = null;
        }
        //One child
        else if (node.left == null || node.right == null) {
            if (parent != null) {
                if (node.left != null) {
                    if (parent.left == node) parent.left = node.left;
                    else parent.right = node.left;
                } else {
                    if (parent.left == node) parent.left = node.right;
                    else parent.right = node.right;
                }
            } else {
                if (node.left != null) root = node.left;
                else root = node.right;
            }
        }
        //Two children
        else {
            Node<T> minNode = node.right;
            Node<T> parentOfMinNode = node;
            while (minNode.left != null) {
                parentOfMinNode = minNode;
                minNode = minNode.left;
            }
            if (minNode != node.right) minNode.right = node.right;
            minNode.left = node.left;
            parentOfMinNode.left = null;
            if (parent == null) root = minNode;
            else {
                if (node == parent.left) parent.left = minNode;
                else parent.right = minNode;
            }
        }
        size--;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    private Node<T> findParent(Node<T> node) {
        if (node == null) throw new IllegalArgumentException();
        if (node == root) return null;
        Node<T> parent = root;
        while (parent.left != node && parent.right != node) {
            int compression = node.value.compareTo(parent.value);
            if (compression < 0) parent = parent.left;
            else parent = parent.right;
        }
        return parent;
    }

    private Node<T> findDoubleChildren(Node<T> node) {
        Node<T> parentNode = findParent(node);
        if (parentNode == null) throw new IllegalArgumentException();
        if (parentNode == root) return null;
        if (parentNode.left != null && parentNode.right != null) {
            return parentNode;
        } else {
            return findDoubleChildren(parentNode);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;

        private boolean isPassedRoot;

        private BinaryTree binaryTree = new BinaryTree();

        private BinaryTreeIterator() {
        }

        private Node<T> findNext() {
            if (!isPassedRoot) {
                if (root == null) {
                    return null;
                }
                current = root;
                binaryTree.add(current.value);
                isPassedRoot = true;
                return current;
            }
            if (current.left != null && current.right != null) {
                current = current.left;
                binaryTree.add(current.value);
                return current;
            }
            if (current.left != null) {
                current = current.left;
                binaryTree.add(current.value);
                return current;
            }
            if (current.right != null) {
                current = current.right;
                binaryTree.add(current.value);
                return current;
            } else {
                if (findDoubleChildren(current) != null) {
                    current = findDoubleChildren(current);
                    current.left = null;
                    if (current.right != null) {
                        current = current.right;
                        binaryTree.add(current.value);
                        return current;
                    }
                }
                root = binaryTree.root;
                binaryTree = new BinaryTree();
                current = null;
                isPassedRoot = false;
                return null;
            }
        }

        @Override
        public boolean hasNext() {
            return (findNext() != null);
        }

        @Override
        public T next() {
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}