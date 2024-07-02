package edu.uf.intracellularState;

import edu.uf.interactable.Cell;
import edu.uf.interactable.IL10;
import edu.uf.interactable.IL6;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class AspergillusMacrophage extends FMacrophageBooleanNetwork{
	
	public static final int M1 = Phenotype.createPhenotype();
	public static final int M2B = Phenotype.createPhenotype();
	public static final int M2C = Phenotype.createPhenotype();
	public static final int _FPN = Phenotype.createPhenotype();
	
	public static final String name = "AspergillusMacrophageBooleanNetwork";

	@Override
	protected void computePhenotype() {
		if(this.booleanNetwork[NFkB] > 0 || this.booleanNetwork[STAT1] > 0 || this.booleanNetwork[STAT5] > 0) {
			this.getPhenotype().put(IL6.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(TNFa.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(MIP1B.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(MIP2.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(M1, this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
		}
		//if(this.booleanNetwork[STAT6] > 0)
		//	this.getPhenotype().put(M2A, this.booleanNetwork[STAT6]);
		if(this.booleanNetwork[ERK] > 0) {
			this.getPhenotype().put(IL6.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(TNFa.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(M2B, this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
		}
		if(this.booleanNetwork[STAT3] > 0) {
			this.getPhenotype().put(TGFb.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(IL10.getMolecule().getPhenotype(), this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
			this.getPhenotype().put(M2C, this.max(new int[] {this.booleanNetwork[NFkB], this.booleanNetwork[STAT1], this.booleanNetwork[STAT5]}));
		}		
	}

	@Override
	public void updateStatus(int id, int x, int y, int z) {
		Macrophage mac = (Macrophage) Cell.get(id);
		//System.out.println(">> " + this.getState(IntracellularModel.LIFE_STATUS));
		if(this.getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD)
            return;
        if(mac.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.NECROTIC) {
        	mac.die();
            //for(InfectiousAgent entry : this.getPhagosome()) //WARNING-COMMENT
            //	entry.setState(Afumigatus.RELEASING);
        }else if(mac.getPhagosome().size() > Constants.MA_MAX_CONIDIA)
        	setState(IntracellularModel.LIFE_STATUS, IntracellularModel.NECROTIC);
        
        if(Rand.getRand().randunif() < Constants.MA_HALF_LIFE && mac.getPhagosome().size() == 0 &&
        		Macrophage.getTotalCells() > Constants.MIN_MA) 
        	mac.die();
        //this.setMoveStep(0);
        mac.setMaxMoveStep(-1);
		
	}

}
