package GUI;

import eg.edu.alexu.csd.datastructure.ExpressionEvaluator;

import eg.edu.alexu.csd.datastructure.Stack;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Scanner;


public class Scene {

    private String sPostfix;
    private Stack symbols = new Stack();
    private String expression;

    @FXML
    private Button evaluateButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField infixTextfield, symbolsTextfield;
    @FXML
    private Label postfixLabel, resultLabel, symbolsLabel, invalidLabel;

    @FXML
    private void displayPostfix() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        String sInfix = infixTextfield.getText();
        if (ev.validInfix(sInfix, false)) {
            sPostfix = ev.infixToPostfix(sInfix);
            postfixLabel.setText(sPostfix);
            evaluateButton.setDisable(false);
        } else {
            postfixLabel.setText("Invalid Infix Expression");
            evaluateButton.setDisable(true);
        }
    }

    @FXML
    private void displayResult() {
        ExpressionEvaluator ev = new ExpressionEvaluator();

        /*creating a stack of symbols in postfix*/
        Scanner sc = new Scanner(sPostfix);
        String sTemp;
        while (sc.hasNext()) {
            sTemp = sc.next();
            if (!sTemp.matches("[+--*/]")/*NOT operand (symbol or int)*/
                    && !sTemp.matches("\\d+")/*Not int*/) {
                symbols.push(sTemp);
            }
        }
        symbols.reverse();
        sc.close();

        if (symbols.isEmpty()) {
            resultLabel.setText(String.valueOf(ev.evaluate(sPostfix)));
        } else {
            expression = sPostfix;
            symbolsLabel.setText(symbols.peek() + " =");
            nextButton.setDisable(false);
            symbolsTextfield.setEditable(true);
        }
    }

    @FXML
    private void getSymbols() {
        ExpressionEvaluator ev = new ExpressionEvaluator();
        int val;
        try {
            val = Integer.parseInt(symbolsTextfield.getText());
        } catch (Exception e) {
            invalidLabel.setText("Invalid Input");
            return;
        }

        invalidLabel.setText("");
        expression = expression.replaceAll((String) symbols.pop(), Integer.toString(val));

        while (!symbols.isEmpty() && !expression.contains((String) symbols.peek())) symbols.pop();

        if (symbols.isEmpty()) {
            nextButton.setDisable(true);
            symbolsTextfield.setText("");
            symbolsTextfield.setEditable(false);
            symbolsLabel.setText("");
            resultLabel.setText(String.valueOf(ev.evaluate(expression)));
        } else {
            symbolsLabel.setText(symbols.peek() + " =");
        }
    }
}
