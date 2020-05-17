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

import java.io.*;

/**
 * Flujo de entrada basado en un doble buffer que optimiza el acceso
 * a un fichero de caracteres y permite retroceder en la lectura
 * 
 * @author Francisco José Moreno Velo
  */
public class BufferedCharStream {

	/**
	 * Tamaño de la mitad del buffer
	 */
	private static final int HALFSIZE = 1024;
	
	/**
	 * Tamaño del buffer
	 */
	private static final int SIZE = 2*HALFSIZE;

	/**
	 * Flujo de entrada
	 */
	private InputStream stream;
	
	/**
	 * Buffer de datos
	 */
	private byte[] buffer;
	
	/**
	 * Contador de línea de los caracteres del buffer
	 */
	private int[] row;
	
	/**
	 * Contador de columna de los caracteres del buffer
	 */
	private int[] column;
	
	/**
	 * Flag que indica si el último trozo en llenarse ha sido
	 * el que empieza al principio (false) o en la mitad (true)
	 */
	private boolean half;
	
	/**
	 * Marcador del último carácter leido
	 */
	private int index;
	
	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public BufferedCharStream(File file) throws IOException 
	{
		this.stream = new FileInputStream(file);
		this.buffer = new byte[SIZE];
		this.row = new int[SIZE];
		this.column = new int[SIZE];
		this.half = true; // para que comience llenando la parte baja
		load();
		this.index = -1;
	}
	
	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public BufferedCharStream(FileInputStream fis) 
	{
		this.stream = fis;
		this.buffer = new byte[SIZE];
		this.row = new int[SIZE];
		this.column = new int[SIZE];
		this.half = true; // para que comience llenando la parte baja
		load();
		this.index = -1;
	}

	
	/**
	 * Cierra el flujo de entrada
	 */
	public void close() 
	{
		try { this.stream.close(); }
		catch(IOException ex) {}
	}
	
	/**
	 * Carga un bloque de medio buffer en la zona correspondiente
	 * y calcula los valores de líneas y columnas
	 */
	private void load() 
	{
		int begin = (half? 0 : HALFSIZE);
		int read = 0;
		try { read = stream.read(buffer,begin,HALFSIZE); }
		catch(IOException ex) {}
		if(read < HALFSIZE) 
		{
			for(int i=read; i<HALFSIZE; i++) buffer[begin+i] = 0;
		}
		int prev = (half? SIZE-1 : HALFSIZE-1);
		int newrow = row[prev];
		int newcolumn = column[prev]+1;
		if(buffer[prev] == '\n') { newrow++; newcolumn = 0; }
		for(int i=begin; i<begin+HALFSIZE; i++)
		{
			row[i] = newrow;
			column[i] = newcolumn;
			newcolumn++;
			if(buffer[i] == '\n') { newrow++; newcolumn = 0; }
		}
		half  = !half;
	}
	
	/**
	 * Obtiene el siguiente carácter del flujo de entrada.
	 * El método es responsable de cargar un nuevo trozo del
	 * buffer cuando sea necesario
	 * @return Siguiente carácter
	 */
	public char getNextChar() 
	{
		index++;
		if(index==HALFSIZE && !half) load();
		else if(index == SIZE && half) { index=0; load(); }
		else if(index == SIZE) { index = 0; }
		return (char) buffer[index];
	}
	
	/**
	 * Obtiene el número de fila correspondiente al último carácter leido
	 * @return Número de fila
	 */
	public int getRow() 
	{
		return row[index];
	}
	
	/**
	 * Obtiene el número de columna correspondiente al último carácter leido
	 * @return Número de columna
	 */
	public int getColumn() 
	{
		return column[index];
	}
	
	/**
	 * Retrocede un número de caracteres en el flujo de entrada
	 * @param disp Número de caracteres a retroceder
	 */
	public void retract(int disp) 
	{
		index -= disp;
		if(index <0) index += SIZE;
	}
}
