package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Granule;
import edu.uf.interactable.IL10;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;

public class PrintCoinjury extends PrintStat{
	

	@Override
	public void printStatistics(int k, File file){
		String str = k + "\t" + 
	              Afumigatus.getTotalCells() + "\t" +
	              /*Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +*/
	              //Granule.getMolecule().getTotalMolecule(0) + "\t" +
	              IL10.getMolecule().getTotalMolecule(0) + "\t" +
	              TGFb.getMolecule().getTotalMolecule(0) + "\t" +
	              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP1B.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
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
