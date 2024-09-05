package math;

import solve.Lexer;

public class Operation {
    byte type;
    int value;
    Operation left;
    Operation right;

    public Operation(Operation left, Operation right, byte type) {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    @Override
    public String toString() {
        String result = "";
        char symbol;

        if (type == Lexer.ADD) symbol = '+';
        else if (type == Lexer.SUB) symbol = '-';
        else if (type == Lexer.MULT) symbol = '*';
        else if (type == Lexer.DIV) symbol = '/';
        else symbol = '?';

        result = result.concat(" (");
        result = result.concat(left.toString());
        result = result.concat(String.valueOf(symbol));
        result = result.concat(right.toString());
        result = result.concat(") ");

        return result;
    }

    public Rational evaluate() {
        if (type == Lexer.ADD) return left.evaluate().add(right.evaluate());
        else if (type == Lexer.SUB) return left.evaluate().subtract(right.evaluate());
        else if (type == Lexer.MULT) return left.evaluate().multiply(right.evaluate());
        else return left.evaluate().divide(right.evaluate());
    }

    public boolean isConst() { return false; }
}
