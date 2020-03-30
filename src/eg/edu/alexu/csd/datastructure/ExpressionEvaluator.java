package eg.edu.alexu.csd.datastructure;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
a very important thing to understand before using is that the class deals with anything apart from {+, -, *, /, (, )}
as operand. operands can be more than one character, for example, "ab" is one operand, whereas "a b"
are two different operands. this allow us to write operands as "x_2", "x2", "Xn", etc...
 */
public class ExpressionEvaluator implements IExpressionEvaluator {

    /**
     * it throws RuntimeException in case of invalid infix. use method validInfix to avoid that.
     *
     * @param expression infix expression
     * @return postfix expression
     */
    @Override
    public String infixToPostfix(String expression) {
        expression = fixInfix(expression);
        if (!validInfix(expression, true)) throw new RuntimeException("Invalid infix expression");

        StringBuilder sb = new StringBuilder();
        Stack stk = new Stack();
        Scanner sc = new Scanner(expression);
        String next = "";
        boolean flag = true;

        while (sc.hasNext()) {
            if (flag) next = sc.next();
            flag = true;
            if (!next.matches("[+--*/()]")) sb.append(next).append(" "); //next is operand
            else if (next.equals("(")) stk.push(next);
            else if (next.equals(")")) {
                next = (String) (stk.pop());
                while (!next.equals("(")) {
                    sb.append(next).append(" ");
                    next = (String) (stk.pop());
                }
            }
            //operator but stack is empty or top is '('
            else if (stk.isEmpty() || stk.peek().equals("(")) stk.push(next);
            else if (next.matches("[*/]") && ((String) stk.peek()).matches("[+--]")) stk.push(next);
            else {
                sb.append((String) stk.pop()).append(" ");
                flag = false;
            }
        }
        while (!stk.isEmpty()) sb.append((String) stk.pop()).append(" ");
        sc.close();
        return sb.toString().trim();
    }

    /**
     * Puts spaces around the operands if not present to be able to validate the expression.
     *
     * @param expression: infix expression before checking spaces
     * @return expression after putting spaces
     */
    public String fixPostfix(String expression) {
        Pattern p = Pattern.compile("[+--*/()]");
        Matcher m = p.matcher(expression);
        StringBuilder sb = new StringBuilder(expression);
        int charAdded = 0, index;
        while (m.find()) {
            index = m.start();
            if (index != 0 && sb.charAt(index + charAdded - 1) != ' ') { //add space before operand
                sb.insert(index + charAdded, ' ');
                ++charAdded;
            }
            if (index != expression.length() - 1 && sb.charAt(index + charAdded + 1) != ' ') { //add space after operand
                sb.insert(index + charAdded + 1, ' ');
                ++charAdded;
            }
        }
        expression = sb.toString().trim();
        expression = expression.replaceAll("  +", " ");
        return expression;
    }

    /**
     * uses fixPostfix to fix spaces then fixes negative number issues
     *
     * @param expression: infix expression to fix
     * @return fixed infix expression
     */
    public String fixInfix(String expression) {
        expression = fixPostfix(expression);
        expression = expression.replaceAll("- \\+|\\+ -", "-");
        expression = expression.replaceAll("- -", "+");
        StringBuilder sb = new StringBuilder(expression);
        int i;
        if (sb.length() == 0) return "";
        if (sb.charAt(0) == '-') { //negative in the beginning
            i = 0; //index of negative
            if (addParenthesis(sb, i)) return ""; //validInfix will return fasle
        }//END section negative in the beginning
        Pattern p = Pattern.compile("\\( -|/ -|\\* -");
        Matcher m = p.matcher(sb);
        while (m.find()) {
            i = m.start() + 2;
            if (addParenthesis(sb, i)) return "";
            m = p.matcher(sb);
        }
        return sb.toString();
    }

    private boolean addParenthesis(StringBuilder sb, int i) { //method extracted from duplicate code
        sb.insert(i, "( 0 ");
        i += 4;
        if (!sb.substring(i + 2, i + 3).matches("[+--*/()]")/*operand*/) {//operand after negative
            sb.insert(i + 3, " )");
        } else if (sb.charAt(i + 2) == '(') {//'(' after negative
            int closingIndex = i + 2;
            int parenthesis = 1;
            while (closingIndex < sb.length() && parenthesis != 0) {
                ++closingIndex;
                if (sb.charAt(closingIndex) == '(') ++parenthesis;
                else if (sb.charAt(closingIndex) == ')') --parenthesis;
            }
            if (parenthesis != 0) return true; //invalid
            sb.replace(i + 2, closingIndex + 1, fixInfix(sb.substring(i + 2, closingIndex + 1) + " )"));
        }
        return false;
    }

    /**
     * Validates an infix expression providing the operands and operators are space separated.
     * A valid infix expression is a one where the operands and operators alternate. It also has
     * to begin and end with operands.
     * Anything apart from {+,-,*,/,(,)} is considered operand
     *
     * @param expression: infix expression to be validated
     * @param fixed:      if the expression is space separated and fixed for negative numbers
     * @return true if the infix expression is valid and false otherwise
     */
    public boolean validInfix(String expression, boolean fixed) {
        if (!fixed) {
            expression = fixInfix(expression);
        }
        /*
         * Note that AFTER calling fixInfix, Strings returning from below calls of sc.next()
         * returns either operators, '(', ')', or operands which are anything else.
         * */
        Scanner sc = new Scanner(expression);
        Stack stk = new Stack();
        String next, top;
        int parenthesis = 0;
        try {
            next = sc.next();
        } catch (Exception e) {
            return false;
        }
        if (next.matches("[+--*/)]")) return false;
        stk.push(next); //operand or '('
        if (next.equals("(")) ++parenthesis;
        while (sc.hasNext()) {
            next = sc.next();
            top = (String) stk.peek();
            if (!next.matches("[+--*/()]") /*IS operand*/ && !top.matches("[+--*/(]")/*IS operand or ')'*/)
                return false;
            if (next.matches("[+--*/]")/*IS operator*/ && top.matches("[+--*/(]")/*IS operator or '('*/)
                return false;
            if (next.equals("(") && !top.matches("[+--*/(]")/*IS operand or ')'*/)
                return false;
            if (next.equals(")") && top.matches("[+--*/(]")/*IS operator or '('*/)
                return false;
            if (next.equals("(")) ++parenthesis;
            else if (next.equals(")")) --parenthesis;

            if (parenthesis < 0) return false;

            stk.push(next);
        }
        sc.close();
        //if top is operator or '(' or if parenthesis is not zero return false. return true otherwise.
        return !((String) (stk.peek())).matches("[+--*/(]") && parenthesis == 0;
    }

    /**
     * Although the postfix expression is ought to be true since it is returned from the
     * infixToPostfix method, the method throws RuntimeException in case of invalid postfix expression.
     *
     * @param expression postfix expression
     * @return result after evaluating expression
     */
    @Override
    public int evaluate(String expression) {
        expression = fixPostfix(expression);
        if (!validPostfix(expression, true)) throw new RuntimeException("Invalid postfix expression");
        Stack stk = new Stack();
        String input;
        Scanner sc = new Scanner(expression);
        while (sc.hasNext()) {
            input = sc.next();

            if (input.matches("[+--*/]")) {
                stk.push(getRes(stk.pop(), stk.pop(), input));
            } else {
                stk.push(Float.parseFloat(input));
            }
        }
        sc.close();
        return ((Float) stk.pop()).intValue();
    }

    /**
     * @param second operand
     * @param first  operand
     * @param op:    operation (one character)
     * @return the result
     */
    private Float getRes(Object second, Object first, String op) {
        Float x = (Float) first;
        Float y = (Float) second;
        char c = op.charAt(0);
        switch (c) {
            case '+':
                return x + y;
            case '-':
                return x - y;
            case '*':
                return x * y;
            /*
            default instead of case divide because 'op' is validated to be one of the four operation
             before calling getRes
             */
            default: // or case '/':
                if (y == 0) throw new RuntimeException("Division by zero");
                return x / y;
        }
    }

    /**
     * a valid post fix expression is one containing only operators or operands—no parenthesis—and the
     * number of operators at any point is less than or equal to the number of operands minus one. the number
     * of operators has to be equal to the number of operands minus one in the end.
     *
     * @param expression: postfix expression to be validated
     * @param fixed:     if the expression is space separated
     * @return true if the postfix expression is valid and false otherwise
     */
    public boolean validPostfix(String expression, boolean fixed) {
        if (!fixed) expression = fixPostfix(expression);
        Scanner sc = new Scanner(expression);
        int operand = 1, operator = 0;

        if (!sc.hasNext()) return false;
        String next = sc.next();
        if (next.matches("[+--*/()]")/*operator or parenthesis*/) return false;

        while (sc.hasNext()) {
            next = sc.next();
            if (!next.matches("[+--*/()]")/*IS operand*/) ++operand;
            else if (next.matches("[+--*/]")/*IS operator*/) ++operator;
            else return false; /*parenthesis*/
            if (operator > operand - 1) return false;
        }
        if (operator != operand - 1) return false;
        sc.close();
        return true;
    }
}
