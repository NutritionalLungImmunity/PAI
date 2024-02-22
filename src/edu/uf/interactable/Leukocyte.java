package edu.uf.interactable;

import java.util.ArrayList;
import java.util.List;

import edu.uf.compartments.Voxel;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public abstract class Leukocyte extends Cell{

    /*public static final int INACTIVE = 0;
    public static final int INACTIVATING = 1;
    public static final int RESTING = 2;
    public static final int ACTIVATING = 3;
    public static final int ACTIVE = 4;
    public static final int APOPTOTIC = 5;
    public static final int NECROTIC = 6;
    public static final int DEAD = 7;
    public static final int ANERGIC = 8;*/

    public static final int FREE = 0;
    public static final int INTERACTING = 1;

    
    
    //private Phagosome phagosome;
    private List<InfectiousAgent> phagosome;
    
    private boolean engaged;
    
    private int state;

	public Leukocyte(BooleanNetwork network) {
    	this(0.0, network);
    }
    
    public Leukocyte (double ironPool, BooleanNetwork network) {
    	super(network);
        this.setIronPool(ironPool);
        //this.setStatusIteration(0);
        //this.setStateIteration(0);
        //this.phagosome = new Phagosome();
        this.phagosome = new ArrayList<>();
        
        this.state = FREE;
    }
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setPhagosome(List<InfectiousAgent> phagosome) {
		this.phagosome = phagosome;
	}

    public abstract int getMaxCell();
    

    public List<InfectiousAgent> getPhagosome() {
        return this.phagosome; 
    }

	public boolean isEngaged() {
		return engaged; 
	}

	public void setEngaged(boolean engaged) {
		this.engaged = engaged;
	}

    public void addAspergillus(Afumigatus aspergillus) {
        this.phagosome.add(aspergillus);
    }

    public void kill() {
        if(Rand.getRand().randunif() < Constants.PR_KILL) {
        	for(InfectiousAgent a : this.phagosome) {
                this.incIronPool(a.getIronPool());
                a.incIronPool(-a.getIronPool());
                a.die();
        	}
            this.phagosome.clear();
        }
    }

    public void move(Voxel oldVoxel, int steps) {
        if(steps < this.getMaxMoveSteps()) {
        	
        	Util.calcDriftProbability(oldVoxel, this);
            Voxel newVoxel = Util.getVoxel(oldVoxel, Rand.getRand().randunif());
        	
            oldVoxel.removeCell(this.getId());
            //System.out.println(oldVoxel + " - " + newVoxel + " " + this);
            newVoxel.setCell(this);
            steps = steps + 1;
            PositionalAgent p = null;
            for(InfectiousAgent a : this.phagosome) {
            	if(a instanceof PositionalAgent) {
            		p = (PositionalAgent) a;
            		p.setX(newVoxel.getX() + Rand.getRand().randunif());
            		p.setY(newVoxel.getY() + Rand.getRand().randunif());
            		p.setZ(newVoxel.getZ() + Rand.getRand().randunif());
            	}
            }
            move(newVoxel, steps);
        }
    }
    
	
}
