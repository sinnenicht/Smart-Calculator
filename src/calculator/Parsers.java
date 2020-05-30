package calculator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsers {
    public static final Pattern posValuePattern = Pattern.compile("\\s*\\+([a-zA-Z]+|[0-9]+)\\s*");
    public static final Pattern negValuePattern = Pattern.compile("\\s*-([a-zA-Z]+|[0-9]+)\\s*");
    public static final Pattern intValuePattern = Pattern.compile("\\s*[-+]?[0-9]+\\s*");

    public static InputType parseInput(String input) {
        Pattern commandPattern = Pattern.compile("/.*");
        Pattern singleValuePattern = Pattern.compile("\\s*[-+]?([a-zA-Z]+|[0-9]+)\\s*");
        Pattern assignmentPattern = Pattern.compile(".*=.*");
        Pattern emptyPattern = Pattern.compile("");

        Matcher command = commandPattern.matcher(input);
        Matcher singleValue = singleValuePattern.matcher(input);
        Matcher assignment = assignmentPattern.matcher(input);
        Matcher empty = emptyPattern.matcher(input);

        if (command.matches()) {
            return InputType.COMMAND;
        } else if (singleValue.matches()) {
            return InputType.SINGLE_VALUE;
        } else if (assignment.matches()) {
            return InputType.ASSIGNMENT;
        } else if (empty.matches()) {
            return InputType.EMPTY;
        } else {
            return InputType.EXPRESSION;
        }
    }

    public static void parseCommand(String input) {
        Pattern helpPattern = Pattern.compile("/help");
        Pattern exitPattern = Pattern.compile("/exit");

        Matcher help = helpPattern.matcher(input);
        Matcher exit = exitPattern.matcher(input);

        if (help.matches()) {
            Print.printHelp();
        } else if (exit.matches()) {
            Print.printExit();
            Main.isRunning = false;
        } else {
            System.out.println("Unknown command");
        }
    }

    public static void parseSingleValue(String input) {
        Matcher posValue = posValuePattern.matcher(input);
        Matcher negValue = negValuePattern.matcher(input);
        Matcher intValue = intValuePattern.matcher(input);

        if (intValue.matches()) {

            if (posValue.matches()) {
                String value = removeOperator(input);
                System.out.println(value);
            } else {
                System.out.println(input);
            }

        } else if (posValue.matches()) {

            String value = removeOperator(input);
            if (Main.variables.containsKey(value)) {
                Print.printVar(value);
            } else {
                Print.printUnknownVar();
            }

        } else if (negValue.matches()) {

            String value = removeOperator(input);
            if (Main.variables.containsKey(value)) {
                Print.printNegativeVar(value);
            } else {
                Print.printUnknownVar();
            }

        } else {

            if (Main.variables.containsKey(input)) {
                Print.printVar(input);
            } else {
                Print.printUnknownVar();
            }
        }
    }

    public static void parseAssignment(String input) {
        Pattern identifierPattern = Pattern.compile("[a-zA-Z]+\\s*=.*");
        Pattern tooManyEqualsPattern = Pattern.compile(".*=.*=.*");
        Pattern simpleAssignmentPattern = Pattern.compile("[a-zA-Z]+\\s*=\\s*[-+]?([a-zA-Z]+|[0-9]+)\\s*");

        Matcher identifier = identifierPattern.matcher(input);
        Matcher tooManyEquals = tooManyEqualsPattern.matcher(input);
        Matcher simpleAssignment = simpleAssignmentPattern.matcher(input);

        if (identifier.matches()) {
            if (tooManyEquals.matches()) {
                Print.printInvalidAssign();
            } else {
                String[] numbers = input.split("(\\s*=\\s*|\\s+)");
                if (simpleAssignment.matches()) {
                    Matcher posValue = Parsers.posValuePattern.matcher(numbers[1]);
                    Matcher negValue = Parsers.negValuePattern.matcher(numbers[1]);
                    Matcher intValue = Parsers.intValuePattern.matcher(numbers[1]);

                    if (intValue.matches()) {

                        if (posValue.matches()) {
                            String value = removeOperator(numbers[1]);
                            Main.variables.put(numbers[0], new BigInteger(value));
                        } else {
                            Main.variables.put(numbers[0], new BigInteger(numbers[1]));
                        }

                    } else if (posValue.matches()) {

                        String value = removeOperator(numbers[1]);
                        if (Main.variables.containsKey(value)) {
                            Main.variables.put(numbers[0], Main.variables.get(value));
                        } else {
                            Print.printUnknownVar();
                        }

                    } else if (negValue.matches()) {

                        String value = removeOperator(numbers[1]);
                        if (Main.variables.containsKey(value)) {
                            Main.variables.put(numbers[0], Main.variables.get(value).negate());
                        } else {
                            Print.printUnknownVar();
                        }

                    } else {

                        if (Main.variables.containsKey(numbers[1])) {
                            Main.variables.put(numbers[0], Main.variables.get(numbers[1]));
                        } else {
                            Print.printUnknownVar();
                        }

                    }
                } else {
                    int length = numbers.length;
                    String[] assignment = Arrays.copyOfRange(numbers, 1, length);
                    StringBuilder exp = new StringBuilder();

                    for (String part : assignment) {
                        exp.append(part);
                    }

                    Expression expression = new Expression(String.valueOf(exp));
                    if (expression.isValidExpression()) {
                        Main.variables.put(numbers[0], expression.getResult());
                    }
                }
            }
        } else {
            System.out.println("Invalid identifier");
        }
    }

    public static String removeOperator(String number) {
        StringBuilder num = new StringBuilder(number);
        num.deleteCharAt(0);
        return String.valueOf(num);
    }
}
