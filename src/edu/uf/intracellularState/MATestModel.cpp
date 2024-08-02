/*
 * MATestModel.cpp
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#include "MATestModel.h"
#include "../utils/Id.h"
#include "../interactable/Cell.h"
#include "../interactable/TNFa.h"
#include <unordered_map>
#include <memory>

namespace edu {
namespace uf {
namespace intracellularState {

using namespace edu::uf::utils;

MATestModel::MATestModel() : booleanNetwork(0) {}

const int MATestModel::M1 = Id::getId();
const int MATestModel::M2B = Id::getId();
const int MATestModel::M2C = Id::getId();
const int MATestModel::_FPN = Id::getId();

void MATestModel::processBooleanNetwork() {
    this->booleanNetwork = getInput(TNFa::getMolecule());

    this->inputs.clear();
    this->clearPhenotype();
    this->computePhenotype();
}

void MATestModel::updateStatus(int id, int x, int y, int z) {
    Cell *cell = Cell::get(id);
    if (cell->getBooleanNetwork()->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::APOPTOTIC) {
        cell->die();
    }
}

void MATestModel::computePhenotype() {
    if (booleanNetwork > 0) {
        this->getPhenotype()[TNFa::getMolecule()->getPhenotype()] = booleanNetwork;
    }
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
