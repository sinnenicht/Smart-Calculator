package calculator;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    private boolean validExpression;
    private Queue<String> infix;
    private Queue<String> postfix;
    private BigInteger result;

    private final String plusMinusOps = "[-+]";
    private final String starSlashOps = "[*/]";
    private final String caretOp = "\\^";
    private final String ParenOps = "[()]";

    private final Pattern plusMinusPattern = Pattern.compile(plusMinusOps);
    private final Pattern starSlashPattern = Pattern.compile(starSlashOps);
    private final Pattern caretPattern = Pattern.compile(caretOp);
    private final Pattern leftParenPattern = Pattern.compile("\\(");
    private final Pattern rightParenPattern = Pattern.compile("\\)");
    private final Pattern operatorPattern = Pattern.compile("(" + plusMinusOps + "|" + starSlashOps + "|" + caretOp + "|" + ParenOps + ")");
    private final Pattern varNamePattern = Pattern.compile("[a-zA-Z]+");

    public Expression(String expression) {
        this.validExpression = checkValidExpression(expression);

        if (validExpression) {
            this.infix = prepareInfix(expression);
            this.postfix = createPostfix(infix);
            this.result = calculate(postfix);
        } else {
            Print.printInvalidExpression();
        }
    }

    public BigInteger getResult() {
        return this.result;
    }

    public boolean isValidExpression() {
        return this.validExpression;
    }

    private boolean checkValidExpression(String expression) {
        String illegalOperatorBuffer = "[^a-zA-Z0-9\\s\\-+]";
        String illegalCharRegex = "[^a-zA-Z0-9\\-+*/^()=\\s]";
        String illegalVariableRegex = "([a-zA-Z][0-9]|[0-9][a-zA-Z])";
        String illegalOperatorRegex = illegalOperatorBuffer + "[+\\-*/]" + illegalOperatorBuffer;
        String illegalCaretRegex = "[^(a-zA-Z|0-9)][\\^][^(a-zA-Z|0-9)]";

        Pattern illegalCharPattern = Pattern.compile(illegalCharRegex);
        Pattern illegalVariablePattern = Pattern.compile(illegalVariableRegex);
        Pattern illegalOperatorPattern = Pattern.compile(illegalOperatorRegex);
        Pattern illegalCaretPattern = Pattern.compile(illegalCaretRegex);

        Matcher illegalChar = illegalCharPattern.matcher(expression);
        Matcher illegalVariable = illegalVariablePattern.matcher(expression);
        Matcher illegalOperator = illegalOperatorPattern.matcher(expression);
        Matcher illegalCaret = illegalCaretPattern.matcher(expression);

        if (illegalChar.find() || illegalVariable.find() || illegalOperator.find() || illegalCaret.find()) {
            return false;
        } else {
            return !hasIllegalVariables(expression) && hasBalancedParens(expression);
        }
    }

    // returns true if an illegal variable is found
    private boolean hasIllegalVariables(String expression) {
        String[] parts = expression.split("\\s+");

        for (String part : parts) {
            Matcher varName = varNamePattern.matcher(part);

            if (varName.find()) {
                String[] noOps = part.split("([+*/^()]|-)");
                int length = noOps.length;

                if (length > 1) {
                    for (String string : noOps) {
                        Matcher innerVarName = varNamePattern.matcher(string);

                        if (innerVarName.matches()) {
                            if (!Main.variables.containsKey(string)) {
                                return true;
                            }
                        }
                    }
                } else {
                    if (!Main.variables.containsKey(noOps[0])) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // returns true if there is an equal number of left and right parens
    private boolean hasBalancedParens(String expression) {
        Matcher leftParen = leftParenPattern.matcher(expression);
        Matcher rightParen = rightParenPattern.matcher(expression);

        if (!leftParen.find() && !rightParen.find()) {
            return true;
        } else {
            int numLeft = 0;
            int numRight = 0;

            for (int index = 0; index < expression.length(); index++) {
                Matcher left = leftParenPattern.matcher(String.valueOf(expression.charAt(index)));
                Matcher right = rightParenPattern.matcher(String.valueOf(expression.charAt(index)));

                if (left.matches()) {
                    numLeft += 1;
                } else if (right.matches()) {
                    numRight += 1;
                }
            }

            return numLeft == numRight;
        }
    }

    private Queue<String> prepareInfix(String expression) {
        String exp = expression.replaceAll("\\+\\++", "+").replaceAll("\\s", "");
        Queue<String> infix = new ArrayDeque<>();
        StringBuilder currentValue = new StringBuilder();
        int length = exp.length();

        for (int index = 0; index < length; index++) {
            char currentChar = exp.charAt(index);

            if (currentChar >= 'a' && currentChar <= 'z' || currentChar >= 'A' && currentChar <= 'Z') {
                currentValue.append(currentChar);

                if (!hasFollowingAlpha(exp, index)) {
                    infix.add(currentValue.toString());
                    currentValue.delete(0, currentValue.length());
                }

            } else if (currentChar >= '0' && currentChar <= '9') {
                currentValue.append(currentChar);

                if (!hasFollowingDigit(exp, index)) {
                    infix.add(currentValue.toString());
                    currentValue.delete(0, currentValue.length());
                }

            } else if (currentChar == '-') {
                currentValue.append(currentChar);

                if (!hasFollowingMinus(exp, index)) {
                    infix.add(minusOrPlus(currentValue));
                    currentValue.delete(0, currentValue.length());
                }

            } else {
                infix.add(String.valueOf(currentChar));
            }
        }

        return infix;
    }

    private boolean hasFollowingAlpha(String expression, int currentIndex) {
        int nextIndex = currentIndex + 1;
        int length = expression.length();

        if (nextIndex < length) {
            char nextChar = expression.charAt(nextIndex);

            return nextChar >= 'a' && nextChar <= 'z' || nextChar >= 'A' && nextChar <= 'Z';
        } else {
            return false;
        }
    }

    private boolean hasFollowingDigit(String expression, int currentIndex) {
        int nextIndex = currentIndex + 1;
        int length = expression.length();

        if (nextIndex < length) {
            char nextChar = expression.charAt(nextIndex);

            return nextChar >= '0' && nextChar <= '9';
        } else {
            return false;
        }
    }

    private boolean hasFollowingMinus(String expression, int currentIndex) {
        int nextIndex = currentIndex + 1;
        int length = expression.length();

        if (nextIndex < length) {
            char nextChar = expression.charAt(nextIndex);

            return nextChar == '-';
        } else {
            return false;
        }
    }

    private String minusOrPlus(StringBuilder string) {
        int length = string.length();

        if (length % 2 == 0) {
            return "+";
        } else {
            return "-";
        }
    }

    private Queue<String> createPostfix(Queue<String> infix) {
        Queue<String> postfix = new ArrayDeque<>();
        Deque<String> operatorStack = new ArrayDeque<>();

        while (infix.peek() != null) {
            Matcher infixOperator = operatorPattern.matcher(infix.peek());

            if (infixOperator.matches()) {
                if (operatorStack.peek() != null) {
                    Matcher incomingLeftParen = leftParenPattern.matcher(infix.peek());
                    Matcher incomingRightParen = rightParenPattern.matcher(infix.peek());
                    Matcher stackLeftParen = leftParenPattern.matcher(operatorStack.peek());

                    if (incomingLeftParen.matches() || stackLeftParen.matches()) {
                        operatorStack.push(infix.poll());
                    } else if (incomingRightParen.matches()) {
                        infix.poll();

                        while (operatorStack.peek() != null) {
                            Matcher leftParen = leftParenPattern.matcher(operatorStack.peek());

                            if (leftParen.matches()) {
                                operatorStack.pop();
                                break;
                            } else {
                                postfix.add(operatorStack.pop());
                            }
                        }
                    } else {
                        if (incomingHasPrecedence(infix.peek(), operatorStack.peek())) {
                            operatorStack.push(infix.poll());
                        } else {
                            while (operatorStack.peek() != null) {
                                Matcher leftParen = leftParenPattern.matcher(operatorStack.peek());

                                if (incomingHasPrecedence(infix.peek(), operatorStack.peek()) || leftParen.matches()) {
                                    operatorStack.push(infix.poll());
                                    break;
                                } else {
                                    postfix.add(operatorStack.pop());
                                }
                            }
                        }
                    }
                } else {
                    operatorStack.push(infix.poll());
                }
            } else {
                postfix.add(infix.poll());
            }
        }

        while (operatorStack.peek() != null) {
            postfix.add(operatorStack.pop());
        }

        return postfix;
    }

    private boolean incomingHasPrecedence(String incoming, String stack) {
        Matcher incomingPlusMinus = plusMinusPattern.matcher(incoming);
        Matcher incomingStarSlash = starSlashPattern.matcher(incoming);
        Matcher incomingCaret = caretPattern.matcher(incoming);
        Matcher stackPlusMinus = plusMinusPattern.matcher(stack);
        Matcher stackStarSlash = starSlashPattern.matcher(stack);
        Matcher stackCaret = caretPattern.matcher(stack);

        return (incomingCaret.matches() && !stackCaret.matches()) ||
                (incomingStarSlash.matches() && !stackCaret.matches() && !stackStarSlash.matches()) ||
                (incomingPlusMinus.matches() && !stackCaret.matches() && !stackStarSlash.matches() && !stackPlusMinus.matches());
    }

    private BigInteger calculate(Queue<String> postfix) {
        Deque<BigInteger> resultStack = new ArrayDeque<>();

        Pattern numberPattern = Pattern.compile("[0-9]+");
        Pattern plusPattern = Pattern.compile("\\+");
        Pattern minusPattern = Pattern.compile("-");
        Pattern starPattern = Pattern.compile("\\*");
        Pattern slashPattern = Pattern.compile("/");
        Pattern caretPattern = Pattern.compile("\\^");

        while (postfix.peek() != null) {
            Matcher variable = varNamePattern.matcher(postfix.peek());
            Matcher number = numberPattern.matcher(postfix.peek());

            if (variable.matches()) {
                resultStack.push(Main.variables.get(postfix.poll()));
            } else if (number.matches()) {
                resultStack.push(new BigInteger(postfix.poll()));
            } else {
                Matcher plus = plusPattern.matcher(postfix.peek());
                Matcher minus = minusPattern.matcher(postfix.peek());
                Matcher star = starPattern.matcher(postfix.peek());
                Matcher slash = slashPattern.matcher(postfix.peek());
                Matcher caret = caretPattern.matcher(postfix.peek());

                if (plus.matches()) {
                    BigInteger firstNum = resultStack.pop();
                    BigInteger secondNum = resultStack.pop();
                    resultStack.push(firstNum.add(secondNum));

                } else if (minus.matches()) {
                    BigInteger firstNum = resultStack.pop();
                    BigInteger secondNum = resultStack.pop();
                    resultStack.push(secondNum.subtract(firstNum));

                } else if (star.matches()) {
                    BigInteger firstNum = resultStack.pop();
                    BigInteger secondNum = resultStack.pop();
                    resultStack.push(firstNum.multiply(secondNum));

                } else if (slash.matches()) {
                    BigInteger firstNum = resultStack.pop();
                    BigInteger secondNum = resultStack.pop();

                    if (firstNum.compareTo(BigInteger.ZERO) == 0) {
                        System.out.println("Error: divide by zero!");
                        if (resultStack.peek() == null) {
                            resultStack.push(BigInteger.ZERO);
                        }
                        break;
                    } else {
                        resultStack.push(secondNum.divide(firstNum));
                    }

                } else if (caret.matches()) {
                    BigInteger firstNum = resultStack.pop();
                    BigInteger secondNum = resultStack.pop();
                    resultStack.push(secondNum.pow(firstNum.intValue()));
                }

                postfix.poll();
            }
        }

        return resultStack.pop();
    }
}
