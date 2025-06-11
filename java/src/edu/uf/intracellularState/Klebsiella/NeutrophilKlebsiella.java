package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.TLRBinder;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.NeutrophilNetwork;

public class NeutrophilKlebsiella extends IntracellularModel{
	
	public static final String name = "NeutrophilKlebsiella";

	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[0] = getInput(TLRBinder.getBinder());
		
		this.inputs.clear();
		
		this.clearPhenotype();
		
		
		this.computePhenotype();
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[0] > 0) {
			this.getPhenotype().put(NeutrophilNetwork.PYROPTOTIC, this.max(new int[] {this.booleanNetwork[0]}));
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
