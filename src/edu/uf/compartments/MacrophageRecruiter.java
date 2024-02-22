package edu.uf.compartments;

import java.util.Map;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Macrophage;
import edu.uf.intracellularState.MacrophageFactory;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;
import edu.uf.interactable.MIP1B;
import edu.uf.interactable.MIP2;

public class MacrophageRecruiter extends Recruiter{

    public Cell createCell() { 
        return new Macrophage(Constants.MA_INTERNAL_IRON, MacrophageFactory.createBooleanNetwork());
    }
    
    public int getQtty() {
    	return getQtty(MIP1B.getMolecule().getTotalMolecule(0));
    }

    protected int getQtty(double chemokine) {
    	//System.out.println(MIP1B.getMolecule().getTotalMolecule(0));
        double avg = Constants.SPACE_VOL * Constants.RECRUITMENT_RATE_MA * chemokine *
              (1 - (Macrophage.getTotalCells()) / Constants.MAX_MA) / Constants.Kd_MIP1B;
        
        //System.out.println(MIP1B.total_molecule[0]);
        if (avg > 0)
            return Rand.getRand().randpois(avg); 
        else {
            if (Macrophage.getTotalCells() < Constants.MIN_MA) 
                return Rand.getRand().randpois(1); 
            return 0;
        }
    }

    public boolean leave() {
        return true; 
    }

    public Cell getCell(Voxel voxel) {
        if (voxel.getCells().size() == 0)
            return null;
        for(Map.Entry<Integer, Cell> entry : voxel.getCells().entrySet())
        	if (entry.getValue() instanceof Macrophage)
        		return entry.getValue();
        return null;
    }
	
}
