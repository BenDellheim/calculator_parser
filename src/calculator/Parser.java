package calculator;


import calculator.Lexer.Token;

/* Requirements:
 * "Your parser should use the tokenization produced by
 * the Lexer to parse the expression according to the
 * grammar defined in Problem 1. Exactly how you do
 * this is up to you; the only thing that we enforce is
 * that the Lexer is passed as an argument to the Parser constructor.
 * b. Write test cases for these methods in a separate file.
 * c. Implement the Parser and make sure all your tests pass."
 */

/* Valid inputs:
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

    Lexer lexer; // Takes the input string and creates tokens through lexer.next().
    int stack;   // Counts how many parentheses/recurses we're inside. Default 0. (255 parentheses should be enough for everybody.)
    private static final double PT_PER_IN = 72;

	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {	}

	/* Constructor.
	 * Parser instances start with a Lexer, which tokenizes the input stream for us.
	 * Marvelous! How convenient!
	 */
    Parser (Lexer lex)
    {
        this.lexer = lex;
        stack = 0;

    }

	/* Class Expression covers 2 kinds of expressions:
	 * One with a single parameter (leaf) with no operator, and
	 * one with 3 parameters (operator, leftExpression, rightExpression)
	 *  i.e. (PLUS, ex1, ex2) -> ex1 PLUS ex2.
	 */
    public class Expression {

        Token leaf;

        Expression leftExpression;
        Token operator;
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
				return value + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
	}

	/* Parse() - not to be confused with the constructor
	 * This iterates through the Lexer object using Lexer.next()
	 * and recursively converts it to an Expression.
	 * Expressions can contain smaller Expressions, or a singular value (leaf).
     * (BTW: next() checks if the string is used up and returns null,
     *  so this code needs to check for a null return.)
	 * */
    public Expression Parse(Lexer lexer) {

        /* Tokens contain a Type, and either a String of text or (null).
         * They can return their value (Double), their Type (i.e MULTIPLY),
         *  or their ValueType (i.e SCALAR).*/

        Expression leftExpression = null;
        Expression tempExpression = null;
        Token operator = null;
        Token token = lexer.next();
        
        // STEP 1 (Priming the loop): Read a # or LPAREN
        if( token != null && token.isNumber())
        {
        	// Make the # a new leaf expression.
        	leftExpression = new Expression(token);

        	// If we're inside () [stack > 0] it's safe to return the leaf
           	//  without exiting the rest of the parse tree.
           	if( stack > 0)
           	{
           		stack--;
           		return leftExpression;
           	}
           	token = lexer.next();
   			// Otherwise continue: Since this is the prime,
   			// the Number is stored directly in leftExpression.
        }
        else if( token != null && token.getOperatorType() == Type.OPENPAREN)
        {
        	// Recurse to evaluate inside of "("
        	stack++;
        	leftExpression = Parse(lexer);
        	token = lexer.next();
        }

        // Main loop
        while(token != null)
        {
        	// STEP 2: Read operator (+-*/) or RPAREN
        	// If operator, add it to the current expression and self-call to further evaluate
        	// If ")", return the current expression if inside a "(" [stack > 0]
        	if( token.isOperator())
        	{
        		operator = token;
        		stack++;
        		if( leftExpression != null){
        			leftExpression = new Expression( operator, leftExpression, Parse(lexer));
        		}
        		else
        			leftExpression = Parse(lexer);
        		token = lexer.next();
        	}
        	else if( token.getOperatorType() == Type.CLOSEPAREN)
        	{
        		// Return result if inside a "("
        		if(stack > 0)
        		{
            		stack--;
            		return leftExpression;
        		}
        		operator = null;
        		token = lexer.next();
        	}

        	if(token == null) return leftExpression;
            
            // STEP 1: Read # or LPAREN
        	// If #, make a new leaf expression for it.
        	// If "(", increase the stack, add the ( to the expression, and self-call.
            if( token.isNumber() || token.isUnitSymbol())
            {
            	tempExpression = new Expression(token);
               	// If we're inside () [stack > 0] it's safe to return the leaf
               	//  without exiting the rest of the parse tree.
               	if( stack > 0)
               	{
               		stack--;
               		return tempExpression;
               	}

       			// Otherwise, add the number to the expression and continue.
       			if( operator != null)
            	{
            		if(leftExpression != null)
            		{
            			leftExpression = new Expression(operator, leftExpression, tempExpression);
            		}
            		else
            			leftExpression = tempExpression; // This SHOULDN'T happen... but just in case
                	operator = null;
            	}
       			else if( token.isUnitSymbol())
       			{
       				/* Usually the unit symbol is absorbed into the number, but
       				 * not if it's right after an R-paren. In this case, we want to
       				 * use makeInch() or makePoint() to make "1 in" or "1 pt",
       				 * then multiply it by the current expression to change its unit.
       				 * --The actual unit conversion is done later in apply()--
       				 */
       				if(token.getOperatorType() == Type.INCHSYMBOL)  token = Lexer.makeInch();
       				if(token.getOperatorType() == Type.POINTSYMBOL) token = Lexer.makePoint();
       				if(leftExpression != null)
       				{
       					leftExpression = new Expression(Lexer.makeMultiply(), leftExpression, new Expression(token));
       				}
       				else
       					leftExpression = new Expression(token);
       			}
       			token = lexer.next();
            }
            else if(token.getOperatorType() == Type.OPENPAREN)
            {
            	stack++;
            	leftExpression = new Expression(token, leftExpression, Parse(lexer));
                token = lexer.next();
            }
        };
        // For when the loop ends (i.e. last token has been parsed)
        return leftExpression;
    }

    /* Applies the operator *+-/ to two Values
     * Any other operator is ignored and a null value is returned
     */
    public Value apply(Token operator, Value leftOperand, Value rightOperand) {
        Type operatorType = operator.getOperatorType();
        ValueType leftType = leftOperand.type;
        ValueType rightType = rightOperand.type;
        Double leftValue = leftOperand.value;
        Double rightValue = rightOperand.value;

        switch (operatorType) {
        
            case PLUS:
                if (leftType == ValueType.SCALAR)
                {	// Take the other value's type
                    return(new Value(leftValue + rightValue, rightType));
                }
                else if (leftType == ValueType.INCHES)
                {	// Convert the right value to inches if necessary
                	if(rightType == ValueType.POINTS)
                	{
                		return(new Value(leftValue + rightValue/PT_PER_IN, leftType));
                	}
                	else
                		return(new Value(leftValue + rightValue, leftType));
                }
                else if (leftType == ValueType.POINTS)
                {	// Convert the right value to points if necessary
                	if(rightType == ValueType.INCHES)
                	{
                		return(new Value(leftValue + rightValue*PT_PER_IN, leftType));
                	}
                	else
                		return(new Value(leftValue + rightValue, leftType));
                }
            case MINUS:
                if (leftType == ValueType.SCALAR)
                {	// Take the other value's type
                    return(new Value(leftValue - rightValue, rightType));
                }
                else if (leftType == ValueType.INCHES)
                {	// Convert the right value to inches if necessary
                	if(rightType == ValueType.POINTS)
                	{
                		return(new Value(leftValue - rightValue/PT_PER_IN, leftType));
                	}
                	else
                		return(new Value(leftValue - rightValue, leftType));
                }
                else if (leftType == ValueType.POINTS)
                {	// Convert the right value to points if necessary
                	if(rightType == ValueType.INCHES)
                	{
                		return(new Value(leftValue - rightValue*PT_PER_IN, leftType));
                	}
                	else
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
                else if (leftType == ValueType.INCHES && rightType == ValueType.POINTS ||
                		 leftType == ValueType.POINTS && rightType == ValueType.INCHES)
                {
                    return(new Value(leftValue*rightValue*PT_PER_IN, ValueType.POINTS));
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
            Token token = parsed.leaf;
            return(new Value(token.getTokenValue(), token.getTokenType()));
        }
        else {
            Token operator = parsed.operator;
            Expression leftExpression = parsed.leftExpression;
            Expression rightExpression = parsed.rightExpression;
            try{
            	// apply() can return null if (operator) is not +-*/
            	return apply(operator, evaluateIter(leftExpression), evaluateIter(rightExpression));
            }catch(NullPointerException e)
            {
            	System.out.println("Invalid operator.");
            	return new Value(0, ValueType.SCALAR);
            }
        }
    }
    }