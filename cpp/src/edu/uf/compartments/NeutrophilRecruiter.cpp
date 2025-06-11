/*
 * NeutrophilRecruiter.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "NeutrophilRecruiter.h"
#include "../interactable/Neutrophil.h"
#include "../intracellularState/NeutrophilStateModel.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../interactable/MIP2.h"

namespace edu {
namespace uf {
namespace compartments {

using namespace edu::uf::utils;
using namespace edu::uf::intracellularState;

Cell* NeutrophilRecruiter::createCell() {
    return new Neutrophil(
        0,
        new NeutrophilStateModel()
    );
}

int NeutrophilRecruiter::getQtty() {
    return getQtty(MIP2::getMolecule()->getTotalMolecule(0));
}

int NeutrophilRecruiter::getQtty(double chemokine) {
    double avg = utils::constexprants::RECRUITMENT_RATE_N * utils::constexprants::SPACE_VOL * chemokine *
                 (1 - (Neutrophil::getTotalCells() / utils::constexprants::MAX_N)) /
				 utils::constexprants::Kd_MIP2;
    if (avg > 0) {
        return edu::uf::utils::Rand::getRand().randpois(avg);
    } else {
        if (Neutrophil::getTotalCells() < utils::constexprants::MIN_MA) {
            return Rand::getRand().randpois(1);
        }
        return 0;
    }
}

bool NeutrophilRecruiter::leave() {
    return false;
}

Cell* NeutrophilRecruiter::getCell(Voxel* voxel) {
    return nullptr;
}

} // namespace compartments
} // namespace uf
} // namespace edu
