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
//import tinto.parserjj.*;


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
			File mainfile = new File(workingdir, "Main.tinto");
			FileInputStream fis = new FileInputStream(mainfile);
			TintoParser parser = new TintoParser(fis);
			parser.tryCompilationUnit();
			//parser.CompilationUnit();
			if(parser.getErrorCount() == 0 ) 
			{
				printOutput(workingdir,"Correcto");
			}
			else 
			{
				printError(workingdir, parser.getErrorCount(), parser.getErrorMsg());
				printOutput(workingdir,"Incorrecto");
			}
		} 
		catch(Error err) 
		{
			printError(workingdir, 1, err.toString());
			printOutput(workingdir,"Incorrecto");

		}
		catch(Exception ex) 
		{
			printError(workingdir, 1, ex.toString());
			printOutput(workingdir,"Incorrecto");
		}
	}
	
	/**
	 * Genera el fichero de error
	 * @param workingdir Directorio de trabajo
	 * @param count Número de errores
	 * @param msg Mensaje de error
	 */
	private static void printError(File workingdir, int count, String msg) 
	{
		try 
		{
			FileOutputStream errorfile =  new FileOutputStream(new File(workingdir, "TintocErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("[File Main.tinto] "+count+" error"+(count>1?"s":"")+" found:");
			errorStream.println(msg);
			errorStream.close();
		}
		catch(Exception ex)
		{
		}
	}
	
	/**
	 * Genera el fichero de salida
	 * @param workingdir Directorio de trabajo
	 * @param e Error a mostrar
	 */
	private static void printOutput(File workingdir, String msg) 
	{
		try 
		{
			FileOutputStream outputfile =  new FileOutputStream(new File(workingdir, "TintocOutput.txt"));
			PrintStream stream = new PrintStream(outputfile);
			stream.println(msg);
			stream.close();
		}
		catch(Exception ex)
		{
		}
	}
}
