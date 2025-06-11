package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.EndothelialCell;
import edu.uf.interactable.IL4;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteI;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Afumigatus.Afumigatus;

public class PrintExample extends PrintStat{

	@Override
	public void printStatistics(int k, File file){
		if(k%15 != 0)return;
		String str = k + "\t" + 
	              Afumigatus.getTotalCells0() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              IL4.getMolecule().getTotalMolecule(0)  + "\t" +
	              EndothelialCell.getTotalCells() + "\t" +
	              PneumocyteI.getTotalCells() + "\t" +
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
