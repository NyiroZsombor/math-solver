package math;

import solve.Lexer;

// import solve.Lexer;

public class Equation {
    Operation left;
    Operation right;

    public Equation(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        String result = "";
        result = result.concat(left.toString());
        result = result.concat(" = ");
        result = result.concat(right.toString());

        return result;
    }

    public void simplify() {
        if (left.type == Lexer.ADD || left.type == Lexer.SUB || left.type == Lexer.MULT) {
            left = simplifyIntegerOperation(left);
        }
    }

    private static Constant simplifyIntegerOperation(Operation op) {
        if (op.left.type == Lexer.ADD || op.left.type == Lexer.SUB || op.left.type == Lexer.MULT) {
            op.left = simplifyIntegerOperation(op.left);
        }
        if (op.right.type == Lexer.ADD || op.right.type == Lexer.SUB || op.right.type == Lexer.MULT) {
            op.right = simplifyIntegerOperation(op.right);
        }

        if (op.type == Lexer.ADD) return new Constant(op.left.value + op.right.value, Lexer.NUM);
        if (op.type == Lexer.SUB) return new Constant(op.left.value - op.right.value, Lexer.NUM);
        else return new Constant(op.left.value * op.right.value, Lexer.NUM);
    }
}
