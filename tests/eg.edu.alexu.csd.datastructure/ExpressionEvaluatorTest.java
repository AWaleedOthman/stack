package eg.edu.alexu.csd.datastructure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

    @Test
    void infixToPostfix() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        assertEquals("a b c + * d *", ev.infixToPostfix("a*(b +c) *d"));
        assertEquals("2 3 4 * +", ev.infixToPostfix("2 + 3 * 4"));
        assertEquals("a b * 5 +", ev.infixToPostfix("a * b + 5"));
        assertEquals("1 2 + 7 *", ev.infixToPostfix("(1 + 2) * 7"));
        assertEquals("a b * c /", ev.infixToPostfix("a * b / c"));
        assertEquals("a b c - d + / e a - * c *", ev.infixToPostfix("(a / (b - c + d)) * (e - a) * c"));
        assertEquals("a b / c - d e * + a c * -", ev.infixToPostfix("a / b - c + d * e - a * c"));
        assertEquals("X_2", ev.infixToPostfix("  X_2  "));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix(""));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix(" "));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("()"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("a*((b +c) *d"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("a*(b +c)) *d"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("a*(b +c) /*d"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("+ a*(b +c) *d"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("a*(b +c) *d/"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("/a*(b +c) *d/"));
        assertThrows(RuntimeException.class, () -> ev.infixToPostfix("a*(b c +c) *d"));
        assertEquals("a bc c + * d *", ev.infixToPostfix("a*(bc +c) *d"));
    }

    @Test
    void evaluate() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        assertEquals(5, ev.evaluate(ev.infixToPostfix("(25*3-25)/(5+2*(6-2)-3)")));
        assertEquals(7, ev.evaluate(ev.infixToPostfix("7")));
        assertEquals(8, ev.evaluate("6 2 / 3 - 4 2 * +"));
        /*division by zero*/
        assertThrows(RuntimeException.class, () -> ev.evaluate("5 0 /"));
        /*evaluating without validating beforehand*/
        assertThrows(RuntimeException.class, () -> ev.evaluate("5 5 / *"));
        assertThrows(RuntimeException.class, () -> ev.evaluate("/ 5 6"));
    }

    @Test
    void validateInfix() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        /*'bc' is considered one operand*/
        assertTrue(ev.validInfix("a*(bc +c) *d", false));
        assertTrue(ev.validInfix("a*(bc +(c)) *d", false));
        assertFalse(ev.validInfix("a*(b +c) /*d", false));
        assertFalse(ev.validInfix("a*((b +c) *d", false));
        assertTrue(ev.validInfix("a*((b +c)) *d", false));
        assertFalse(ev.validInfix("a*((b +c)) *d+", false));
        assertFalse(ev.validInfix("a*(bc +c-) *d", false));
        assertFalse(ev.validInfix("a*() *d", false));
        assertFalse(ev.validInfix("a*(a+b) *c(d)", false));
        assertFalse(ev.validInfix("a*(a+b)(a-b) *c", false));
        assertFalse(ev.validInfix("", false));
        assertFalse(ev.validInfix(" ", false));
        assertFalse(ev.validInfix(")", false));
        assertTrue(ev.validInfix("0", false));
        assertTrue(ev.validInfix("a/(-b)", false));
        assertTrue(ev.validInfix("(-b)", false));
        assertTrue(ev.validInfix("5*-(b /-(a-2))", false));
        assertFalse(ev.validInfix("-5*((6*", false));
    }

    @Test
    void validatePostfix() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        assertTrue(ev.validPostfix("a b c -d + / e a - * c *", false));
        assertTrue(ev.validPostfix("a b /c - d e* + a c *   -", false));
        assertFalse(ev.validPostfix("a b / c - d e * + a c *", false));
        assertFalse(ev.validPostfix("a b c -d + / e( a - * c *", false));
        assertFalse(ev.validPostfix("a b c -d + / e a - * c *)", false));
        assertFalse(ev.validPostfix("", false));
        assertFalse(ev.validPostfix(" ", false));
    }

}
