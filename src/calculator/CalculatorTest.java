package calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {

	@Test
	public void test() {
		MultiUnitCalculator calculator;
		calculator = new MultiUnitCalculator();
		assertEquals("5.1",calculator.evaluate("3+2.1"));
		assertEquals("5.2",calculator.evaluate("3+2.2"));
		assertEquals("-1.4",calculator.evaluate("1-2.4"));
		assertEquals("5.4", calculator.evaluate("3+2.4"));
		assertEquals("5.4", calculator.evaluate("3 + 2.4"));
		assertEquals("16.8", calculator.evaluate("(3 + 4)*2.4"));
		assertEquals("5.4 in", calculator.evaluate("3 + 2.4 in"));
		
		
		
	}
	

}