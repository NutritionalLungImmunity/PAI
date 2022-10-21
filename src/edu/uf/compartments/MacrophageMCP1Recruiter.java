package edu.uf.compartments;

import edu.uf.interactable.MCP1;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.Macrophage;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class MacrophageMCP1Recruiter extends MacrophageRecruiter{

	public int getQtty() {
    	return getQtty(MCP1.getMolecule().getTotalMolecule(0));
    }
    
    public int getQtty(Quadrant q) { 
    	return getQtty(q.chemokines.get(MCP1.getMolecule().getName()));
    }

    protected int getQtty(double chemokine) {
    	//System.out.println(MIP1B.getMolecule().getTotalMolecule(0));
        double avg = Constants.SPACE_VOL * Constants.RECRUITMENT_RATE * chemokine *
              (1 - (Macrophage.getTotalCells()) / Constants.MAX_MA) / Constants.Kd_MCP1;
        
        //System.out.println(MIP1B.total_molecule[0]);
        if (avg > 0)
            return Rand.getRand().randpois(avg); 
        else {
            if (Macrophage.getTotalCells() < Constants.MIN_MA) 
                return Rand.getRand().randpois(1); 
            return 0;
        }
    }
	
}
