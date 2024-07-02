package edu.uf.interactable.klebsiela;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.Siderophore;
import edu.uf.interactable.TLRBinder;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.primitives.Interactions;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;

public class Klebsiella extends InfectiousAgent implements Internalizable{
	
	public static final int FREE = 0;
	public static final int INTERNALIZING = 1;
	public static final String NAME = "Klebsiela";
	
	public static final TLRBinder LPS = new TLRBinder();
	
	//private static final int interactionId = Id.getId();
	
	private static int totalCells = 0;
	
	private IntracellularModel model;
	
	private Clock growthClock;
	private static double totalIron;
	private double ironPool;
	private boolean engulfed;
	private boolean entrapped;
	
	private Set<Siderophore> hasSiderophore;
	
	private boolean aerobactin;
	private boolean enterobactin;
	private boolean salmochelin;
	private boolean yersiniabactin;
	private boolean cps;
	
	public Klebsiella(IntracellularModel model, double ironPool, boolean cps, boolean aerobactin, boolean enterobactin, boolean salmochelin, boolean yersiniabactin) {
		super(model);
		this.model = model;
		totalCells++;
		this.growthClock = new Clock((int) Constants.INV_UNIT_T); //PUT GROWTH RATE
		this.ironPool = ironPool;
		Klebsiella.totalIron = Klebsiella.totalIron + ironPool;
		this.engulfed = false;
		this.entrapped = false;
		
		this.hasSiderophore  = new HashSet<>();
		if(yersiniabactin) this.hasSiderophore.add(Yersiniabactin.getMolecule());
		if(aerobactin) this.hasSiderophore.add(Aerobactin.getMolecule());
		if(enterobactin) this.hasSiderophore.add(Enterobactin.getMolecule());
		if(salmochelin) this.hasSiderophore.add(Salmochelin.getMolecule());
		
		this.aerobactin = aerobactin;
		this.enterobactin = enterobactin;
		this.salmochelin = salmochelin;
		this.yersiniabactin = yersiniabactin;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}
	
	public double getIronPool() {
		return ironPool;
	}
	
	public boolean isInternalizing() {
        return this.getBooleanNetwork().hasPhenotype(Klebsiella.INTERNALIZING);
	}

	@Override
	public int getInteractionId() {
		return -1;
		//return interactionId;
	}
	
	/**
	 * Set the bacteria to the entrapped state. Bacteria can become entrapped by NETs, and that prevents their movement. 
	 * @param entrapped
	 */
	public void setEntrapped(boolean entrapped) {
		this.entrapped = entrapped;
	}
	
	/**
	 * Returns true if the bacteria is encapsulated. The encapsulated status is defined
	 *  a-priory during simulation initialization, and it affects (or should) bacterial virulence. 
	 * @return
	 */
	public boolean isEncapsulated() {
		return this.cps;
	}
	
	public boolean hasSiderophore(Siderophore siderophore) {
		return this.hasSiderophore.contains(siderophore);
	}

	@Override
	public void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte) {
		Voxel[][][] grid = GridFactory.getGrid();
		if(this.growthClock.toc()) {
			grid[x][y][z].setCell(new Klebsiella(
					this.model,
					this.getIronPool()/2.0, 
					this.isEncapsulated(),
					this.aerobactin, 
					this.enterobactin,
					this.salmochelin, 
					this.yersiniabactin
				));
			this.setIronPool(this.getIronPool()/2.0);
		}
		
	}
	
	/*@Override
	public void updateStatus(int x, int y, int z) {
		super.updateStatus(x, y, z);
		this.growthClock.tic();
		if(this.getState() == Afumigatus.INTERNALIZING)
            this.setState(Afumigatus.FREE);
	}*/

	@Override
	public void move(Voxel oldVoxel, int steps) {
		if(!engulfed && !entrapped && Rand.getRand().randunif() < 0.01) { //REVIEW HARD-CODE
			List<Voxel> neighbors = oldVoxel.getNeighbors();
			int numNeighbors = neighbors.size();
			int i = Rand.getRand().randunif(0, numNeighbors);
			oldVoxel.removeCell(this.getId());
			neighbors.get(i).setCell(this);
		}
		this.entrapped = false;
	}

	@Override
	public void die() {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, Cell.DEAD);
            Klebsiella.totalCells--;
        }
    }

	@Override
	public void incIronPool(double qtty) {
		this.setIronPool(this.getIronPool() + qtty);
		Klebsiella.totalIron = Klebsiella.totalIron + qtty;
	}

	@Override
	/**
     * Disabled.
     */
	public int getMaxMoveSteps() {
		return -1;
	}

	@Override
	public boolean isTime() {
		return this.getClock().toc();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if(interactable instanceof Macrophage) {
			Interactions.intKlebsiela((Macrophage)interactable, this);
			return true;
		}
		if(interactable instanceof Neutrophil) {
			if(this.isEncapsulated()) {
				((Neutrophil)interactable).bind(LPS, 4);
				return true;
			}
			Interactions.intKlebsiela((Neutrophil)interactable, this);
			return true;
		}
		if(interactable instanceof PneumocyteII) {
			((PneumocyteII)interactable).bind(LPS, 4);
			return true;
		}
		
		return interactable.interact(this, x, y, z);
	}
	

	@Override
	public boolean isSecretingSiderophore(Siderophore mol) {
		return true;
	}

	@Override
	public boolean isUptakingSiderophore(Siderophore mol) {
		if(!this.isDead() && mol.hasSiderophore()) 
    		return true;
    	return false;
	}

}
