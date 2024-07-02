package edu.uf.interactable;

import edu.uf.compartments.Voxel;
import edu.uf.utils.Constants;
import edu.uf.utils.Id;
import edu.uf.utils.Util;

public class Liver extends Cell{
    public static final String NAME = "Liver";
    
    public static final double LIVER_BN_EPS = 1.0/500.0;
    
    private static final double LOG_NULL_VALUE = -100;

    private int id;
    private double logHepcidin;
    
    private double systemicIl6Concentration;
    
    private boolean hasInteractedWithIL6 = false;
    private boolean hasInteractedWithTNFa = false;
    private boolean hasInteractedWithTGFb = false;
    private boolean hasInteractedWithTf = false;
    private boolean hasProcessBN = false;
    private boolean hasUpdated = false;
    
    public static final int SPECIES_NUM = 15;
    public static final int ENSEMBLE_SIZE = 400;
    
    
    
    public static final int TNFR = 0;
    public static final int IL1R = 1;
    public static final int IL6R = 2;
    public static final int TGFR = 3;
    public static final int TFR2 = 4;
    public static final int NFKB = 5;
    public static final int MAPK = 6;
    public static final int STAT3 = 7;
    public static final int SMAD_6_7 = 8;
    public static final int ERK = 9;
    public static final int SMAD_1_5_8 = 10;
    public static final int CEBP = 11;
    public static final int HP = 12;
    public static final int HPX = 13;
    public static final int HEP = 14;
    
    private double serumHep = 0.0;
    private double serumHepAux = 0.0;
    
    
    private int[][] booleanNetwork = new int[ENSEMBLE_SIZE][SPECIES_NUM];
    private double[] booleanEnsemble = new double [SPECIES_NUM];
    
    private static Liver liver;
    
    private static int interactionId = Id.getMoleculeId();
    
    private  Liver() {
    	super(null);
        this.id = Id.getId();
        this.logHepcidin = LOG_NULL_VALUE;
    }
    
    public boolean isTime() {
		return true;//this.getClock().toc();
	}

    public static Liver getLiver() {
    	if(liver == null) {
    		liver = new Liver(); 
    	}
    	return liver;
    }
    
    public int[][] getBooleanNetworkEnsemble(){
    	return this.booleanNetwork;
    }
    
    public double[] getBooleanEnsemble() {
    	return booleanEnsemble;
    }
    
    public double getIL6() {
		return systemicIl6Concentration;
	}
    
    /*
	public void setHemoglobin(double hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public double getHeme() {
		return heme;
	}

	public void setHeme(double heme) {
		this.heme = heme;
	}*/
    
	
    /*protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof Transferrin) {
            Molecule tfr = (Molecule) interactable;
        	double tf = 0;
        	if (this.logHepcidin <= LOG_NULL_VALUE || this.logHepcidin < Constants.THRESHOLD_LOG_HEP)
                tf = Constants.TF_INTERCEPT + Constants.TF_SLOPE * Constants.THRESHOLD_LOG_HEP;
            else
                tf = Constants.TF_INTERCEPT + Constants.TF_SLOPE * this.logHepcidin;
            //tf = tf;//#*0.25142602860942986

            double rateTf = Util.turnoverRate(
            		tfr.get("Tf", x, y, z), tf * Constants.DEFAULT_APOTF_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;
            double rateTffe = Util.turnoverRate(
            		tfr.get("TfFe", x, y, z), tf * Constants.DEFAULT_TFFE_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;
            double rateTffe2 = Util.turnoverRate(
            		tfr.get("TfFe2", x, y, z), tf * Constants.DEFAULT_TFFE2_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;

            tfr.pinc(rateTf, "Tf", x, y, z);
            tfr.pinc(rateTffe, "TfFe", x, y, z);
            tfr.pinc(rateTffe2, "TfFe2", x, y, z);

            return true;
        }
        if (interactable instanceof IL6) {
        	IL6 il6 = (IL6) interactable;
        	
        	this.globalIl6Concentration = il6.totalMolecules[0]/(2*Constants.SPACE_VOL);// #div 2 : serum
        	//r = Rand.getRand().randunif() > 0.99;
        	//if(r)System.out.println(globalIl6Concentration + "\t" + Constants.IL6_THRESHOLD);
            if (globalIl6Concentration > Constants.IL6_THRESHOLD)
                this.logHepcidin = Constants.HEP_INTERCEPT + Constants.HEP_SLOPE*Math.log10(globalIl6Concentration);
            else
                this.logHepcidin = LOG_NULL_VALUE;
            return true;
        }
        if (interactable instanceof Hepcidin) {
        	Hepcidin hep = (Hepcidin) interactable;
        	double rate = 0;
        	//if(r)System.out.println("# " + Constants.IL6_THRESHOLD + "\t" + this.logHepcidin + "\t" + Constants.THRESHOLD_LOG_HEP);
        	if (this.logHepcidin <= LOG_NULL_VALUE || this.logHepcidin < Constants.THRESHOLD_LOG_HEP) {
                rate = Util.turnoverRate(hep.get("Hepcidin", x, y, z), Constants.THRESHOLD_HEP * Constants.VOXEL_VOL) - 1;
                //if(r)System.out.println("--> " + this.logHepcidin  + "\t" + rate);
        	}else {
                rate = Util.turnoverRate(hep.get("Hepcidin", x, y, z), Math.pow(10, this.logHepcidin) * Constants.VOXEL_VOL) - 1;
                //if(r)System.out.println("---> " + this.logHepcidin + "\t" + rate);
        	}
        	if(rate > 0) System.out.println(rate);
        	//hep.pinc(rate, x, y, z);
            return true;
        }
        return interactable.interact(this, x, y, z);
    }*/
	
	
	protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
		//System.out.println(interactable);
        if (interactable instanceof Transferrin) {
            Molecule tfr = (Transferrin) interactable;
            
           /* if(!hasInteractedWithTf) { 
            	double totalTf = tfr.getTotalMolecule(2);
            	//double p = Util.activationFunction(totalTf, Constants.Kd_TfR2, Constants.STD_UNIT_T,  Constants.SPACE_VOL, 1);   
            	for(int i = 0; i < Liver.ENSEMBLE_SIZE; i++) {
            		if(Util.activationFunction(totalTf, Constants.Kd_TfR2, Constants.SPACE_VOL)) {
            			this.booleanNetwork[i][TFR2] = 1;
            		}else {
            			this.booleanNetwork[i][TFR2] = 0;
            		}
            	}
            	hasInteractedWithTf = true;
        	}
            
            
        	double tf = 0;
        	
        	if (this.logHepcidin <= LOG_NULL_VALUE) // || this.logHepcidin < Constants.THRESHOLD_LOG_HEP)
                tf = Constants.TF_INTERCEPT + Constants.TF_SLOPE * Constants.THRESHOLD_LOG_HEP;
            else
                tf = Constants.TF_INTERCEPT + Constants.TF_SLOPE * this.logHepcidin;
            //tf = tf;//#*0.25142602860942986
*/
            
            double tf = Constants.TF_INTERCEPT + Constants.TF_SLOPE * Constants.THRESHOLD_LOG_HEP;
            double rateTf = Util.turnoverRate(
            		tfr.get("Tf", x, y, z), tf * Constants.DEFAULT_APOTF_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;
            double rateTffe = Util.turnoverRate(
            		tfr.get("TfFe", x, y, z), tf * Constants.DEFAULT_TFFE_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;
            double rateTffe2 = Util.turnoverRate(
            		tfr.get("TfFe2", x, y, z), tf * Constants.DEFAULT_TFFE2_REL_CONCENTRATION * Constants.VOXEL_VOL
            ) - 1;

            tfr.pinc(rateTf, "Tf", x, y, z);
            tfr.pinc(rateTffe, "TfFe", x, y, z);
            tfr.pinc(rateTffe2, "TfFe2", x, y, z);
            //System.out.println(rateTffe2);

            return true;
        }
        /*if (interactable instanceof IL6) {
        	if(hasInteractedWithIL6) return true;
        	IL6 il6 = (IL6) interactable;
        	this.systemicIl6Concentration = il6.getTotalMolecule(0)/(2*Constants.SPACE_VOL);// #div 2 : serum
        	//double p = Util.activationFunction(systemicIl6Concentration, Constants.Kd_IL6, Constants.STD_UNIT_T);
        	//System.out.println(p + "\t" + this.systemicIl6Concentration);
        	for(int i = 0; i < Liver.ENSEMBLE_SIZE; i++) {
        		if(Util.activationFunction(systemicIl6Concentration, Constants.Kd_IL6)) {
        			this.booleanNetwork[i][IL6R] = 1;
        			
        		}else {
        			this.booleanNetwork[i][IL6R] = 0;
        		}
        	}
        	hasInteractedWithIL6 = true;
            return true;
        }
        if (interactable instanceof TNFa) {
        	if(hasInteractedWithTNFa) return true;
        	TNFa tnfa = (TNFa) interactable;
        	double systemicConcentration = tnfa.getTotalMolecule(0)/(2*Constants.SPACE_VOL);// #div 2 : serum
        	//double p = Util.activationFunction(systemicConcentration, Constants.Kd_TNF, Constants.STD_UNIT_T);
        	for(int i = 0; i < Liver.ENSEMBLE_SIZE; i++) {
        		if(Util.activationFunction(systemicConcentration, Constants.Kd_TNF)) {
        			this.booleanNetwork[i][TNFR] = 1;
        		}else {
        			this.booleanNetwork[i][TNFR] = 0;
        		}
        	}
        	hasInteractedWithTNFa = true;
            return true;
        }
        if (interactable instanceof TGFb) {
        	if(hasInteractedWithTGFb) return true;
        	TGFb tgfb = (TGFb) interactable;
        	double systemicConcentration = tgfb.getTotalMolecule(0)/(2*Constants.SPACE_VOL);// #div 2 : serum
        	//double p = Util.activationFunction(systemicConcentration, Constants.Kd_TGF, Constants.STD_UNIT_T);
        	for(int i = 0; i < Liver.ENSEMBLE_SIZE; i++) {
        		if(Util.activationFunction(systemicConcentration, Constants.Kd_TGF)) {
        			this.booleanNetwork[i][TGFR] = 1;
        		}else {
        			this.booleanNetwork[i][TGFR] = 0;
        		}
        	}
        	hasInteractedWithTGFb = true;
            return true; 
        }
        if (interactable instanceof Hepcidin) {
        	Hepcidin hep = (Hepcidin) interactable;
        	double hepQtty = this.booleanEnsemble[HEP]/((double)Liver.ENSEMBLE_SIZE);
        	if(hepQtty == 0.0)return true;
        	
        	hepQtty *= Constants.L_HEP_QTTY;
        	//serumHepAux += hepQtty;
        	
        	double rate = Util.turnoverRate(hep.get("Hepcidin", x, y, z), hepQtty * Constants.VOXEL_VOL) - 1; //serumHep
        	//System.out.println(rate + " " + hepQtty + " " + hep.get("Hepcidin", x, y, z));
        	//if(Rand.getRand().randunif() > 0.99)System.out.println(rate + " " + hepQtty * Constants.VOXEL_VOL + " " + hep.get("Hepcidin", x, y, z));
        	hep.pinc(rate, 0, x, y, z);
        	
        	this.logHepcidin = Math.log10(hepQtty);//hep.getTotalMolecule(0));
        	
        	
            return true;
        }*/
        return interactable.interact(this, x, y, z);
    }

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getId() {
		return id;
	}

	int count = 0;
	
	public void processBooleanNetwork(int... args) {
		if(hasProcessBN)return;
		hasProcessBN = true;
		int[] temp = new int[Liver.SPECIES_NUM];
		for(int k = 0; k < ENSEMBLE_SIZE; k++) {
			for(int i = 0; i < Liver.SPECIES_NUM; i++) 
				temp[i] = 0;
			
			temp[Liver.IL1R] = this.booleanNetwork[k][Liver.IL1R];
			temp[Liver.IL6R] = this.booleanNetwork[k][Liver.IL6R];
			temp[Liver.TNFR] = this.booleanNetwork[k][Liver.TNFR];
			temp[Liver.TGFR] = this.booleanNetwork[k][Liver.TGFR];
			temp[Liver.TFR2] = this.booleanNetwork[k][Liver.TFR2];

			temp[Liver.NFKB] = this.booleanNetwork[k][Liver.TNFR] | this.booleanNetwork[k][Liver.IL1R];
			temp[Liver.MAPK] = this.booleanNetwork[k][Liver.TNFR] | this.booleanNetwork[k][Liver.IL1R] | this.booleanNetwork[k][Liver.IL6R];
			temp[Liver.STAT3] = this.booleanNetwork[k][Liver.IL6R];
			temp[Liver.SMAD_6_7] = this.booleanNetwork[k][Liver.TNFR] | this.booleanNetwork[k][Liver.TGFR];
			temp[Liver.ERK] = this.booleanNetwork[k][Liver.TGFR];
			temp[Liver.SMAD_1_5_8] = (this.booleanNetwork[k][Liver.TGFR] | this.booleanNetwork[k][Liver.TFR2]) & (-this.booleanNetwork[k][Liver.SMAD_6_7] + 1);
			temp[Liver.CEBP] = this.booleanNetwork[k][Liver.MAPK] & (-this.booleanNetwork[k][Liver.ERK] + 1);
			//temp[Liver.HP] = this.booleanNetwork[k][Liver.NFKB] & this.booleanNetwork[k][Liver.STAT3] & this.booleanNetwork[k][Liver.CEBP];
			//temp[Liver.HPX] = this.booleanNetwork[k][Liver.NFKB] & this.booleanNetwork[k][Liver.STAT3] & this.booleanNetwork[k][Liver.CEBP];
			temp[Liver.HP] = this.booleanNetwork[k][Liver.NFKB] | this.booleanNetwork[k][Liver.STAT3] | this.booleanNetwork[k][Liver.CEBP];
			temp[Liver.HPX] = this.booleanNetwork[k][Liver.NFKB] | this.booleanNetwork[k][Liver.STAT3] | this.booleanNetwork[k][Liver.CEBP];
			temp[Liver.HEP] = this.booleanNetwork[k][Liver.CEBP] | this.booleanNetwork[k][Liver.STAT3] | this.booleanNetwork[k][Liver.SMAD_1_5_8];
			
			for(int i = 0; i < Liver.SPECIES_NUM; i++) {
				this.booleanNetwork[k][i] = temp[i];
				if(k == 0)this.booleanEnsemble[i] = temp[i];///((double) ENSEMBLE_SIZE);
				else this.booleanEnsemble[i] += temp[i];///((double) ENSEMBLE_SIZE);
			}
			//System.out.println();
			
		}
		
		/*System.out.println(
				//IL1.getMolecule().getTotalMolecule(0) + " " + 
				"IL1R = " + this.booleanEnsemble[Liver.IL1R] + " " + 
				IL6.getMolecule().getTotalMolecule(0) + " " + 
				"IL6R = " + this.booleanEnsemble[Liver.IL6R] + " " + 
				TNFa.getMolecule().getTotalMolecule(0) + " " + 
				"TNFR = " + this.booleanEnsemble[Liver.TNFR] + " " + 
				TGFb.getMolecule().getTotalMolecule(0) + " " + 
				"TGFR = " + this.booleanEnsemble[Liver.TGFR] + " " + 
				Transferrin.getMolecule().getTotalMolecule(2) + " " + 
				"TFR2 = " + this.booleanEnsemble[Liver.TFR2]
				);*/
		
		/*System.out.println("# " + 
		"IL1R = " + this.booleanEnsemble[Liver.IL1R] + " " + 
		"IL6R = " + this.booleanEnsemble[Liver.IL6R] + " " + 
		"TNFR = " + this.booleanEnsemble[Liver.TNFR] + " " + 
		"TGFR = " + this.booleanEnsemble[Liver.TGFR] + " " + 
		"TFR2 = " + this.booleanEnsemble[Liver.TFR2] + " " + 
		"MAPK = " + this.booleanEnsemble[Liver.MAPK] + " " + 
		"NFKB = " + this.booleanEnsemble[Liver.NFKB] + " " + 
		"STAT3 = " + this.booleanEnsemble[Liver.STAT3] + " " + 
		"ERK = " + this.booleanEnsemble[Liver.ERK] + " " + 
		"SMAD1 = " + this.booleanEnsemble[Liver.SMAD_1_5_8] + " " + 
		"SMAD6 = " + this.booleanEnsemble[Liver.SMAD_6_7] + " " +
		"CEBP = " + this.booleanEnsemble[Liver.CEBP] + " " + 
		"HP = " + this.booleanEnsemble[Liver.HP] + " " + 
		"HPX = " + this.booleanEnsemble[Liver.HPX]);*/
		
	}

	@Override
	public void updateStatus(int x, int y, int z) {
		//this.processBooleanNetwork(null);
		//super.updateStatus(x, y, z);
		/*if(!hasUpdated) {
			serumHep = serumHepAux;// - serumHep * Constants.HEP_HALF_LIFE;
			System.out.println(this.booleanEnsemble[HEP] + " " + Constants.L_HEP_QTTY + " " + serumHep);
			serumHepAux = 0.0;
			hasUpdated = true;
		}*/
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(int x, int y, int z, int steps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incIronPool(double ironPool) {
		// TODO Auto-generated method stub
		
	} 
	
	public void reset() {
		hasInteractedWithIL6 = false;
	    hasInteractedWithTNFa = false;
	    hasInteractedWithTGFb = false;
	    hasInteractedWithTf = false;
	    hasProcessBN = false;
	    hasUpdated = false;
	}

	@Override
	public int getMaxMoveSteps() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getInteractionId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
