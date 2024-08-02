/*
 * AspergillusMacrophage.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "AspergillusMacrophage.h"
#include "../interactable/Cell.h"
#include "../interactable/IL10.h"
#include "../interactable/MIP1B.h"
#include "../interactable/MIP2.h"
#include "../interactable/Macrophage.h"
#include "../interactable/TGFb.h"
#include "../interactable/TNFa.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace intracellularState {

using namespace edu::uf::utils;

const int AspergillusMacrophage::M1 = Id::getId();
const int AspergillusMacrophage::M2B = Id::getId();
const int AspergillusMacrophage::M2C = Id::getId();
const int AspergillusMacrophage::_FPN = Id::getId();
const std::string AspergillusMacrophage::name = "AspergillusMacrophageBooleanNetwork";

AspergillusMacrophage::AspergillusMacrophage() : FMacrophageBooleanNetwork() {
    // Constructor implementation (if any)
}

void AspergillusMacrophage::computePhenotype() {
	//printf("M0\n");
    if (booleanNetwork[NFkB] > 0 || booleanNetwork[STAT1] > 0 || booleanNetwork[STAT5] > 0) {
        //getPhenotype().put(IL6::getMolecule()->getPhenotype(), max({booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}));
        getPhenotype()[TNFa::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[MIP1B::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[MIP2::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[IL10::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[M1] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
    }
    if (booleanNetwork[STAT6] > 0) {
        //getPhenotype().put(IL4::getMolecule()->getPhenotype(), booleanNetwork[STAT6]);
    }
    if (booleanNetwork[ERK] > 0) {
        //getPhenotype().put(IL6::getMolecule()->getPhenotype(), max({booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}));
        getPhenotype()[TNFa::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[IL10::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[M2B] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
    }
    if (booleanNetwork[STAT3] > 0) {
        getPhenotype()[TGFb::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[IL10::getMolecule()->getPhenotype()] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
        getPhenotype()[M2C] = amax(new int[]{booleanNetwork[NFkB], booleanNetwork[STAT1], booleanNetwork[STAT5]}, 3);
    }
}

void AspergillusMacrophage::updateStatus(int id, int x, int y, int z) {
    Macrophage* mac = dynamic_cast<Macrophage*>(Cell::get(id));
    if (this->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD) {
        return;
    }
    if (mac->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::NECROTIC) {
        mac->die();
        // for (InfectiousAgent* entry : this->getPhagosome()) //WARNING-COMMENT
        //     entry->setState(Afumigatus::RELEASING);
    } else if (mac->getPhagosome().size() > utils::constexprants::MA_MAX_CONIDIA) {
        setState(IntracellularModel::LIFE_STATUS, IntracellularModel::NECROTIC);
    }

    if (Rand::getRand().randunif() < utils::constexprants::MA_HALF_LIFE && mac->getPhagosome().size() == 0 &&
        Macrophage::getTotalCells() > utils::constexprants::MIN_MA) {
        mac->die();
    }
    // this->setMoveStep(0);
    mac->setMaxMoveStep(-1);
}

} // namespace intracellularState
} // namespace uf
} // namespace edu
