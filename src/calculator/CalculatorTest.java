package calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {

	@Test
	public void test() {
		assertEquals("5.4 in", MultiUnitCalculator.evaluate("(3 + 2.4)in"));
		assertEquals("7.0", MultiUnitCalculator.evaluate("(3 + 4)"));
		assertEquals("18.0",MultiUnitCalculator.evaluate("3+2.2+12.8"));
		assertEquals("5.1",MultiUnitCalculator.evaluate("3+2.1"));
		assertEquals("1.1",MultiUnitCalculator.evaluate("4.4/4"));
		assertEquals("518.4 pt", MultiUnitCalculator.evaluate("(3in * 2.4) pt"));
		assertEquals("5.0 in", MultiUnitCalculator.evaluate("4in + 72pts"));
	}
	
}
	
