/*
 * CoupledmacrophageAspergillus.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_PRIMITIVES_COUPLEDINTERACTIONS_H_
#define EDU_UF_PRIMITIVES_COUPLEDINTERACTIONS_H_


#include "../interactable/Cell.h"
//#include "../interactable/Blood.h"
#include "../interactable/InfectiousAgent.h"
//#include "../interactable/LPS.h"
#include "../interactable/Leukocyte.h"
#include "../interactable/Macrophage.h"
#include "../interactable/Molecule.h"
#include "../interactable/Neutrophil.h"
#include "../interactable/PositionalInfectiousAgent.h"
#include "../interactable/afumigatus/Afumigatus.h"
#include "../intracellularState/AspergillusIntracellularModel.h"
#include "../intracellularState/AspergillusMacrophage.h"
//#include "../intracellularState/IntracellularModel.h"
//#include "../intracellularState/Klebsiella/KlebsiellaIntracellularModel.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../compartments/GridFactory.h"
#include <string>




namespace edu {
namespace uf {
namespace primitives {

using namespace edu::uf::interactable;
using namespace edu::uf::utils;
using namespace edu::uf::intracellularState;
using namespace edu::uf::compartments;
using namespace afumigatus;

class CoupledInteractions {
public:

	/*static bool set(Molecule* mol, Cell* blood, double qtty, int x, int y, int z, int w) {
	    if (dynamic_cast<Blood*>(blood)->hasBlood(x, y, z))
	        mol->set(qtty, w, x, y, z);
	    return true;
	}*/

	/*static bool setHemorrhage(Cell* blood, Cell* pneumocyteI, int status, int x, int y, int z) {
	    if (pneumocyteI->isDead())
	        dynamic_cast<Blood*>(blood)->setStatus(status, x, y, z);
	    return true;
	}*/

	static bool lactoferrinDegranulation(Leukocyte* neutr, Molecule* lac, int x, int y, int z) {
	    Neutrophil* neutrophil = dynamic_cast<Neutrophil*>(neutr);
	    if (neutr->getBooleanNetwork()->hasPhenotype(*lac) && !neutrophil->hasDegranulated()) {
	        lac->inc(utils::constexprants::LAC_QTTY, "Lactoferrin", x, y, z);
	        neutrophil->degranulate();
	    }
	    return true;
	}

	/*static bool typeIPneumocyteAspergillus(Cell* typeI, PositionalInfectiousAgent* asp, bool injury) {
	    Afumigatus* aspergillus = dynamic_cast<Afumigatus*>(asp);
	    if (typeI->isDead())
	        return true;
	    if (typeI->isDead())
	        aspergillus->setEpithelialInhibition(1);
	    if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE) {
	        if (aspergillus->getAspEpiInt() && Rand::getRand()->randunif() < Constants::PR_ASP_KILL_EPI) {
	            typeI->die();
	        } else {
	            injury = true;
	        }
	        aspergillus->setAspEpiInt(false);
	    }
	    return injury;
	}*/

	static bool macrophageAspergillus(Leukocyte* mac, PositionalInfectiousAgent* asp, int x, int y, int z) {
		Afumigatus* aspergillus = dynamic_cast<Afumigatus*>(asp);
	    if (mac->isEngaged())
	        return true;
	    if (!mac->isDead()) {
	        if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) != AspergillusIntracellularModel::RESTING_CONIDIA) {
	        	double prInteract = (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE) ? utils::constexprants::PR_MA_HYPHAE : utils::constexprants::PR_MA_PHAG;
	            if (Rand::getRand().randunif() < prInteract) {
	            	intAspergillus(mac, aspergillus, x,  y, z, aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) != AspergillusIntracellularModel::HYPHAE);
	                if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE && mac->getBooleanNetwork()->hasPhenotype(new int[]{AspergillusMacrophage::M1, AspergillusMacrophage::M2B}, 2)) {
	                	aspergillus->getBooleanNetwork()->setState(IntracellularModel::LIFE_STATUS, getDeadState(aspergillus, IntracellularModel::DYING));
	                    if (aspergillus->getNextSepta() != nullptr) {
	                    	aspergillus->getNextSepta()->setRoot(true);
	                    }
	                    if (aspergillus->getNextBranch() != nullptr) {
	                    	aspergillus->getNextBranch()->setRoot(true);
	                    }
	                } else {
	                    if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE && mac->getBooleanNetwork()->hasPhenotype(AspergillusMacrophage::M1)) {
	                    	mac->setEngaged(true);
	                    }
	                }
	            }
	        }
	    }
	    return true;
	}

	static void intAspergillus(Leukocyte* leukocyte, PositionalInfectiousAgent* aspergillus, int x, int y, int z, bool phagocytose = false) {
	    if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::LOCATION) == AspergillusIntracellularModel::FREE) {
	        if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::RESTING_CONIDIA ||
	            aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::SWELLING_CONIDIA ||
	            aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::STERILE_CONIDIA || phagocytose) {
	            if (!leukocyte->isDead()) {
	                if (leukocyte->getPhagosome().size() < leukocyte->getMaxCell()) {
	                    aspergillus->getBooleanNetwork()->setState(IntracellularModel::LOCATION, AspergillusIntracellularModel::INTERNALIZING);
	                    aspergillus->setEngulfed(true);
	                    leukocyte->getPhagosome().push_back(aspergillus);
	                    GridFactory::getGrid()[x][y][z]->removeCell(aspergillus->getId());

	                }
	            }
	        }
	        if (aspergillus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) != AspergillusIntracellularModel::RESTING_CONIDIA) {
	            if (Macrophage* macrophage = dynamic_cast<Macrophage*>(leukocyte)) {
	                macrophage->bind(aspergillus, 4);
	            }
	        }
	    }
	}

	/*static void intKlebsiela(Leukocyte* leukocyte, InfectiousAgent* klebsiela) {
	    if (klebsiela->getBooleanNetwork()->hasPhenotype(KlebsiellaIntracellularModel::FREE)) {
	        if (!leukocyte->isDead()) {
	            if (leukocyte->getPhagosome().size() < leukocyte->getMaxCell()) {
	                klebsiela->getBooleanNetwork()->setState(IntracellularModel::LOCATION, KlebsiellaIntracellularModel::INTERNALIZING);
	                klebsiela->setEngulfed(true);
	                leukocyte->getPhagosome().push_back(klebsiela);
	            }
	        }
	        if (Macrophage* macrophage = dynamic_cast<Macrophage*>(leukocyte)) {
	            leukocyte->bind(LPS::getMolecule(), 4);
	        }
	    }
	}*/

	static int getDeadState(PositionalInfectiousAgent* asp, int state) {
	    if (asp->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD)
	        return IntracellularModel::DEAD;
	    return state;
	}

	/*static bool aspergillusHemeUptake(PositionalInfectiousAgent* asp, Molecule* heme, int x, int y, int z) {
	    Afumigatus* afumigatus = dynamic_cast<Afumigatus*>(asp);
	    if (afumigatus->getBooleanNetwork()->getState(AspergillusIntracellularModel::STATUS) == AspergillusIntracellularModel::HYPHAE) {
	        double qtty = Constants::HEME_UP * heme->get(0, x, y, z);
	        afumigatus->incHeme(qtty);
	        afumigatus->incIronPool(qtty);
	        heme->dec(qtty, 0, x, y, z);
	    }
	    return true;
	}

	/*static void intAspergillus(Leukocyte* leukocyte, PositionalInfectiousAgent* aspergillus) {
	    intAspergillus(leukocyte, aspergillus, false);
	}*/
};

} // namespace primitives
} // namespace uf
} // namespace edu


#endif /* EDU_UF_PRIMITIVES_COUPLEDINTERACTIONS_H_ */
