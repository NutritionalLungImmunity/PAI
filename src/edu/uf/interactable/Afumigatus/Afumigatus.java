package edu.uf.interactable.Afumigatus;

import java.util.Set;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import edu.uf.compartments.GridFactory;
import edu.uf.compartments.Voxel;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Internalizable;
import edu.uf.interactable.Iron;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.PositionalInfectiousAgent;
import edu.uf.intracellularState.BooleanNetwork;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.LinAlg;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

import java.util.HashSet;

public class Afumigatus extends PositionalInfectiousAgent implements Internalizable{

    public static final String NAME = "Afumigatus";
    
    public static final int[] INIT_AFUMIGATUS_BOOLEAN_STATE = new int[] {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    

    private static double totalIron = 0;
    private static int[] totalCells = new int[5];
    
    public static final int hapX = 0;
    public static final int sreA = 1;
    public static final int HapX = 2;
    public static final int SreA = 3;
    public static final int RIA = 4;
    public static final int EstB = 5;
    public static final int MirB = 6;
    public static final int SidA = 7;
    public static final int Tafc = 8;
    public static final int ICP = 9;
    public static final int LIP = 10;
    public static final int CccA = 11;
    public static final int FC0fe = 12;
    public static final int FC1fe = 13;
    public static final int VAC = 14;
    public static final int ROS = 15;
    public static final int Yap1 = 16;
    public static final int SOD2_3 = 17;
    public static final int Cat1_2 = 18;
    public static final int ThP = 19;
    public static final int Fe = 20;
    public static final int O = 21;
    public static final int SPECIES_NUM = 22;
    
    public static final int RESTING_CONIDIA = 5;
    public static final int SWELLING_CONIDIA = 6;
    public static final int GERM_TUBE = 7;
    public static final int HYPHAE = 8;
    public static final int STERILE_CONIDIA = 9;
    
    private static final int Af_STATUS_START = RESTING_CONIDIA;
    
    public static final int FREE = 0;
    public static final int INTERNALIZING = 1;
    public static final int RELEASING = 2;
    public static final int ENGAGED = 3;

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
    			0, 0, RESTING_CONIDIA, 0, true);
    }


    public Afumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
        super(xRoot, yRoot, zRoot, Afumigatus.createNewBooleanNetwork());
        this.setIronPool(ironPool);
        this.setState(state);
        super.setStatus(status);
        this.isRoot = isRoot;
        this.xTip = xTip;
        this.yTip = yTip;
        this.zTip = zTip;
        double[] ds = new double[] {dx, dy, dz};
        LinAlg.multiply(ds, 1.0/LinAlg.norm(ds));
        
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
        
        if(status >=  Afumigatus.RESTING_CONIDIA)Afumigatus.totalCells[status - Af_STATUS_START + 1]++;
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
    
    public static void setVerboseFile(String file) throws FileNotFoundException {
    	
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
    
    public static int getTotalCells() {
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
        return this.getState() == Afumigatus.INTERNALIZING;
	}
	
	public boolean isGrowable() {
		return growable;
	}
	
	protected static BooleanNetwork createNewBooleanNetwork() {
    	BooleanNetwork network = new BooleanNetwork() {

			@Override
			public void processBooleanNetwork(int... args) {
	        	int[] temp = new int[SPECIES_NUM];
	        	//for(int i = 0; i < Afumigatus.SPECIES_NUM; i++)
	        	//	temp[i] = 0;

	            temp[hapX] = -this.booleanNetwork[SreA] + 1;
	            temp[sreA] = -this.booleanNetwork[HapX] + 1;
	            temp[HapX] = this.booleanNetwork[hapX] & (-this.booleanNetwork[LIP] + 1);
	            temp[SreA] = this.booleanNetwork[sreA] & this.booleanNetwork[LIP];
	            temp[RIA] = -this.booleanNetwork[SreA] + 1;
	            temp[EstB] = -this.booleanNetwork[SreA] + 1;
	            temp[MirB] = this.booleanNetwork[HapX] & (-this.booleanNetwork[SreA] + 1);
	            temp[SidA] = this.booleanNetwork[HapX] & (-this.booleanNetwork[SreA] + 1);
	            temp[Tafc] = this.booleanNetwork[SidA];
	            temp[ICP] = (-this.booleanNetwork[HapX] + 1) & (this.booleanNetwork[VAC] | this.booleanNetwork[FC1fe]);
	            temp[LIP] = (this.booleanNetwork[Fe] & this.booleanNetwork[RIA]) | args[0];
	            temp[CccA] = -this.booleanNetwork[HapX] + 1;
	            temp[FC0fe] = this.booleanNetwork[SidA];
	            temp[FC1fe] = this.booleanNetwork[LIP] & this.booleanNetwork[FC0fe];
	            temp[VAC] = this.booleanNetwork[LIP] & this.booleanNetwork[CccA];
	            temp[ROS] = (this.booleanNetwork[O] & (- (this.booleanNetwork[SOD2_3] & this.booleanNetwork[ThP] 
	                                   & this.booleanNetwork[Cat1_2]) + 1)) 
	                                  | (this.booleanNetwork[ROS] & (- (this.booleanNetwork[SOD2_3] 
	                                   & (this.booleanNetwork[ThP] | this.booleanNetwork[Cat1_2])) + 1));
	            temp[Yap1] = this.booleanNetwork[ROS];
	            temp[SOD2_3] = this.booleanNetwork[Yap1];
	            temp[Cat1_2] = this.booleanNetwork[Yap1] & (-this.booleanNetwork[HapX] + 1);
	            temp[ThP] = this.booleanNetwork[Yap1];

	            temp[Fe] = 0; // might change according to iron environment?
	            temp[O] = 0;
	            //temp[Afumigatus.TAFCBI] = 0;

	            //print(this.boolean_network)
	            for(int i = 0; i < SPECIES_NUM; i++)
	                this.booleanNetwork[i] = temp[i];
	            this.setBnIteration(0);
				
			}
			
			
    		
    	};
    	
    	network.setBnIteration(0);
    	network.setBooleanNetwork(INIT_AFUMIGATUS_BOOLEAN_STATE.clone());
    	
    	return network;
    }
    
    protected int lipActivation() {
		//System.out.println(Util.activationFunction(Afumigatus.this.getIronPool(), Constants.Kd_LIP, Constants.HYPHAE_VOL, 1.0) + " " + (this.getIronPool()/Constants.VOXEL_VOL) + " " + Constants.Kd_LIP);
        return Rand.getRand().randunif() < Util.activationFunction(Afumigatus.this.getIronPool(), Constants.Kd_LIP, Constants.HYPHAE_VOL, 1.0) ? 1 : 0;
    }
	
	public void setStatus(int status) {
		//System.out.println(this.getStatus() + " " + status + " " + Af_STATUS_START + " " + Afumigatus.totalCells.length);
		if(status < Afumigatus.RESTING_CONIDIA && this.getStatus() >= Afumigatus.RESTING_CONIDIA) {
			Afumigatus.totalCells[this.getStatus() - Af_STATUS_START + 1]--;
			Afumigatus.totalCells[0]--;
		}else if(status >= Afumigatus.RESTING_CONIDIA && this.getStatus() >= Afumigatus.RESTING_CONIDIA) {
			Afumigatus.totalCells[this.getStatus() - Af_STATUS_START + 1]--;
			Afumigatus.totalCells[status - Af_STATUS_START + 1]++;
		}
		super.setStatus(status);
	}
	
	public synchronized void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte phagocyte) {
		Voxel[][][] grid = GridFactory.getGrid();
		
		this.computeGrowthRate();
		if(!this.getClock().toc()) {
			
			//if(this.growing())
				return;
		}
		
        if(phagocyte instanceof Leukocyte) { // If instead of being in a voxel it is inside a phagosome
        	ArrayList<InfectiousAgent> tmpPhagossome = (ArrayList<InfectiousAgent>) ((ArrayList)phagocyte.getPhagosome()).clone();
        	Afumigatus phagent = null;
            for(InfectiousAgent agent : tmpPhagossome) {
                if(agent instanceof Afumigatus) {
                	phagent = (Afumigatus) agent;
                	if (phagent.getStatus() == Afumigatus.GERM_TUBE && phagent.booleanNetwork.getBooleanNetwork()[10] == 1) {
                			//phagent.getBooleanNetwork(Afumigatus.LIP) == 1) {
                		//pass
                        //#voxel.status = Phagocyte.NECROTIC if voxel.status != Phagocyte.DEAD else Phagocyte.DEAD
                	}
                }
            }
        }           
        if (this.getState() == Afumigatus.FREE) {
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
            if(this.getStatus() == Afumigatus.HYPHAE) {
                //if(this.growthIteration >= this.iterationsToGrow) {
            	if(this.growing()) {
                    //this.growthIteration = 0;
                    this.growable = false;
                    this.branchable = true;
                    this.setIronPool(this.getIronPool() / 2.0);
                    this.nextSepta = createAfumigatus(this.xTip, this.yTip, this.zTip, 
                                                 this.xTip + this.dx, this.yTip + this.dy, this.zTip + this.dz,
                                                 this.dx, this.dy, this.dz, 0, 
                                                 0, Afumigatus.HYPHAE, this.getState(), false);
                    this.nextSepta.setIronPool(this.getIronPool());
                    this.nextSepta.setNitrogen((nitrogenPool * this.treeSize) / (this.treeSize + 1.0));
                    septa = this.nextSepta;
                } //else 
                  //  this.growthIteration = this.growthIteration + 1;
            }else if(this.getStatus() == Afumigatus.GERM_TUBE) {
                //if(this.growthIteration >= Constants.ITER_TO_GROW){
                if(this.growing()) {
                    this.setStatus(Afumigatus.HYPHAE);
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
    	if(this.branchable && this.getStatus() == Afumigatus.HYPHAE) {// && canGrow()) {
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
                                              0, Afumigatus.HYPHAE, this.getState(), false);

                this.nextBranch.setIronPool(this.getIronPool());
                branch = this.nextBranch;
            }
            this.branchable = false;
        }
        return branch;
    }
    
    protected boolean canGrow() {
    	return this.getBooleanNetwork().getBooleanNetwork()[LIP] == 1; // && this.hasNitrogen();//Rand.getRand().randunif() < Util.activationFunction(this.getIronPool(), Constants.Kd_GROW, 1.0, Constants.HYPHAE_VOL, 1.0);
    }
    
    protected void computeGrowthRate() {
    	double k = 0.5;
    	double iron = this.getIronPool()/Constants.HYPHAE_VOL;
    	double heme = this.heme/Constants.HYPHAE_VOL;
    	//double heme = Constants.HEME_INIT_QTTY/Constants.HYPHAE_VOL;
    	//this.iterationsToGrow = ((Constants.ITER_TO_GROW * (1 - Math.exp(-k)))/(1 - Math.exp((-iron/Constants.Kd_LIP))));
    	//this.iterationsToGrow = ((Constants.ITER_TO_GROW * k * 2 *(Constants.INTERNAL_IRON_KM + iron))/(iron*(1 + k)));
    	this.iterationsToGrow = (Constants.ITER_TO_GROW*epithelialInhibition*(Constants.INTERNAL_HEME_KM*iron + Constants.INTERNAL_IRON_KM*heme + iron*heme))/(heme*iron);
    	//if(Rand.getRand().randunif() < 0.01) System.out.println(this.heme + " " + heme + " " + iron + " " + this.iterationsToGrow + " " + epithelialInhibition);
    }
    
    protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
    	return new Afumigatus(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                dx, dy, dy, growthIteration,
                ironPool, status, state, isRoot);
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
    	if(interactable instanceof Iron) { //UNCOMENT WHEN CREATE IRON
            Molecule mol = (Molecule) interactable;
        	if(this.getStatus() == Afumigatus.DYING || this.getStatus() == Afumigatus.DEAD) {
        		mol.inc(this.getIronPool(), 0, x, y, z);
                this.incIronPool(-this.getIronPool());
            }
            return true;
        }else if(interactable instanceof Macrophage) {
        	Macrophage m = (Macrophage) interactable;
            if(m.isEngaged())
                return true;
            if(!m.isDead()) {
                if(this.getStatus() != Afumigatus.RESTING_CONIDIA) {
                    double prInteract = this.getStatus() == Afumigatus.HYPHAE ? Constants.PR_MA_HYPHAE : Constants.PR_MA_PHAG;
                    //if(this.getExternalState() == 1)  prInteract *= Constants.NET_COUNTER_INHIBITION;
                    if(Rand.getRand().randunif() < prInteract) {
                        intAspergillus(m, this, this.getStatus() != Afumigatus.HYPHAE);
                        if(this.getStatus() == Afumigatus.HYPHAE && m.hasPhenotype(new int[] {Macrophage.M1, Macrophage.M2B})){ 
                            this.setStatus(Afumigatus.DYING);
                            if(this.nextSepta != null) {
                                this.nextSepta.isRoot = true;
                            if(this.nextBranch != null)
                                this.nextBranch.isRoot = true;
                            }
                        }else {
                            if(this.getStatus() == Afumigatus.HYPHAE && m.hasPhenotype(Macrophage.M1)) {
                            	m.setEngaged(true);
                            }
                        }
                    }
                }
            }
            return true;
        }
            
        return interactable.interact(this, x, y, z);
    }

    public void updateStatus(int x, int y, int z) {
    	super.updateStatus(x, y, z);
    	if(!this.getClock().toc())return;
    	int i = this.lipActivation();
    	this.processBooleanNetwork(i);
        
        if(this.getStatus() == Afumigatus.RESTING_CONIDIA && 
        		this.getClock().getCount() >= Constants.ITER_TO_SWELLING && 
                Rand.getRand().randunif() < (Constants.PR_ASPERGILLUS_CHANGE)) {
            this.setStatus(Afumigatus.SWELLING_CONIDIA);
        }else if(!this.engaged && this.getStatus() == Afumigatus.SWELLING_CONIDIA && this.getClock().getCount() >= Constants.ITER_TO_GERMINATE){// and \
            this.setStatus(Afumigatus.GERM_TUBE);
        }else if(this.getStatus() == Afumigatus.DYING) {
            this.die();
        }

        if(this.nextSepta == null) 
            this.growable = true;

        if(this.getState() == Afumigatus.INTERNALIZING || this.getState() == Afumigatus.RELEASING)
            this.setState(Afumigatus.FREE); 

        this.diffuseIron();
        if(this.nextBranch == null)
            this.growable = true;
        this.epithelialInhibition = 2.0;
    }

    public boolean isDead() {
        return super.isDead() || this.getStatus() == STERILE_CONIDIA;
    }
    
    private void diffuseIron() {
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
        if(this.getStatus() != Afumigatus.DEAD) {
            this.setStatus(Afumigatus.DEAD);
            //Afumigatus.totalCells[0]--;
        }
    }
    
    public String getName() {
    	return NAME;
    }


	@Override
	public void move(Voxel oldVoxel, int steps) {}
	
	public double getTAFCQTTY() {
		return Constants.TAFC_QTTY;
	}


	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}
	
	public static void intAspergillus(Leukocyte phagocyte, Afumigatus aspergillus) {
		intAspergillus(phagocyte, aspergillus, false);
	}	
	
	public static void intAspergillus(Leukocyte phagocyte, Afumigatus aspergillus, boolean phagocytize) {
        if(aspergillus.getState() == Afumigatus.FREE) {
            if (aspergillus.getStatus() == Afumigatus.RESTING_CONIDIA || aspergillus.getStatus() == Afumigatus.SWELLING_CONIDIA || aspergillus.getStatus() == Afumigatus.STERILE_CONIDIA || phagocytize){
            	if (!phagocyte.isDead()) {
            		if(phagocyte.getPhagosome().size() < phagocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
                        aspergillus.setState(Afumigatus.INTERNALIZING);
                        aspergillus.setEngulfed(true);
                        phagocyte.getPhagosome().add(aspergillus);
                    }
                }
            }
            if(aspergillus.getStatus() != Afumigatus.RESTING_CONIDIA) {
                phagocyte.setState(Leukocyte.INTERACTING);
                if(phagocyte instanceof Macrophage) {
                	((Macrophage)phagocyte).bind(aspergillus, 4);
                }
                    
                //else
                    //phagocyte.setStatusIteration(0);
            }
        }
    }

}
