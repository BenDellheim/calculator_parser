package calculator;

import calculator.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import calculator.Parser.ValueType;
/**
 * Calculator lexical analyzer.
 */
public class Lexer {

    int i;
    int N;
    final String input;

    /**
     * Token in the stream.
     */
    public static class Token {
        final Type type;
        final String text;

        Token(Type type, String text) {
            this.type = type;
            this.text = text;
        }

        Token(Type type) {
            this(type, null);
        }

        public double getTokenValue() {
            if (type == Type.SCALAR) {
                return (Double.parseDouble(text));
            } else {
                return (Double.parseDouble(text.substring(0, text.length() - 2)));
            }
        }

        public Type getOperatorType() {
            return type;
        }

        public ValueType getTokenType() {
            if (type == Type.SCALAR) {
                return (ValueType.SCALAR);
            } else if (type == Type.INCHES) {
                return (ValueType.INCHES);
            } else {
                return (ValueType.POINTS);
            }
        }
    }

    @SuppressWarnings("serial")
    static class TokenMismatchException extends Exception {
    }

    /*
     * Problem 2: Implement Lexter
     */
    public Lexer(String input) {
        i = 0;
        String inputTemp = input;
        this.input = input.replaceAll("\\s+","");
        N = this.input.length();
    }


    public boolean hasNext() {
        return i < N;
    }

    public static Token makeMultiply() {
        return (new Token(Type.MULTIPLY, "*"));
    }

    public static Token makePoint() {
        return (new Token(Type.POINTS, "1PT"));
    }

    public static Token makeInch() {
        return (new Token(Type.INCHES, "1IN"));
    }

    public boolean isPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?pt?s");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isInch(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?in");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isScalar(String s) {
    	System.out.println("S="+s);
        Pattern p = Pattern.compile("^(\\d+(?:\\.\\d+)?)");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?(pt|pts){1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public Integer getInch(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?in");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public boolean isInchSymbol(String s) {
        Pattern p = Pattern.compile("^in{1}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getInchSymbol(String s) {
        Pattern p = Pattern.compile("^in{1}");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }

    public boolean isPointSymbol(String s) {
    	s = s.replaceAll("\\s+","");
    	Pattern p = Pattern.compile("^pt{1}");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getPointSymbol(String s) {
    	s = s.replaceAll("\\s+","");
    	Pattern p = Pattern.compile("^(pts?)");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }


    public Integer getScalar(String s) {
    	System.out.println("getScalar="+s);
    	Pattern p = Pattern.compile("^(\\d+(?:\\.\\d+)?(pt|pts|in)?)");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }


    public Token next() {
    	if (hasNext()) {
    		System.out.println("Chart = " +input.charAt(i));
    		if (input.charAt(i) == '(') {
                i++;
                System.out.println("a");
                return (new Token(Type.OPENPAREN, "("));
            } else if (input.charAt(i) == ')') {
                i++;
                System.out.println("b");
                return (new Token(Type.CLOSEPAREN, ")"));
            } else if (input.charAt(i) == '+') {
                i++;
                System.out.println("c");
                return (new Token(Type.PLUS, "+"));
            } else if (input.charAt(i) == '-') {
                this.i++;
                System.out.println("d");
                return (new Token(Type.MINUS, "-"));
            } else if (input.charAt(i) == '*') {
                i++;
                System.out.println("e");
                return (new Token(Type.MULTIPLY, "*"));
            } else if (isInch(input.substring(i, N))) {
                int endMatch = getInch(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                System.out.println("f");
                return (new Token(Type.INCHES, input.substring(j, i)));
            } else if (isPoint(input.substring(i, N))) {
                int endMatch = getPoint(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                System.out.println("g");
                System.out.println(input.substring(j, i));
                return (new Token(Type.POINTS, input.substring(j, i)));
            } else if (isScalar(input.substring(i, N))) {
                int endMatch = getScalar(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                System.out.println("h");
                System.out.println(input.substring(j, i));
                return (new Token(Type.SCALAR, input.substring(j, i)));
            } else if (isInchSymbol(input.substring(i, N))) {
                int endMatch = getInchSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                System.out.println("i");
                return (new Token(Type.INCHSYMBOL, input.substring(j, i)));
            } else if (isPointSymbol(input.substring(i, N))) {
                int endMatch = getPointSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.POINTSYMBOL, input.substring(j, i)));
            } else {
                i++;
                System.out.println("Division");
                return (new Token(Type.DIVIDE, "/"));
            }
        } 
    	else {
            System.out.println("l");
            System.out.println(i);
    		return null;
    	}
    }
}