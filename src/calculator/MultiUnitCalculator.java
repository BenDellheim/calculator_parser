package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import calculator.Parser.Value;

/**
 * Multi-unit calculator.
 */
public class MultiUnitCalculator {

	/**
	 * @param expression
	 *            a String representing a multi-unit expression, as defined in
	 *            the problem set
	 * @return the value of the expression, as a number possibly followed by
	 *         units, e.g. "72pt", "3", or "4.882in"
	 */
	public static String evaluate(String expression) {
		Lexer lexer = new Lexer(expression);
		Parser parser = new Parser(lexer);
		Value value = parser.evaluate(expression);
		return(value.toString());
}

	/**
	 * Repeatedly reads expressions from the console, and outputs the results of
	 * evaluating them. Inputting an empty line will terminate the program.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		String input = "";
		
		System.out.println("Rawr, I am a calculator! Enter an expression. Type Q to quit.");
		
		do
		{
			System.out.print(":");
			try{
				input = br.readLine();
				if(input.charAt(0) == 'q' || input.charAt(0) == 'Q') break;
				System.out.println(evaluate(input));
			}catch(StringIndexOutOfBoundsException q)
			{
				System.out.println("You have to type q to quit.");
			}catch(IOException s)
			{
				System.out.println("Whoops! I couldn't read that.");
				break;
			}
		}while(true);
		System.out.println("Thanks, it's been a blast!");
	}
}
