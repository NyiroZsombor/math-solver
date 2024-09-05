package main;

import java.util.ArrayList;

import solve.Lexer;

public class Error {
    public static final byte unexpectedToken = 0;

    public Error(byte type, byte[] tokens, int pos, ArrayList<Byte> expected) {
        if (type == unexpectedToken) {
            System.err.println(String.format("Unexpected token %d at position %d. Expected: ", tokens[pos], pos).concat(expected.toString()));

            String ptr = " v";
            for (int i = 0; i < pos; i++) {
                ptr = "   ".concat(ptr);
            }

            System.err.println(ptr);
            System.err.println(Lexer.toString(tokens));
            System.err.println();
        }
    }
}
