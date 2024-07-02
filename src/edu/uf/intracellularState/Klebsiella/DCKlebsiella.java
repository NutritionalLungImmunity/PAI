package edu.uf.intracellularState.Klebsiella;

import edu.uf.interactable.Cell;
import edu.uf.interactable.DC;
import edu.uf.interactable.IL1;
import edu.uf.interactable.IL17;
import edu.uf.interactable.IL23;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.TLRBinder;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class DCKlebsiella extends IntracellularModel{

	
	public static final String name = "DCKlebsiella";
	
	public static final int NIL23 = 0;
	public static final int NIFN = 1;
	public static final int NIL1  =2;
	
	@Override
	public void processBooleanNetwork(int... args) {
		this.booleanNetwork[NIL23] = input(TLRBinder.getBinder());
		this.booleanNetwork[NIFN] = input(TLRBinder.getBinder());
		this.booleanNetwork[NIL1] = input(TLRBinder.getBinder());
		
		for(int i = 0; i < NUM_RECEPTORS; i++)
			this.inputs[i] = 0;
		
		this.clearPhenotype();
		
		
		this.clearPhenotype();
		
	}

	protected void computePhenotype() {
		if(this.booleanNetwork[NIL23] > 0)
			this.getPhenotype().put(IL17.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NIL23]}));
		if(this.booleanNetwork[NIFN] > 0)
			this.getPhenotype().put(IL23.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NIFN]}));
		if(this.booleanNetwork[NIL1] > 0)
			this.getPhenotype().put(IL1.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NIL1]}));
	}

	@Override
	public void updateStatus(int id, int x, int y, int z) {
		DC mac = (DC) Cell.get(id);
		if(this.getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD)
            return;
        if(mac.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.NECROTIC) {
        	mac.die();
            //for(InfectiousAgent entry : this.getPhagosome()) //WARNING-COMMENT
            //	entry.setState(Afumigatus.RELEASING);
        }else if(mac.getPhagosome().size() > Constants.MA_MAX_CONIDIA)
        	mac.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, IntracellularModel.NECROTIC);
        
        if(Rand.getRand().randunif() < Constants.MA_HALF_LIFE && mac.getPhagosome().size() == 0 &&
        		Macrophage.getTotalCells() > Constants.MIN_MA) 
        	mac.die();
        //this.setMoveStep(0);
        mac.setMaxMoveStep(-1);
		
	}
	
}
