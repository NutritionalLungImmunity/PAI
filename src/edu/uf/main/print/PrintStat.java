package edu.uf.main.print;

import java.io.File;
import java.io.PrintWriter;

public abstract class PrintStat {

	private PrintWriter pw;
	
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
