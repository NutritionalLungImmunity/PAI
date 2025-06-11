package edu.uf.interactable;

import java.util.HashMap;
import java.util.Map;

import edu.uf.intracellularState.IntracellularModel;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;

public abstract class Cell extends Interactable{
	
	private static Map<Integer, Cell> cells;
	
	static {
		cells = new HashMap<>();
	}
	
	
	
	private int id;
    
    private double ironPool;
    //private int status;
    private boolean engulfed;
    protected Clock clock;
    protected IntracellularModel intracellularModel;
    private int externalState;
    
    
    public Cell(IntracellularModel intracellularModel) {
    	this.intracellularModel = intracellularModel;
    	this.clock = new Clock((int) Constants.INV_UNIT_T);
    	this.id = Id.getId(); 
    	this.externalState = 0;
    	Cell.cells.put(this.id, this);
    }
    
    public static void remove(int id) {
    	Cell.cells.remove(id);
    }
    
    public static Cell get(int id) {
    	return Cell.cells.get(id);
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
	 * Part of the binding mechanism.  
	 * 
	 * This method uses a {@code Binder} object to activate its corresponding receptor 
	 * in the {@code intracellularModel} at the specified {@code level}. The 
	 * {@code intracellularModel} selects the appropriate receptor based on the 
	 * {@code interactionId} of the given {@code Binder}.
	 * 
	 * This method handles interactions between cells and ligands that lead to receptor activation, 
	 * such as those triggered by cytokines, PAMPs, DAMPs, and other signaling molecules.
	 * 
	 * <br/><br/>
	 * All {@code Interactable} objects implement {@code Binder}; therefore, all cells and molecules 
	 * are {@code Binder} instances.
	 * 
	 * <p>See also:</p>
	 * <ul>
	 *   <li>{@code IntracellularModel.activateReceptor()}</li>
	 *   <li>{@code IntracellularModel.input()}</li>
	 *   <li>{@code Binder.getInteractionId()}</li>
	 * </ul>
	 *
	 * @param iter the binding (ligand) element—usually, but not always, a molecule
	 * @param level the strength of the interaction signal (0–4), where 0 represents no binding
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
	
    
    //REVIEW  THIS!!!
    /**
     * <strong>Note:</strong> This method may not accurately determine whether the cell is dead 
     * and should be reviewed. It is not used by the garbage collection system in the {@code Exec} class.
     *
     * @return {@code true} if the cell is considered dead; {@code false} otherwise
     */
	public boolean isDead() {
		return this.intracellularModel.isDead();
	}

	/**
	 * Part of a quintet of cell methods invoked by {@code Voxel} objects 
	 * (see also {@code grow()}, {@code kill()}, {@code move()} and {@code interact()}).
	 * 
	 * This method updates the internal clock of the cell. If the clock indicates that it is time 
	 * to act, it triggers the execution of the Boolean network and updates the cell’s status accordingly.
	 * 
	 * <br/><br/>
	 * The clock triggers once every {@code INV_UNIT_T} time steps.
	 *
	 * @param x the x-axis position in the grid
	 * @param y the y-axis position in the grid
	 * @param z the z-axis position in the grid
	 */
    public void updateStatus(int x, int y, int z) {
    	this.clock.tic();
    	if(!this.getClock().toc())return;
    	this.intracellularModel.processBooleanNetwork();
    	this.intracellularModel.updateStatus(this.getId(), x, y, z);
    	//if(this.intracellularModel.isDead())
    	//	this.die();
    }
    
    /**
     * Called by the garbage collection system in the {@code Exec} class, 
     * this method determines whether a dead cell should be removed from the simulator. 
     * 
     * The cell object is removed only if it is dead and this method returns {@code true}.
     * By default, this implementation always returns {@code true}, but it can be 
     * overridden in subclasses to provide custom behavior.
     *
     * @return {@code true} if the dead cell should be removed from the simulator; {@code false} otherwise
     */
    public boolean removeUponDeath() {
    	return true;
    }

    /**
     * Part of a quintet of cell methods invoked by {@code Voxel} objects 
     * (see also {@code grow()}, {@code kill()}, {@code updateStatus()}, and {@code interact()}).
     * 
     * This method moves the cell from the voxel at position ({@code x}, {@code y}, {@code z}) 
     * to a new voxel. If the cell contains internal elements (e.g., a phagocytosed bacterium), 
     * those are moved along with the cell.
     *
     * @param x the x-axis position in the grid
     * @param y the y-axis position in the grid
     * @param z the z-axis position in the grid
     * @param steps a counter indicating how many voxels the cell has traversed
     */
    public abstract void move(int x, int y, int z, int steps);

    /**
     * Sets the life status of the {@code intracellularModel} to {@code DEAD} if it is not already, 
     * and decrements the static cell counter associated with this cell type.
     * 
     * The cell counter is a static variable that tracks the number of cells of a specific kind.
     */
    public abstract void die();
    
    public abstract void incIronPool(double ironPool);
    
    /**
     * Returns the maximum number of voxels that a cell can traverse in a single iteration.
     *
     * @return the maximum number of voxels a cell can move per iteration
     */
    public abstract int getMaxMoveSteps();

    /**
     * Returns the name of the chemokine that chemoattracts the cell. 
     * For example, {@code "MIP2"} chemoattracts neutrophils.
     * 
     * This method is used during cell movement to calculate chemoattraction. 
     * By default, it returns {@code null}, meaning the cell will move randomly 
     * without bias. If overridden to return a specific chemokine name, the cell 
     * will move with a directional bias toward the gradient of that chemokine.
     *
     * @return the name of the chemokine that attracts the cell, or {@code null} if none
     */
    public String attractedBy() {
        return null;
    }
	
}
