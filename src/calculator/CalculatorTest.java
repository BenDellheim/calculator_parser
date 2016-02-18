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

		
		
		
		
	}
	boolean approxEquals(String expr1, String expr2, boolean compareUnits) {
		return new Value(expr1).approxEquals(new Value(expr2), compareUnits);
	}

	static class Value {
		static float delta = 0.001f;
 
		enum Unit {
			POINT, INCH, SCALAR
		}

		Unit unit;
		// in points if a length
		float value;

		Value(String value) {
			value = value.trim();
			if (value.endsWith("pt")) {
				unit = Unit.POINT;
				this.value = Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else if (value.endsWith("in")) {
				unit = Unit.INCH;
				this.value = 72 * Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else {
				unit = Unit.SCALAR;
				this.value = Float.parseFloat(value);
			}
		}

		boolean approxEquals(Value that, boolean compareUnits) {
			return (this.unit == that.unit || !compareUnits)
					&& Math.abs(this.value - that.value) < delta;
		}
	}

}