package eg.edu.alexu.csd.datastructure;

import java.util.Scanner;

public class UIApplication {

    public static void main(String[] args) {
        System.out.println("\n-Anything apart from {+, -, *, /, (, )} is considered an operand.\n" +
                "-Operands can be more than one character without space—i.e. \"ab\" is one operand,\n whereas \"a b\" " +
                "are two different operands.\n" +
                "-\"x_2\", \"x2\", \"Xn\", \"Xnew\", etc... are all examples of operands.\n");
        ExpressionEvaluator ev = new ExpressionEvaluator();
        String postfix = "", infix = "";
        Scanner sc = new Scanner(System.in);
        String in;
        boolean same = false;
        while (true) {
            boolean flag = false;
            if (!same) {
                System.out.println("=================================");
                System.out.println("(input \"exit\" to exit)");
                System.out.println("Enter infix expression:");
                in = sc.nextLine();
                if (in.trim().equalsIgnoreCase("exit")) break;
                in = ev.fixInfix(in);

                while (!ev.validInfix(in, true)) {
                    System.out.println("Invalid Input");
                    System.out.println("=================================");
                    System.out.println("(input \"exit\" to exit)");
                    System.out.println("Enter infix expression");
                    in = sc.nextLine();
                    if (in.trim().equalsIgnoreCase("exit")) {
                        flag = true;
                        break;
                    }
                    in = ev.fixInfix(in);
                }
                if (flag) break;
                flag = false;

                /*infix is valid*/
                infix = in;
                postfix = ev.infixToPostfix(infix);
                same = true;
            }
            System.out.println("=================================");
            System.out.println("infix: " + infix + "\npostfix: " + postfix);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Stack symbols = new Stack();
            Scanner temp = new Scanner(postfix);
            String inTemp;
            /*creating a stack of symbols in postfix*/
            while (temp.hasNext()) {
                inTemp = temp.next();
                if (!inTemp.matches("[+--*/]")/*IS operand (symbol or int)*/ && !inTemp.matches("\\d+")/*Not int*/) {
                    symbols.push(inTemp);
                }
            }
            symbols.reverse();
            temp.close();
            if (symbols.size() == 0) same = false;

            String expression = postfix;
            int val;
            while (!symbols.isEmpty()) {
                if (!expression.contains((String) symbols.peek())) {//solves problem of duplicate symbols
                    symbols.pop();
                    continue;
                }
                System.out.println("=================================");
                System.out.println("(input \"back\" to go back)");
                System.out.println("Enter value for " + symbols.peek() + " :");
                in = sc.nextLine();
                if (in.trim().equalsIgnoreCase("back")) {
                    flag = true;
                    same = false;
                    break;
                }
                try {
                    val = Integer.parseInt(in);
                } catch (Exception e) {
                    System.out.println("Invalid Input");
                    continue;
                }
                expression = expression.replaceAll((String) symbols.pop(), Integer.toString(val));
            }
            if (flag) continue;
            System.out.println("=================================");
            System.out.println("Result = " + ev.evaluate(expression));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sc.close();
    }
}
