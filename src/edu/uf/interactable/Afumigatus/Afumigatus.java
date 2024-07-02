package edu.uf.interactable.Afumigatus;

import java.util.Set;
import java.util.ArrayList;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.PositionalInfectiousAgent;
import edu.uf.interactable.Siderophore;
import edu.uf.intracellularState.AspergillusIntracellularModel;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.Phenotype;
import edu.uf.primitives.Interactions;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

import java.util.HashSet;

public class Afumigatus extends PositionalInfectiousAgent implements Internalizable{

    public static final String NAME = "Afumigatus";    
    
    public static final int RESTING_CONIDIA = Phenotype.createPhenotype();
    public static final int SWELLING_CONIDIA = Phenotype.createPhenotype();
    public static final int GERM_TUBE = Phenotype.createPhenotype();
    public static final int HYPHAE = Phenotype.createPhenotype();
    public static final int STERILE_CONIDIA = Phenotype.createPhenotype(); //PROBABLY NOT BEEN USED
    
    
    public static final int FREE = Phenotype.createPhenotype();
    public static final int INTERNALIZING = Phenotype.createPhenotype();
    public static final int RELEASING = Phenotype.createPhenotype();
    public static final int ENGAGED = Phenotype.createPhenotype();

    private static double totalIron = 0;
    private static int[] totalCells = new int[5];

    private static Set<Afumigatus> treeSepta = null;
    
    private Afumigatus nextSepta;
    private Afumigatus nextBranch;
    private boolean isRoot;
    private boolean growable;
    private boolean branchable;
    private int growthIteration;
    private int treeSize;
    
    private double xTip;
    private double yTip;
    private double zTip;
    private double dx;
    private double dy;
    private double dz;
    private double percentGrow;
    private double iterationsToGrow;
    private double epithelialInhibition;
    private double netGermBust;
    private boolean aspEpiInt;
    private boolean engaged;
    
    private double nitrogenPool;
    private double heme;
    
    private static int interactionId = Id.getMoleculeId();
    
    public static final Afumigatus DEF_OBJ = new Afumigatus();
    
    
    public Afumigatus() {
    	this(0,0,0, 0,0,0, Rand.getRand().randunif(), Rand.getRand().randunif(), Rand.getRand().randunif(),
    			0, 0, Afumigatus.RESTING_CONIDIA, true);
    }


    public Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, boolean isRoot) {
        super(xRoot, yRoot, zRoot, new AspergillusIntracellularModel(status));
        this.setIronPool(ironPool);
        this.isRoot = isRoot;
        this.xTip = xTip;
        this.yTip = yTip;
        this.zTip = zTip;
        double[] ds = new double[] {dx, dy, dz};
        LinAlg.multiply(ds, 1.0/LinAlg.norm2(ds));
        
        this.dx = ds[0];
        this.dy = ds[1];
        this.dz = ds[2];
        this.setEngulfed(false);

        //this.cfu = None

        this.growable = true;
        this.branchable = false;
        this.growthIteration = growthIteration;

        this.nextSepta = null;
        this.nextBranch = null;
        //this.Fe = false;
        
        Afumigatus.totalIron = Afumigatus.totalIron + ironPool;
        Afumigatus.totalCells[0]++;
        
        this.clock = new Clock((int) Constants.INV_UNIT_T, 0);
        this.iterationsToGrow = Constants.ITER_TO_GROW;
        
        this.nitrogenPool = 0.0;
        this.treeSize = 0;
        this.percentGrow = 1e-4;
        
        this.epithelialInhibition = 2.0;
        this.netGermBust = 1.0;
        this.aspEpiInt = true;
        this.engaged = false;
        
        this.heme = Constants.HEME_INIT_QTTY;
        
        int i = AspergillusIntracellularModel.hasLifeStage(status);
        
        if(i >= 0)Afumigatus.totalCells[i]++;
    }
    
    protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, boolean isRoot) {
    	return new Afumigatus(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                dx, dy, dy, growthIteration,
                ironPool, status, isRoot);
    }
    
    public boolean hasSiderophore(Siderophore siderophore) {
    	if(siderophore instanceof TAFC)
    		return true;
    	return false;
    }
    
    public static void decTotalCells(int idx) {
    	Afumigatus.totalCells[idx]--;
    }
    
    public static void incTotalCells(int idx) {
    	Afumigatus.totalCells[idx]++;
    }
    
    public boolean getAspEpiInt() {
    	return aspEpiInt;
    }
    
    public void setAspEpiInt(boolean b) {
    	this.aspEpiInt =  b;
    }
    
    public void setEngaged(boolean b) {
    	this.engaged =  b;
    }
    
    public boolean isEngaged() {
    	return this.engaged;
    }
    
    public int getInteractionId() {
    	return interactionId;
    }
    
    public boolean isTime() {
		return this.getClock().toc();
	}
    
    public double getIterationsToGrow() {
    	return this.iterationsToGrow;
    }
    
    public void setEpithelialInhibition(double inhibition) {
    	this.epithelialInhibition = inhibition;
    }
    
    public double getEpithelialInhibition() {
    	return this.epithelialInhibition;
    }
    
    public void setNetGermBust(double bust) {
    	this.netGermBust = bust;
    }
    
    public void incHeme(double qtty) {
    	this.heme += qtty;
    }
    
    public void setHeme(double qtty) {
    	this.heme = qtty;
    }
    
    public double getHeme() {
    	return heme;
    }
    
    public double getNitrogen() {
    	return this.nitrogenPool;
    }
    
    protected void setNitrogen(double nitrogen) {
    	this.nitrogenPool = nitrogen;
    }
    
    public void incNitrogen(double qtty) {
    	this.nitrogenPool += qtty;
    }
    
    public static int getTotalCells0() {
    	return Afumigatus.totalCells[0];
    }
    
    public static int getTotalRestingConidia() {
    	return Afumigatus.totalCells[1];
    }
    
    public static int getTotalSwellingConidia() {
    	return Afumigatus.totalCells[2];
    }
    
    public static int getTotalGerminatingConidia() {
    	return Afumigatus.totalCells[3];
    }
    
    public static int getTotalHyphae() {
    	return Afumigatus.totalCells[4];
    }
    
    public static  double getTotalIron() {
    	return Afumigatus.totalIron;
    }
    
    public Afumigatus getNextSepta() { 
		return nextSepta;
	}

    
	public Afumigatus getNextBranch() {
		return nextBranch;
	}


	public boolean isRoot() {
		return isRoot;
	}
	
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}


	public double getxTip() {
		return xTip;
	}


	public void setxTip(double xTip) {
		this.xTip = xTip;
	}


	public double getyTip() {
		return yTip;
	}


	public void setyTip(double yTip) {
		this.yTip = yTip;
	}


	public double getzTip() {
		return zTip;
	}


	public void setzTip(double zTip) {
		this.zTip = zTip;
	}

	public double getDx() {
		return dx;
	}


	public double getDy() {
		return dy;
	}


	public double getDz() {
		return dz;
	}
	
	public boolean isInternalizing() {
        return this.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == Afumigatus.INTERNALIZING;
	}
	
	public boolean isGrowable() {
		return growable;
	}
	
	public void setGrowable(boolean growable) {
		this.growable = growable;
	}
	
	public synchronized void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte) {
		if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == Afumigatus.DEAD)return;
		Voxel[][][] grid = GridFactory.getGrid();
		
		this.computeGrowthRate();
		if(!this.getClock().toc()) {
			
			//if(this.growing())
				return;
		}
		
        /*if(phagocyte instanceof Leukocyte) { // If instead of being in a voxel it is inside a phagosome
        	ArrayList<InfectiousAgent> tmpPhagossome = (ArrayList<InfectiousAgent>) ((ArrayList)phagocyte.getPhagosome()).clone();
        	Afumigatus phagent = null;
            for(InfectiousAgent agent : tmpPhagossome) {
                if(agent instanceof Afumigatus) {
                	phagent = (Afumigatus) agent;
                	if (phagent.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.GERM_TUBE && phagent.intracellularModel.getBooleanNetwork()[10] == 1) {
                			//phagent.getBooleanNetwork(Afumigatus.LIP) == 1) {
                		//pass
                        //#voxel.status = Phagocyte.NECROTIC if voxel.status != Phagocyte.DEAD else Phagocyte.DEAD
                	}
                }
            }
        }   */        
        if (this.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == Afumigatus.FREE) {
            Voxel voxel = Util.findVoxel(this.xTip, this.yTip, this.zTip, xbin, ybin, zbin, grid);
            if (voxel != null && voxel.getTissueType() != voxel.AIR) {
                Afumigatus nextSepta = this.elongate();
                if (nextSepta != null)
                    voxel.setCell(nextSepta);
                nextSepta = this.branch();
                if (nextSepta != null)
                    voxel.setCell(nextSepta);
            }
        }
	}
	
	public boolean growing() {
		this.percentGrow += this.clock.getSize()/(this.iterationsToGrow);
		//System.out.println(percentGrow + "\t" + this.clock.getSize() + "\t" + iterationsToGrow);
		//if(Rand.getRand().randunif() < 0.1)System.out.println(this.clock.getSize() + " " + this.iterationsToGrow + " " + this.percentGrow + " " + (this.getIronPool()/Constants.VOXEL_VOL) + " " + Constants.Kd_LIP);
		if(this.percentGrow >= 1.0) {
			this.percentGrow -= 1.0;
			return true;
		}
		return false;
	}
        

    protected Afumigatus elongate() {

    	Afumigatus septa = null;
        //if(this.growable && this.getBooleanNetwork(Afumigatus.LIP) == 1) {
    	if(this.growable) {// && canGrow()) {
            if(this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE) {
                //if(this.growthIteration >= this.iterationsToGrow) {
            	if(this.growing()) {
                    //this.growthIteration = 0;
                    this.growable = false;
                    this.branchable = true;
                    this.setIronPool(this.getIronPool() / 2.0);
                    this.nextSepta = createAfumigatus(this.xTip, this.yTip, this.zTip, 
                                                 this.xTip + this.dx, this.yTip + this.dy, this.zTip + this.dz,
                                                 this.dx, this.dy, this.dz, 0, 
                                                 0, Afumigatus.HYPHAE, false);
                    this.nextSepta.setIronPool(this.getIronPool());
                    this.nextSepta.setNitrogen((nitrogenPool * this.treeSize) / (this.treeSize + 1.0));
                    septa = this.nextSepta;
                } //else 
                  //  this.growthIteration = this.growthIteration + 1;
            }else if(this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.GERM_TUBE) {
                //if(this.growthIteration >= Constants.ITER_TO_GROW){
                if(this.growing()) {
                    this.getBooleanNetwork().setState(AspergillusIntracellularModel.STATUS, Afumigatus.HYPHAE);
                    Afumigatus.incTotalCells(4);
        			Afumigatus.decTotalCells(3);
                    this.xTip = this.getX() + this.dx;
                    this.yTip = this.getY() + this.dy;
                    this.zTip = this.getZ() + this.dz;
                }//else
                 //   this.growthIteration = this.growthIteration + 1;
            }
        }
    	this.iterationsToGrow = Constants.ITER_TO_GROW;
        return septa;
    }

    public Afumigatus branch() {
    	return this.branch(null, Constants.PR_BRANCH);
    }
    
    public Afumigatus branch(double prBranch) {
    	return this.branch(null, prBranch);
    }
    
    private Afumigatus branch(Double phi, double prBranch) {
    	Afumigatus branch = null;
    	if(this.branchable && this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE) {// && canGrow()) {
        //if(this.branchable && this.getStatus() == Afumigatus.HYPHAE && this.getBooleanNetwork(Afumigatus.LIP) == 1) {
            if(Rand.getRand().randunif() < prBranch) {
                if(phi == null) {
                    phi = 2*Rand.getRand().randunif()*Math.PI;
                }
                
                
                this.setIronPool(this.getIronPool() / 2.0);
                double[] growthVector = new double[] {dx, dy, dz};
				double[][] base = LinAlg.gramSchimidt(this);
				double[][] baseInv = LinAlg.transpose(base);
				double[][] R = LinAlg.rotation(2*Rand.getRand().randunif()*Math.PI);
				R = LinAlg.dotProduct(base, LinAlg.dotProduct(R, baseInv));
				growthVector = LinAlg.dotProduct(R, growthVector);
				
                this.nextBranch = createAfumigatus(this.xTip, this.yTip, this.zTip,
                                             this.xTip + growthVector[0], this.yTip + growthVector[1], 
                                             this.zTip + growthVector[2],
                                             growthVector[0], growthVector[1], growthVector[2], -1,
                                              0, Afumigatus.HYPHAE, false);

                this.nextBranch.setIronPool(this.getIronPool());
                branch = this.nextBranch;
            }
            this.branchable = false;
        }
        return branch;
    }
    
    /*protected boolean canGrow() {
    	return this.getBooleanNetwork().getBooleanNetwork()[LIP] == 1; // && this.hasNitrogen();//Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_GROW, 1.0, Constants.HYPHAE_VOL, 1.0);
    }*/
    
    protected void computeGrowthRate() {
    	double k = 0.5;
    	double eps = 1e-16;
    	double iron = this.getIronPool()/Constants.HYPHAE_VOL + eps;
    	double heme = this.heme/Constants.HYPHAE_VOL + eps;
    	//double heme = Constants.HEME_INIT_QTTY/Constants.HYPHAE_VOL;
    	//this.iterationsToGrow = ((Constants.ITER_TO_GROW * (1 - Math.exp(-k)))/(1 - Math.exp((-iron/Constants.Kd_LIP))));
    	//this.iterationsToGrow = ((Constants.ITER_TO_GROW * k * 2 *(Constants.INTERNAL_IRON_KM + iron))/(iron*(1 + k)));
    	this.iterationsToGrow = (Constants.ITER_TO_GROW*epithelialInhibition*(Constants.INTERNAL_HEME_KM*iron + Constants.INTERNAL_IRON_KM*heme + iron*heme))/(heme*iron);
    	//if(this.getClock().count > 20) System.out.println(Constants.ITER_TO_GROW + " " + epithelialInhibition + " " + Constants.INTERNAL_HEME_KM + " " + iron + " " + Constants.INTERNAL_IRON_KM + " " + heme + " " + this.iterationsToGrow);
    	//if(Rand.getRand().randunif() < 0.01) System.out.println(this.heme + " " + heme + " " + iron + " " + this.iterationsToGrow + " " + epithelialInhibition);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Iron) {  //UNCOMENT WHEN CREATE IRON
    		if(Interactions.releaseIron(this, (Iron) interactable, Cell.DYING, x, y, z)) return true;
            return Interactions.releaseIron(this, (Iron) interactable, Cell.DEAD, x, y, z);
    	}
            
        if(interactable instanceof Macrophage) 
        	return Interactions.macrophageAspergillus((Macrophage) interactable, this);
            
        return interactable.interact(this, x, y, z);
    }

    public boolean isDead() {
        return super.isDead() || this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.STERILE_CONIDIA;
    }
    
    public void diffuseIron() {
    	this.diffuseIron(null);
    }

    private void diffuseIron(Afumigatus afumigatus) {
        if(afumigatus == null) {
            if(this.isRoot) {
                Afumigatus.treeSepta = new HashSet<>();
                Afumigatus.treeSepta.add(this);
                this.diffuseIron(this);
                double totalIron = 0;
                double totalHeme = 0;
                double totalN = 0;
              //for a in Afumigatus.tree_septa:
                for(Afumigatus a : Afumigatus.treeSepta) {
                    totalIron += a.getIronPool();
                    totalHeme += a.getHeme();
                    totalN += a.getNitrogen();
                }
                totalIron = totalIron/Afumigatus.treeSepta.size();
                totalHeme = totalHeme/Afumigatus.treeSepta.size();
                totalN = totalN/Afumigatus.treeSepta.size();
                //for a in Afumigatus.tree_septa:
                for(Afumigatus a : Afumigatus.treeSepta) {
                    a.setIronPool(totalIron);
                    a.setHeme(totalIron);
                    a.setNitrogen(totalN);
                }
            }
        }else {
            if(afumigatus.nextSepta != null && afumigatus.nextBranch == null){
                Afumigatus.treeSepta.add(afumigatus.nextSepta);
                this.diffuseIron(afumigatus.nextSepta);
            }else if(afumigatus.nextSepta != null && afumigatus.nextBranch != null) {
                Afumigatus.treeSepta.add(afumigatus.nextSepta);
                Afumigatus.treeSepta.add(afumigatus.nextBranch);
                this.diffuseIron(afumigatus.nextBranch);
                this.diffuseIron(afumigatus.nextSepta);
            }
        }
    }
    
    public boolean hasNitrogen() {
    	int size = this.treeSize + 1;
    	double nitrogen = this.nitrogenPool * this.treeSize;
    	
    	return nitrogen >= Constants.NITROGEN_THRESHOLD * size;
    }

    public void incIronPool(double qtty) {
        this.setIronPool(this.getIronPool() + qtty);
        Afumigatus.totalIron = Afumigatus.totalIron + qtty;
    }

    public void die() {
    	if(this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DEAD) {
    		this.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, Cell.DEAD);
            Afumigatus.totalCells[AspergillusIntracellularModel.hasLifeStage(this.getBooleanNetwork().getState(IntracellularModel.STATUS))]--;
            Afumigatus.totalCells[0]--;
        }
    }
    
    public String getName() {
    	return NAME;
    }


	@Override
	public void move(Voxel oldVoxel, int steps) {}


	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}

	public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	this.diffuseIron();
    }

	@Override
	public boolean isSecretingSiderophore(Siderophore mol) {
		if (this.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == Afumigatus.FREE && 
				this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DYING && 
				this.getBooleanNetwork().getState(AspergillusIntracellularModel.LIFE_STATUS) != Cell.DEAD) 
    		if (this.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.SECRETING_TAFC) && 
                    (this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.SWELLING_CONIDIA || 
                    		this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE ||
                    				this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS)  == Afumigatus.GERM_TUBE))
    			return true;
		return false;
	}


	@Override
	public boolean isUptakingSiderophore(Siderophore mol) {
		if (this.getBooleanNetwork().getState(IntracellularModel.LOCATION) == Afumigatus.FREE && 
				this.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) != Cell.DYING && 
				this.getBooleanNetwork().getState(AspergillusIntracellularModel.LIFE_STATUS) != Cell.DEAD) 
            if (this.getBooleanNetwork().hasPhenotype(AspergillusIntracellularModel.UPTAKING_TAFC) && 
                    (this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.SWELLING_CONIDIA || 
            		this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE ||
            				this.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS)  == Afumigatus.GERM_TUBE)) 
            	return true;
		return false;
	}

}
