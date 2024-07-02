package edu.uf.intracellularState;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class AspergillusIntracellularModel extends IntracellularModel{
	
	public static final int[] INIT_AFUMIGATUS_BOOLEAN_STATE = new int[] {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    private static final int hapX = 0;
    private static final int sreA = 1;
    private static final int HapX = 2;
    private static final int SreA = 3;
    private static final int RIA = 4;
    private static final int EstB = 5;
    private static final int MirB = 6;
    private static final int SidA = 7;
    private static final int Tafc = 8;
    private static final int ICP = 9;
    private static final int LIP = 10;
    private static final int CccA = 11;
    private static final int FC0fe = 12;
    private static final int FC1fe = 13;
    private static final int VAC = 14;
    private static final int ROS = 15;
    private static final int Yap1 = 16;
    private static final int SOD2_3 = 17;
    private static final int Cat1_2 = 18;
    private static final int ThP = 19;
    private static final int Fe = 20;
    private static final int O = 21;
    private static final int NUM_SPECIES = 22;
    
    
    
    public static final int SECRETING_TAFC = Phenotype.createPhenotype();
    public static final int UPTAKING_TAFC = Phenotype.createPhenotype();
    
    //public static final int Af_STATUS_START = RESTING_CONIDIA;
    
    private int lipActivation;
    
    public AspergillusIntracellularModel(final int phenotype) {
    	setBnIteration(0);
    	setBooleanNetwork(INIT_AFUMIGATUS_BOOLEAN_STATE.clone());
    	//System.out.println(LOCATION + " " + STATUS);
    	super.setState(LOCATION, Afumigatus.FREE);
    	super.setState(STATUS, phenotype);
    	//super.addPhenotype(INTERACTION_STATE, phenotype);
    	
    }

    static int i = 0;
    static int j = 0;
	@Override
	public void updateStatus(int id, int x, int y, int z) {
		Afumigatus asp = (Afumigatus) Cell.get(id);
		
		this.lipActivation = Rand.getRand().randunif() < Util.activationFunction(asp.getIronPool(), Constants.Kd_LIP, Constants.HYPHAE_VOL, 1.0) ? 1 : 0;
		//System.out.println(lipActivation);
		
		//System.out.println(this.hasPhenotype(RESTING_CONIDIA));
		//System.out.println(Constants.PR_ASPERGILLUS_CHANGE);
		//System.out.println(Rand.getRand().randunif());
		if(this.getState(STATUS) == Afumigatus.RESTING_CONIDIA && asp.getClock().getCount() >= Constants.ITER_TO_SWELLING)j++;
        if(this.getState(STATUS) == Afumigatus.RESTING_CONIDIA && asp.getClock().getCount() >= Constants.ITER_TO_SWELLING && Rand.getRand().randunif() < Constants.PR_ASPERGILLUS_CHANGE) { 
            this.setState(STATUS, Afumigatus.SWELLING_CONIDIA);
            Afumigatus.incTotalCells(2);
			Afumigatus.decTotalCells(1);
			i++;
			//System.out.println(i/((double)j));
        }else if(!asp.isEngaged() && this.getState(STATUS) == Afumigatus.SWELLING_CONIDIA && asp.getClock().getCount() >= Constants.ITER_TO_GERMINATE) {
            this.setState(STATUS, Afumigatus.GERM_TUBE);
            Afumigatus.incTotalCells(3);
			Afumigatus.decTotalCells(2);
        }else if(this.getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DYING) {
        	asp.die();
        }

        if(asp.getNextSepta() == null) 
        	asp.setGrowable(true);

        if(this.getState(LOCATION) == Afumigatus.INTERNALIZING || this.getState(LOCATION) == Afumigatus.RELEASING)
        	this.setState(LOCATION, Afumigatus.FREE); 

        asp.diffuseIron();
        if(asp.getNextBranch() == null)
        	asp.setGrowable(true);
        asp.setEpithelialInhibition(2.0);
		
	}
	
	public static int hasLifeStage(int phenotype) {
		if(phenotype == Afumigatus.RESTING_CONIDIA)
			return 1;
		if(phenotype == Afumigatus.SWELLING_CONIDIA)
			return 2;
		if(phenotype == Afumigatus.GERM_TUBE)
			return 3;
		if(phenotype == Afumigatus.HYPHAE)
			return 4;
		if(phenotype == Afumigatus.STERILE_CONIDIA) //PROBABLY NOT BEEN CONSIDERED
			return 5; //THAT'S TOO MUCH. PROBABLY GOING TO RAISE AN ERROR
		return -1;
	}

	@Override
	public void processBooleanNetwork(int... args) {
		int[] temp = new int[NUM_SPECIES];
		
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
        temp[LIP] = (this.booleanNetwork[Fe] & this.booleanNetwork[RIA]) | this.lipActivation;
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
        for(int i = 0; i < NUM_SPECIES; i++)
            this.booleanNetwork[i] = temp[i];
        this.setBnIteration(0);
        
        this.computePhenotype();
		
	}

	@Override
	protected void computePhenotype() {
		if(this.booleanNetwork[MirB] == 1 && this.booleanNetwork[EstB] == 1)
			this.addPhenotype(UPTAKING_TAFC);
		if(this.booleanNetwork[Tafc] == 1)
			this.addPhenotype(SECRETING_TAFC);
		
	}

}
