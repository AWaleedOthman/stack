package eg.edu.alexu.csd.datastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    @Test
    void pop() {
        Stack stk = new Stack();
        stk.push("first");
        stk.push("second");
        assertEquals("second", stk.pop());
        assertEquals("first", stk.pop());
        assertThrows(RuntimeException.class, stk::pop);
        stk.push(" ");
        assertEquals(" ", stk.pop());
    }

    @Test
    void peek() {
        Stack stk = new Stack();
        stk.push("first");
        assertEquals("first", stk.peek());
        stk.push("second");
        assertEquals("second", stk.peek());
        assertEquals("second", stk.peek());
        assertEquals("second", stk.pop());
        assertEquals("first", stk.peek());
        assertEquals("first", stk.peek());
        assertEquals("first", stk.pop());
        assertThrows(RuntimeException.class, stk::peek);
        stk.push(" ");
        assertEquals(" ", stk.peek());
    }

    @Test
    void push() {
        Stack stk = new Stack();
        assertTrue(stk.isEmpty());
        stk.push("first");
        assertFalse(stk.isEmpty());
        stk.push("second");
        assertEquals(2, stk.size());
        assertEquals("second", stk.pop());
    }

    @Test
    void isEmpty() {
        Stack stk = new Stack();
        assertTrue(stk.isEmpty());
        stk.push("first");
        assertFalse(stk.isEmpty());
        stk.push("second");
        assertFalse(stk.isEmpty());
        stk.pop();
        assertFalse(stk.isEmpty());
        stk.pop();
        assertTrue(stk.isEmpty());
        stk.push(" ");
        assertFalse(stk.isEmpty());
        stk.peek();
        assertFalse(stk.isEmpty());
    }

    @Test
    void size() {
        Stack stk = new Stack();
        assertEquals(0, stk.size());
        stk.push("first");
        assertEquals(1, stk.size());
        stk.push("second");
        assertEquals(2, stk.size());
        stk.peek();
        assertEquals(2, stk.size());
        stk.pop();
        assertEquals(1, stk.size());
        stk.peek();
        assertEquals(1, stk.size());
        stk.pop();
        assertEquals(0, stk.size());
        assertThrows(RuntimeException.class, stk::pop);
        assertEquals(0, stk.size());

    }
}
