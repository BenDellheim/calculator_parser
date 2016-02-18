package calculator;

import static org.junit.Assert.*;

import org.junit.Test;


public class LexerTest {

	@Test
	public void testNext() throws Exception {
		String s0 = "3.14234";
		Lexer lex0 = new Lexer(s0);
		assertEquals( new Lexer.Token(Type.SCALAR, "3.14234").toString(), lex0.next().toString() );
		
		String s1 = "in";
		Lexer lex1 = new Lexer(s1);
		assertEquals( new Lexer.Token(Type.INCHES, "in").toString(), lex1.next().toString() );
		
		String s2 = "4.5354 pts 3";
		Lexer lex2 = new Lexer(s2);
		assertEquals( new Lexer.Token(Type.SCALAR, "4.5354").toString(),lex2.next().toString() );
		assertEquals( new Lexer.Token(Type.POINTS, "pts").toString(),lex2.next().toString() );
		assertEquals( new Lexer.Token(Type.SCALAR, "3").toString(),lex2.next().toString() );
		
		String s3 = "5 / 3pts";
		Lexer lex3 = new Lexer(s3);
		assertEquals( new Lexer.Token(Type.SCALAR, "5").toString(), lex3.next().toString() );
		assertEquals( new Lexer.Token(Type.DIVIDE, "/").toString(), lex3.next().toString() );
		assertEquals( new Lexer.Token(Type.SCALAR, "3").toString(), lex3.next().toString() );
		assertEquals( new Lexer.Token(Type.SCALAR, "pts").toString(), lex3.next().toString() );
		
		String s4 = "((5+3pts)/4in)";
		Lexer lex4 = new Lexer(s4);
		assertEquals( new Lexer.Token(Type.OPENPAREN, "(").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.OPENPAREN, "(").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.SCALAR, "5").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.PLUS, "+").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.SCALAR, "3").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.POINTS, "pts").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.CLOSEPAREN, ")").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.DIVIDE, "/").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.SCALAR, "4").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.INCHES, "in").toString(), lex4.next().toString());
		assertEquals( new Lexer.Token(Type.CLOSEPAREN, ")").toString(), lex4.next().toString());
		
	}


}