package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.Cell;
import edu.uf.interactable.IFN_I;
import edu.uf.interactable.IFN_II;
import edu.uf.interactable.klebsiela.Klebsiella;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.IntracellularModel;

public class NKKlebsiella extends IntracellularModel{

	
	public static final String name = "NKKlebsiella";
	
	public NKKlebsiella() {
		super();
		this.setState(IntracellularModel.LOCATION, Klebsiella.FREE);
	}

	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[0] = input(IFN_I.getMolecule());
		
		for(int i = 0; i < NUM_RECEPTORS; i++)
			this.inputs[i] = 0;
		
		this.clearPhenotype();
		
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[0] > 0) {
			this.getPhenotype().put(IFN_II.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[0]}));
		}
	}

	/**
	 * Disabled.
	 */
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
}
