package solve;

import java.util.ArrayList;

import main.Error;


public class PreParser {
    ArrayList<Byte> expected;
    ArrayList<Byte> preParsed;
    byte[] tokens;
    int idx;
    boolean isEquation;

    public PreParser(byte[] tokens) {
        this.tokens = tokens;
        expected = new ArrayList<>(Lexer.TOKEN_COUNT);
        idx = 0;
        isEquation = false;

        preParsed = parseExpression();
        preParsed.add(Lexer.EOF);
    }

    public ArrayList<Byte> parseExpression() {
        ArrayList<Byte> operations = new ArrayList<>();
        ArrayList<Byte> result = new ArrayList<>();
        byte lastOperationID = Lexer.EOF;

        operations.add(Lexer.ADD);
        operations.add(Lexer.SUB);
        operations.add(Lexer.MULT);
        operations.add(Lexer.DIV);

        expected.clear();
        expected.add(Lexer.LEFT);
        expected.add(Lexer.NUM);
        expected.add(Lexer.VAR);

        while (idx < tokens.length) {
            isEquation = isEquation || tokens[idx] == Lexer.EQUAL;

            if (!expected.contains(tokens[idx])) {
                new Error(Error.unexpectedToken, tokens, idx, expected);
                return result;
            }

            if (tokens[idx] == Lexer.EOF) return result;

            if (tokens[idx] == Lexer.RIGHT) return result;

            else if (operations.contains(tokens[idx])) {
                lastOperationID = tokens[idx];

                expected.clear();
                expected.add(Lexer.LEFT);
                expected.add(Lexer.NUM);
                expected.add(Lexer.VAR);
            }
            
            else if (tokens[idx] == Lexer.LEFT) {
                if (lastOperationID == Lexer.EOF) {
                    idx++;
                    result = parseProduct();
                }
                else if (lastOperationID == Lexer.ADD || lastOperationID == Lexer.SUB) {
                    result.add(0, Lexer.LEFT);
                    result.add(lastOperationID);
                    ArrayList<Byte> expr = parseProduct();

                    for (int j = 0; j < expr.size(); j++) {
                        result.add(expr.get(j));
                    }

                    result.add(Lexer.RIGHT);
                }
                else System.err.println("MULT or DIV");

                expected.clear();
                expected.add(Lexer.ADD);
                expected.add(Lexer.SUB);
                expected.add(Lexer.MULT);
                expected.add(Lexer.DIV);
                expected.add(Lexer.RIGHT);
                expected.add(Lexer.EQUAL);
                expected.add(Lexer.EOF);
            }

            else if (tokens[idx] == Lexer.NUM || tokens[idx] == Lexer.VAR) {
                if (lastOperationID == Lexer.EOF) {
                    if (tokens[idx] == Lexer.NUM) {
                        result = parseProduct();
                        idx--;
                    }
                    else result.add(Lexer.VAR);
                }
                else if (lastOperationID == Lexer.ADD || lastOperationID == Lexer.SUB) {
                    result.add(0, Lexer.LEFT);
                    result.add(lastOperationID);
                    ArrayList<Byte> expr = parseProduct();

                    for (int j = 0; j < expr.size(); j++) {
                        result.add(expr.get(j));
                    }

                    result.add(Lexer.RIGHT);
                }
                else System.err.println("MULT or DIV");

                expected.clear();
                expected.add(Lexer.ADD);
                expected.add(Lexer.SUB);
                expected.add(Lexer.MULT);
                expected.add(Lexer.DIV);
                expected.add(Lexer.RIGHT);
                expected.add(Lexer.EQUAL);
                expected.add(Lexer.EOF);
            }

            idx++;
        }

        return result;
    }

    public ArrayList<Byte> parseProduct() {
        ArrayList<Byte> result = new ArrayList<>();
        byte lastOperationID = Lexer.EOF;
        
        expected.clear();
        expected.add(Lexer.LEFT);
        expected.add(Lexer.NUM);
        expected.add(Lexer.VAR);

        while (idx < tokens.length) {
            if (!expected.contains(tokens[idx])) {
                new Error(Error.unexpectedToken, tokens, idx, expected);
                return result; // TODO
            }

            else if (tokens[idx] == Lexer.EQUAL || tokens[idx] == Lexer.EOF) break;
            else if (tokens[idx] == Lexer.ADD || tokens[idx] == Lexer.SUB) break;
            else if (tokens[idx] == Lexer.RIGHT) break;

            else if (tokens[idx] == Lexer.MULT || tokens[idx] == Lexer.DIV) {
                lastOperationID = tokens[idx];

                expected.clear();
                expected.add(Lexer.LEFT);
                expected.add(Lexer.NUM);
                expected.add(Lexer.VAR);
            }

            else if (tokens[idx] == Lexer.NUM || tokens[idx] == Lexer.VAR) {
                ArrayList<Byte> number = new ArrayList<>(5);
                if (tokens[idx] == Lexer.NUM) {
                    number.add(tokens[idx]);

                    for (int j = 0; j < 4; j++) {
                        idx++;
                        number.add(tokens[idx]);
                    }
                }
                else if (tokens[idx] == Lexer.VAR) number.add(Lexer.VAR);

                if (lastOperationID == Lexer.EOF) result = number;
                else if (lastOperationID == Lexer.MULT || lastOperationID == Lexer.DIV) {
                    result.add(0, Lexer.LEFT);
                    result.add(lastOperationID);

                    for (int j = 0; j < number.size(); j++) {
                        result.add(number.get(j));
                    }

                    result.add(Lexer.RIGHT);
                }
                else System.err.println("MULT or DIV");

                expected.clear();
                expected.add(Lexer.ADD);
                expected.add(Lexer.SUB);
                expected.add(Lexer.MULT);
                expected.add(Lexer.DIV);
                expected.add(Lexer.RIGHT);
                expected.add(Lexer.EQUAL);
                expected.add(Lexer.EOF);
            }

            else if (tokens[idx] == Lexer.LEFT) {
                idx++;

                if (lastOperationID == Lexer.EOF) result = parseExpression();
                else if (lastOperationID == Lexer.MULT || lastOperationID == Lexer.DIV) {
                    result.add(0, Lexer.LEFT);
                    result.add(lastOperationID);
                    ArrayList<Byte> expr = parseProduct();

                    for (int j = 0; j < expr.size(); j++) {
                        result.add(expr.get(j));
                    }

                    result.add(Lexer.RIGHT);
                }
                else System.err.println("MULT or DIV");

                expected.clear();
                expected.add(Lexer.ADD);
                expected.add(Lexer.SUB);
                expected.add(Lexer.MULT);
                expected.add(Lexer.DIV);
                expected.add(Lexer.RIGHT);
                expected.add(Lexer.EQUAL);
                expected.add(Lexer.EOF);
            }

            idx++;
        }

        return result;
    }

    public void addExpressionToResult(ArrayList<Byte> expr, ArrayList<Byte> result) {
        result.add(0, Lexer.LEFT);
        for (int j = 0; j < expr.size(); j++) {
            result.add(expr.get(j));
        }
        result.add(Lexer.RIGHT);
    }

    public byte[] getTokens() {
        byte[] resultArray = new byte[preParsed.size()];
        for (int j = 0; j < preParsed.size(); j++) {
            resultArray[j] = preParsed.get(j);
        }

        return resultArray;
    }
}
