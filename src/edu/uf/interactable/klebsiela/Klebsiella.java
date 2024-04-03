package edu.uf.interactable.klebsiela;

import java.util.List;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.TLRBinder;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

public class Klebsiella extends InfectiousAgent implements Internalizable{
	
	public static final int FREE = 0;
	public static final int INTERNALIZING = 1;
	public static final String NAME = "Klebsiela";
	
	public static final TLRBinder LPS = new TLRBinder();
	
	//private static final int interactionId = Id.getId();
	
	private static int totalCells = 0;
	
	private Clock growthClock;
	private static double totalIron;
	private double ironPool;
	private boolean engulfed;
	private boolean entraped;
	
	public Klebsiella(double ironPool) {
		super(null);
		totalCells++;
		this.growthClock = new Clock((int) Constants.INV_UNIT_T); //PUT GROWTH RATE
		this.ironPool = ironPool;
		Klebsiella.totalIron = Klebsiella.totalIron + ironPool;
		this.engulfed = false;
		this.entraped = false;
	}
	
	public static int getTotalCells() {
		return totalCells;
	}
	
	public double getIronPool() {
		return ironPool;
	}
	
	public boolean isInternalizing() {
        return this.getState() == Klebsiella.INTERNALIZING;
	}

	@Override
	public int getInteractionId() {
		return -1;
		//return interactionId;
	}
	
	public void setEntraped(boolean entraped) {
		this.entraped = entraped;
	}

	@Override
	public void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte) {
		Voxel[][][] grid = GridFactory.getGrid();
		if(this.growthClock.toc()) {
			grid[x][y][z].setCell(new Klebsiella(this.getIronPool()/2.0));
			this.setIronPool(this.getIronPool()/2.0);
		}
		
	}
	
	@Override
	public void updateStatus(int x, int y, int z) {
		super.updateStatus(x, y, z);
		this.growthClock.tic();
		if(this.getState() == Afumigatus.INTERNALIZING)
            this.setState(Afumigatus.FREE);
	}

	@Override
	public void move(Voxel oldVoxel, int steps) {
		if(!engulfed && !entraped && Rand.getRand().randunif() < 0.01) { //REVIEW HARD-CODE
			List<Voxel> neighbors = oldVoxel.getNeighbors();
			int numNeighbors = neighbors.size();
			int i = Rand.getRand().randunif(0, numNeighbors);
			oldVoxel.removeCell(this.getId());
			neighbors.get(i).setCell(this);
		}
		this.entraped = false;
	}

	@Override
	public void die() {
		if(this.getStatus() != Klebsiella.DEAD) {
            this.setStatus(Klebsiella.DEAD);
            Klebsiella.totalCells--;
        }
	}

	@Override
	public void incIronPool(double qtty) {
		this.setIronPool(this.getIronPool() + qtty);
		Klebsiella.totalIron = Klebsiella.totalIron + qtty;
	}

	@Override
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
			intKlebsiela((Macrophage)interactable, this);
			return true;
		}
		if(interactable instanceof Neutrophil) {
			intKlebsiela((Neutrophil)interactable, this);
			return true;
		}
		if(interactable instanceof PneumocyteII) {
			((PneumocyteII)interactable).bind(LPS, 4);
			return true;
		}
		
		return interactable.interact(this, x, y, z);
	}
	
	
	public static void intKlebsiela(Leukocyte phagocyte, Klebsiella klebsiela) {
        if(klebsiela.getState() == Klebsiella.FREE) {
            if (!phagocyte.isDead()) {
            	if(phagocyte.getPhagosome().size() < phagocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
            		klebsiela.setState(Klebsiella.INTERNALIZING);
            		klebsiela.setEngulfed(true);
                    phagocyte.getPhagosome().add(klebsiela);
                }
            }
            phagocyte.setState(Leukocyte.INTERACTING);
            if(phagocyte instanceof Macrophage) {
            	phagocyte.bind(LPS, 4);
            }
        }
    }

}
