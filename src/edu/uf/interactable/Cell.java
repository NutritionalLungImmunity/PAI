package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.Phenotype;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Cell extends Interactable{
	
	public static final int DEAD = Phenotype.createPhenotype();;
	public static final int ALIVE = Phenotype.createPhenotype();
	public static final int APOPTOTIC = Phenotype.createPhenotype();
	public static final int NECROTIC = Phenotype.createPhenotype();
	public static final int DYING = Phenotype.createPhenotype();
	
	private int id;
    
    private double ironPool;
    //private int status;
    private int state;
    private boolean engulfed;
    protected Clock clock;
    protected IntracellularModel intracellularModel;
    private int externalState;
    
    
    public Cell(IntracellularModel intracellularModel) {
    	this.intracellularModel = intracellularModel;
    	this.clock = new Clock((int) Constants.INV_UNIT_T);
    	this.id = Id.getId(); 
    	this.externalState = 0;
    }
    
    public void setExternalState(int state) {
    	this.externalState = state;
    }
    
    public int getExternalState() {
    	return this.externalState;
    }


	public double getIronPool() {
		return ironPool;
	}


	public void setIronPool(double ironPool) {
		this.ironPool = ironPool;
	}


	public boolean isEngulfed() {
		return engulfed;
	}


	public void setEngulfed(boolean engulfed) {
		this.engulfed = engulfed;
	}

	
	public int getId() {
    	return id;
    }

	
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * This method takes an object of the kind "Binder" and uses it to activate its receptor in the 
	 * "intracellularModel" object to the level "level." The "intracellularModel" object can select 
	 * the appropriate receptor for the "Binder" object given the "Binder" "interactionId." This method 
	 * is used to handle interaction between cell and ligands that leads to receptor activation, 
	 * such as cytokines, PAMS, DAMPS, and others. 
	 * <br/><br/>
	 * All "Interactable" objects are "Binder" therefore, all cells and molecules are "Binder."
	 * @param iter the binding or ligand element (usually, but not always, a molecule).
	 * @param level the strength of the interaction signal 1-4. (0 is also allowed, but it would be equivalent to no binding).
	 */
	public void bind(Binder iter, int level) {
		this.intracellularModel.activateReceptor(iter.getInteractionId(), level);
	}
	
	
	public Clock getClock() {
		return clock;
	}
	
	
    public IntracellularModel getBooleanNetwork() {
    	return this.intracellularModel;
    }

	/*protected void processBooleanNetwork(int... args) {
		this.intracellularModel.processBooleanNetwork(args);
    }*/
	
	/**
	 * <strong>This method may not really test if the cell is dead and should be reviewed. 
	 * This method is not used by the garbage collection system of the "Exec" class.</strong>
	 * @return
	 */
	public boolean isDead() {
		return this.intracellularModel.isDead();
	}

	/**
	 * This method belongs to a quartet of cell methods that are called by 
	 * the Voxels objects (see grow, kill, and interact). This method updates 
	 * the clock, and if the clock time is true, it runs the boolean network 
	 * and updates the status.
	 * <br/><br/>
	 * The clock time becomes true for one iteration every "INV_UNIT_T" time-steps.
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 */
    public void updateStatus(int x, int y, int z) {
    	this.clock.tic();
    	if(!this.getClock().toc())return;
    	this.intracellularModel.processBooleanNetwork();
    	this.intracellularModel.updateStatus(this, x, y, z);
    }
    
    /**
     * This method is called by the garbage collection system of the "Exec" class, 
     * which will remove the cell object if and only if the cell is dead and this 
     * method returns true. This method implementation always returns true by default, 
     * but the method can be reimplemented in the subclasses.
     * @return true if the the dead cell should be removed from the simulator. False otherwise.
     */
    public boolean removeUponDeath() {
    	return true;
    }

    /**
     * This method belongs to a quintet of cell methods that are called by the Voxels objects 
     * (see grow, kill, updateStatus, and interact). This method moves the cell from the current 
     * voxel "oldVoxel" to a new voxel. If the cell has internal content like a phagocytosed 
     * bacterium, the content is moved, too.
     * @param oldVoxel current voxel
     * @param steps a counter of how many voxels the cell has traversed.
     */
    public abstract void move(Voxel oldVoxel, int steps);

    /**
     * If the "intracellularModel" life status is not "DEAD," this method sets it to "DEAD" 
     * and decrements the cell counter. The cell counter is a static variable that counts 
     * the number of cells of one kind.
     */
    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    /**
     * This method gives the maximum number of voxels that a cell can traverse in a given iteration. 
     * @return
     */
    public abstract int getMaxMoveSteps();

    /**
     * This method returns the Chemokine "NAME" that chemoattracts the cell. For example, MIP2 
     * chemoattracts neutrophils. This method is used in the context of cell movement to compute 
     * chemoattraction. The default implementation of this method returns "null," in which case the 
     * cell will not have any chemoattraction and will move randomly. If this method is overwritten 
     * to return a chemokine name, then the cell will move with a bias toward that chemokine 
     * concentration gradient.
     * @return
     */
    public String attractedBy() {
        return null;
    }
	
}
