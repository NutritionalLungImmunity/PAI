/*
 * Interactions.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_PRIMITIVES_INTERACTIONS_H_
#define EDU_UF_PRIMITIVES_INTERACTIONS_H_

#include <memory>
#include "../interactable/Cell.h"
//#include "../interactable/InfectiousAgent.h"
#include "../interactable/Leukocyte.h"
#include "../interactable/Molecule.h"
//#include "../interactable/PneumocyteI.h"
//#include "../interactable/PositionalInfectiousAgent.h"
#include "../interactable/Siderophore.h"
#include "../intracellularState/AspergillusIntracellularModel.h"
//#include "../intracellularState/IntracellularModel.h"
#include "../intracellularState/NeutrophilStateModel.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../utils/Util.h"
#include "CoupledInteractions.h"

namespace edu {
namespace uf {
namespace primitives {

using namespace edu::uf;
using namespace interactable;
using namespace intracellularState;
using namespace utils;

class Interactions {
public:
	static bool bind(Cell* cell, Molecule* molecule, int x, int y, int z, int w) {
	    cell->bind(molecule, Util::activationFunction5(molecule->get(w, x, y, z), molecule->getKd()));
	    return true;
	}

	static bool secrete(Cell* cell, Molecule* mol, double qtty, int x, int y, int z, int w) {
		//printf("%s\t%s\t%d\n", cell->getName().c_str(), mol->getName().c_str(), cell->getBooleanNetwork()->hasPhenotype(*mol));
	    if (cell->getBooleanNetwork()->hasPhenotype(*mol)) {
	        double level = cell->getBooleanNetwork()->getPhenotype().at(mol->getPhenotype()) / 4.0;
	        mol->inc(qtty * level, w, x, y, z);
	    }
	    return true;
	}

	static bool leukocuteDampInteraction(Leukocyte* leukocyte, Molecule* damp, int x, int y, int z, double qtty) {
	    if (leukocyte->isDead()) {
	        damp->inc(qtty, 0, x, y, z);
	    } else {
	        bind(leukocyte, damp, x, y, z, 0);
	    }
	    return true;
	}

	/*static bool set(Molecule* mol, Cell* blood, double qtty, int x, int y, int z, int w) {
	    return primitives::Coupledset(mol, blood, qtty, x, y, z, w);
	}*/

	static bool kill(Cell* cell, Molecule* mol, int x, int y, int z) {
	    if (Util::activationFunction(mol->get(0, x, y, z), mol->getKd())) {
	        cell->die();
	    }
	    return true;
	}

	/*static bool setHemorrhage(Cell* blood, Cell* pneumocyteI, int status, int x, int y, int z) {
	    return primitives::CoupledsetHemorrhage(blood, pneumocyteI, status, x, y, z);
	}*/

	/*static bool aspergillusHemeUptake(PositionalInfectiousAgent* afumigatus, Molecule* heme, int x, int y, int z) {
	    return primitives::CoupledaspergillusHemeUptake(afumigatus, heme, x, y, z);
	}*/

	static bool releaseIron(Cell* cell, Molecule* iron, int status, int x, int y, int z) {
	    if (cell->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == status) {
	        iron->inc(cell->getIronPool(), "Iron", x, y, z);
	        cell->incIronPool(-cell->getIronPool());
	        return true;
	    }
	    return false;
	}

	static bool lactoferrinTransferrinChelation(Molecule* tf, Molecule* lac, int x, int y, int z) {
	    double dfe2dt = Util::michaelianKinetics(tf->get("TfFe2", x, y, z), lac->get("Lactoferrin", x, y, z), constexprants::K_M_TF_LAC, constexprants::STD_UNIT_T);
	    double dfedt = Util::michaelianKinetics(tf->get("TfFe", x, y, z), lac->get("Lactoferrin", x, y, z), constexprants::K_M_TF_LAC, constexprants::STD_UNIT_T);

	    double dfe2dtFe = Util::michaelianKinetics(tf->get("TfFe2", x, y, z), lac->get("LactoferrinFe", x, y, z), constexprants::K_M_TF_LAC, constexprants::STD_UNIT_T);
	    double dfedtFe = Util::michaelianKinetics(tf->get("TfFe", x, y, z), lac->get("LactoferrinFe", x, y, z), constexprants::K_M_TF_LAC, constexprants::STD_UNIT_T);

	    if (dfe2dt + dfedt > lac->get("Lactoferrin", x, y, z)) {
	        double rel = lac->get("Lactoferrin", x, y, z) / (dfe2dt + dfedt);
	        dfe2dt = dfe2dt * rel;
	        dfedt = dfedt * rel;
	    }

	    if (dfe2dtFe + dfedtFe > lac->get("LactoferrinFe", x, y, z)) {
	        double rel = lac->get("LactoferrinFe", x, y, z) / (dfe2dtFe + dfedtFe);
	        dfe2dtFe = dfe2dtFe * rel;
	        dfedtFe = dfedtFe * rel;
	    }

	    tf->dec(dfe2dt + dfe2dtFe, "TfFe2", x, y, z);
	    tf->inc(dfe2dt + dfe2dtFe, "TfFe", x, y, z);

	    tf->dec(dfedt + dfedtFe, "TfFe", x, y, z);
	    tf->inc(dfedt + dfedtFe, "Tf", x, y, z);

	    lac->dec(dfe2dt + dfedt, "Lactoferrin", x, y, z);
	    lac->inc(dfe2dt + dfedt, "LactoferrinFe", x, y, z);

	    lac->dec(dfe2dtFe + dfedtFe, "LactoferrinFe", x, y, z);
	    lac->inc(dfe2dtFe + dfedtFe, "LactoferrinFe2", x, y, z);

	    return true;
	}

	static bool lactoferrinMacrophageUpatake(Leukocyte* mac, Molecule* lac, int x, int y, int z) {
	    double qttyFe2 = lac->get("LactoferrinFe2", x, y, z) * constexprants::MA_IRON_IMPORT_RATE * constexprants::STD_UNIT_T;
	    double qttyFe = lac->get("LactoferrinFe", x, y, z) * constexprants::MA_IRON_IMPORT_RATE * constexprants::STD_UNIT_T;

	    qttyFe2 = qttyFe2 < lac->get("LactoferrinFe2", x, y, z) ? qttyFe2 : lac->get("LactoferrinFe2", x, y, z);
	    qttyFe = qttyFe < lac->get("LactoferrinFe", x, y, z) ? qttyFe : lac->get("LactoferrinFe", x, y, z);

	    lac->dec(qttyFe2, "LactoferrinFe2", x, y, z);
	    lac->dec(qttyFe, "LactoferrinFe", x, y, z);
	    mac->incIronPool(2 * qttyFe2 + qttyFe);
	    return true;
	}

	static bool transferrinMacrophage(Leukocyte* mac, Molecule* tf, int status, int phenotype, int x, int y, int z) {
	    double qttyFe2 = tf->get("TfFe2", x, y, z) * constexprants::MA_IRON_IMPORT_RATE * constexprants::STD_UNIT_T;
	    double qttyFe = tf->get("TfFe", x, y, z) * constexprants::MA_IRON_IMPORT_RATE * constexprants::STD_UNIT_T;

	    qttyFe2 = qttyFe2 < tf->get("TfFe2", x, y, z) ? qttyFe2 : tf->get("TfFe2", x, y, z);
	    qttyFe = qttyFe < tf->get("TfFe", x, y, z) ? qttyFe : tf->get("TfFe", x, y, z);

	    tf->dec(qttyFe2, "TfFe2", x, y, z);
	    tf->dec(qttyFe, "TfFe", x, y, z);
	    tf->inc(qttyFe2 + qttyFe, "Tf", x, y, z);
	    mac->incIronPool(2 * qttyFe2 + qttyFe);
	    if (mac->getBooleanNetwork()->getBooleanNetwork()[status] == 1 && !mac->getBooleanNetwork()->hasPhenotype(phenotype)) {
	        double qtty = mac->getIronPool() * tf->get("Tf", x, y, z) * constexprants::MA_IRON_EXPORT_RATE * constexprants::STD_UNIT_T;
	        qtty = qtty <= 2 * tf->get("Tf", x, y, z) ? qtty : 2 * tf->get("Tf", x, y, z);
	        double relTfFe = Util::ironTfReaction(qtty, tf->get("Tf", x, y, z), tf->get("TfFe", x, y, z));
	        double tffeQtty = relTfFe * qtty;
	        double tffe2Qtty = (qtty - tffeQtty) / 2;
	        tf->dec(tffeQtty + tffe2Qtty, "Tf", x, y, z);
	        tf->inc(tffeQtty, "TfFe", x, y, z);
	        tf->inc(tffe2Qtty, "TfFe2", x, y, z);
	        mac->incIronPool(-qtty);
	    }
	    return true;
	}

	static bool lactoferrinDegranulation(Leukocyte* neutr, Molecule* lac, int x, int y, int z) {
	    return primitives::CoupledInteractions::lactoferrinDegranulation(neutr, lac, x, y, z);
	}

	static bool transferrinIronChelation(Molecule* tf, Molecule* iron, int x, int y, int z) {
	    double qtty = iron->get("Iron", x, y, z);
	    qtty = qtty <= 2 * tf->get(0, x, y, z) + tf->get(1, x, y, z) ? qtty : 2 * tf->get(0, x, y, z) + tf->get(1, x, y, z);
	    double relTfFe = Util::ironTfReaction(qtty, tf->get(0, x, y, z), tf->get(1, x, y, z));
	    double tffeQtty = relTfFe * qtty;
	    double tffe2Qtty = (qtty - tffeQtty) / 2;
	    tf->dec(tffeQtty + tffe2Qtty, 0, x, y, z);
	    tf->inc(tffeQtty, 1, x, y, z);
	    tf->inc(tffe2Qtty, 2, x, y, z);
	    iron->dec(qtty, "Iron", x, y, z);
	    return true;
	}

	static bool neutrophilAspergillus(Leukocyte* neutrophil, PositionalInfectiousAgent* aspergillus, int x, int y, int z) {
	    if (neutrophil->isEngaged())
	        return true;
	    if (!neutrophil->isDead() && !neutrophil->getBooleanNetwork()->hasPhenotype(NeutrophilStateModel::NETOTIC)) {
	    	//printf("3.n\n");
	    	if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE ||
	            aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::GERM_TUBE) {
	    		//printf("4.n\n");
	            double pr = constexprants::PR_N_HYPHAE;
	            if (Rand::getRand().randunif() < pr) {
	            	//printf("5.n\n");
	            	CoupledInteractions::intAspergillus(neutrophil, aspergillus, x, y, z);
	                aspergillus->getBooleanNetwork()->setState(IntracellularModel::LIFE_STATUS, CoupledInteractions::getDeadState(aspergillus, IntracellularModel::DYING));
	                neutrophil->bind(aspergillus, 4);
	            } else {
	            	//printf("6.n\n");
	                neutrophil->setEngaged(true);
	            }
	            if (Rand::getRand().randunif() < constexprants::PR_N_PHAG) {
	                // Additional logic if necessary
	            }
	        } else if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::SWELLING_CONIDIA) {
	        	//printf("7.n\n");
	        	if (Rand::getRand().randunif() < constexprants::PR_N_PHAG) {
	        		//printf("8.n\n");
	            	CoupledInteractions::intAspergillus(neutrophil, aspergillus, x, y, z);
	                neutrophil->bind(aspergillus, 4);
	            }
	        }
	    }
	    return true;
	}

	static bool macrophagePhagApoptoticNeutrophilS(Leukocyte* cell, Leukocyte* macrophage) {
	    if (cell->getBooleanNetwork()->hasPhenotype(IntracellularModel::APOPTOTIC)) {
	        macrophage->incIronPool(cell->getIronPool());
	        cell->incIronPool(cell->getIronPool());
	        cell->die();
	    }
	    return true;
	}

	/*static bool typeIPneumocyteNET(Leukocyte* neutrophil, PneumocyteI> pneumocyteI, bool control, bool netOnly) {
	    if (neutrophil->getBooleanNetwork()->hasPhenotype(NeutrophilStateModel::NETOTIC)) {
	        if (netOnly) {
	            if (control && Rand::getRand().randunif() < constexprants::PR_NET_KILL_EPI) {
	                pneumocyteI->die();
	            }
	        } else {
	            if (pneumocyteI->isInjury() || (control && Rand::getRand().randunif() < constexprants::PR_NET_KILL_EPI)) {
	                pneumocyteI->die();
	            }
	        }
	        control = false;
	    }
	    return control;
	}*/

	/*static bool typeIPneumocyteAspergillus(Cell* typeI, PositionalInfectiousAgent* aspergillus, bool injury) {
	    return primitives::CoupledtypeIPneumocyteAspergillus(typeI, aspergillus, injury);
	}*/

	//UNCOMMENT
	static bool typeIIPneumocyteAspergillus(Cell* typeII, PositionalInfectiousAgent* aspergillus) {
	    if (!typeII->isDead()) {
	        if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) != AspergillusIntracellularModel::RESTING_CONIDIA) {
	            typeII->bind(aspergillus, 4);
	        }
	    }
	    return true;
	}

	static bool rosActivation(Cell* cell, Molecule* ros, int status, int x, int y, int z) {
	    if (Util::activationFunction(ros->get(0, x, y, z) * ros->get(0, x, y, z), constexprants::Kd_H2O2 * constexprants::Kd_H2O2, constexprants::VOXEL_VOL * constexprants::VOXEL_VOL)) {
	        cell->getBooleanNetwork()->setState(IntracellularModel::LIFE_STATUS, status);
	    }
	    return true;
	}

	static bool macrophageAspergillus(Leukocyte* mac, PositionalInfectiousAgent* aspergillus, int x, int y, int z) {
	    return CoupledInteractions::macrophageAspergillus(mac, aspergillus, x, y, z);
	}

	/*static bool intKlebsiella(Leukocyte* leuk, InfectiousAgent* klebsiella) {
	    primitives::CoupledintKlebsiela(leuk, klebsiella);
	    return true;
	}*/

	static bool uptakeSiderophore(InfectiousAgent* agent, Siderophore *mol, double rate, int x, int y, int z) {
	    if (agent->isUptakingSiderophore(mol)) {
	        double qtty = mol->get("SIDBI", x, y, z) * rate;
	        qtty = qtty < mol->get("SIDBI", x, y, z) ? qtty : mol->get("SIDBI", x, y, z);

	        mol->dec(qtty, "SIDBI", x, y, z);
	        agent->incIronPool(qtty);
	    }
	    return true;
	}

	static bool secreteSiderophore(InfectiousAgent* agent, Siderophore *mol, int x, int y, int z) {
	    if (agent->isSecretingSiderophore(mol)) {
	        mol->inc(mol->getSiderophoreQtty(), "SID", x, y, z);
	    }
	    return true;
	}

	static bool siderophoreIronChelation(Molecule* iron, Siderophore* mol, int x, int y, int z) {
	    double qttyIron = iron->get("Iron", x, y, z);
	    double qttyTafc = mol->get(0, x, y, z);
	    double qtty = qttyTafc < qttyIron ? qttyTafc : qttyIron;
	    mol->dec(qtty, 0, x, y, z);
	    mol->inc(qtty, 1, x, y, z);
	    iron->dec(qtty, "Iron", x, y, z);

	    return true;
	}

	static bool siderophoreTransferrinChelation(Molecule* tf, Siderophore* mol, double km, int x, int y, int z) {
	    double dfe2dt = Util::michaelianKinetics(tf->get(2, x, y, z), mol->get("SID", x, y, z), km, constexprants::STD_UNIT_T);
	    double dfedt = Util::michaelianKinetics(tf->get(1, x, y, z), mol->get("SID", x, y, z), km, constexprants::STD_UNIT_T);

	    if (dfe2dt + dfedt > mol->get("SID", x, y, z)) {
	        double rel = mol->get("SID", x, y, z) / (dfe2dt + dfedt);
	        dfe2dt = dfe2dt * rel;
	        dfedt = dfedt * rel;
	    }
	    tf->dec(dfe2dt, 2, x, y, z);
	    tf->inc(dfe2dt, 1, x, y, z);

	    tf->dec(dfedt, 1, x, y, z);
	    tf->inc(dfedt, 0, x, y, z);

	    mol->inc(dfe2dt + dfedt, "SIDBI", x, y, z);
	    mol->dec(dfe2dt + dfedt, "SID", x, y, z);

	    return true;
	}

	/*static bool lipocalin2EnterobactinInteraction(Molecule* lip, Siderophore ent, int x, int y, int z) {
	    for (int i = 0; i < 1; i++) {
	        int j = Rand::getRand().randunif(0, 1);
	        switch (j) {
	            case 0: {
	                double dsdt = Util::michaelianKinetics(lip->get(0, x, y, z), ent->get("SID", x, y, z), constexprants::K_M_LPC2_ENT, constexprants::STD_UNIT_T);
	                ent->dec(dsdt, "SID", x, y, z);
	                lip->dec(dsdt, "LPC", x, y, z);
	                break;
	            }
	            case 1: {
	                double dsidt = Util::michaelianKinetics(lip->get(0, x, y, z), ent->get("SIDBI", x, y, z), constexprants::K_M_LPC2_ENT, constexprants::STD_UNIT_T);
	                ent->dec(dsidt, "SIDBI", x, y, z);
	                lip->dec(dsidt, "LPC", x, y, z);
	                lip->inc(dsidt, "LPCBI", x, y, z);
	                break;
	            }
	            default:
	                std::cerr << "No such reaction " << j << "!" << std::endl;
	        }
	    }
	    return true;
	}*/

	/*static void intKlebsiela(Leukocyte* leukocyte, InfectiousAgent* klebsiela) {
	    primitives::CoupledintKlebsiela(leukocyte, klebsiela);
	}*/
};

} // namespace primitives
} // namespace uf
} // namespace edu

#endif /* EDU_UF_PRIMITIVES_INTERACTIONS_H_ */
