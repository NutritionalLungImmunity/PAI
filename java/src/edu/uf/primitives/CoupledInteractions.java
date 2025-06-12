package edu.uf.primitives;

import edu.uf.interactable.Cell;
import edu.uf.interactable.Blood;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.LPS;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PositionalInfectiousAgent;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.intracellularState.AspergillusIntracellularModel;
import edu.uf.intracellularState.AspergillusMacrophage;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;


/**
 * A utility class providing a library of static methods for defining tightly coupled primitive interactions between agents.
 *
 * <p>This class serves as a continuation of the {@code Interactions} class. Methods that are more tightly coupled 
 * to specific agent implementations are placed here as a temporary workaround to manage coupling.</p>
 *
 * <p>Note: This is a transitional solution to address tight coupling issues and may be refactored in the future.</p>
 *
 * @author henriquedeassis
 */
public class CoupledInteractions {

	/**
     * This method sets the molecule "mol" amount to "qtty" in the voxel at 
     * position (x, y, z) if the Blood is in the "HEMORRHAGIC" state. This 
     * method should be used only for blood molecules (e.g., albumin or heme).
     * @param mol a blood molecule (e.g., albumin or heme)
     * @param blood
     * @param qtty amount of molecule to be set.
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @param w axis position in the grid. Extra dimension for molecules that have more than one state (e.g. free-siderophore/siderophore-bound-to-iron)
     * @return
     */
    static boolean set(Molecule mol, Cell blood, double qtty, int x, int y, int z, int w) {
    	if(((Blood)blood).hasBlood(x, y, z))
    		mol.set(qtty, w, x, y, z);
    	return true;
    }
    
    /**
     * This method sets the Blood at the voxel in position (x, y, z) 
     * to "HEMORRHAGIC" if the type I pneumocyte is dead. 
     * @param blood
     * @param pneumocyteI
     * @param status
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    static boolean setHemorrhage(Cell blood, Cell pneumocyteI, int status, int x, int y, int  z) {
    	if(pneumocyteI.isDead())
    		((Blood)blood).setStatus(status, x, y, z);
		return true;
    }
    
    /**
     * Method handling Lactoferrin degranulation by neutrophils. 
     * If the neutrophil is in a Lactoferrin-secreting phenotype 
     * and has not degranulated recently, it will release a fixed 
     * amount of Lactoferrin in the voxel at position (x, y, z). 
     * That is, the amount of free Lactoferrin is incremented. This 
     * method sets the neutrophil as "degranulated," and its degranulation 
     * becomes blocked until the status is reset.
     * <br/><br/>
     * <strong>This method can potentially cause conflict with other degranulation methods.</strong>
     * @param neutr Neutrophil
     * @param lac Lactoferrin
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    static boolean lactoferrinDegranulation(Leukocyte neutr, Molecule lac, int x, int y, int z) {
    	Neutrophil neutrophil = (Neutrophil) neutr;
    	if (neutr.getBooleanNetwork().hasPhenotype(lac) && !neutrophil.hasDegranulated()){ 
    		lac.inc(Constants.LAC_QTTY, "Lactoferrin", x, y, z);
    		neutrophil.degranulate();
    	}
    	
        return true;
    }
    
    /**
     * Method handling the killing of type I pneumocytes by A. fumigatus hyphae. 
     * If the Aspergillus is in HYPHAE status and the Aspergillus flag "aspEpiInt" 
     * is true the method will kill the type I pneumocyte with a probability. It 
     * will set the  Aspergillus flag "aspEpiInt" to false, which will prevent the 
     * hyphae from killing the pneumocytes in subsequent attempts if the first fails. 
     * This simulates a "single-time shot." 
     * <br/><br/>
     * <strong>This method sets epithelial inhibition over Aspergillus growth to zero (1) 
     * if the type I pneumocyte is dead. Maybe "typeIPneumocyteNET" should do that, too.</strong>
     * @param typeI Type I pneumocyte
     * @param aspergillus
     * @param injury - Unused in this implementation
     * @return injury
     */
    static boolean typeIPneumocyteAspergillus(Cell typeI, PositionalInfectiousAgent asp, boolean injury) {
    	Afumigatus aspergillus = (Afumigatus) asp;
    	
    	if(typeI.isDead())return true;
		if(typeI.isDead())aspergillus.setEpithelialInhibition(1);
		if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.HYPHAE) {
				//if(injury || (a.getAspEpiInt() && Rand.getRand().randunif() < Constants.PR_ASP_KILL_EPI)) //*0.5
				if((aspergillus.getAspEpiInt() && Rand.getRand().randunif() < Constants.PR_ASP_KILL_EPI)) //*0.5
					typeI.die();
				else
					injury = true;
				aspergillus.setAspEpiInt(false);
			
		}
		
		//a.set
		//if(a.getStatus() == Afumigatus.HYPHAE)this.die();
		return injury;
    }
    
    /**
     * Method handling the interaction between macrophages and Aspergillus. 
     * The interaction happens with three distinct probabilities and outcomes 
     * depending on the fungal state. (1) hyphae - low probability "PR_MA_HYPHAE." 
     * The hyphae are killed, and the macrophage B-glucan receptor is set to 
     * the maximum activation state. (2) resting conidia -  does not interact 
     * (I.e.,  the probability is zero). (3) otherwise (e.g., swelling conidia, 
     * germ-tubes, etc.) - high probability "PR_MA_PHAG." The fungus is 
     * phagocytosed, and the macrophage B-glucan receptor is set to the maximum 
     * activation state. 
     * @param mac
     * @param aspergillus
     * @return
     */
    static boolean macrophageAspergillus(Leukocyte mac, PositionalInfectiousAgent asp) {
    	Afumigatus aspergillus = (Afumigatus) asp;
    	if(mac.isEngaged())
            return true;
        if(!mac.isDead()) {
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != AspergillusIntracellularModel.RESTING_CONIDIA) {
                double prInteract = aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.HYPHAE ? Constants.PR_MA_HYPHAE : Constants.PR_MA_PHAG;
                //if(this.getExternalState() == 1)  prInteract *= Constants.NET_COUNTER_INHIBITION;
                if(Rand.getRand().randunif() < prInteract) {
                	intAspergillus(mac, aspergillus, aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != AspergillusIntracellularModel.HYPHAE);
                    if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.HYPHAE && 
                    		mac.getBooleanNetwork().hasPhenotype(new int[] {AspergillusMacrophage.M1, AspergillusMacrophage.M2B})){ 
                    	aspergillus.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, getDeadState(aspergillus, IntracellularModel.DYING));
                        if(aspergillus.getNextSepta() != null) //{ //{???
                        	aspergillus.getNextSepta().setRoot(true);
                        if(aspergillus.getNextBranch() != null)
                        	aspergillus.getNextBranch().setRoot(true);
                        //}
                    }else {
                        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.HYPHAE && mac.getBooleanNetwork().hasPhenotype(AspergillusMacrophage.M1)) {
                        	mac.setEngaged(true);
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * 
     * @param leukocyte
     * @param aspergillus
     * @param phagocytose
     */
	static void intAspergillus(Leukocyte leukocyte, PositionalInfectiousAgent aspergillus, boolean phagocytose) {
        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == AspergillusIntracellularModel.FREE) {
            if (aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.RESTING_CONIDIA || 
            	aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.SWELLING_CONIDIA || 
            	aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.STERILE_CONIDIA || phagocytose){
            	if (!leukocyte.isDead()) {
            		if(leukocyte.getPhagosome().size() < leukocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
                        aspergillus.getBooleanNetwork().setState(IntracellularModel.LOCATION, AspergillusIntracellularModel.INTERNALIZING);
                        aspergillus.setEngulfed(true);
                        leukocyte.getPhagosome().add(aspergillus);
                    }
                }
            }
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != AspergillusIntracellularModel.RESTING_CONIDIA) {
                //phagocyte.getBooleanNetwork().addPhenotype(LeukocyteIntracellularModel.INTERACTING);
                if(leukocyte instanceof Macrophage) {
                	((Macrophage)leukocyte).bind(aspergillus, 4);
                }
                    
                //else
                    //phagocyte.setStatusIteration(0);
            }
        }
    }
	
	/**
	 * This method returns the state "state" if the cell life status is "DEAD." 
	 * The state "state" is meant to be one of the dead states, such as "DYING." 
	 * To use this function to return other states is beyond the the intended 
	 * scope of this method.
	 * @param asp Afumigatus
	 * @param state
	 * @return
	 */
	static int getDeadState(PositionalInfectiousAgent asp, int state) {
		if(asp.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == IntracellularModel.DEAD)return IntracellularModel.DEAD;
		return state;
	}
	
	/**
     * Method handling the uptake of heme by Aspergillus. If the Aspergillus status 
     * is "HYPHAE," a fixed percent of heme in the voxel at position (x, y, z) is 
     * uptake. That is, the external heme will be decremented while the internal 
     * heme and iron will be incremented by that quantity.
     * @param afumigatus
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean aspergillusHemeUptake(PositionalInfectiousAgent asp, Molecule heme, int x, int y, int z) {
    	Afumigatus afumigatus = (Afumigatus) asp;
    	if(afumigatus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == AspergillusIntracellularModel.HYPHAE) {
        	double qtty = Constants.HEME_UP * heme.get(0, x, y, z);
        	afumigatus.incHeme(qtty);
        	afumigatus.incIronPool(qtty);
        	heme.dec(qtty, 0, x, y, z);
    	}
    	return true;
    }
    
    /**
     * This method only calls intAspergillus(Leukocyte leukocyte, Afumigatus aspergillus, boolean phagocytize)
     * @param leukocyte
     * @param aspergillus
     */
    static void intAspergillus(Leukocyte leukocyte, PositionalInfectiousAgent aspergillus) {
		CoupledInteractions.intAspergillus(leukocyte, aspergillus, false);
	}
	
}
