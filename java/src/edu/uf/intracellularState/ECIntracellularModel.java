package edu.uf.intracellularState;

import edu.uf.interactable.Cell;
import edu.uf.interactable.IL4;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.TLRBinder;

public class ECIntracellularModel extends IntracellularModel{

	private int booleanNetwork; //This is usually an array.
	
	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork = getInput(TLRBinder.getBinder());
		
		this.inputs.clear();
		this.clearPhenotype();
		this.computePhenotype();
		
	}

	@Override
	public void updateStatus(int id, int x, int y, int z) {
		Cell cell = Cell.get(id);
		if(cell.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.APOPTOTIC)
			cell.die();
	}

	@Override
	protected void computePhenotype() {
		if(booleanNetwork > 0) this.getPhenotype().put(MIP2.getMolecule().getPhenotype(), booleanNetwork);
		if(booleanNetwork > 0) this.getPhenotype().put(IL4.getMolecule().getPhenotype(), booleanNetwork);
	}

}
