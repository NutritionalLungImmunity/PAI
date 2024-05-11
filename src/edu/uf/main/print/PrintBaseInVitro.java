package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Afumigatus.Afumigatus;

public class PrintBaseInVitro extends PrintStat{
	
	@Override
	public void printStatistics(int k, File file){
    	
		String str = k + "\t" + 
	              Afumigatus.getTotalCells0() + "\t" +
	              Macrophage.getTotalCells() + "\t" +
	              Neutrophil.getTotalCells();
		
		if(file == null) {
			System.out.println(str);
		}else {
			try {
		
				if(getPrintWriter() == null) 
					setPrintWriter(new PrintWriter(file)); 
				getPrintWriter().println(str);
			}catch(FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}
}
