package edu.uf.primitives;

import edu.uf.interactable.Blood;
import edu.uf.interactable.Cell;
import edu.uf.interactable.Heme;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Iron;
import edu.uf.interactable.LPS;
import edu.uf.interactable.Lactoferrin;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Lipocalin2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.PneumocyteI;
import edu.uf.interactable.PneumocyteII;
import edu.uf.interactable.ROS;
import edu.uf.interactable.Siderophore;
import edu.uf.interactable.Transferrin;
import edu.uf.interactable.Afumigatus.Afumigatus;
import edu.uf.interactable.klebsiela.Enterobactin;
import edu.uf.interactable.klebsiela.Klebsiella;
import edu.uf.intracellularState.AspergillusIntracellularModel;
import edu.uf.intracellularState.AspergillusMacrophage;
import edu.uf.intracellularState.IntracellularModel;
import edu.uf.intracellularState.NeutrophilStateModel;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;


/**
 * This class contains static methods with primitive interactions between agents. 
 * The pattern followed here is that each method should have the two agents interacting 
 * as arguments, plus additional information, such as grid coordinates. The x, y, and z 
 * coordinates in the grid are especially important when one of the agents is a molecule. 
 * @author henriquedeassis
 *
 */
public class Interactions {
	
	
	/**
	 * Method handling the cell-molecule bind interaction. Upon calling this method, 
	 * the cell receptor for that molecule will be activated to a discreet level 
	 * between 0 and 4 based on the molecule concentration on the voxel. The values 
	 * x, y, and z are necessary to find the voxel. The value w is needed to find the 
	 * "state" of the molecule. Some but not all molecules have multiple states; for 
	 * example, siderophores can be free (0) or bound to iron (1). 
	 * <br/><br/>
	 * This method calls the cell method "bind," which selects the correct cell 
	 * receptor based on the molecule it receives. 
	 * @param cell
	 * @param molecule
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @param w axis position in the grid. Extra dimension for molecules that have more than one state (e.g. free-siderophore/siderophore-bound-to-iron)
	 * @return
	 */
	public static boolean bind(Cell cell, Molecule molecule, int x, int y, int z, int w) {
    	cell.bind(molecule, Util.activationFunction5(molecule.get(w, x, y, z), molecule.getKd()));
    	return true;
    }
    
	/**
	 * Method handling the molecules secretion by cells. 
	 * Upon calling this method, the cell will secrete 
	 * the molecule "mol" if and only if the cell is in 
	 * a secreting-mol phenotype. 
	 * <br/><br/>
	 * If the cell is in the secreting-mol phenotype, it will 
	 * increment the molecule quantity on the voxel at position 
	 * (x, y, z). I.e., the cell will secrete the molecule. 
	 * The amount of secreted molecule will be "qtty" multiplied 
	 * by the relative level of activation of the cell. Relative 
	 * activation levels can be 0.0, 0.25, 0.5, 0.75, or 1.0.  
	 * @param cell
	 * @param mol molecule to be secreted
	 * @param qtty amount of molecule to be secreted.
	 * @param x axis position in the grid
	 * @param y axis position in the grid
	 * @param z axis position in the grid
	 * @param w axis position in the grid. Extra dimension for molecules that have more than one state (e.g. free-siderophore/siderophore-bound-to-iron)
	 * @return
	 */
    public static boolean secrete(Cell cell, Molecule mol, double qtty, int x, int y, int z, int w) {
    	
    	if (cell.getBooleanNetwork().hasPhenotype(mol)) {
    		double level = cell.getBooleanNetwork().getPhenotype().get(mol.getPhenotype())/4.0; //CHANGE FROM 5.0 -> 4.0
    		mol.inc(qtty * level, w, x, y, z);
    	}
    	return true;
    }
    
    /**
     * Method handling the interaction between leukocytes and Dump (e.g., Adenosine). 
     * Dumps are inflammatory molecules released by necrotic cells. They do not form 
     * a subclass in the model; therefore, this method signature receives the more general 
     * molecule type. However, this method should not be used with molecules, not Dumps. 
     * <br/><br/>
     * Upon calling this method, one of two outcomes happens: (1) if the leukocyte is dead, 
     * the amount of Dump is incremented; (2) if the leukocyte is alive, it interacts with 
     * Dumb via the "bind" method, and the receptor is activated to a discreet level between 
     * 0 and 4 based on Dump levels in the voxel at position (x, y, z).  
     * @param leukocyte
     * @param dump
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @param qtty quantity of dump released upon cell death.
     * @return
     */
    public static boolean leukocuteDumpInteraction(Leukocyte leukocyte, Molecule dump, int x, int y, int z, double qtty) {
    	if(leukocyte.isDead()) {
    		dump.inc(qtty, 0, x, y, z); //CHANGE TO ADO_QTTY
		}else {
			bind(leukocyte, dump, x, y, z, 0);
		}
        return true;
    }
    
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
    public static boolean set(Molecule mol, Blood blood, double qtty, int x, int y, int z, int w) {
    	if(blood.hasBlood(x, y, z))
    		mol.set(qtty, w, x, y, z);
    	return true;
    }
    
    /**
     * This method kills a cell upon contact with a molecule. 
     * The probability of cell death grows asymptotically to 100% 
     * as the molecule quantity in the voxel tends to infinity. 
     * This method should only be used for pairs of cells and 
     * molecules for which the molecule is cytotoxic 
     * (e.g., bacterium and defensins).
     * @param cell
     * @param mol
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean kill(Cell cell, Molecule mol, int x, int y, int z) {
    	if(Util.activationFunction(mol.get(0, x, y, z), mol.getKd()))
    		cell.die();
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
    public static boolean setHemorrhage(Blood blood, PneumocyteI pneumocyteI, int status, int x, int y, int  z) {
    	if(pneumocyteI.isDead())
    		blood.setStatus(status, x, y, z);
		return true;
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
    public static boolean aspergillusHemeUptake(Afumigatus afumigatus, Heme heme, int x, int y, int z) {
    	if(afumigatus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE) {
        	double qtty = Constants.HEME_UP * heme.get(0, x, y, z);
        	afumigatus.incHeme(qtty);
        	afumigatus.incIronPool(qtty);
        	heme.dec(qtty, 0, x, y, z);
    	}
    	return true;
    }
    
    /**
     * Upon calling this method, all the iron inside the cell will be released 
     * into the voxel at position (x, y, z) if the cell has life status "status." 
     * This method was created to handle the release of iron upon cell death. 
     * However, there are several "dead" life statuses (e.g., necrotic, NETotic, 
     * pyroptotic, etc.) Therefore, this method does not impose a specific status 
     * for iron release. Nevertheless, passing a "non-dead" status to this method 
     * is beyond its scope.   
     * @param cell
     * @param status a target life status. This is meant to be one of the dead life status (e.g., necrotic, NETotic, pyroptotic, etc.)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean releaseIron(Cell cell, Iron iron, int status, int x, int y, int z) {
    	if (cell.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == status){
    		iron.inc(cell.getIronPool(), "Iron", x, y, z);
            cell.incIronPool(-cell.getIronPool());
            return true; 
    	}
        return false;
    }
    
    /**
     * Method handling the interaction between Transferrin and Lactoferrin. 
     * Transferrin and Lactoferrin have three forms or states: free, also called 
     * Apo-transferrin (Tf), and Apo-lactoferrin (Lac); Transferrin and Lactoferrin 
     * bound to one iron ion (TfFe and LacFe); and bound to two iron irons 
     * (TfFe2 and LacFe2). This method uses Michaelian kinetics to compute the 
     * chelation of iron from TfFe and TfFe2 by Lactoferrin (Lac and LacFe). The 
     * reaction implemented in this method is irreversible, and upon calling this 
     * method, TfFe and TfFe2 tend to decrease while LacFe and LacFe2 tend to increase. 
     * @param tf Transferrin
     * @param lac Lactoferrin
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean lactoferrinTransferrinChelation(Transferrin tf, Lactoferrin lac, int x, int y, int z) {
    	double dfe2dt = Util.michaelianKinetics(
    			tf.get("TfFe2", x, y, z), lac.get("Lactoferrin", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
    	);
        double dfedt  = Util.michaelianKinetics(
        		tf.get("TfFe", x, y, z), lac.get("Lactoferrin", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
        );

        double dfe2dtFe = Util.michaelianKinetics(
        		tf.get("TfFe2", x, y, z), lac.get("LactoferrinFe", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
        );
        double dfedtFe = Util.michaelianKinetics(
        		tf.get("TfFe", x, y, z), lac.get("LactoferrinFe", x, y, z), Constants.K_M_TF_LAC, Constants.STD_UNIT_T
        );

        if (dfe2dt + dfedt > lac.get("Lactoferrin", x, y, z)) {
            double rel = lac.get("Lactoferrin", x, y, z) / (dfe2dt + dfedt);
            dfe2dt = dfe2dt * rel;
            dfedt = dfedt * rel;
        }

        if (dfe2dtFe + dfedtFe > lac.get("LactoferrinFe", x, y, z)) {
            double rel = lac.get("LactoferrinFe", x, y, z) / (dfe2dtFe + dfedtFe);
            dfe2dtFe = dfe2dtFe * rel;
            dfedtFe = dfedtFe * rel;
        }

        tf.dec(dfe2dt + dfe2dtFe, "TfFe2", x, y, z);
        tf.inc(dfe2dt + dfe2dtFe, "TfFe", x, y, z);

        tf.dec(dfedt + dfedtFe, "TfFe", x, y, z);
        tf.inc(dfedt + dfedtFe, "Tf", x, y, z);

        lac.dec(dfe2dt + dfedt, "Lactoferrin", x, y, z);
        lac.inc(dfe2dt + dfedt, "LactoferrinFe", x, y, z);

        lac.dec(dfe2dtFe + dfedtFe, "LactoferrinFe", x, y, z);
        lac.inc(dfe2dtFe + dfedtFe, "LactoferrinFe2", x, y, z);

        return true;
    }
    
    /**
     * Method handling the uptake of Lactoferrin bound to iron 
     * (LacFe and LacFe2) by macrophages. Upon calling this method, 
     * the internal macrophage iron pool will increase while LacFe 
     * and LacFe2 in the voxel at position (x, y, z) will decrease. 
     * This method does not recycle Lactoferrin; therefore, free 
     * Lactoferrin does NOT increase.  
     * @param mac Macrophage
     * @param lac Lactoferrin
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean lactoferrinMacrophageUpatake(Macrophage mac, Lactoferrin lac, int x, int y, int z) {
    	double qttyFe2 = lac.get("LactoferrinFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
    			Constants.STD_UNIT_T;
        double qttyFe = lac.get("LactoferrinFe", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
        		Constants.STD_UNIT_T;
        
        qttyFe2 = qttyFe2 < lac.get("LactoferrinFe2", x, y, z) ? qttyFe2 : lac.get("LactoferrinFe2", x, y, z);
        qttyFe = qttyFe < lac.get("LactoferrinFe", x, y, z) ? qttyFe : lac.get("LactoferrinFe", x, y, z);

        lac.dec(qttyFe2, "LactoferrinFe2", x, y, z);
        lac.dec(qttyFe, "LactoferrinFe", x, y, z);
        mac.incIronPool(2 * qttyFe2 + qttyFe);
        return true;
    }
    
    /**
     * Method handling the interaction between macrophages and Transferrin. 
     * This method executes two opposing but not mutually exclusive actions. 
     * (1) Macrophage will uptake Transferrin bound to iron (TfFe and TfFe2), 
     * increasing its internal iron pool and decreasing the levels of TfFe and 
     * TfFe2 in the voxel at position (x, y, z). Transferrin is recycled; 
     * therefore, free Transferrin will also increase. 
     * <br/><br/>
     * (2) If the macrophage is in an iron-exporting status and not in an 
     * iron-holding phenotype, it will export iron. Exporting iron may undo 
     * the actions in (1). The macrophage will export iron proportional to its 
     * internal iron pool and free Transferrin amount in the voxel at position 
     * (x, y, z). Upon iron exportin, the internal iron pool and free Transferrin 
     * will decrease, and TfFe and TfFe2 will increase. 
     * @param mac Macrophage
     * @param tf Transferrin
     * @param status iron exporting "status"
     * @param phenotype iron holding phenotype. (This negates the status).
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean transferrinMacrophage(Macrophage mac, Transferrin tf, int status, int phenotype, int x, int y, int z) {
    	double qttyFe2 = tf.get("TfFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * Constants.STD_UNIT_T;
        double qttyFe  = tf.get("TfFe", x, y, z)  * Constants.MA_IRON_IMPORT_RATE * Constants.STD_UNIT_T;

        qttyFe2 = qttyFe2 < tf.get("TfFe2", x, y, z) ? qttyFe2 : tf.get("TfFe2", x, y, z);
        qttyFe = qttyFe < tf.get("TfFe", x, y, z) ? qttyFe : tf.get("TfFe", x, y, z);

        tf.dec(qttyFe2, "TfFe2", x, y, z);
        tf.dec(qttyFe, "TfFe", x, y, z);
        tf.inc(qttyFe2 + qttyFe, "Tf", x, y, z);
        mac.incIronPool(2 * qttyFe2 + qttyFe);
        if (mac.getBooleanNetwork().getBooleanNetwork()[status] == 1 && !mac.getBooleanNetwork().hasPhenotype(phenotype)) {
            double qtty = mac.getIronPool() * 
            		tf.get("Tf", x, y, z) * Constants.MA_IRON_EXPORT_RATE * Constants.STD_UNIT_T;
            qtty = qtty <= 2*tf.get("Tf", x, y, z) ? qtty : 2*tf.get("Tf", x, y, z);
            double relTfFe = Util.ironTfReaction(qtty, tf.get("Tf", x, y, z), tf.get("TfFe", x, y, z));
            double tffeQtty  = relTfFe*qtty;
            double tffe2Qtty = (qtty - tffeQtty)/2;
            tf.dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
            tf.inc(tffeQtty, "TfFe", x, y, z);
            tf.inc(tffe2Qtty, "TfFe2", x, y, z);
            mac.incIronPool(-qtty);
        }
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
    public static boolean lactoferrinDegranulation(Neutrophil neutr, Lactoferrin lac, int x, int y, int z) {
    	if (neutr.getBooleanNetwork().hasPhenotype(lac) && !neutr.hasDegranulated()){ 
    		lac.inc(Constants.LAC_QTTY, "Lactoferrin", x, y, z);
    		neutr.degranulate();
    	}
    	
        return true;
    }
    
    /**
     * Method handling the interaction between Transferrin (Tf) and iron 
     * or Lactoferrin (Lac) and iron. It can be used for both molecules. 
     * Upon calling this method, all the iron in the voxel is chelated by 
     * Tf or Lac up to their maximum capacity. 
     * @param tf Transferrin or Lactoferrin
     * @param iron
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean transferrinIronChelation(Molecule tf, Iron iron, int x, int y, int z) {
    	double qtty = iron.get("Iron", x, y, z);
        qtty  = qtty <= 2 * tf.get(0, x, y, z) + tf.get(1, x, y, z) ?
        		qtty : 2 * tf.get(0, x, y, z) + tf.get(1, x, y, z);
        double relTfFe = Util.ironTfReaction(qtty, tf.get(0, x, y, z), tf.get(1, x, y, z));
        double tffeQtty = relTfFe * qtty;
        double tffe2Qtty = (qtty - tffeQtty) / 2;
        tf.dec(tffeQtty + tffe2Qtty, 0, x, y, z);
        tf.inc(tffeQtty, 1, x, y, z);
        tf.inc(tffe2Qtty, 2, x, y, z);
        iron.dec(qtty, "Iron", x, y, z);
        return true; 
    }
    
    /**
     * Method handling the interaction between neutrophils and Aspergillus. 
     * If the neutrophil is not dead or NETotic, the method does one of three 
     * things depending on the Aspergillus status. (1) If the Aspergillus is 
     * a hyphae or germ tube, the neutrophil kills it with a predefined probability. 
     * The method will also call "bind" to and activate the neutrophil receptor 
     * to B-glucan to the maximum level. (2) If the Aspergillus is a swelling 
     * conidia, the neutrophil phagocytoses it with a predefined probability. 
     * The method will also call "bind" to and activate the neutrophil receptor 
     * to B-glucan to the maximum level. (3) If the Aspergillus is in other states 
     * (e.g., resting conidia), the method returns and does nothing.
     * @param neutrophil
     * @param aspergillus
     * @return
     */
    public static boolean neutrophilAspergillu(Neutrophil neutrophil, Afumigatus aspergillus) {
    	if(neutrophil.isEngaged())
            return true;
    	//System.out.println(this.hasPhenotype(NeutrophilStateModel.APOPTOTIC) + " " + this.hasPhenotype(NeutrophilStateModel.NETOTIC));
        if(!neutrophil.isDead() && !neutrophil.getBooleanNetwork().hasPhenotype(NeutrophilStateModel.NETOTIC)) {
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE || aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.GERM_TUBE) {
                double pr = Constants.PR_N_HYPHAE;
                //if(this.getExternalState() == 1)  pr *= Constants.NET_COUNTER_INHIBITION;
                //System.out.println(Rand.getRand().randunif());
                if (Rand.getRand().randunif() < pr) {
                    intAspergillus(neutrophil, aspergillus);
                    aspergillus.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, getDeadState(aspergillus, Cell.DYING));
                    neutrophil.bind(aspergillus, 4);
                    
                }else {
                	neutrophil.setEngaged(true);
                    //interac.setEngaged(true);
                    
                }
                if (Rand.getRand().randunif() < Constants.PR_N_PHAG) {
                	
                	//Afumigatus.intAspergillus(this, interac);
                    //this.bind(interac, 4);
                    
                }
            }else if (aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.SWELLING_CONIDIA) {
            	if (Rand.getRand().randunif() < Constants.PR_N_PHAG) {
            		intAspergillus(neutrophil, aspergillus);
            		neutrophil.bind(aspergillus, 4);
            	
            		//interac.setEngaged(true);
            	}else {
            		//interac.setEngaged(true);
            	}
            }
        }//if(this.hasPhenotype(NeutrophilStateModel.NETOTIC)) {
        //	interac.setEpithelialInhibition(interac.getEpithelialInhibition());
        	//interac.setNetGermBust(Constants.NET_COUNTER_INHIBITION);
        //}
        
        return true;
    }

    /**
     * Method handling the phagocytosis of apoptotic neutrophils with 
     * "NeutrophilStateModel" intracellular model by macrophages. If 
     * the neutrophil is in the status "NeutrophilStateModel.APOPTOTIC," 
     * all its iron will be transferred to the macrophage, and it will be 
     * set to "DEAD." 
     * <br/><br/>
     * <strong>This method has a very narrow scope, and its use is discouraged. 
     * This method should activate the PtS receptor in macrophages, but it does not. </strong>
     * @param cell
     * @param macrophage
     * @param status
     * @return
     */
    public static boolean macrophagePhagApoptoticNeutrophilS(Neutrophil cell, Macrophage macrophage) {
    	if (cell.getBooleanNetwork().hasPhenotype(NeutrophilStateModel.APOPTOTIC)) {
    		macrophage.incIronPool(cell.getIronPool());
    		cell.incIronPool(cell.getIronPool());
    		cell.die();
            //interact.bind(RECEPTOR_IDX);
    	}
        return true;
    }
    
    /**
     * Method handling the killing of type I pneumocytes by NETs. NETs kill 
     * type I pneumocytes with a probability if the boolean flag "control" 
     * is true. The method sets the flag to false and returns it. The flag 
     * can be used to ensure that if the NET does not kill the type I pneumocyte 
     * the first time the method is called, it will not kill in the future. 
     * This simulates a "single-time shot."  
     * @param neutrophil
     * @param pneumocyteI Type I pneumocyte
     * @param control a boolean flag
     * @return control
     */
    public static boolean typeIPneumocyteNET(Neutrophil neutrophil, PneumocyteI pneumocyteI, boolean control) {
    	if(neutrophil.getBooleanNetwork().hasPhenotype(NeutrophilStateModel.NETOTIC)) {
    		//cell.setInjury(true);
    		//if(cell.isInjury() || (control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
    		if((control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
    		//if(false) {
    			pneumocyteI.die();
    		}//else {
    			control = false;
    		//}
    	}
		return control;
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
    public static boolean typeIPneumocyteAspergillus(PneumocyteI typeI, Afumigatus aspergillus, boolean injury) {
    	if(typeI.isDead())return true;
		if(typeI.isDead())aspergillus.setEpithelialInhibition(1);
		if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE) {
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
     * Method handling the interaction between type II pneumocytes and Aspergillus. 
     * If the type II pneumocyte is not dead and the Aspergillus is not resting conidia, 
     * the interaction happens with a probability. If the interaction occurs, the 
     * Aspergillus method "bind" is called, and the B-glucan receptor is activated 
     * to the maximum level (4). 
     * @param typeII type II pneumocyte
     * @param aspergillus
     * @return
     */
    public static boolean typeIIPneumocyteAspergillus(PneumocyteII typeII, Afumigatus aspergillus) {
    	if(!typeII.isDead()) 
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) 
            	typeII.bind(aspergillus, 4);
        return true;
    }
    
    /**
     * Method handling the interaction between cells and ROS. Upon calling 
     * this method, the life status of the cell will be set to "status" with 
     * a probability. The probability is given by a Hill function that depends 
     * on the levels of ROS in the voxel at position (x, y, z).
     * @param cell
     * @param ros
     * @param status
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean rosActivation(Cell cell, ROS ros, int status, int x, int y, int z) {
    	if (Util.activationFunction(ros.get(0, x, y, z)*ros.get(0, x, y, z), Constants.Kd_H2O2*Constants.Kd_H2O2, Constants.VOXEL_VOL*Constants.VOXEL_VOL)) {
        	//System.out.println(this.get(0, x, y, z)/Constants.VOXEL_VOL + " " + Constants.Kd_H2O2);
    		cell.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, status);
        	//cell.getBooleanNetwork().getPhenotype().put(status, 4);
        }
        return true;
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
    public static boolean macrophageAspergillus(Macrophage mac, Afumigatus aspergillus) {
    	if(mac.isEngaged())
            return true;
        if(!mac.isDead()) {
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) {
                double prInteract = aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE ? Constants.PR_MA_HYPHAE : Constants.PR_MA_PHAG;
                //if(this.getExternalState() == 1)  prInteract *= Constants.NET_COUNTER_INHIBITION;
                if(Rand.getRand().randunif() < prInteract) {
                	intAspergillus(mac, aspergillus, aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.HYPHAE);
                    if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE && mac.getBooleanNetwork().hasPhenotype(new int[] {AspergillusMacrophage.M1, AspergillusMacrophage.M2B})){ 
                    	aspergillus.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, getDeadState(aspergillus, Cell.DYING));
                        if(aspergillus.getNextSepta() != null) {
                        	aspergillus.getNextSepta().setRoot(true);
                        if(aspergillus.getNextBranch() != null)
                        	aspergillus.getNextBranch().setRoot(true);
                        }
                    }else {
                        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE && mac.getBooleanNetwork().hasPhenotype(AspergillusMacrophage.M1)) {
                        	mac.setEngaged(true);
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * This method handles the interaction between leukocytes (e.g., neutrophils and macrophages) 
     * and Klebsiella pneumonia. If Klebsiella is free and the leukocyte phagosome is not full, 
     * the leukocyte will phagocytose the bacteria. The Klebsiella will become "INTERNALIZING" 
     * (I.e., not free), and if the leukocyte is a macrophage, LPS will activate its TLR4 to the 
     * maximum level (4). This method just calls the method intKlebsiela.
     * @param leuk a leukocyte
     * @param klebsiella
     * @return
     */
    public static boolean intKlebsiella(Leukocyte leuk, Klebsiella klebsiella) {
    	intKlebsiela(leuk, klebsiella);
    	return true;
    }
    
    /**
     * Method handling the interaction between infectious agents (e.g., Afumigatus and Klebsiella) 
     * and siderophores (e.g., TAFC, Aerobactin, etc). If the infectious agent is in a state where 
     * it is able to uptake that particular siderophore, the local quantity of siderophore bound to 
     * iron in the voxel at position (x, y, z) is decreased, and the internal iron pool is increased. 
     * This method does not recycle siderophores.  
     * @param agent infectious angent (e.g., Afumigatus and Klebsiella)
     * @param mol a siderophore (e.g., TAFC, Aerobactin, etc.)
     * @param rate
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean uptakeSiderophore(InfectiousAgent agent, Siderophore mol, double rate, int x, int y, int z) {
    	if(agent.isUptakingSiderophore(mol)) {
    		double qtty = mol.get("SIDBI", x, y, z) * rate;
            qtty = qtty < mol.get("SIDBI", x, y, z) ? qtty : mol.get("SIDBI", x, y, z);

            mol.dec(qtty, "SIDBI", x, y, z); 
            agent.incIronPool(qtty);
    	}
    	return true;
    }
    
    /**
     * Method handling the secretion of siderophores (e.g., TAFC, Aerobactin, etc.) by infectious 
     * agents (e.g., Afumigatus and Klebsiella). If the infectious agent is in a siderophore-secreting 
     * phenotype, the siderophore quantity in the voxel at the position (x, y, z) will be increased. 
     * @param agent infectious angent (e.g., Afumigatus and Klebsiella)
     * @param mol a siderophore (e.g., TAFC, Aerobactin, etc.)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean secreteSiderophore(InfectiousAgent agent, Siderophore mol, int x, int y, int z) {
    	if(agent.isSecretingSiderophore(mol))
    		mol.inc(mol.getSiderophoreQtty(), "SID", x, y, z);
		
    	return true;
    }
    
    /**
     * Method handling the interaction between siderophores (e.g., TAFC, Aerobactin, etc.) 
     * and iron. Upon calling this method, all the iron in the voxel at position (x, y, z) 
     * is chelated by the siderophore in the same voxel up to its maximum capacity. Iron will 
     * decrease, potentially becoming zero, free-siderophore will decrease, and siderophore 
     * bound to iron will increase. 
     * @param iron
     * @param mol a siderophore (e.g., TAFC, Aerobactin, etc.)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean siderophoreIronChelation(Iron iron, Siderophore mol, int x, int y, int z) {
    	double qttyIron = iron.get("Iron", x, y, z);
        double qttyTafc = mol.get(0, x, y, z);
        double qtty = qttyTafc < qttyIron ? qttyTafc : qttyIron;
        mol.dec(qtty, 0, x, y, z);
        mol.inc(qtty, 1, x, y, z);
        iron.dec(qtty, "Iron", x, y, z);

        return true; 
    }
    
    /**
     * This method handles the interaction between Transferrin and siderophores 
     * (e.g., TAFC, Aerobactin, etc.). Upon interaction, the siderophore in the 
     * voxel at position (x, y, z) will chelate iron from Transferrin bound to 
     * iron (TfFe and TfFe2) at the same voxel. TfFe, TfFe2, and free siderophore 
     * will decrease while Apo-Transferrin and siderophore-bound-to-iron will 
     * increase. The chelation reaction is handled by Michaelean kinetics.  
     * @param tf Transferrin
     * @param mol a siderophore (e.g., TAFC, Aerobactin, etc.)
     * @param km the siderophore-Transferrin Michaelian-constant 
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean siderophoreTransferrinChelation(Transferrin tf, Siderophore mol, double km, int x, int y, int z) {
    	double dfe2dt = Util.michaelianKinetics(
    			tf.get(2, x, y, z), mol.get("SID", x, y, z), km, Constants.STD_UNIT_T
    	);
        double dfedt  = Util.michaelianKinetics(
        		tf.get(1, x, y, z), mol.get("SID", x, y, z), km, Constants.STD_UNIT_T
        );

        if (dfe2dt + dfedt > mol.get("SID", x, y, z)) {
            double rel = mol.get("SID", x, y, z) / (dfe2dt + dfedt);
            dfe2dt = dfe2dt * rel;
            dfedt = dfedt * rel;
        }
        tf.dec(dfe2dt, 2, x, y, z);
        tf.inc(dfe2dt, 1, x, y, z);

        tf.dec(dfedt, 1, x, y, z);
        tf.inc(dfedt, 0, x, y, z);

        mol.inc(dfe2dt + dfedt, "SIDBI", x, y, z);
        mol.dec(dfe2dt + dfedt, "SID", x, y, z);

        return true;
    }
    
    /**
     * Method handling the interaction between lipocalin-2 and enterobactin 
     * (one of the Klebsiella siderophores). Upon interaction, lipocalin-2 
     * and enterobactin (both free and bound to iron) in the voxel at position 
     * (x, y, z) are consumed. The method computes a reaction rate with 
     * Michaelian kinetics and decrements the three species (lipocalin-2, 
     * free enterobactin, and enterobactin-bound-to-iron).
     * @param lip lipocalin-2
     * @param ent Enterobactin (a Klebsiella siderophore)
     * @param x axis position in the grid
     * @param y axis position in the grid
     * @param z axis position in the grid
     * @return
     */
    public static boolean lipocalin2EnterobactinInteraction(Lipocalin2 lip, Enterobactin ent, int x, int y, int z) {
    	for(int i = 0; i < 1; i++) {
    		int j = Rand.getRand().randunif(0, 1);
    		switch(j) {
    		case 0:
    			double dsdt  = Util.michaelianKinetics(
    					lip.get(0, x, y, z), ent.get("SID", x, y, z), Constants.K_M_LPC2_ENT, Constants.STD_UNIT_T
                );
    			ent.dec(dsdt, "SID", x, y, z);
    			lip.dec(dsdt, "LPC", x, y, z);
    			break;
    		case 1:
    			double dsidt  = Util.michaelianKinetics(
    					lip.get(0, x, y, z), ent.get("SIDBI", x, y, z), Constants.K_M_LPC2_ENT, Constants.STD_UNIT_T
                );
    			ent.dec(dsidt, "SIDBI", x, y, z);
    			lip.dec(dsidt, "LPC", x, y, z);
    			lip.inc(dsidt, "LPCBI", x, y, z);
    			break;
    			default:
    				System.err.println("No such reaction " + j + "!");
    		}
    	}
    	return true;
    }
    
    /**
     * This method only calls intAspergillus(Leukocyte leukocyte, Afumigatus aspergillus, boolean phagocytize)
     * @param leukocyte
     * @param aspergillus
     */
    public static void intAspergillus(Leukocyte leukocyte, Afumigatus aspergillus) {
		intAspergillus(leukocyte, aspergillus, false);
	}	
	
    /**
     * 
     * @param leukocyte
     * @param aspergillus
     * @param phagocytose
     */
	public static void intAspergillus(Leukocyte leukocyte, Afumigatus aspergillus, boolean phagocytose) {
        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == Afumigatus.FREE) {
            if (aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.RESTING_CONIDIA || aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.SWELLING_CONIDIA || aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.STERILE_CONIDIA || phagocytose){
            	if (!leukocyte.isDead()) {
            		if(leukocyte.getPhagosome().size() < leukocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
                        aspergillus.getBooleanNetwork().setState(IntracellularModel.LOCATION, Afumigatus.INTERNALIZING);
                        aspergillus.setEngulfed(true);
                        leukocyte.getPhagosome().add(aspergillus);
                    }
                }
            }
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) {
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
	 * Method handling the mechanics of Klebsiela-leukocyte interaction. If Klebsiella is 
	 * the free phenotype and the leukocyte phagosome is not full, the leukocyte will 
	 * phagocytose the bacteria. The "spatial" status of the cells (free/internalizing) 
	 * is coded as a phenotype in this model. Upon interaction, the Klebsiella will become 
	 * "INTERNALIZING" (I.e., not free). If the leukocyte is a macrophage, LPS will activate 
	 * its TLR4 to the maximum level (4). 
	 * @param phagocyte
	 * @param klebsiela
	 */
	public static void intKlebsiela(Leukocyte leukocyte, Klebsiella klebsiela) {
        if(klebsiela.getBooleanNetwork().hasPhenotype(Klebsiella.FREE)) {
            if (!leukocyte.isDead()) {
            	if(leukocyte.getPhagosome().size() < leukocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
            		klebsiela.getBooleanNetwork().setState(IntracellularModel.LOCATION, Klebsiella.INTERNALIZING);
            		klebsiela.setEngulfed(true);
            		leukocyte.getPhagosome().add(klebsiela);
                }
            }
            //phagocyte.getBooleanNetwork().addPhenotype(Cell.INTERACTING);
            if(leukocyte instanceof Macrophage) {
            	leukocyte.bind(LPS.getMolecule(), 4);
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
	private static int getDeadState(Afumigatus asp, int state) {
		if(asp.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == Afumigatus.DEAD)return Afumigatus.DEAD;
		return state;
	}
    
}
