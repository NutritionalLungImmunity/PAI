/*
 * MacrophageRecruiter.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "MacrophageRecruiter.h"
#include "../interactable/Macrophage.h"
#include "../intracellularState/AspergillusMacrophage.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../interactable/MIP1B.h"
#include <map>

namespace edu {
namespace uf {
namespace compartments {

using namespace edu::uf::utils;
using namespace edu::uf::intracellularState;

Cell* MacrophageRecruiter::createCell() {
    return new Macrophage(
    		utils::constexprants::MA_INTERNAL_IRON,
		new AspergillusMacrophage()
    );
}

int MacrophageRecruiter::getQtty() {
    return getQtty(MIP1B::getMolecule()->getTotalMolecule(0));
}

int MacrophageRecruiter::getQtty(double chemokine) {
    double avg = utils::constexprants::SPACE_VOL * utils::constexprants::RECRUITMENT_RATE_MA * chemokine *
                 (1 - (Macrophage::getTotalCells() / utils::constexprants::MAX_MA)) /
				 utils::constexprants::Kd_MIP1B;

    if (avg > 0) {
        return Rand::getRand().randpois(avg);
    } else {
        if (Macrophage::getTotalCells() < utils::constexprants::MIN_MA) {
            return Rand::getRand().randpois(1);
        }
        return 0;
    }
}

bool MacrophageRecruiter::leave() {
    return true;
}

Cell* MacrophageRecruiter::getCell(Voxel* voxel) {
    if (voxel->getCells().size() == 0) {
        return nullptr;
    }
    for (const auto& entry : voxel->getCells()) {
        if (dynamic_cast<Macrophage*>(entry.second)) {
            return entry.second;
        }
    }
    return nullptr;
}

} // namespace compartments
} // namespace uf
} // namespace edu
