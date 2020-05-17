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
//   Dpto. Ing. Electr�nica, de Sistemas Inform�ticos y Atom�tica   //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto.parser;

/**
 * Constantes que definen los si�mbolos no terminales de la gram�tica
 * 
 * @author Francisco Jos� Moreno Velo
 */
public interface SymbolConstants {

	/** CompilationUnit   (S�mbolo inicial) */
	public int S_COMPILATION_UNIT  = 0;
	
	/** ImportClauseList   */
	public int S_IMPORT_CLAUSE_LIST = 1;
	
	/** ImportClause   */
	public int S_IMPORT_CLAUSE = 2;
	
	/** LibraryDecl  */
	public int S_LIBRARY_DECL = 3;

	/** FunctionList   */
	public int S_FUNCTION_LIST = 4;
	
	/** FunctionDecl   */
	public int S_FUNCTION_DECL = 5;
	
	/** Access */
	public int S_ACCESS = 6;
	
	/** FunctionType   */
	public int S_FUNCTION_TYPE = 7;
	
	/** Type */
	public int S_TYPE = 8;
	
	/** ArgumentDecl  */
	public int S_ARGUMENT_DECL = 9;
	
	/** ArgumentList  */
	public int S_ARGUMENT_LIST = 10;
	
	/** Argument  */
	public int S_ARGUMENT = 11;
	
	/** FunctionBody  */
	public int S_FUNCTION_BODY = 12;
	
	/** StatementList  */
	public int S_STATEMENT_LIST = 13;
	
	/** Statement   */
	public int S_STATEMENT = 14;
	
	/** Decl  */
	public int S_DECL = 15;
	
	/** IdList  */
	public int S_ID_LIST = 16;
	
	/** IfStm  */
	public int S_IF_STM = 17;
	
	/** WhileStm  */
	public int S_WHILE_STM = 18;
	
	/** ReturnStm  */
	public int S_RETURN_STM = 19;
	
	/** NoStm  */
	public int S_NO_STM = 20;
	
	/** IdStm  */
	public int S_ID_STM = 21;
	
	/** BlockStm  */
	public int S_BLOCK_STM = 22;
	
	/** Expr  */
	public int S_EXPR = 23;
	
	/** AndExpr  */
	public int S_AND_EXPR = 24;
	
	/** RelExpr  */
	public int S_REL_EXPR = 25;
	
	/** SumExpr  */
	public int S_SUM_EXPR = 26;
	
	/** ProdExpr  */
	public int S_PROD_EXPR = 27;
	
	/** Factor  */
	public int S_FACTOR = 28;
	
	/** Literal  */
	public int S_LITERAL = 29;
	
	/** Reference  */
	public int S_REFERENCE = 30;

	/** FunctionCall  */
	public int S_FUNCTION_CALL = 31;
	
	/** ExprList  */
	public int S_EXPR_LIST = 32;
	
	// AÑADIDOS NUEVOS SÍMBOLOS NO TERMINALES
	
	public int S_DO_WHILE_STM = 33;
	
	public int S_FOR_STM = 34;
	
	public int S_FOR_INIT = 35;
	
	public int S_FOR_COND = 36;
	
	public int S_FOR_UPDATE = 37;
	
	public int S_ID_STM_LIST = 38;
	
	public int S_SWITCH_STM = 39;
	
	public int S_CLAUSE_LIST = 40;
	
	public int S_CASE_CLAUSE = 41;
	
	public int S_DEFAULT_CLAUSE = 42;
	
	public int S_BREAK_STM = 43;
	
	public int S_CONTINUE_STM = 44;
}
