//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2017, Francisco José Moreno Velo                   //
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
//           Departamento de Tecnologías de la Información          //
//   Área de Ciencias de la Computación e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto.parser;

/**
 * Interfaz que define los códigos de las diferentes categorías léxicas
 *  
 * * @author Francisco José Moreno Velo
 *
 */
public interface TokenConstants {

	/**
	 * Final de fichero
	 */
	public int EOF = 0;
	
	//--------------------------------------------------------------//
	// Palabras clave
	//--------------------------------------------------------------//
	
	/**
	 * Palabra clave "boolean"
	 */
	public int BOOLEAN = 1;
	
	/**
	 * Palabra clave "char"
	 */
	public int CHAR = 2;
	
	/**
	 * Palabra clave "else"
	 */
	public int ELSE = 3;
	
	/**
	 * Palabra clave "false"
	 */
	public int FALSE = 4;
	
	/**
	 * Palabra clave "if"
	 */
	public int IF = 5;
	
	/**
	 * Palabra clave "import"
	 */
	public int IMPORT = 6;
	
	/**
	 * Palabra clave "int"
	 */
	public int INT = 7;
	
	/**
	 * Palabra clave "library"
	 */
	public int LIBRARY = 8;
	
	/**
	 * Palabra clave "native"
	 */
	public int NATIVE = 9;
	
	/**
	 * Palabra clave "private"
	 */
	public int PRIVATE = 10;
	
	/**
	 * Palabra clave "public"
	 */
	public int PUBLIC = 11;
	
	/**
	 * Palabra clave "return"
	 */
	public int RETURN = 12;
	
	/**
	 * Palabra clave "true"
	 */
	public int TRUE = 13;
	
	/**
	 * Palabra clave "void"
	 */
	public int VOID = 14;
	
	/**
	 * Palabra clave "while"
	 */
	public int WHILE = 15;
	
	//--------------------------------------------------------------//
	// Identificadores y literales
	//--------------------------------------------------------------//

	/**
	 * Identificador
	 */
	public int IDENTIFIER = 16;

	/**
	 * Literal de tipo int
	 */
	public int INTEGER_LITERAL = 17;
	
	/**
	 * Literal de tipo char
	 */
	public int CHAR_LITERAL = 18;
	
	//--------------------------------------------------------------//
	// Separadores
	//--------------------------------------------------------------//
	
	/**
	 * Paréntesis abierto "("
	 */
	public int LPAREN = 19;
	
	/**
	 * Paréntesis cerrado ")"
	 */
	public int RPAREN = 20;
	
	/**
	 * Llave abierta "{"
	 */
	public int LBRACE = 21;
	
	/**
	 * Llave cerrada "}"
	 */
	public int RBRACE = 22;
	
	/**
	 * Punto y coma ";"
	 */
	public int SEMICOLON = 23;
	
	/**
	 * Coma ","
	 */
	public int COMMA = 24;
	
	/**
	 * Punto "."
	 */
	public int DOT = 25;
	
	//--------------------------------------------------------------//
	// Operadores
	//--------------------------------------------------------------//

	/**
	 * Asignación "="
	 */
	public int ASSIGN = 26;

	/**
	 * Igualdad "=="
	 */
	public int EQ = 27;
	
	/**
	 * Menor "<"
	 */
	public int LT = 28;
	
	/**
	 * Menor o igual "<="
	 */
	public int LE = 29;
	
	/**
	 * Mayor ">"
	 */
	public int GT = 30;
	
	/**
	 * Mayor o igual ">="
	 */
	public int GE = 31;
	
	/**
	 * Distinto "!="
	 */
	public int NE = 32;
	
	/**
	 * Disyunción "||"
	 */
	public int OR = 33;
	
	/**
	 * Conjunción "&&"
	 */
	public int AND = 34;
	
	/**
	 * Negación "!"
	 */
	public int NOT = 35;
	
	/**
	 * Suma "+"
	 */
	public int PLUS = 36;
	
	/**
	 * Resta "-"
	 */
	public int MINUS = 37;
	
	/**
	 * Producto "*"
	 */
	public int PROD = 38;
	
	/**
	 * División "/"
	 */
	public int DIV = 39;
	
	/**
	 * Módulo "%"
	 */
	public int MOD = 40;
}
