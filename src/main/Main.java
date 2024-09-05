package main;

import java.util.Scanner;

import solve.Lexer;
import solve.PreParser;
import solve.Parser;

public class Main {
    public static void main(String[] args) {
        testInput();
    }
    
    public static void testInput() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println(" \n");
    
        System.out.print("Please enter an equation to be solved: ");
        String input = scanner.nextLine();
        PreParser preParser = new PreParser(new Lexer(input).getTokens());
        System.out.println(Lexer.toStringTokens(preParser.getTokens()));
    
        scanner.close();
    }

    public static void input() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(" \n");

        System.out.print("Please enter an equation to be solved: ");
        String input = scanner.nextLine();
        Parser parser = new Parser(new Lexer(input).getTokens());
        if (parser.isEquation()) {
            System.out.println(parser.getEquation());
            parser.getEquation().simplify();
            System.out.println(parser.getEquation());
        }
        else {
            displayExpression(parser);

        }

        scanner.close();
    }

    public static void displayExpression(Parser parser) {
        System.out.print(parser.getOperation());
        System.out.print(" = ");
        System.out.print(parser.getOperation().evaluate());
        System.out.print(" = ");
        System.out.println(parser.getOperation().evaluate().getValue());
    }

    public static void test() {
        String[] tests = new String[5];

        tests[0] = "((4+(3*2))-5)/(1+2) = (7*(2+3))/(9-6)";
        tests[1] = "(12/((3+1)*2))+(5-(3*2))=((15-4)*2)-(7+1)";
        tests[2] = "((9-(2*(3+4)))/(2+8)) = (14/(7*(2-1)))+3";
        tests[3] = "(6+((7*2)-(5/1)))-(3*2) = ((4*2)-(7+1))+12";
        tests[4] = "(((8/(2+3))*4)-(6+1)) = ((9-(3*2))+(10-4))";

        for (String test : tests) {
            System.out.println(new Parser(new Lexer(test).getTokens()).getEquation());
            System.out.println();
        }
    }
}