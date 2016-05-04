package calculator;

import static org.junit.Assert.*;

import org.junit.Test;


public class LexerTest {

	@Test
	public void testNext() throws Exception {
		String s0 = "3.14234";
		Lexer lex0 = new Lexer(s0);
		assertEquals("3.14234", lex0.next().text);
		assertNull(lex0.next());
		assertFalse(lex0.hasNext());
		String s1 = "in";
		Lexer lex1 = new Lexer(s1);
		assertEquals( "in", lex1.next().text );
		String s2 = "4.5354pts";
		Lexer lex2 = new Lexer(s2);
		assertEquals( new Lexer.Token(Type.POINTS, "4.5354").text,lex2.next().text );
		String s3 = "5/3pt";
		Lexer lex3 = new Lexer(s3);
		assertEquals( new Lexer.Token(Type.SCALAR, "5").text, lex3.next().text );
		assertEquals( new Lexer.Token(Type.DIVIDE, "/").text, lex3.next().text );
		assertEquals( new Lexer.Token(Type.POINTS, "3").text, lex3.next().text );
		
		String s4 = "12 pt((5+3pts)/4 in)";
		Lexer lex4 = new Lexer(s4);
		assertEquals( new Lexer.Token(Type.POINTS, "12").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.OPENPAREN, "(").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.OPENPAREN, "(").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.SCALAR, "5").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.PLUS, "+").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.POINTS, "3").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.DIVIDE, "/").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.CLOSEPAREN, ")").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.DIVIDE, "/").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.INCHES, "4").text, lex4.next().text);
		assertEquals( new Lexer.Token(Type.CLOSEPAREN, ")").text, lex4.next().text);
		
	}


}