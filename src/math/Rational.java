package math;

import solve.Lexer;

public class Rational {
    private Constant numerator;
    private Constant denominator;

    public Rational(Constant numerator, Constant denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Rational add(Rational r) {
        return new Rational(
            new Constant(numerator.value * r.denominator.value + denominator.value * r.numerator.value, Lexer.NUM),
            new Constant(r.denominator.value * denominator.value, Lexer.NUM)
        );
    }

    public Rational subtract(Rational r) {
        return new Rational(
            new Constant(numerator.value * r.denominator.value + denominator.value * -r.numerator.value, Lexer.NUM),
            new Constant(r.denominator.value * denominator.value, Lexer.NUM)
        );
    }

    public Rational multiply(Rational r) {
        return new Rational(
            new Constant(numerator.value * r.numerator.value, Lexer.NUM),
            new Constant(denominator.value * r.denominator.value, Lexer.NUM)
        );
    }

    public Rational divide(Rational r) {
        return new Rational(
            new Constant(numerator.value * r.denominator.value, Lexer.NUM),
            new Constant(denominator.value * r.numerator.value, Lexer.NUM)
        );
    }

    public Rational simplify() {
        Rational result = new Rational(numerator, denominator);
        boolean retry = true;

        while (retry) {
            retry = false;
            for (int i = 2; i <= Math.floorDiv(Math.max(result.numerator.value, result.denominator.value), 2); i += (i == 2 ? 1 : 2)) {
                if (result.numerator.value % i == 0 && result.denominator.value % i == 0) {
                    result.numerator.value /= i;
                    result.denominator.value /= i;
                    retry = true;
                    break;
                }
            }
        } 

        return result;
    }

    public String toString() {
        return numerator + " / " + denominator;
    }

    public void setNumerator(int i) { numerator.value = i; }
    public void setDenominator(int i) { denominator.value = i; }

    public Rational inverse() { return new Rational(denominator, numerator); }
    public Constant getNumerator() { return numerator; }
    public Constant getDenominator() { return denominator; }
    public double getValue() { return (double)numerator.value / (double)denominator.value; }
}
