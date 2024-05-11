package edu.uf.primitives;

import edu.uf.interactable.Blood;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Iron;
import edu.uf.interactable.LPS;
import edu.uf.interactable.Leukocyte;
import edu.uf.interactable.Lipocalin2;
import edu.uf.interactable.Macrophage;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Neutrophil;
import edu.uf.interactable.Siderophore;
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

public class Interactions {
	
	public static boolean bind(Cell cell, Molecule molecule, int x, int y, int z, int w) {
    	cell.bind(molecule, Util.activationFunction5(molecule.get(w, x, y, z), molecule.getKd()));
    	return true;
    }
    
    public static boolean secrete(Cell cell, Molecule molecule, double qtty, int x, int y, int z, int w) {
    	
    	if (cell.getBooleanNetwork().hasPhenotype(molecule)) {
    		double level = cell.getBooleanNetwork().getPhenotype().get(molecule.getPhenotype())/5.0;
    		molecule.inc(qtty * level, w, x, y, z);
    	}
    	return true;
    }
    
    public static boolean leukocuteDumpInteraction(Leukocyte cell, Molecule mol, int x, int y, int z, double qtty) {
    	if(cell.isDead()) {
    		mol.inc(qtty, 0, x, y, z); //CHANGE TO ADO_QTTY
		}else {
			bind(cell, mol, x, y, z, 0);
		}
        return true;
    }
    
    public static boolean set(Molecule mol, double qtty, int x, int y, int z, int w) {
    	if(Blood.getBlood().hasBlood(x, y, z))
    		mol.set(qtty, w, x, y, z);
    	return true;
    }
    
    public static boolean kill(Cell cell, Molecule mol, int x, int y, int z) {
    	if(Util.activationFunction(mol.get(0, x, y, z), mol.getKd()))
    		cell.die();
    	return true;
    }
    
    public static boolean sethemorrhage(Blood blood, Cell cell, int status, int x, int y, int  z) {
    	if(cell.isDead())
			blood.setStatus(status, x, y, z);
		return true;
    }
    
    public static boolean aspergillusHemeUptake(Afumigatus afumigatus, Molecule mol, int x, int y, int z) {
    	if(afumigatus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE) {
        	double qtty = Constants.HEME_UP * mol.get(0, x, y, z);
        	afumigatus.incHeme(qtty);
        	afumigatus.incIronPool(qtty);
        	mol.dec(qtty, 0, x, y, z);
    	}
    	return true;
    }
    
    public static boolean releaseIron(Cell cell, Iron iron, int status, int x, int y, int z) {
    	if (cell.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == status){
    		iron.inc(cell.getIronPool(), "Iron", x, y, z);
            cell.incIronPool(-cell.getIronPool());
            return true; 
    	}
        return false;
    }
    
    public static boolean lactoferrinTransferrinChelation(Molecule tf, Molecule lac, int x, int y, int z) {
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
    
    public static boolean lactoferrinMacrophageUpatake(Cell cell, Molecule mol, int x, int y, int z) {
    	double qttyFe2 = mol.get("LactoferrinFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
    			Constants.STD_UNIT_T;
        double qttyFe = mol.get("LactoferrinFe", x, y, z) * Constants.MA_IRON_IMPORT_RATE * 
        		Constants.STD_UNIT_T;
        
        qttyFe2 = qttyFe2 < mol.get("LactoferrinFe2", x, y, z) ? qttyFe2 : mol.get("LactoferrinFe2", x, y, z);
        qttyFe = qttyFe < mol.get("LactoferrinFe", x, y, z) ? qttyFe : mol.get("LactoferrinFe", x, y, z);

        mol.dec(qttyFe2, "LactoferrinFe2", x, y, z);
        mol.dec(qttyFe, "LactoferrinFe", x, y, z);
        cell.incIronPool(2 * qttyFe2 + qttyFe);
        return true;
    }
    
    public static boolean transferrinMacrophage(Cell cell, Molecule mol, int status, int phenotype, int x, int y, int z) {
    	double qttyFe2 = mol.get("TfFe2", x, y, z) * Constants.MA_IRON_IMPORT_RATE * Constants.STD_UNIT_T;
        double qttyFe  = mol.get("TfFe", x, y, z)  * Constants.MA_IRON_IMPORT_RATE * Constants.STD_UNIT_T;

        qttyFe2 = qttyFe2 < mol.get("TfFe2", x, y, z) ? qttyFe2 : mol.get("TfFe2", x, y, z);
        qttyFe = qttyFe < mol.get("TfFe", x, y, z) ? qttyFe : mol.get("TfFe", x, y, z);

        mol.dec(qttyFe2, "TfFe2", x, y, z);
        mol.dec(qttyFe, "TfFe", x, y, z);
        mol.inc(qttyFe2 + qttyFe, "Tf", x, y, z);
        cell.incIronPool(2 * qttyFe2 + qttyFe);
        if (cell.getBooleanNetwork().getBooleanNetwork()[status] == 1 && !cell.getBooleanNetwork().hasPhenotype(phenotype)) {
            double qtty = cell.getIronPool() * 
            		mol.get("Tf", x, y, z) * Constants.MA_IRON_EXPORT_RATE * Constants.STD_UNIT_T;
            qtty = qtty <= 2*mol.get("Tf", x, y, z) ? qtty : 2*mol.get("Tf", x, y, z);
            double relTfFe = Util.ironTfReaction(qtty, mol.get("Tf", x, y, z), mol.get("TfFe", x, y, z));
            double tffeQtty  = relTfFe*qtty;
            double tffe2Qtty = (qtty - tffeQtty)/2;
            mol.dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
            mol.inc(tffeQtty, "TfFe", x, y, z);
            mol.inc(tffe2Qtty, "TfFe2", x, y, z);
            cell.incIronPool(-qtty);
        }
        return true;
    }
    
    public static boolean lactoferrinDegranulation(Neutrophil cell, Molecule mol, int x, int y, int z) {
    	if (cell.getBooleanNetwork().hasPhenotype(mol) && !cell.hasDegranulated()){ 
    		mol.inc(Constants.LAC_QTTY, "Lactoferrin", x, y, z);
    		cell.degranulate();
    	}
    	
        return true;
    }
    
    public static boolean transferrinIronChelation(Molecule tf, Molecule iron, int x, int y, int z) {
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

    public static boolean macrophagePhagApoptotic(Cell cell, Macrophage macrophage, int status) {
    	if (cell.getBooleanNetwork().hasPhenotype(status)) {//change so that APOPTOTIC becomes a general phenotype that can be applied to any cell
    		macrophage.incIronPool(cell.getIronPool());
    		cell.incIronPool(cell.getIronPool());
    		cell.die();
            //interact.bind(RECEPTOR_IDX);
    	}
        return true;
    }
    
    public static boolean typeIPneumocyteNET(Cell neutrophil, Cell cell, boolean control) {
    	if(neutrophil.getBooleanNetwork().hasPhenotype(NeutrophilStateModel.NETOTIC)) {
    		//cell.setInjury(true);
    		//if(cell.isInjury() || (control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
    		if((control && Rand.getRand().randunif() < Constants.PR_NET_KILL_EPI)) {
    		//if(false) {
    			cell.die();
    		}//else {
    			control = false;
    		//}
    	}
		return control;
    }
    
    public static boolean typeIPneumocyteAspergillus(Cell typeI, Afumigatus aspergillus, boolean injury) {
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
    
    public static boolean typeIIPneumocyteAspergillus(Cell typeII, Cell aspergillus) {
    	if(!typeII.isDead()) 
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) 
            	typeII.bind(aspergillus, 4);
        return true;
    }
    
    public static boolean rosActivation(Cell cell, Molecule molecule, int status, int x, int y, int z) {
    	if (Util.activationFunction(molecule.get(0, x, y, z)*molecule.get(0, x, y, z), Constants.Kd_H2O2*Constants.Kd_H2O2, Constants.VOXEL_VOL*Constants.VOXEL_VOL)) {
        	//System.out.println(this.get(0, x, y, z)/Constants.VOXEL_VOL + " " + Constants.Kd_H2O2);
    		cell.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, status);
        	//cell.getBooleanNetwork().getPhenotype().put(status, 4);
        }
        return true;
    }
    
    public static boolean macrophageAspergillus(Leukocyte cell, Afumigatus aspergillus) {
    	if(cell.isEngaged())
            return true;
        if(!cell.isDead()) {
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) {
                double prInteract = aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE ? Constants.PR_MA_HYPHAE : Constants.PR_MA_PHAG;
                //if(this.getExternalState() == 1)  prInteract *= Constants.NET_COUNTER_INHIBITION;
                if(Rand.getRand().randunif() < prInteract) {
                	intAspergillus(cell, aspergillus, aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.HYPHAE);
                    if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE && cell.getBooleanNetwork().hasPhenotype(new int[] {AspergillusMacrophage.M1, AspergillusMacrophage.M2B})){ 
                    	aspergillus.getBooleanNetwork().setState(IntracellularModel.LIFE_STATUS, getDeadState(aspergillus, Cell.DYING));
                        if(aspergillus.getNextSepta() != null) {
                        	aspergillus.getNextSepta().setRoot(true);
                        if(aspergillus.getNextBranch() != null)
                        	aspergillus.getNextBranch().setRoot(true);
                        }
                    }else {
                        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.HYPHAE && cell.getBooleanNetwork().hasPhenotype(AspergillusMacrophage.M1)) {
                        	cell.setEngaged(true);
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean intKlebsiella(Leukocyte cell, Klebsiella klebsiella) {
    	intKlebsiela(cell, klebsiella);
    	return true;
    }
    
    public static boolean uptakeSiderophore(InfectiousAgent agent, Siderophore mol, double rate, int x, int y, int z) {
    	if(agent.isUptakingSiderophore(mol)) {
    		double qtty = mol.get("SIDBI", x, y, z) * rate;
            qtty = qtty < mol.get("SIDBI", x, y, z) ? qtty : mol.get("SIDBI", x, y, z);

            mol.dec(qtty, "SIDBI", x, y, z); 
            agent.incIronPool(qtty);
    	}
    	return true;
    }
    
    public static boolean secreteSiderophore(InfectiousAgent agent, Siderophore mol, int x, int y, int z) {
    	if(agent.isSecretingSiderophore(mol))
    		mol.inc(mol.getSiderophoreQtty(), "SID", x, y, z);
		
    	return true;
    }
    
    public static boolean siderophoreIronChelation(Iron iron, Molecule mol, int x, int y, int z) {
    	double qttyIron = iron.get("Iron", x, y, z);
        double qttyTafc = mol.get(0, x, y, z);
        double qtty = qttyTafc < qttyIron ? qttyTafc : qttyIron;
        mol.dec(qtty, 0, x, y, z);
        mol.inc(qtty, 1, x, y, z);
        iron.dec(qtty, "Iron", x, y, z);

        return true; 
    }
    
    public static boolean siderophoreTransferrinChelation(Molecule tf, Molecule mol, double km, int x, int y, int z) {
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
    
    public static void intAspergillus(Leukocyte phagocyte, Afumigatus aspergillus) {
		intAspergillus(phagocyte, aspergillus, false);
	}	
	
	public static void intAspergillus(Leukocyte phagocyte, Afumigatus aspergillus, boolean phagocytize) {
        if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.LOCATION) == Afumigatus.FREE) {
            if (aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.RESTING_CONIDIA || aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.SWELLING_CONIDIA || aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) == Afumigatus.STERILE_CONIDIA || phagocytize){
            	if (!phagocyte.isDead()) {
            		if(phagocyte.getPhagosome().size() < phagocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
                        aspergillus.getBooleanNetwork().setState(IntracellularModel.LOCATION, Afumigatus.INTERNALIZING);
                        aspergillus.setEngulfed(true);
                        phagocyte.getPhagosome().add(aspergillus);
                    }
                }
            }
            if(aspergillus.getBooleanNetwork().getState(AspergillusIntracellularModel.STATUS) != Afumigatus.RESTING_CONIDIA) {
                //phagocyte.getBooleanNetwork().addPhenotype(LeukocyteIntracellularModel.INTERACTING);
                if(phagocyte instanceof Macrophage) {
                	((Macrophage)phagocyte).bind(aspergillus, 4);
                }
                    
                //else
                    //phagocyte.setStatusIteration(0);
            }
        }
    }
	
	public static void intKlebsiela(Leukocyte phagocyte, Klebsiella klebsiela) {
        if(klebsiela.getBooleanNetwork().hasPhenotype(Klebsiella.FREE)) {
            if (!phagocyte.isDead()) {
            	if(phagocyte.getPhagosome().size() < phagocyte.getMaxCell()) {
                        //phagocyte.phagosome.hasConidia = true;
            		klebsiela.getBooleanNetwork().setState(IntracellularModel.LOCATION, Klebsiella.INTERNALIZING);
            		klebsiela.setEngulfed(true);
                    phagocyte.getPhagosome().add(klebsiela);
                }
            }
            //phagocyte.getBooleanNetwork().addPhenotype(Cell.INTERACTING);
            if(phagocyte instanceof Macrophage) {
            	phagocyte.bind(LPS.getMolecule(), 4);
            }
        }
    }
	
	private static int getDeadState(Afumigatus asp, int state) {
		if(asp.getBooleanNetwork().getState(IntracellularModel.LIFE_STATUS) == Afumigatus.DEAD)return Afumigatus.DEAD;
		return state;
	}
    
}
