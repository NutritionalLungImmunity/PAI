package edu.uf.compartments;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Neutrophil;
import edu.uf.intracellularState.NeutrophilFactory;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;
import edu.uf.interactable.MIP2;
import edu.uf.interactable.Macrophage;

public class NeutrophilRecruiter extends Recruiter{
	
    public Cell createCell() {
        return new Neutrophil(0, NeutrophilFactory.createBooleanNetwork()); 
    }
    
    public int getQtty() {
    	return getQtty(MIP2.getMolecule().getTotalMolecule(0));
    }
    
    protected int getQtty(double chemokine) {
        double avg = Constants.RECRUITMENT_RATE_N * Constants.SPACE_VOL * chemokine *
              (1 - (Neutrophil.getTotalCells()) / Constants.MAX_N) / Constants.Kd_MIP2;
        if (avg > 0)
            return Rand.getRand().randpois(avg); 
        else {
            if (Neutrophil.getTotalCells() < Constants.MIN_MA) {
                return Rand.getRand().randpois(1); 
            }
            return 0;
        }
        
        /*if (avg > 0)
            return Rand.getRand().randpois(avg);
        else
            return 0;*/
    }

    public boolean leave() {
        return false;
    }

    public Cell getCell(Voxel voxel) {
        return null;
    }
}
