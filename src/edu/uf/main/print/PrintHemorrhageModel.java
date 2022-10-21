package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Erythrocyte;
import edu.uf.interactable.Haptoglobin;
import edu.uf.interactable.Heme;
import edu.uf.interactable.Hemoglobin;
import edu.uf.interactable.Hemopexin;
import edu.uf.interactable.Hepcidin;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.Transferrin;

public class PrintHemorrhageModel extends PrintStat {

	@Override
	public void printStatistics(int k, File file){
    	
		String str = k + "\t" + 
	              Afumigatus.getTotalCells() + "\t" +
	              Afumigatus.getTotalRestingConidia() + "\t" +
	              Afumigatus.getTotalSwellingConidia() + "\t" +
	              Afumigatus.getTotalGerminatingConidia() + "\t" +
	              Afumigatus.getTotalHyphae() + "\t" +
	              //(TAFC.getMolecule().getTotalMolecule(0) + TAFC.getMolecule().getTotalMolecule(1)) + "\t" +
	              //TAFC.getMolecule().getTotalMolecule(0) + "\t" +
	              //TAFC.getMolecule().getTotalMolecule(1) + "\t" +
	              //Lactoferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              //Lactoferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              //Lactoferrin.getMolecule().getTotalMolecule(2) + "\t" +
	              //(Transferrin.getMolecule().getTotalMolecule(0) + Transferrin.getMolecule().getTotalMolecule(1) + Transferrin.getMolecule().getTotalMolecule(2)) + "\t" +
	              //Transferrin.getMolecule().getTotalMolecule(0) + "\t" +
	              //Transferrin.getMolecule().getTotalMolecule(1) + "\t" +
	              //Transferrin.getMolecule().getTotalMolecule(2) + "\t" + 
	              //Hemoglobin.getMolecule().getTotalMolecule(0) + "\t" +
	              Heme.getMolecule().getTotalMolecule(0) + "\t" +
	              //Hepcidin.getMolecule().getTotalMolecule(0) + "\t" +
	              Hemopexin.getMolecule().getTotalMolecule(0) + "\t" +
	              Hemopexin.getMolecule().getTotalMolecule(1) + "\t" +
	              //Haptoglobin.getMolecule().getTotalMolecule(0) + "\t" +
	              //Haptoglobin.getMolecule().getTotalMolecule(1) + "\t" +
	              //TGFb.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL6.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL10.getMolecule().getTotalMolecule(0) + "\t" +
	              //IL1.getMolecule().getTotalMolecule(0) + "\t" + 
	              //TNFa.getMolecule().getTotalMolecule(0) + "\t" +
	              //MIP1B.getMolecule().getTotalMolecule(0) + "\t" +
	              //MIP2.getMolecule().getTotalMolecule(0) + "\t" +
	              //Erythrocyte.getTotalCells() + "\t" + 
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
