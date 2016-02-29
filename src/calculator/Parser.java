package calculator;


import calculator.Lexer.Token;

/* Your parser should use the tokenization produced by
 * the Lexer to parse the expression according to the
 * grammar defined in Problem 1. Exactly how you do
 * this is up to you; the only thing that we enforce is
 * that the Lexer is passed as an argument to the Parser constructor.
 * b. Write test cases for these methods in a separate file.
 * c. Implement the Parser and make sure all your tests pass.
 */

/*
 * Expression ::= number | number unit | operand
 * Number ::= scalar | unit
 * Scalar ::= whole | decimal
 * Operand ::= + | - | * | /
 * Unit ::= inches | points
 */


/* Class Parser - our Calculator parser. All values are measured in Points.
 * evaluate(String) - Main public function that calls all the parser routines.
 *                    It returns a Value, so be sure to import Parser.Value. 
 * 
 * Subclasses: Value, Expression
 */
class Parser {

    Lexer lexer;
    private static final double PT_PER_IN = 72;

	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
	}

	/* Constructor.
	 * Parser instances start with a Lexer, which tokenizes the input stream for us.
	 * Marvelous! How convenient!
	 */
    Parser (Lexer lex) {
        this.lexer = lex;
    }

	/* Class Expression covers 2 kinds of expressions:
	 * One with a single parameter (leaf) with no operator, and
	 * one with 3 parameters (operator, leftExpression, rightExpression)
	 *  i.e. (PLUS, ex1, ex2) -> ex1 PLUS ex2.
	 */
    public class Expression {

        Token operator;
        Token leaf;
        Expression leftExpression;
        Expression rightExpression;

        public Expression(Token leaf) {
            this.leaf = leaf;
        }

        public Expression(Token operator, Expression leftExpression, Expression rightExpression) {
        	this.leaf = null;
            this.operator = operator;
            this.leftExpression = leftExpression;
            this.rightExpression = rightExpression;
        }

        public Boolean isLeaf() {
            return this.leaf != null;
        }
    }

    /* Class Value pairs a (double) with the enum ValueType (INCHES, POINTS, SCALAR)
     * and overrides toString() to display it properly.
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
				// Internal value is always in points.
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}

	/* Parse() - not to be confused with the constructor
	 * This takes a tokenized Lexer and converts it to an Expression.
	 * Expressions can contain smaller Expressions, or a singular value (leaf).
	 * */
    public Expression Parse(Lexer lexer) {
        /* Tokens contain a Type, and either a String of text or (null).
         * They can return their value (Double), their Type (i.e MULTIPLY), or their ValueType (i.e SCALAR).*/
        Token token;
        Expression expression;

        /*******************************************************
         * Okay, next up is the clusterfuck I'm trying to fix.
         * (I fixed some of it.)
         * Parse() is supposed to recursively create an Expression,
         * by iterating through the passed Lexer object with Lexer.next().
         * (BTW: next() checks if the string is used up and returns null,
         *  so this code needs to check for a null return.)
         * 
         * The trick is that it's recursive, so returning a scalar
         * WOULD work halfway through (i.e. nothing further to evaluate),
         * but a scalar up front means it returns without checking anything.
         * 
         * I'm thinking of counting the number of recursions so it
         *  keeps reading the string if it's at 0.
         *  
         * After that, not really sure if that Paren code works correctly.
         * So there's that.
         *******************************************************/

        token = lexer.next();
        if (token.isNumber()) {
            expression = new Expression(token);
//            return expression; // This return makes Parse stop at the first value.
        }
       // token is an operator
       Token operator;
       Expression leftExpression;
       Expression rightExpression;
       leftExpression = Parse(lexer);

//       if (lexer.hasNext()) {    // Not necessary since next() checks this
           operator = lexer.next();
           if (operator.type == Type.OPENPAREN) {
        	   return leftExpression;
           }
           if (operator.type == Type.CLOSEPAREN) {
               return leftExpression;
           } else if (operator.isUnitSymbol()) {
               Expression rightLeftExpression;
               if (operator.type == Type.INCHSYMBOL) {
                   rightLeftExpression = new Expression(Lexer.makeInch());
               } else {
                   rightLeftExpression = new Expression(Lexer.makePoint());
               }
               Expression UnitConversion = new Expression(Lexer.makeMultiply(), rightLeftExpression, leftExpression);
               leftExpression = UnitConversion;
           }
        //   if (lexer.hasNext()) {
               operator = lexer.next();
                        
               // Recurse to finish evaluating token stream (lexer)
               rightExpression = Parse(lexer);

        //       if (lexer.hasNext()) {
                   Token rightOperator = lexer.next();
                   if (rightOperator.type == Type.CLOSEPAREN) {
                       return new Expression(operator, leftExpression, rightExpression);
                   } else if (rightOperator.isUnitSymbol()) {
                       Expression rightLeftExpression;
                       if (rightOperator.type == Type.INCHSYMBOL) {
                           rightLeftExpression = new Expression(Lexer.makeInch());
                       } else {
                           rightLeftExpression = new Expression(Lexer.makePoint());
                       }
                       Expression UnitConversion = new Expression(Lexer.makeMultiply(), rightLeftExpression, rightExpression);
                       rightExpression = UnitConversion;
                   }
//               } else {
//                   return leftExpression;
//               }
//           } else {
//               return leftExpression;
//           }
//       }
        // In case there's nothing to parse
//        return null;

          // Alternative test output so JUnit doesn't throw a Null Pointer Exception when I get here         
          return new Expression(new Token(Type.SCALAR, "0"));
    }

    /* Applies the operator *+-/ to two Values
     * Any other operator is ignored and a null value is returned
     * (TODO: Encapsulate in error handling in case it tries to add null to something.
     * In the meantime, please don't break it. >_>)
     */
    public Value apply(Token operator, Value leftOperand, Value rightOperand) {
    	 System.out.println("In apply()");
        Type operatorType = operator.getOperatorType();
        ValueType leftType = leftOperand.type;
        ValueType rightType = rightOperand.type;
        Double leftValue = leftOperand.value;
        Double rightValue = rightOperand.value;

        switch (operatorType) {
        
            case PLUS:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue + rightValue, rightType));
                }
                else {
                    return(new Value(leftValue + rightValue, leftType));
                }
            case MINUS:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue - rightValue, rightType));
                }
                else {
                    return(new Value(leftValue - rightValue, leftType));
                }
            case MULTIPLY:
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue*rightValue, rightType));
                }
                else if (rightType == ValueType.SCALAR){
                    return(new Value(leftValue*rightValue, leftType));
                }
                else if (leftType == rightType) {
                    return(new Value(leftValue*rightValue, leftType));
                }
                else if (leftType == ValueType.INCHES & rightType == ValueType.POINTS) {
                    return(new Value(leftValue*rightValue, ValueType.SCALAR));
                }
                else {
                    return(new Value(leftValue*rightValue, ValueType.SCALAR));
                }
            case DIVIDE:
            	if (rightValue == 0) return null;
                if (leftType == ValueType.SCALAR) {
                    return(new Value(leftValue/rightValue, rightType));
                }
                else if (rightType == ValueType.SCALAR){
                    return(new Value(leftValue/rightValue, leftType));
                }
                else {
                    return(new Value(leftValue/rightValue, ValueType.SCALAR));
                }
            default:
            	return null;
        }
    }

    // Main function called for a Parser instance
	public Value evaluate(String expression) {
        Lexer lexer = new Lexer(expression);
        Expression parsed = Parse(lexer); // Parses the token stream (lexer) -- NOT a constructor call!
        return evaluateIter(parsed);
	}

	// Evaluates the expression recursively and returns a Value
    public Value evaluateIter(Expression parsed) {
    	if (parsed.isLeaf()) {
    		System.out.println("In evaluateIter()");
            Token token = parsed.leaf;
            return(new Value(token.getTokenValue(), token.getTokenType()));
        }
        else {
            Token operator = parsed.operator;
            Expression leftExpression = parsed.leftExpression;
            Expression rightExpression = parsed.rightExpression;
            return apply(operator, evaluateIter(leftExpression), evaluateIter(rightExpression));
            // Note that apply() can return null if (operator) is not +-*/
            }
        }
    }