package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.Cell;
import edu.uf.interactable.IFN_III;
import edu.uf.interactable.PneumocyteI;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.IntracellularModel;

public class PneumocyteIKlebsiella extends IntracellularModel{
	
	public static final String name = "PneumocyteIKlebsiella";

	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[0] = input(IFN_III.getMolecule());
		
		for(int i = 0; i < NUM_RECEPTORS; i++)
			this.inputs[i] = 0;
		
		this.clearPhenotype();
		
		
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[0] > 0) 
			this.getPhenotype().put(PneumocyteI.OPEN, this.max(new int[] {this.booleanNetwork[0]}));
	}

	/**
	 * Disabled.
	 */
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
}
