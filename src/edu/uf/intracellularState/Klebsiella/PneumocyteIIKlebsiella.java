package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.BDefensin;
import edu.uf.interactable.IFN_III;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL17;
import edu.uf.interactable.Lipocalin2;
import edu.uf.interactable.TLRBinder;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.IntracellularModel;

public class PneumocyteIIKlebsiella extends IntracellularModel{
	
	public static final String name = "PneumocyteIIKlebsiella";

	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[0] = getInput(TLRBinder.getBinder());
		this.booleanNetwork[1] = max(new int[] {getInput(IL1.getMolecule()), getInput(IL17.getMolecule()), getInput(TNFa.getMolecule())});
		this.booleanNetwork[2] = getInput(IL1.getMolecule());
		
		this.inputs.clear();
		
		this.clearPhenotype();
		
		this.computePhenotype();
		
	}
	
	protected void computePhenotype() {
		if(this.booleanNetwork[0] > 0) 
			this.getPhenotype().put(IFN_III.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[0]}));
		if(this.booleanNetwork[1] > 0) 
			this.getPhenotype().put(Lipocalin2.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[1]}));
		if(this.booleanNetwork[2] > 0) 
			this.getPhenotype().put(BDefensin.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[2]}));
	}

	/**
	 * Disabled.
	 */
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
	
}
