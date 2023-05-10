package edu.uf.main.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.IL10;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.MCP1;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.covid.DC;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.covid.Pneumocyte;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.interactable.covid.DAMP;
import edu.uf.interactable.covid.Defensin;
import edu.uf.interactable.covid.IFN1;
import edu.uf.interactable.covid.IL6Complex;
import edu.uf.interactable.covid.NK;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.covid.H2O2;
import edu.uf.interactable.covid.SarsCoV2;

public class PrintCovid extends PrintStat{

	//IFN		IL6		IL10		MCP1	TNF
    //17921, 103231		316227		95480	885
	
	@Override
	public void printStatistics(int k, File file) {
		if((k+1)%15!=0 && k != 1)return;
		count();
		double avogrado = 6.02e23;
		String str = k + "\t" + 
				((int) (avogrado*1000*SarsCoV2.getMolecule().getTotalMolecule(0))) + "\t" + //5.95656e+23  6.02e+26    9.40625e+27
				((int)(avogrado*1000*((SarsCoV2)SarsCoV2.getMolecule()).getInternalLoad())) + "\t" + //1.505e+25*
	              ((int) (IFN1.getMolecule().getTotalMolecule(0) * 21725 * 1e3 * 1e12)) + "\t" + 
	              //IFN1.getMolecule().getTotalMolecule(0) + "\t" + 
	              ((int) (Lactoferrin.getMolecule().getTotalMolecule(0) * 78182 * 1e3 * 1e9)) + "\t" +
	              //Lactoferrin.getMolecule().getTotalMolecule(0) + "\t" + 
	              ((int) (H2O2.getMolecule().getTotalMolecule(0) * 1e3 * 1e12)) + "\t" +
	              //SAMP.getMolecule().getTotalMolecule(0) + "\t" +
	              //DAMP.getMolecule().getTotalMolecule(0) + "\t" +
	              Pneumocyte.getTotalCells() + "\t" + 
	              DC.getTotalCells() + "\t" +
	              Neutrophil.getTotalCells() + "\t" + 
	              NK.getTotalCells() + "\t" + MActive + "\t" + NActive + "\t" + infected + "\t" + PActive + "\t" + irf9;
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
	
	public Voxel[][][] grid;
	int MResting = 0;
	int MActive = 0;
	int NResting = 0;
	int NActive = 0;
	int infected = 0;
	int PActive = 0;
	int irf9 = 0;
	int neut;
	int dead;
	
	void count() {
		MResting = 0;
		MActive = 0;
		NResting = 0;
		NActive = 0;
		neut = 0;
		infected = 0;
		PActive = 0;
		irf9 = 0;
		dead = 0;
		for(Voxel[][] VV : grid) {
			for(Voxel[] V : VV) {
				for(Voxel v : V) {
					for(Entry<Integer, Interactable> entry : v.getInteractables().entrySet()) {
						Interactable cell = entry.getValue();
						
						if(cell instanceof DC) {
							DC p = (DC) cell;
							if(p.hasPhenotype(DC.ACTIVE))MActive++;
						}
						if(cell instanceof Neutrophil) {
							neut++;
							Neutrophil p = (Neutrophil) cell;
							if(p.hasPhenotype(DC.ACTIVE))NActive++;
						}
						if(cell instanceof Pneumocyte) {
							neut++;
							Pneumocyte p = (Pneumocyte) cell;
							if(p.getViralLoad() > 0.0)infected++;
							if(p.hasPhenotype(DC.IRF3))PActive++;
							if(p.hasPhenotype(DC.IRF9))irf9++;
						}
					}
				}
			}
		}
	}

}
