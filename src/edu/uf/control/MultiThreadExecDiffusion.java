package edu.uf.control;

import edu.uf.Diffusion.Diffuse;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Molecule;

public class MultiThreadExecDiffusion extends Exec implements Runnable{

	private String threadName;
	
	private static int k = 0;
	
	public MultiThreadExecDiffusion(String threadName) {
		this.threadName = threadName; 
	}
	
	private static synchronized Molecule getMolecule() {
		if(k >= molecules.size()) {
			return null;
		}
		return molecules.get(k++);
	}
	
	public static void reset() {
		k = 0;
	}
	
	public void run() {
		while(true) {
			
			Molecule mol = getMolecule();
			
			if (mol == null) {
				return;
			}else {
				mol.diffuse();
			}
		}
	}
	
}
