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


import java.io.FileInputStream;


/**
 * Analizador sintáctico de Tinto basado en una gramática BNF y LL(1)
 *  
 * @author Francisco José Moreno Velo
 *
 */
public class TintoParser implements TokenConstants {

	//----------------------------------------------------------------//
	//           Miembros privados dedicados al análisis              //
	//----------------------------------------------------------------//

	/**
	 * Analizador léxico
	 */
	private TintoLexer lexer;

	/**
	 * Último token analizado
	 */
	private Token prevToken;

	/**
	 * Siguiente token de la cadena de entrada
	 */
	private Token nextToken;

	//----------------------------------------------------------------//
	//      Miembros privados dedicados al tratamiento de errores     //
	//----------------------------------------------------------------//

	/**
	 * Contador de errores
	 */
	private int errorCount;

	/**
	 * Mensaje de errores
	 */
	private String errorMsg;

	//----------------------------------------------------------------//
	//              Métodos relacionados con el análisis              //
	//----------------------------------------------------------------//

	/**
	 * Constructor
	 */
	public TintoParser(FileInputStream fis) 
	{
		this.lexer = new TintoLexer(fis);
		this.prevToken = null;
		this.nextToken = lexer.getNextToken();
		this.errorCount = 0;
		this.errorMsg = "";
	}

	//----------------------------------------------------------------//
	//       Métodos relacionados con el tratamiento de errores       //
	//----------------------------------------------------------------//

	/**
	 * Obtiene el número de errores del análisis
	 * @return
	 */
	public int getErrorCount()
	{
		return this.errorCount;
	}

	/**
	 * Obtiene el mensaje de error del análisis
	 * @return
	 */
	public String getErrorMsg()
	{
		return this.errorMsg;
	}

	/**
	 * Almacena un error de análisis
	 * @param ex
	 */
	private void catchError(Exception ex)
	{
		this.errorCount++;
		this.errorMsg += ex.toString();
	}

	/**
	 * Sincroniza la cadena de tokens
	 * @param left
	 * @param right
	 */
	private void skipTo(int[] left, int[] right) {
		boolean flag = false;
		if(prevToken.getKind() == EOF || nextToken.getKind() == EOF) flag = true;
		for(int i=0; i<left.length; i++) if(prevToken.getKind() == left[i]) flag = true;
		for(int i=0; i<right.length; i++) if(nextToken.getKind() == right[i]) flag = true;

		while(!flag) 
		{
			prevToken = nextToken;
			nextToken = lexer.getNextToken();
			if(prevToken.getKind() == EOF || nextToken.getKind() == EOF) flag = true;
			for(int i=0; i<left.length; i++) if(prevToken.getKind() == left[i]) flag = true;
			for(int i=0; i<right.length; i++) if(nextToken.getKind() == right[i]) flag = true;
		}
	}

	//----------------------------------------------------------------//
	//     Métodos relacionados con el análisis de la gramática       //
	//----------------------------------------------------------------//

	/**
	 * Método que consume un token de la cadena de entrada
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token 
	 */
	private void match(int kind) throws SintaxException 
	{
		if(nextToken.getKind() == kind) 
		{
			prevToken = nextToken;
			nextToken = lexer.getNextToken();
		}
		else throw new SintaxException(nextToken,kind);
	}

	/**
	 * Analiza el símbolo <CompilationUnit>
	 */
	public void tryCompilationUnit() 
	{
		int[] lsync = { };
		int[] rsync = { EOF };
		try
		{
			parseCompilationUnit();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <CompilationUnit>
	 * @throws SintaxException
	 */
	private void parseCompilationUnit() throws SintaxException 
	{
		int[] expected = { IMPORT, LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
		case IMPORT:
		case LIBRARY:
		case NATIVE:
			tryImportClauseList();
			tryTintoDecl();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ImportClauseList>
	 */
	private void tryImportClauseList() 
	{
		int[] lsync = { };
		int[] rsync = { LIBRARY, NATIVE };

		try
		{
			parseImportClauseList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ImportClauseList>
	 * @throws SintaxException
	 */
	private void parseImportClauseList() throws SintaxException 
	{
		int[] expected = { IMPORT, LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
		case IMPORT:
			tryImportClause();
			tryImportClauseList();
			break;
		case LIBRARY:	
		case NATIVE:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ImportClause>
	 */
	private void tryImportClause() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { IMPORT, LIBRARY, NATIVE };

		try
		{
			parseImportClause();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ImportClause>
	 * @throws SintaxException
	 */
	private void parseImportClause() throws SintaxException 
	{
		int[] expected = { IMPORT };
		switch(nextToken.getKind()) 
		{
		case IMPORT:
			match(IMPORT);
			match(IDENTIFIER);
			match(SEMICOLON);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <TintoDecl>
	 */
	private void tryTintoDecl()  
	{
		int[] lsync = { };
		int[] rsync = { };

		try
		{
			parseTintoDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <LibraryDecl>
	 * @throws SintaxException
	 */
	private void parseTintoDecl() throws SintaxException 
	{
		int[] expected = { LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
		case LIBRARY:
			tryLibraryDecl();
			break;
		case NATIVE:
			tryNativeDecl();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el símbolo <LibraryDecl>
	 */
	private void tryLibraryDecl()  
	{
		int[] lsync = { };
		int[] rsync = { };

		try
		{
			parseLibraryDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <LibraryDecl>
	 * @throws SintaxException
	 */
	private void parseLibraryDecl() throws SintaxException 
	{
		int[] expected = { LIBRARY };
		switch(nextToken.getKind()) 
		{
		case LIBRARY:
			match(LIBRARY);
			match(IDENTIFIER);
			match(LBRACE);
			tryFunctionList();
			match(RBRACE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <FunctionList>
	 */
	private void tryFunctionList() 
	{
		int[] lsync = { };
		int[] rsync = { RBRACE }; 

		try
		{
			parseFunctionList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		} 
	}

	/**
	 * Analiza el símbolo <FunctionList>
	 * @throws SintaxException
	 */
	private void parseFunctionList() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
		case PUBLIC:
		case PRIVATE:
			tryFunctionDecl();
			tryFunctionList();
			break;
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <FunctionDecl>
	 */
	private void tryFunctionDecl() 
	{
		int[] lsync = { };
		int[] rsync = { PUBLIC, PRIVATE }; 
		try
		{
			parseFunctionDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}  
	}

	/**
	 * Analiza el símbolo <FunctionDecl>
	 * @throws SintaxException
	 */
	private void parseFunctionDecl() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
		case PUBLIC:
		case PRIVATE:
			tryAccess();
			tryFunctionType();
			match(IDENTIFIER);
			tryArgumentDecl();
			tryFunctionBody();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <NativeDecl>
	 */
	private void tryNativeDecl()  
	{
		int[] lsync = { };
		int[] rsync = { };

		try
		{
			parseNativeDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <NativeDecl>
	 * @throws SintaxException
	 */
	private void parseNativeDecl() throws SintaxException 
	{
		int[] expected = { NATIVE };
		switch(nextToken.getKind()) 
		{
			case NATIVE:
				match(NATIVE);
				match(IDENTIFIER);
				match(LBRACE);
				tryNativeFunctionList();
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el símbolo <NativeFunctionList>
	 */
	private void tryNativeFunctionList() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { RBRACE }; 

		try
		{
			parseNativeFunctionList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		} 
	}
	
	/**
	 * Analiza el símbolo <NativeFunctionList>
	 * @throws SintaxException
	 */
	private void parseNativeFunctionList() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				tryNativeFunctionDecl();
				tryNativeFunctionList();
				break;
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el símbolo <NativeFunctionDecl>
	 */
	private void tryNativeFunctionDecl() 
	{
		int[] lsync = { };
		int[] rsync = { PUBLIC, PRIVATE }; 
		try
		{
			parseNativeFunctionDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}  
	}
	
	/**
	 * Analiza el símbolo <NativeFunctionDecl>
	 * @throws SintaxException
	 */
	private void parseNativeFunctionDecl() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				tryAccess();
				tryFunctionType();
				match(IDENTIFIER);
				tryArgumentDecl();
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el símbolo <Access>
	 */
	private void tryAccess() 
	{
		int[] lsync = { };
		int[] rsync = { VOID, INT, CHAR, BOOLEAN };

		try
		{
			parseAccess();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Access>
	 * @throws SintaxException
	 */
	private void parseAccess() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
		case PUBLIC:
			match(PUBLIC);
			break;
		case PRIVATE:
			match(PRIVATE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <FunctionType>
	 */
	private void tryFunctionType() 
	{
		int[] lsync = { };
		int[] rsync = { IDENTIFIER };

		try
		{
			parseFunctionType();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <FunctionType>
	 * @throws SintaxException
	 */
	private void parseFunctionType() throws SintaxException 
	{
		int[] expected = { VOID, INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
		case VOID:
			match(VOID);
			break;
		case INT:
		case CHAR:
		case BOOLEAN:
			tryType();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Type>
	 */
	private void tryType() 
	{
		int[] lsync = { };
		int[] rsync = { IDENTIFIER };

		try
		{
			parseType();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Type>
	 * @throws SintaxException
	 */
	private void parseType() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
		case INT:
			match(INT);
			break;
		case CHAR:
			match(CHAR);
			break;
		case BOOLEAN:
			match(BOOLEAN);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ArgumentDecl>
	 */
	private void tryArgumentDecl() 
	{
		int[] lsync = { RPAREN };
		int[] rsync = { LBRACE };
		try
		{
			parseArgumentDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ArgumentDecl>
	 * @throws SintaxException
	 */
	private void parseArgumentDecl() throws SintaxException 
	{
		int[] expected = { LPAREN };
		switch(nextToken.getKind()) 
		{
		case LPAREN:
			match(LPAREN);
			tryArgumentList();
			match(RPAREN);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ArgumentList>
	 */
	private void tryArgumentList() 
	{
		int[] lsync = { };
		int[] rsync = { RPAREN };

		try
		{
			parseArgumentList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ArgumentList>
	 * @throws SintaxException
	 */
	private void parseArgumentList() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, RPAREN };
		switch(nextToken.getKind()) 
		{
		case INT:
		case CHAR:
		case BOOLEAN:
			tryArgument();
			tryMoreArguments();
			break;
		case RPAREN:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Argument>
	 */
	private void tryArgument() 
	{
		int[] lsync = { };
		int[] rsync = { COMMA, RPAREN };

		try
		{
			parseArgument();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Argument>
	 * @throws SintaxException
	 */
	private void parseArgument() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
		case INT:
		case CHAR:
		case BOOLEAN:
			tryType();
			match(IDENTIFIER);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreArguments>
	 */
	private void tryMoreArguments() 
	{
		int[] lsync = { };
		int[] rsync = { RPAREN };

		try
		{
			parseMoreArguments();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <MoreArguments>
	 * @throws SintaxException
	 */
	private void parseMoreArguments() throws SintaxException 
	{
		int[] expected = { COMMA, RPAREN };
		switch(nextToken.getKind()) 
		{
		case COMMA:
			match(COMMA);
			tryArgument();
			tryMoreArguments();
			break;
		case RPAREN:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <FunctionBody>
	 */
	private void tryFunctionBody() 
	{
		int[] lsync = { };
		int[] rsync = { PUBLIC, PRIVATE }; 
		try
		{
			parseFunctionBody();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}  
	}

	/**
	 * Analiza el símbolo <FunctionBody>
	 * @throws SintaxException
	 */
	private void parseFunctionBody() throws SintaxException 
	{
		int[] expected = { LBRACE };
		switch(nextToken.getKind()) 
		{
		case LBRACE:
			match(LBRACE);
			tryStatementList();
			match(RBRACE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**************************************************************/
	/*       EL ANÁLISIS DE LAS INSTRUCCIONES COMIENZA AQUÍ       */
	/**************************************************************/

	/**
	 * Analiza el símbolo <StatementList>
	 */
	private void tryStatementList() 
	{
		int[] lsync = { };
		int[] rsync = { RBRACE }; 
		try
		{
			parseStatementList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}  
	}

	/**
	 * Analiza el símbolo <StatementList>
	 * @throws SintaxException
	 */
	private void parseStatementList() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
				RETURN, SEMICOLON, LBRACE, RBRACE };
		switch(nextToken.getKind()) 
		{
		case INT:
		case CHAR:
		case BOOLEAN:
		case IDENTIFIER:
		case IF:
		case WHILE:
		case RETURN:
		case SEMICOLON:
		case LBRACE:
			tryStatement();
			tryStatementList();
			break;
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 *  Analiza el símbolo <Statement>
	 * @param symtab
	 * @return
	 */
	private void tryStatement() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseStatement();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Statement>
	 * @throws SintaxException
	 */
	private void parseStatement() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
				RETURN, SEMICOLON, LBRACE };
		switch(nextToken.getKind()) 
		{
		case INT:
		case CHAR:
		case BOOLEAN:
			tryDecl();
			break;
		case IDENTIFIER:
			tryIdStm();
			break;
		case IF:
			tryIfStm();
			break;
		case WHILE:
			tryWhileStm();
			break;
		case RETURN:
			tryReturnStm();
			break;
		case SEMICOLON:
			tryNoStm();
			break;
		case LBRACE:
			tryBlockStm();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Decl>
	 */
	private void tryDecl() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Decl>
	 * @throws SintaxException
	 */
	private void parseDecl() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
		case INT:
		case CHAR:
		case BOOLEAN:
			tryType();
			match(IDENTIFIER);
			tryAssignement();
			tryMoreDecl();
			match(SEMICOLON);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreDecl>
	 */
	private void tryMoreDecl() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};

		try
		{
			parseMoreDecl();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <MoreDecl>
	 * @throws SintaxException
	 */
	private void parseMoreDecl() throws SintaxException 
	{
		int[] expected = { COMMA, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case COMMA:
			match(COMMA);
			match(IDENTIFIER);
			tryAssignement();
			tryMoreDecl();
			break;
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Assignement>
	 */
	private void tryAssignement() 
	{
		int[] lsync = { };
		int[] rsync = { COMMA, SEMICOLON };
		try
		{
			parseAssignement();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Assignement>
	 * @throws SintaxException
	 */
	private void parseAssignement() throws SintaxException 
	{
		int[] expected = { ASSIGN, COMMA, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case ASSIGN:
			match(ASSIGN);
			tryExpr();
			break;
		case COMMA:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <IfStm>
	 * @param symtab
	 * @return
	 */
	private void tryIfStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseIfStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <IfStm>
	 * @throws SintaxException
	 */
	private void parseIfStm() throws SintaxException 
	{
		int[] expected = { IF };
		switch(nextToken.getKind()) 
		{
		case IF:
			match(IF);
			match(LPAREN);
			tryExpr();
			match(RPAREN);
			tryStatement();
			tryElseStm();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ElseStm>
	 * @param symtab
	 * @return
	 */
	private void tryElseStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseElseStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ElseStm>
	 * @throws SintaxException
	 */
	private void parseElseStm() throws SintaxException 
	{
		int[] expected = { ELSE, INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
				RETURN, SEMICOLON, LBRACE, RBRACE };
		switch(nextToken.getKind()) 
		{
		case ELSE:
			match(ELSE);
			tryStatement();
			break;
		case INT:
		case CHAR:
		case BOOLEAN:
		case IDENTIFIER:
		case IF:
		case WHILE:
		case RETURN:
		case SEMICOLON:
		case LBRACE:
		case RBRACE:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <WhileStm>
	 * @param symtab
	 * @return
	 */
	private void tryWhileStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseWhileStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <WhileStm>
	 * @throws SintaxException
	 */
	private void parseWhileStm() throws SintaxException 
	{
		int[] expected = { WHILE };
		switch(nextToken.getKind()) 
		{
		case WHILE:
			match(WHILE);
			match(LPAREN);
			tryExpr();
			match(RPAREN);
			tryStatement();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ReturnStm>
	 */	
	private void tryReturnStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseReturnStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ReturnStm>
	 * @throws SintaxException
	 */
	private void parseReturnStm() throws SintaxException 
	{
		
		int[] expected = { RETURN };
		switch(nextToken.getKind()) 
		{
		case RETURN:
			match(RETURN);
			tryReturnExpr();
			match(SEMICOLON);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ReturnExpr>
	 */	
	private void tryReturnExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON };
		try
		{
			parseReturnExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <ReturnExpr>
	 * @throws SintaxException
	 */
	private void parseReturnExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			tryExpr();
			break;
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <NoStm>
	 */
	private void tryNoStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseNoStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <NoStm>
	 * @throws SintaxException
	 */
	private void parseNoStm() throws SintaxException 
	{
		int[] expected = { SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case SEMICOLON:
			match(SEMICOLON);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <IdStm>
	 */
	private void tryIdStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseIdStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
		
	/**
	 * Analiza el símbolo <IdStm>
	 * @throws SintaxException
	 */
	private void parseIdStm() throws SintaxException 
	{
		int[] expected = { IDENTIFIER };
		switch(nextToken.getKind()) 
		{
		case IDENTIFIER:
			match(IDENTIFIER);
			tryIdStmContinue();
			match(SEMICOLON);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <IdStmContinue>
	 */
	private void tryIdStmContinue() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseIdStmContinue();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <IdStmContinue>
	 * @throws SintaxException
	 */
	private void parseIdStmContinue() throws SintaxException 
	{
		int[] expected = { ASSIGN, LPAREN, DOT };
		switch(nextToken.getKind()) 
		{
		case ASSIGN:
			match(ASSIGN);
			tryExpr();
			break;
		case LPAREN:
			tryFunctionCall();
			break;
		case DOT:
			match(DOT);
			match(IDENTIFIER);
			tryFunctionCall();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <BlockStm>
	 */
	private void tryBlockStm() 
	{
		int[] lsync = { SEMICOLON };
		int[] rsync = { INT, CHAR, BOOLEAN, IF, WHILE, RETURN, RBRACE};
		try
		{
			parseBlockStm();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
		
	/**
	 * Analiza el símbolo <BlockStm>
	 * @throws SintaxException
	 */
	private void parseBlockStm() throws SintaxException 
	{
		int[] expected = { LBRACE };
		switch(nextToken.getKind()) 
		{
		case LBRACE:
			match(LBRACE);
			tryStatementList();
			match(RBRACE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}


	/**************************************************************/
	/*       EL ANÁLISIS DE LAS EXPRESIONES COMIENZA AQUÍ         */
	/**************************************************************/

	/**
	 * Analiza el símbolo <Expr>
	 */
	private void tryExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN };
		try
		{
			parseExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}

	/**
	 * Analiza el símbolo <Expr>
	 * @throws SintaxException
	 */
	private void parseExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			tryAndExpr();
			tryMoreOrExpr();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Expr>
	 */
	private void tryMoreOrExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN };
		try
		{
			parseMoreOrExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreOrExpr>
	 * @throws SintaxException
	 */
	private void parseMoreOrExpr() throws SintaxException 
	{
		int[] expected = { OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case OR:
			match(OR);
			tryAndExpr();
			tryMoreOrExpr();
			break;
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <AndExpr>
	 */
	private void tryAndExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR };
		try
		{
			parseAndExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <AndExpr>
	 * @throws SintaxException
	 */
	private void parseAndExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			tryRelExpr();
			tryMoreAndExpr();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreAndExpr>
	 */
	private void tryMoreAndExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR };
		try
		{
			parseMoreAndExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreAndExpr>
	 * @throws SintaxException
	 */
	private void parseMoreAndExpr() throws SintaxException 
	{
		int[] expected = { AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case AND:
			match(AND);
			tryRelExpr();
			tryMoreAndExpr();
			break;
		case OR:
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <RelExpr>
	 */
	private void tryRelExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND };
		try
		{
			parseRelExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
		
	/**
	 * Analiza el símbolo <RelExpr>
	 * @throws SintaxException
	 */
	private void parseRelExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			trySumExpr();
			tryMoreRelExpr();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreRelExpr>
	 */
	private void tryMoreRelExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND };
		try
		{
			parseMoreRelExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreRelExpr>
	 * @throws SintaxException
	 */
	private void parseMoreRelExpr() throws SintaxException 
	{
		int[] expected = { EQ, NE, GT, GE, LT, LE, AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
			parseRelOp();
			tryRelExpr();
			break;
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <RelOp>
	 * @throws SintaxException
	 */
	private void parseRelOp() throws SintaxException 
	{
		int[] expected = { EQ, NE, GT, GE, LT, LE };
		switch(nextToken.getKind()) 
		{
		case EQ:
			match(EQ);
			break;
		case NE:
			match(NE);
			break;
		case GT:
			match(GT);
			break;
		case GE:
			match(GE);
			break;
		case LT:
			match(LT);
			break;
		case LE:
			match(LE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <SumExpr>
	 */
	private void trySumExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE };
		try
		{
			parseSumExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
		
	/**
	 * Analiza el símbolo <SumExpr>
	 * @throws SintaxException
	 */
	private void parseSumExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			parseUnOp();
			tryProdExpr();
			tryMoreSumExpr();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <UnOp>
	 * @throws SintaxException
	 */
	private void parseUnOp() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case NOT:
			match(NOT);
			break;
		case MINUS:
			match(MINUS);
			break;
		case PLUS:
			match(PLUS);
			break;
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreSumExpr>
	 */
	private void tryMoreSumExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE };
		try
		{
			parseMoreSumExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreSumExpr>
	 * @throws SintaxException
	 */
	private void parseMoreSumExpr() throws SintaxException 
	{
		int[] expected = { PLUS, MINUS, EQ, NE, GT, GE, LT, LE, 
				AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case PLUS:
		case MINUS:
			parseSumOp();
			tryProdExpr();
			tryMoreSumExpr();
			break;
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <SumOp>
	 * @throws SintaxException
	 */
	private void parseSumOp() throws SintaxException 
	{
		int[] expected = { MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
		case MINUS:
			match(MINUS);
			break;
		case PLUS:
			match(PLUS);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ProdExpr>
	 */
	private void tryProdExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS };
		try
		{
			parseProdExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
		
	/**
	 * Analiza el símbolo <ProdExpr>
	 * @throws SintaxException
	 */
	private void parseProdExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN };
		switch(nextToken.getKind()) 
		{
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			tryFactor();
			tryMoreProdExpr();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreProdExpr>
	 */
	private void tryMoreProdExpr() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS };
		try
		{
			parseMoreProdExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreProdExpr>
	 * @throws SintaxException
	 */
	private void parseMoreProdExpr() throws SintaxException 
	{
		int[] expected = { PROD, DIV, MOD, PLUS, MINUS, EQ, NE, GT, GE, LT, LE, 
				AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case PROD:
		case DIV:
		case MOD:
			parseProdOp();
			tryFactor();
			tryMoreProdExpr();
			break;
		case PLUS:
		case MINUS:
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ProdOp>
	 * @throws SintaxException
	 */
	private void parseProdOp() throws SintaxException 
	{
		int[] expected = { PROD, DIV, MOD };
		switch(nextToken.getKind()) 
		{
		case PROD:
			match(PROD);
			break;
		case DIV:
			match(DIV);
			break;
		case MOD:
			match(MOD);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Factor>
	 */
	private void tryFactor() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try
		{
			parseFactor();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <Factor>
	 * @throws SintaxException
	 */
	private void parseFactor() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN };
		switch(nextToken.getKind()) 
		{
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
			tryLiteral();
			break;
		case IDENTIFIER:
			tryReference();
			break;
		case LPAREN:
			match(LPAREN);
			tryExpr();
			match(RPAREN);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Literal>
	 */
	private void tryLiteral() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try
		{
			parseLiteral();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <Literal>
	 * @throws SintaxException
	 */
	private void parseLiteral() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, FALSE };
		switch(nextToken.getKind()) 
		{
		case INTEGER_LITERAL:
			match(INTEGER_LITERAL);
			break;
		case CHAR_LITERAL:
			match(CHAR_LITERAL);
			break;
		case TRUE:
			match(TRUE);
			break;
		case FALSE:
			match(FALSE);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <Reference>
	 */
	private void tryReference() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try
		{
			parseReference();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <Reference>
	 * @throws SintaxException
	 */
	private void parseReference() throws SintaxException 
	{
		int[] expected = { IDENTIFIER };
		switch(nextToken.getKind()) 
		{
		case IDENTIFIER:
			match(IDENTIFIER);
			tryReferenceContinue();
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ReferenceContinue>
	 */
	private void tryReferenceContinue() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try
		{
			parseReferenceContinue();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <ReferenceContinue>
	 * @throws SintaxException
	 */
	private void parseReferenceContinue() throws SintaxException 
	{
		int[] expected = { LPAREN, DOT, PROD, DIV, MOD, PLUS, MINUS, EQ, 
				NE, GT, GE, LT, LE, AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
		case LPAREN:
			tryFunctionCall();
			break;
		case DOT:
			match(DOT);
			match(IDENTIFIER);
			tryFunctionCall();
			break;
		case PROD:
		case DIV:
		case MOD:
		case PLUS:
		case MINUS:
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
		case AND:
		case OR:
		case COMMA:
		case RPAREN:
		case SEMICOLON:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <FunctionCall>
	 */
	private void tryFunctionCall() 
	{
		int[] lsync = { };
		int[] rsync = { SEMICOLON, COMMA, RPAREN, OR, AND, EQ, NE, GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD };
		try
		{
			parseFunctionCall();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <FunctionCall>
	 * @throws SintaxException
	 */
	private void parseFunctionCall() throws SintaxException 
	{
		int[] expected = { LPAREN };
		switch(nextToken.getKind()) 
		{
		case LPAREN:
			match(LPAREN);
			tryExprList();
			match(RPAREN);
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <ExprList>
	 */
	private void tryExprList() 
	{
		int[] lsync = { };
		int[] rsync = { RPAREN };
		try
		{
			parseExprList();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <ExprList>
	 * @throws SintaxException
	 */
	private void parseExprList() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS , RPAREN };
		switch(nextToken.getKind()) 
		{
		case NOT:
		case MINUS:
		case PLUS:
		case INTEGER_LITERAL:
		case CHAR_LITERAL:
		case TRUE:
		case FALSE:
		case IDENTIFIER:
		case LPAREN:
			tryExpr();
			tryMoreExpr();
			break;
		case RPAREN:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el símbolo <MoreExpr>
	 */
	private void tryMoreExpr() 
	{
		int[] lsync = { };
		int[] rsync = { RPAREN };
		try
		{
			parseMoreExpr();
		}
		catch(Exception ex)
		{
			catchError(ex);
			skipTo(lsync,rsync);
		}
	}
	
	/**
	 * Analiza el símbolo <MoreExpr>
	 * @throws SintaxException
	 */
	private void parseMoreExpr() throws SintaxException 
	{
		int[] expected = { COMMA, RPAREN };
		switch(nextToken.getKind()) 
		{
		case COMMA:
			match(COMMA);
			tryExpr();
			tryMoreExpr();
			break;
		case RPAREN:
			break;
		default:
			throw new SintaxException(nextToken,expected);
		}
	}
}
