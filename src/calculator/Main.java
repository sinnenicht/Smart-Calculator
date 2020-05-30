package calculator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static Map<String, BigInteger> variables = new HashMap<>();
    public static boolean isRunning = true;

    public static void main(String[] args) {
        while (isRunning) {
            programRuns();
        }
    }

    public static void programRuns() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        InputType currentInput = Parsers.parseInput(input);

        switch (currentInput) {
            case COMMAND:
                Parsers.parseCommand(input);
                break;
            case SINGLE_VALUE:
                Parsers.parseSingleValue(input);
                break;
            case ASSIGNMENT:
                Parsers.parseAssignment(input);
                break;
            case EXPRESSION:
                Expression expression = new Expression(input);

                if (expression.isValidExpression()) {
                    System.out.println(expression.getResult());
                }

                break;
            case EMPTY:
                break;
        }
    }
}