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
//          Departamento de Tecnologías de la Información           //
//   Área de Ciencias de la Computación e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//


package tinto;

import java.io.*;
import tinto.parser.*;


/**
 * Clase que desarrolla el punto de entrada al compilador.
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class TintoCompiler {

	/**
	 * Punto de entrada de la aplicación
	 * @param args
	 */
	public static void main(String[] args)
	{

		// Busca el directorio de trabajo
		String path = (args.length == 0 ? System.getProperty("user.dir") : args[0]);
		File workingdir = new File(path);
		
		try
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(workingdir, "TintocOutput.txt"));
			PrintStream stream = new PrintStream(outputfile);

			File mainfile = new File(workingdir, "Main.tinto");
			FileInputStream fis = new FileInputStream(mainfile);
			TintoParserTokenManager lexer = new TintoParserTokenManager(new SimpleCharStream(fis));
			Token tk;
			do 
			{
				tk = lexer.getNextToken();
				stream.println("[Row: "+tk.beginLine+"][Column: "+tk.beginColumn+"][Kind: "+tk.kind+"] "+tk.toString());
			} 
			while(tk.kind != TintoParserConstants.EOF);
			
			stream.close();
		} 
		catch(Error err) 
		{
			printError(workingdir, err);
		}
		catch(Exception ex) 
		{
			printError(workingdir, ex);
		}
	}
	
	/**
	 * 
	 * @param workingdir Directorio de trabajo
	 * @param e Error a mostrar
	 */
	private static void printError(File workingdir, Throwable e) 
	{
		try 
		{
			FileOutputStream errorfile =  new FileOutputStream(new File(workingdir, "TintocErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("[File Main.tinto] 1 error found:");
			errorStream.println(e.toString());
			errorStream.close();
		}
		catch(Exception ex)
		{
		}
	}
}
