package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.IL17;
import edu.uf.intracellularState.IntracellularModel;

public class GDTKlebsiella extends IntracellularModel{
	
	public static final String name = "GDTKlebsiella";

	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[0] = getInput(IL17.getMolecule());
		
		this.inputs.clear();
		
		this.clearPhenotype();
		
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[0] > 0)
			this.getPhenotype().put(IL17.IL17, this.max(new int[] {this.booleanNetwork[0]}));
	}

	/**
	 * Disabled.
	 */
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

}
