package edu.uf.interactable;

import edu.uf.compartments.GridFactory;
import edu.uf.primitives.Interactions;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;

/**
 * Blood is a Molecule-like cell. It's a singleton and it works like a molecule. It is implemented as a cell because I needed the "updateStatus" method.
 * @author henriquedeassis
 *
 */
public class Blood extends Cell{
	
	public static final int RESTING = 0;
	public static final int HEMORRHAGIC = 1;
	public static final int FILLED = 2;
	public static final int COAGULATED = 3;
	
	//public static final int TREATED = 3;
	
	private String NAME = "BLOOD";
	private static int interactionId = Id.getMoleculeId();
	
	private int[][][] status;
	
	private static Blood hemorrhage = null;
	
	private boolean[][][] txa;
	private int[][][] iteration;
	
	private Blood(int xbin, int ybin, int zbin) {
		super(null);
		this.txa = new boolean[xbin][ybin][zbin];
		this.status = new int[xbin][ybin][zbin];
		this.iteration = new int[xbin][ybin][zbin];

        for(int x = 0; x < xbin; x++) {
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++)
        			this.status[x][y][z] = 0;
        }
	}
	
	/**
	 * Ad-hoc method to set Tranexamic acid in the Blood in the Voxel at position (x, y, z).
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTxa(int x, int y, int z) {
		this.txa[x][y][z] = true;
	}
	
	public static Blood getBlood(int xbin, int ybin, int zbin) {
		if(hemorrhage == null)
			hemorrhage = new Blood(xbin, ybin, zbin);
		return hemorrhage;
	}
	
	public static Blood getBlood() {
		return hemorrhage;
	}

	/**
	 * Returns the Blood array. This is a Molecule-like array.
	 * @return
	 */
	public int[][][] getHemorrhageStatus(){
		return status;
	}
	
	/**
	 * Returns true if the status of Blot in the Voxel at position (x, y, z) is "HEMORRHAGIC" or "FILLED."
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public boolean hasBlood(int x, int y, int z) {
		return status[x][y][z] == HEMORRHAGIC || status[x][y][z] == FILLED;
	}
	
	/**
	 * Sets the blood status in the Voxel at position (x, y, z). 
	 * The status are "RESTING," "HEMORRHAGIC," "COAGULATED," and "FILLED."
	 */
	public void setStatus(int status, int x, int y, int z) {
		this.status[x][y][z] = status;
	}
	
	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if (interactable instanceof PneumocyteI)
    		return Interactions.setHemorrhage(this, (Cell) interactable, HEMORRHAGIC, x, y, z);
		
		return interactable.interact(this, x, y, z);
	}
	
	/**
	 * Updates the blood status in the Voxel at position (x, y, z). 
	 * The status are "RESTING," "HEMORRHAGIC," "COAGULATED," and "FILLED."
	 */
	@Override
	public void updateStatus(int x, int y, int z) {
		int xbin = GridFactory.getXbin();
		int ybin = GridFactory.getYbin();
		int zbin = GridFactory.getZbin();
		
		//System.out.println(this.txa);
		if(this.txa[x][y][z]) {
			this.iteration[x][y][z]++;
			if(this.iteration[x][y][z] >= Constants.TRANEXAMIC_ACID_LIFE_SPAN) {
				this.txa[x][y][z] = false;
				this.iteration[x][y][z] = 0;
			}
		}
		
    	if(status[x][y][z] == HEMORRHAGIC)// {}
    		status[x][y][z] = COAGULATED;
    	/*if(status[x][y][z] == COAGULATED && Rand.getRand().randunif() < Util.tranexamicActivation(
    			this.txaQtty, Constants.TRANEXAMIC_ACID_Kd, Constants.VOXEL_VOL, Constants.PR_COAGULUM_BREAK
    	))*/
    	//if(status[x][y][z] == COAGULATED && this.txaQtty > Constants.TRANEXAMIC_ACID_THRESHOLD)
    	if(status[x][y][z] == COAGULATED && !txa[x][y][z])  //REVIEW TXA (is txa or !txa)
    		status[x][y][z] = HEMORRHAGIC;
    
    	if(status[x][y][z] == HEMORRHAGIC) {
    		
	    	final int k = Rand.getRand().randunif(0, 6);
	    	
	    	switch(k) {
		    	case 0:
		    		x = (x+1+xbin)%xbin;
		    		break;
		    	case 1: 
		    		z = (z+1+zbin)%zbin;
		    		break;
		    	case 2:
		    		x = (x-1+xbin)%xbin;
		    		break;
		    	case 3:
		    		z = (z-1+zbin)%zbin;
		    		break;
		    	case 4:
		    		y = (y+1+ybin)%ybin;
		    		break;
		    	case 5:
		    		y = (y-1+ybin)%ybin;
		    		break;
		    	default:
		    		System.err.println("No such neighbor "  + k);
		    		System.exit(1);	
	    	}
	    	status[x][y][z] = FILLED;
    	}
	}
	

	@Override
	public int getInteractionId() {
		return interactionId;
	}

	@Override
	public void move(int x, int y, int z, int steps) {}

	/**
	 * Disabled.
	 */
	@Override
	public void die() {}

	/**
	 * Disabled.
	 */
	@Override
	public void incIronPool(double ironPool) {}

	/**
	 * Disabled.
	 */
	@Override
	public int getMaxMoveSteps() {return -1;}

	/**
	 * Disabled.
	 */
	@Override
	public boolean isTime() {return true;}

	@Override
	public String getName() {
		return NAME;
	}

}
