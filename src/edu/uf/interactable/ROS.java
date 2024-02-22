package edu.uf.interactable;

import edu.uf.Diffusion.Diffuse;
import edu.uf.utils.Constants;
import edu.uf.utils.Util;

public class ROS extends Molecule{

	public static final String NAME = "ROS";
	public static final int NUM_STATES = 1;
	
	private static ROS molecule = null;
    
    protected ROS(double[][][][] qttys, Diffuse diffuse, int[] phenotypes) {
		super(qttys, diffuse, phenotypes);
	}
    
    public static ROS getMolecule(double[][][][] values, Diffuse diffuse, int[] phenotypes) {
    	if(molecule == null) {
    		molecule = new ROS(values, diffuse, phenotypes);
    	}
    	return molecule;
    }
    
    public static ROS getMolecule() {
    	return molecule;
    }
    
    public void turnOver(int x, int y, int z) {
    	this.pdec(1-Constants.H2O2_HALF_LIFE, 0, x, y, z);
    }
    
    public void degrade() {
    	degrade(Constants.H2O2_HALF_LIFE, 0);
    }

    public int getIndex(String str) {
        return 0;
    }

    public void computeTotalMolecule(int x, int y, int z) {
    	this.totalMoleculesAux[0] = this.totalMoleculesAux[0] + this.get(0, x, y, z);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if(interactable instanceof PneumocyteII) {
        	PneumocyteII cell = (PneumocyteII) interactable;
	        if (Util.activationFunction(this.get(0, x, y, z)*this.get(0, x, y, z), Constants.Kd_H2O2*Constants.Kd_H2O2, Constants.VOXEL_VOL*Constants.VOXEL_VOL)) {
	        	//System.out.println(this.get(0, x, y, z)/Constants.VOXEL_VOL + " " + Constants.Kd_H2O2);
	        	cell.getBooleanNetwork().getPhenotype().put(PneumocyteII.APOPTOTIC, 4);
	        }
	        return true;
        }
        if(interactable instanceof Neutrophil) {
        	Neutrophil cell = (Neutrophil) interactable;
	        if (cell.hasPhenotype(this.getPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.H2O2_QTTY, 0, x, y, z);
	        return true;
        }
        if(interactable instanceof Macrophage) {
        	Macrophage cell = (Macrophage) interactable;
	        if (cell.hasPhenotype(this.getPhenotype()))//# and interactable.state == Neutrophil.INTERACTING:
        		this.inc(Constants.H2O2_QTTY, 0, x, y, z);
	        return true;
        }
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getThreshold() {
		return -1;
	}

	@Override
	public int getNumState() {
		return NUM_STATES;
	}
	
	@Override
	public boolean isTime() {
		return true;
	}
	
}
