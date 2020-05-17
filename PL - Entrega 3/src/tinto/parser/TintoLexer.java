//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2017, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//           Departamento de Tecnolog�as de la Informaci�n          //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto.parser;

import java.io.*;

/**
 * Clase que desarrolla el analizador l�xico de Tinto
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class TintoLexer extends Lexer implements TokenConstants {

	/**
	 * Transiciones del aut�mata del analizador l�xico
	 * 
	 * @param state Estado inicial
	 * @param symbol S�mbolo del alfabeto
	 * @return Estado final
	 */
	protected int transition(int state, char symbol) 
	{
		switch(state) 
		{
			case 0: 
				if(symbol == '/') return 1;
				else if(symbol == ' ' || symbol == '\t') return 7;
				else if(symbol == '\r' || symbol == '\n') return 7;
				else if(symbol >= 'a' && symbol <= 'z') return 8;
				else if(symbol >= 'A' && symbol <= 'Z') return 8;
				else if(symbol == '_') return 8;
				else if(symbol >= '1' && symbol <= '9') return 9;
				else if(symbol == '0') return 10;
				else if(symbol == '\'') return 16;
				else if(symbol == '(') return 22;
				else if(symbol == ')') return 23;
				else if(symbol == '{') return 24;
				else if(symbol == '}') return 25;
				else if(symbol == ';') return 26;
				else if(symbol == ',') return 27;
				else if(symbol == '.') return 28;
				else if(symbol == '=') return 29;
				else if(symbol == '<') return 31;
				else if(symbol == '>') return 33;
				else if(symbol == '!') return 35;
				else if(symbol == '|') return 37;
				else if(symbol == '&') return 39;
				else if(symbol == '+') return 41;
				else if(symbol == '-') return 42;
				else if(symbol == '*') return 43;
				else if(symbol == '%') return 44;
				else if(symbol == ':') return 45;
				else return -1;
			case 1:
				if(symbol == '*') return 2;
				else if(symbol == '/') return 5;
				else return -1;
			case 2:
				if(symbol == '*') return 3;
				else return 2;
			case 3:
				if(symbol == '*') return 3;
				else if(symbol == '/') return 4;
				else return 2;
			case 5:
				if(symbol == '\n') return 6;
				else return 5;
			case 7:
				if(symbol == ' ' || symbol == '\t') return 7;
				else if(symbol == '\r' || symbol == '\n') return 7;
				else return -1;
			case 8:
				if(symbol >= 'a' && symbol <= 'z') return 8;
				else if(symbol >= 'A' && symbol <= 'Z') return 8;
				else if(symbol >= '0' && symbol <= '9') return 8;
				else if(symbol == '_') return 8;
				else return -1;
			case 9:
				if(symbol >= '0' && symbol <= '9') return 9;
				else return -1;
			case 10:
				if(symbol >= '0' && symbol <= '7') return 11;
				else if(symbol == 'x' || symbol == 'X') return 12;
				else if(symbol == 'b' || symbol == 'B') return 14;
				else return -1;
			case 11:
				if(symbol >= '0' && symbol <= '7') return 11;
				else return -1;
			case 12:
				if(symbol >= '0' && symbol <= '9') return 13;
				else if(symbol >= 'a' && symbol <= 'f') return 13;
				else if(symbol >= 'A' && symbol <= 'F') return 13;
				else return -1;
			case 13:
				if(symbol >= '0' && symbol <= '9') return 13;
				else if(symbol >= 'a' && symbol <= 'f') return 13;
				else if(symbol >= 'A' && symbol <= 'F') return 13;
				else return -1;
			case 14:
				if(symbol == '0' || symbol == '1') return 15;
				return -1;
			case 15:
				if(symbol == '0' || symbol == '1') return 15;
				return -1;				
			case 16:
				if(symbol == '\\') return 19;
				else if(symbol != '\'' && symbol != '\n' && symbol != '\r') return 17;
				else return -1;
			case 17:
				if(symbol == '\'') return 18;
				else return -1;
			case 19:
				if(symbol == 'n' || symbol == 't' || symbol == 'b') return 17;
				else if(symbol == 'r' || symbol == 'f' || symbol == '\\') return 17;
				else if(symbol == '\'' || symbol == '\"') return 17;
				else if(symbol >= '0' && symbol <= '3') return 20;
				else if(symbol >= '4' && symbol <= '7') return 21;
				else return -1;
			case 20:
				if(symbol >= '0' && symbol <= '7') return 21;
				else return -1;
			case 21:
				if(symbol >= '0' && symbol <= '7') return 17;
				else return -1;
			case 29:
				if(symbol == '=') return 30;
				else return -1;
			case 31:
				if(symbol == '=') return 32;
				else return -1;
			case 33:
				if(symbol == '=') return 34;
				else return -1;
			case 35:
				if(symbol == '=') return 36;
				else return -1;
			case 37:
				if(symbol == '|') return 38;
				else return -1;
			case 39:
				if(symbol == '&') return 40;
				else return -1;
			default:
				return -1;
		}
	}
	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected boolean isFinal(int state) 
	{
		if(state <=0 || state > 45) return false;
		switch(state) 
		{
			case 2:
			case 3:
			case 5:
			case 12:
			case 14:
			case 16:
			case 17:
			case 19:
			case 20:
			case 21:
			case 37:
			case 39:
				return false;
			default: 
				return true;
		}
	}
	
	/**
	 * Genera el componente l�xico correspondiente al estado final y
	 * al lexema encontrado. Devuelve null si la acci�n asociada al
	 * estado final es omitir (SKIP).
	 * 
	 * @param state Estado final alcanzado
	 * @param lexeme Lexema reconocido
	 * @param row Fila de comienzo del lexema
	 * @param column Columna de comienzo del lexema
	 * @return Componente l�xico correspondiente al estado final y al lexema
	 */
	protected Token getToken(int state, String lexeme, int row, int column) 
	{
		switch(state) 
		{
			case 1: return new Token(DIV, lexeme, row, column);
			case 4: return null;
			case 6: return null;
			case 7: return null;
			case 8: return new Token(getKind(lexeme),lexeme, row, column);
			case 9: return new Token(INTEGER_LITERAL, lexeme, row, column);
			case 10: return new Token(INTEGER_LITERAL, lexeme, row, column);
			case 11: return new Token(INTEGER_LITERAL, lexeme, row, column);
			case 13: return new Token(INTEGER_LITERAL, lexeme, row, column);
			case 15: return new Token(INTEGER_LITERAL, lexeme, row, column);
			case 18: return new Token(CHAR_LITERAL, lexeme, row, column);
			case 22: return new Token(LPAREN, lexeme, row, column);
			case 23: return new Token(RPAREN, lexeme, row, column);
			case 24: return new Token(LBRACE, lexeme, row, column);
			case 25: return new Token(RBRACE, lexeme, row, column);
			case 26: return new Token(SEMICOLON, lexeme, row, column);
			case 27: return new Token(COMMA, lexeme, row, column);
			case 28: return new Token(DOT, lexeme, row, column);
			case 29: return new Token(ASSIGN, lexeme, row, column);
			case 30: return new Token(EQ, lexeme, row, column);
			case 31: return new Token(LT, lexeme, row, column);
			case 32: return new Token(LE, lexeme, row, column);
			case 33: return new Token(GT, lexeme, row, column);
			case 34: return new Token(GE, lexeme, row, column);
			case 35: return new Token(NOT, lexeme, row, column);
			case 36: return new Token(NE, lexeme, row, column);
			case 38: return new Token(OR, lexeme, row, column);
			case 40: return new Token(AND, lexeme, row, column);
			case 41: return new Token(PLUS, lexeme, row, column);
			case 42: return new Token(MINUS, lexeme, row, column);
			case 43: return new Token(PROD, lexeme, row, column);
			case 44: return new Token(MOD, lexeme, row, column);
			case 45: return new Token(COLON, lexeme, row, column);
			default: return null;
		}
	}
	
	/**
	 * Estudia si un identificador corresponde a una palabra clave del lenguaje
	 * y devuelve el c�digo del token adecuado
	 * @param id
	 * @return
	 */
	private int getKind(String lexeme) 
	{
		if(lexeme.equals("boolean")) return BOOLEAN;
		else if(lexeme.equals("char")) return CHAR;
		else if(lexeme.equals("else")) return ELSE;
		else if(lexeme.equals("false")) return FALSE;
		else if(lexeme.equals("if")) return IF;
		else if(lexeme.equals("import")) return IMPORT;
		else if(lexeme.equals("int")) return INT;
		else if(lexeme.equals("library")) return LIBRARY;
		else if(lexeme.equals("native")) return NATIVE;
		else if(lexeme.equals("private")) return PRIVATE;
		else if(lexeme.equals("public")) return PUBLIC;
		else if(lexeme.equals("return")) return RETURN;
		else if(lexeme.equals("true")) return TRUE;
		else if(lexeme.equals("void")) return VOID;
		else if(lexeme.equals("while")) return WHILE;
		// Modificaciones
		else if(lexeme.equals("for")) return FOR;
		else if(lexeme.equals("do")) return DO;
		else if(lexeme.equals("switch")) return SWITCH;
		else if(lexeme.equals("case")) return CASE;
		else if(lexeme.equals("default")) return DEFAULT;
		else if(lexeme.equals("break")) return BREAK;
		else if(lexeme.equals("continue")) return CONTINUE;
		else return IDENTIFIER;
	}
	
	/**
	 * Constructor
	 * @param filename Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public TintoLexer(File file) throws IOException 
	{
		super(file);
	}
	
}
