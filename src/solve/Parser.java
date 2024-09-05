package solve;

import math.Constant;
import math.Equation;
import math.Operation;

import main.Error;

import java.util.ArrayList;
// import java.util.HashMap;

public class Parser {
    Equation equation;
    Operation operation;
    ArrayList<Byte> expected = new ArrayList<>(Lexer.TOKEN_COUNT);
    // HashMap<Byte, ArrayList<Byte>> expectedMap = new HashMap<>();
    boolean isEquation = false;
    byte[] tokens;
    int idx;


    public Parser(byte[] tokens) {
        this.tokens = tokens;
        this.idx = 0;

        // setUpExpectedMap();
        
        Operation left = parseExpression();

        if (isEquation) {
            // System.out.println("Equation");
            Operation right = parseExpression();
            equation = new Equation(left, right);
        }
        else operation = left;
        
    }

    /*
    private void setUpExpectedMap() {
        ArrayList<Byte> expectedAfterOperation = new ArrayList<>();
        expectedAfterOperation.add(Lexer.LEFT);
        expectedAfterOperation.add(Lexer.NUM);
        expectedAfterOperation.add(Lexer.VAR);
        expectedMap.put(Lexer.ADD, expectedAfterOperation);
        expectedMap.put(Lexer.SUB, expectedAfterOperation);
        expectedMap.put(Lexer.MULT, expectedAfterOperation);
        expectedMap.put(Lexer.DIV, expectedAfterOperation);

        ArrayList<Byte> expectedAfterLeft = new ArrayList<>();
        expectedAfterLeft.add(Lexer.ADD);
        expectedAfterLeft.add(Lexer.SUB);
        expectedAfterLeft.add(Lexer.MULT);
        expectedAfterLeft.add(Lexer.DIV);
        expectedAfterLeft.add(Lexer.RIGHT);
        expectedAfterLeft.add(Lexer.EQUAL);
        expectedAfterLeft.add(Lexer.EOF);
        expectedMap.put(Lexer.LEFT, expectedAfterLeft);
        
        ArrayList<Byte> expectedAfterConst = new ArrayList<>();
        expectedAfterConst.add(Lexer.ADD);
        expectedAfterConst.add(Lexer.SUB);
        expectedAfterConst.add(Lexer.MULT);
        expectedAfterConst.add(Lexer.DIV);
        expectedAfterConst.add(Lexer.EQUAL);
        expectedAfterConst.add(Lexer.EOF);
        expectedMap.put(Lexer.NUM, expectedAfterConst);
        expectedMap.put(Lexer.VAR, expectedAfterConst);
    }
    */

    public Operation parseExpression() {
        ArrayList<Byte> operations = new ArrayList<>();
        Operation value = new Constant(-1, Lexer.EOF);
        // int lastValue;
        byte lastOperationID = Lexer.EOF;
        // boolean isLastOperationInitialized = false;
        // boolean isLastValueInitialized = false;
        // *boolean parenthesisOpen = false;

        operations.add(Lexer.ADD);
        operations.add(Lexer.SUB);
        operations.add(Lexer.MULT);
        operations.add(Lexer.DIV);

        expected.clear();
        expected.add(Lexer.LEFT);
        expected.add(Lexer.NUM);
        expected.add(Lexer.VAR);

        while (true) {
            if (!expected.contains(tokens[idx])) {
                new Error(Error.unexpectedToken, tokens, idx, expected);
                return new Constant(Integer.MAX_VALUE, Lexer.NUM); // TODO
            }

            else if (tokens[idx] == Lexer.EOF) return value;

            else if (tokens[idx] == Lexer.RIGHT) {
                idx++;
                return value;
            }
            
            else if (tokens[idx] == Lexer.EQUAL) {
                idx++;
                isEquation = true;
                return value;
            }

            else if (operations.contains(tokens[idx])) {
                lastOperationID = tokens[idx];

                expected.clear();
                expected.add(Lexer.LEFT);
                expected.add(Lexer.NUM);
                expected.add(Lexer.VAR);
            }
            
            else if (tokens[idx] == Lexer.LEFT) {
                if (lastOperationID == Lexer.EOF) value = parseProduct();
                else if (lastOperationID == Lexer.ADD || lastOperationID == Lexer.SUB) {
                    value = new Operation(value, parseProduct(), lastOperationID);
                }
                else System.err.println("MULT or DIV");
                idx--;

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
                if (lastOperationID == Lexer.EOF) value = parseProduct();
                else if (lastOperationID == Lexer.ADD || lastOperationID == Lexer.SUB) {
                    value = new Operation(value, parseProduct(), lastOperationID);
                }
                else System.err.println("MULT or DIV");

                idx -= 1;

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
    }

    public Operation parseProduct() {
        Operation value = new Constant(-1, Lexer.EOF);
        byte lastOperationID = Lexer.EOF;

        expected.clear();
        expected.add(Lexer.LEFT);
        expected.add(Lexer.NUM);
        expected.add(Lexer.VAR);

        while (true) {
            if (!expected.contains(tokens[idx])) {
                new Error(Error.unexpectedToken, tokens, idx, expected);
                return new Constant(Integer.MAX_VALUE, Lexer.NUM); // TODO
            }

            else if (tokens[idx] == Lexer.EQUAL || tokens[idx] == Lexer.EOF) return value; 
            else if (tokens[idx] == Lexer.ADD || tokens[idx] == Lexer.SUB) return value;
            else if (tokens[idx] == Lexer.RIGHT) return value;

            else if (tokens[idx] == Lexer.MULT || tokens[idx] == Lexer.DIV) {
                lastOperationID = tokens[idx];

                expected.clear();
                expected.add(Lexer.LEFT);
                expected.add(Lexer.NUM);
                expected.add(Lexer.VAR);
            }

            else if (tokens[idx] == Lexer.NUM || tokens[idx] == Lexer.VAR) {
                Constant constant = new Constant(parseNum(tokens, idx), tokens[idx]);
                if (tokens[idx] == Lexer.NUM) idx += 4;

                if (lastOperationID == Lexer.EOF) value = constant;
                else if (lastOperationID == Lexer.MULT || lastOperationID == Lexer.DIV) {
                    value = new Operation(value, constant, lastOperationID);
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

                if (lastOperationID == Lexer.EOF) value = parseExpression();
                else if (lastOperationID == Lexer.MULT || lastOperationID == Lexer.DIV) {
                    value = new Operation(value, parseExpression(), lastOperationID);
                }
                else System.err.println("MULT or DIV");

                idx--;

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
    }

    public static int parseNum(byte[] tokens, int currentTokenIdx) {
        int value = 0;

        if (tokens[currentTokenIdx] == Lexer.NUM) {
            for (int j = 0; j < 4; j++) {
                int currentByte = tokens[currentTokenIdx + j + 1];

                if (currentByte < 0) {
                    currentByte += 256;
                }

                value += currentByte << (24 - j * 8);
            }
        }

        return value;
    }

    public Equation getEquation() { return equation; }
    public Operation getOperation() { return operation; }
    public boolean isEquation() { return isEquation; }
}
