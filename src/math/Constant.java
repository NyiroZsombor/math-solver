package math;

import solve.Lexer;

public class Constant extends Operation {
    int value;
    
    public Constant(int value, byte type) {
        super(null, null, type);
        this.value = value;
    }

    @Override
    public String toString() {
        if (type == Lexer.NUM) return " ".concat(Integer.toString(value)).concat(" ");
        else return " x ";
    }

    public Rational evaluate() { return new Rational(this, new Constant(1, Lexer.NUM)); }

    public boolean isConst() { return true; }
}
