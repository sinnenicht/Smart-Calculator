package calculator;

import java.math.BigInteger;

public class Print {
    public static void printHelp() {
        System.out.println("SMART CALCULATOR");
        System.out.println("================");
        System.out.println("Welcome!");
        System.out.println("Enter an expression, variable assignment, or single variable/number to get a result.");
        System.out.println("This calculator supports parentheses and the following operators: + - * / ^");
        System.out.println("The operators '+' and '-' can be repeated, e.g. '+++'.");
        System.out.println("For '-', the number of times it is repeated affects whether the value should be added or subtracted.");
        System.out.println("Numbers should be integers and may be positive or negative. Results will also be integers.");
        System.out.println("Variables may contain upper or lowercase alphabet characters.");
        System.out.println("Variables are assigned using '='. The assignment may be an expression or another variable.");
        System.out.println("Entering the variable name or a number will return the value of the variable or number.");
    }

    public static void printExit() {
        System.out.println("Bye!");
    }

    public static void printUnknownVar() {
        System.out.println("Unknown variable");
    }

    public static void printInvalidAssign() {
        System.out.println("Invalid assignment");
    }

    public static void printInvalidExpression() {
        System.out.println("Invalid expression");
    }

    public static void printVar(String var) {
        System.out.println(Main.variables.get(var));
    }

    public static void printNegativeVar(String var) {
        BigInteger value = Main.variables.get(var);
        System.out.println(value.negate());
    }
}
