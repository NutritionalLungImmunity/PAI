package edu.uf.main.print;

import java.io.File;
import java.io.PrintWriter;

public abstract class PrintStat {

	private PrintWriter pw;
	
	/**
	 * Print the simulation statistics (e.g., number of macrophages, amount of TNF, etc) 
	 * on each iteration (k==-1) or each "k" iteration. The exact statistics depend on the 
	 * implementation. Prints on the screen if file==null or into a file if provided. 
	 * @param k
	 * @param file
	 */
	public abstract void printStatistics(int k, File file);

	public void close() {
		if(pw != null) 
			pw.close();
	}
	
	public PrintWriter getPrintWriter() {
		return pw;
	}
	
	public void setPrintWriter(PrintWriter pw) {
		this.pw = pw;
	}
	
}
