package calculator;

import calculator.Lexer;

/*
 * Grammar for the calculator:
 * 
 * 	Expression ::= (Primitive | Combination) EOF
 * 	Primitive ::= Number (Unit | )
 * 	Number ::= [0-9]+((.[0-9]+) | )
 * 	Unit ::= 'in' | 'pt'
 * 	Combination ::= Arithmetic
 * 				  | Unit_Conversion
 * 				  | '(' Expression ')'
 * 	Arithmetic ::= Expression Operator Expression
 * 	Operator ::= '+' | '-' | '*' | '/'
 * 	Unit_Conversion ::= '(' Expression ')' Unit	
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {
	
	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
	}

	/**
	 * Type of values.
	 */
	private enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}

	private static final double PT_PER_IN = 72;
	private final Lexer lexer = null;

	// TODO write method spec
	Parser(Lexer lexer) {
		// TODO implement for Problem 3
	}

	// TODO write method spec
	public Value evaluate() {
		// TODO implement for Problem 3
		return null;
	}
}
