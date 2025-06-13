package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.IL10;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.Afumigatus.TAFC;

public class PrintBaseModel extends PrintStat{

	@Override
	public void printStatistics(int k, File file){
		if(k%15 != 0)return;
		String str = k + "\t" + 
	              Afumigatus.getTotalCells0() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              (TAFC.getMolecule().getTotalMolecule(0) + TAFC.getMolecule().getTotalMolecule(1)) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(0) + "\t" +
	              TAFC.getMolecule().getTotalMolecule(1) + "\t" +
	              Lactoferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              Lactoferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              Lactoferrin.getMolecule().getTotalMolecule(2) + "\t" +
	              (Transferrin.getMolecule().getTotalMolecule(0) + Transferrin.getMolecule().getTotalMolecule(1) + Transferrin.getMolecule().getTotalMolecule(2)) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              Transferrin.getMolecule().getTotalMolecule(2) + "\t" +
	              //Hepcidin.getMolecule().getTotalMolecule(0) + "\t" +
	              TGFb.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL6.getMolecule().getTotalMolecule(0) + "\t" +
	              IL10.getMolecule().getTotalMolecule(0) + "\t" +
	              TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP1B.getMolecule().getTotalMolecule(0) + "\t" +
	              MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              Macrophage.getTotalCells() + "\t" +
	              PneumocyteII.getTotalCells() + "\t" + 
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

