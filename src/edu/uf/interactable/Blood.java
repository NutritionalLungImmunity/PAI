package edu.uf.interactable;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class Blood extends Cell{
	
	public static final int RESTING = 0;
	public static final int HEMORRHAGIC = 1;
	public static final int FILLED = 2;
	public static final int COAGULATED = 3;
	
	//public static final int TREATED = 3;
	
	private String NAME = "HEMORRHAGE";
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

	public int[][][] getHemorrhageStatus(){
		return status;
	}
	
	public boolean hasBlood(int x, int y, int z) {
		return status[x][y][z] == HEMORRHAGIC || status[x][y][z] == FILLED;
	}
	
	@Override
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		if (interactable instanceof PneumocyteI){
    		PneumocyteI cell = (PneumocyteI) interactable;
    		if(cell.isDead())
    			//if(status[x][y][z] == RESTING)
    				status[x][y][z] = HEMORRHAGIC;
    		return true;
    	}
		/*if (interactable instanceof TranexamicAcid){
			TranexamicAcid mol = (TranexamicAcid) interactable;
			this.txaQtty = mol.get(0, x, y, z);
			//if(status[x][y][z]  == HEMORRHAGIC)
			//	if(Util.activationFunction(mol.get(0, x, y, z), Constants.TRANEXAMIC_ACID_Kd))
    		//		status[x][y][z] = TREATED;
    		return true;
    	}*/
		
		return interactable.interact(this, x, y, z);
	}
	
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
	public void move(Voxel oldVoxel, int steps) {}

	@Override
	public void die() {}

	@Override
	public void incIronPool(double ironPool) {}

	@Override
	public int getMaxMoveSteps() {return -1;}

	@Override
	public boolean isTime() {return true;}

	@Override
	public String getName() {
		return NAME;
	}

}
