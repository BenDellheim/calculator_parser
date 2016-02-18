package calculator;

/*
 * Legitimate tokens for a calculator parser:
 * 
 * 	EOF		(end of input)
 * 	'6', '6.25', ... (any positive number)
 * 	'pt'	(point)
 * 	'in'	(inch)
 * 	'+'		(plus)
 * 	'-'		(minus)
 * 	'*'		(multiply)
 * 	'/'		(divide)
 * 	'('		(open parentheses)
 * 	')'		(close parentheses)
 */

/**
 * Token types:
 * 
 * 	EOF,			// end of input
 * 	NUMBER,			// any positive number
 * 	POINT, 			// unit 'pt'
 *	INCH,			// unit 'in'
 *	PLUS,			// arithmetic operator '+'
 *	MINUS,			// arithmetic operator '-'
 *	MULTIPLY,		// arithmetic operator '*'
 *	DIVIDE,			// arithmetic operator '/'
 *	OPEN_PAREN,		// open parentheses '('
 *	CLOSE_PAREN		// close parentheses ')'
 *
 */
enum Type {
	EOF,
	NUMBER,
	POINT,
	INCH,
	PLUS,
	MINUS,
	MULTIPLY,
	DIVIDE,
	OPEN_PAREN,
	CLOSE_PAREN
}