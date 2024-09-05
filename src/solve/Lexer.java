package solve;

import java.util.ArrayList;

public class Lexer {
    byte[] tokens;

    public static final byte ADD = 0;
    public static final byte SUB = 1;
    public static final byte MULT = 2;
    public static final byte DIV = 3;
    public static final byte LEFT = 4;
    public static final byte RIGHT = 5;
    public static final byte NUM = 6;
    public static final byte VAR = 7;
    public static final byte EQUAL = 8;
    public static final byte DOT = 9;
    public static final byte EOF = 127;

    public static final byte TOKEN_COUNT = 11;

    private static final short NUMS_MIN = 48; 
    private static final short NUMS_MAX = 57;
    private static final short UPPER_CASE_MIN = 65;
    private static final short UPPER_CASE_MAX = 90;
    private static final short LOWER_CASE_MIN = 97;
    private static final short LOWER_CASE_MAX = 122;

    public Lexer(String input) {
        tokens = lex(input);
    }

    private static int lexNums(String rawString, ArrayList<Byte> result, int i) {
        int value = 0;
        boolean tokenIsNum = false;
        while (rawString.charAt(i) >= NUMS_MIN && rawString.charAt(i) <= NUMS_MAX) {
            tokenIsNum = true;
            value *= 10;
            value += (rawString.charAt(i) - NUMS_MIN);
            i++;
            if (i == rawString.length()) break;
        }

        if (tokenIsNum) {
            result.add(NUM);
            result.add((byte)(255 & (value >> 24)));
            result.add((byte)(255 & (value >> 16)));
            result.add((byte)(255 & (value >> 8)));
            result.add((byte)(255 & value));
        }

        return i;
    }

    public static byte[] lex(String rawString) {
        ArrayList<Byte> result = new ArrayList<>();
        
        for (int i = 0; i < rawString.length(); i++) {
            char chr = rawString.charAt(i);
            
            i = lexNums(rawString, result, i);
            if (i == rawString.length()) break;
            chr = rawString.charAt(i);

            if ((chr >= LOWER_CASE_MIN && chr <= LOWER_CASE_MAX)
            || (chr >= UPPER_CASE_MIN && chr <= UPPER_CASE_MAX)) result.add(VAR);

            else if (chr == '+') result.add(ADD);
            else if (chr == '-') result.add(SUB);
            else if (chr == '*') result.add(MULT);
            else if (chr == '/') result.add(DIV);
            else if (chr == '(') result.add(LEFT);
            else if (chr == ')') result.add(RIGHT);
            else if (chr == '=') result.add(EQUAL);
            else if (chr == ' ') continue;
            //else if (chr == '.' || chr == ',') result.add(DOT);
            else {
                System.err.println("Unknown character ".concat(Character.toString(chr)).concat(" at position ").concat(Integer.toString(i)).concat("."));

                result.add(EOF);
                byte[] resultArray = new byte[result.size()];
                for (int j = 0; j < result.size(); j++) {
                    resultArray[j] = result.get(j);
                }

                return resultArray;
            }
        }
        
        result.add(EOF);
        byte[] resultArray = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }

        return resultArray;
    }

    public static String toString(byte[] tokens) {
        String result = "[".concat(Byte.toString(tokens[0]));

        for (int i = 1; i < tokens.length; i++) {
            result = result.concat(", ".concat(Byte.toString(tokens[i])));
        }

        result = result.concat("]");
        return result;
    }

    public byte[] getTokens() { return tokens; }
}
