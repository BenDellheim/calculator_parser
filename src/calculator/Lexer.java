package calculator;

import calculator.Type;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        	return (Double.parseDouble(text));
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

        // Brevity functions to make Parser.java easier to read
        public Boolean isNumber() {
            if (type == Type.SCALAR | type == Type.POINTS | type == Type.INCHES)
            	return true;
            return false;
        }
        
        public Boolean isOperator() {
        	if (type == Type.PLUS | type == Type.MINUS | type == Type.MULTIPLY | type == Type.DIVIDE)
        		return true;
        	return false;
        }
        
        public Boolean isUnitSymbol() {
        	if (type == Type.POINTSYMBOL | type == Type.INCHSYMBOL)
        		return true;
        	return false;
        }

    }

    @SuppressWarnings("serial")
    static class TokenMismatchException extends Exception {
    }

    /*
     * Problem 2: Implement Lexer
     */
    public Lexer(String input) {
        i = 0;
        this.input = input.replaceAll("\\s+",""); // strip whitespaces for examples like 3 + 2.4
        N = this.input.length();
    }


    public boolean hasNext() {
        return i < N;
    }

    public static Token makeMultiply() {
        return (new Token(Type.MULTIPLY, "*"));
    }

    public static Token makePoint() {
        return (new Token(Type.POINTS, "1"));
    }

    public static Token makeInch() {
        return (new Token(Type.INCHES, "1"));
    }

    public boolean isPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?pts?");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isInch(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?in");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public boolean isScalar(String s) {
        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?");
        Matcher m = p.matcher(s);
        return (m.find());
    }

    public Integer getPoint(String s) {
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?(pts|pt){1}");
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
    	Pattern p = Pattern.compile("^(\\d+(?:\\.\\d+)?(pt|pts|in)?)");
        Matcher m = p.matcher(s);
        m.find();
        m.group();
        return (m.end());
    }


    public Token next() {
    	if (hasNext()) {
    		if (input.charAt(i) == '(') {
                i++;
                return (new Token(Type.OPENPAREN, "("));
            } else if (input.charAt(i) == ')') {
                i++;
                return (new Token(Type.CLOSEPAREN, ")"));
            } else if (input.charAt(i) == '+') {
                i++;
                return (new Token(Type.PLUS, "+"));
            } else if (input.charAt(i) == '-') {
                i++;
                return (new Token(Type.MINUS, "-"));
            } else if (input.charAt(i) == '*') {
                i++;
                return (new Token(Type.MULTIPLY, "*"));
            } else if (isInch(input.substring(i, N))) {
                int endMatch = getInch(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.INCHES, input.substring(j, i).replaceAll("[^0-9.]", "")));
            } else if (isPoint(input.substring(i, N))) {
                int endMatch = getPoint(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.POINTS, input.substring(j, i).replaceAll("[^0-9.]", "")));
            } else if (isScalar(input.substring(i, N))) {
                int endMatch = getScalar(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.SCALAR, input.substring(j, i).replaceAll("[^0-9.]", "")));
            } else if (isInchSymbol(input.substring(i, N))) {
                int endMatch = getInchSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.INCHSYMBOL, input.substring(j, i)));
            } else if (isPointSymbol(input.substring(i, N))) {
                int endMatch = getPointSymbol(input.substring(i, N));
                int j = i;
                i = i + endMatch;
                return (new Token(Type.POINTSYMBOL, input.substring(j, i)));
            } else {
                i++;
                return (new Token(Type.DIVIDE, "/"));
            }
        } 
    	else {
    		return null;
    	}
    }
}