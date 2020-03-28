package eg.edu.alexu.csd.datastructure;

public interface IStack {
    /**
     * Removes the element at the top of stack and returns that element.
     *
     * @return top of stack element, or through exception if empty
     */
    Object pop();
    /**
     * Get the element at the top of stack without removing it from stack.
     *
     * @return top of stack element, or through exception if empty
     */
    Object peek();
    /**
     * Pushes an item onto the top of this stack.
     *
     * @param element
     * to insert
     */
    void push(Object element);
    /**
     * Tests if this stack is empty
     *
     * @return true if stack empty
     */
    boolean isEmpty();
    /**
     * Returns the number of elements in the stack.
     *
     * @return number of elements in the stack
     */
    int size();
}
