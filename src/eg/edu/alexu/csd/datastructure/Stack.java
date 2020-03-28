package eg.edu.alexu.csd.datastructure;

public class Stack implements IStack{
    /*
    Did not use singly linked list from last assignment because this is much simpler
    and we do not need most of the singly linked list's features.
     */
    private static class Node{
        Object val;
        Node prev;
        public Node(Object val, Node prev) {
            this.val = val;
            this.prev = prev;
        }
    }

    private int size;
    private Node last;

    /**
     * RunTimeException is thrown in case of calling method for empty stack
     * @return top element in stack after removing it from the stack
     */
    @Override
    public Object pop() {
        if (size == 0) throw new RuntimeException("pop from empty stack");
        Object element = last.val;
        last = last.prev;
        --size;
        return element;
    }

    /**
     * RunTimeException is thrown in case of calling method for empty stack
     * @return top element in stack without removing it from the stack
     */
    @Override
    public Object peek() {
        if (size == 0) throw new RuntimeException("peek from empty stack");
        return last.val;
    }

    /**
     *
     * @param element the object to be added at the top of the stack
     */
    @Override
    public void push(Object element) {
        if (size != 0) {
            last = new Node(element, last);
        } else {
            last = new Node(element, null);
        }
        ++size;
    }

    /**
     *
     * @return true if the stack is empty and false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @return the current size of the stack
     */
    @Override
    public int size() {
        return size;
    }
}
