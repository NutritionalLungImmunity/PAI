/*
 * PneumocyteStateModel.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "PneumocyteStateModel.h"
#include "../interactable/MIP1B.h"
#include "../interactable/MIP2.h"
#include "../interactable/TNFa.h"
#include "../interactable/afumigatus/Afumigatus.h"

namespace edu {
namespace uf {
namespace intracellularState {

using namespace afumigatus;

const string PneumocyteStateModel::name = "PneumocyteStateModel";

PneumocyteStateModel::PneumocyteStateModel()
    : iterations1(0), iterations2(0) {
    this->booleanNetwork = new int[size]();
}

int PneumocyteStateModel::countMix() {
    if (this->booleanNetwork[MIX_ACTIVE] == 0) {
        return 0;
    }
    if (iterations1++ >= _ITER_REST) {
        iterations1 = 0;
        return 0;
    }
    return 1;
}

int PneumocyteStateModel::countActive() {
    if (this->booleanNetwork[ACTIVE] == 0) {
        return 0;
    }
    if (iterations2++ >= _ITER_REST) {
        iterations2 = 0;
        return 0;
    }
    return 1;
}

void PneumocyteStateModel::processBooleanNetwork() {
    this->booleanNetwork[MIX_ACTIVE] = min(
        max(getInput(Afumigatus::DEF_OBJ), N * countMix()),
        -this->booleanNetwork[ACTIVE] + N
    );
    this->booleanNetwork[ACTIVE] = max(
        min(
            getInput(TNFa::getMolecule()),
            this->booleanNetwork[MIX_ACTIVE]
        ),
        N * countActive()
    );

    this->iterations1 = getInput(Afumigatus::DEF_OBJ) > 0 ? 0 : this->iterations1;
    this->iterations2 = (getInput(Afumigatus::DEF_OBJ) > 0 || /*getInput(IL1::getMolecule()) > 0 ||*/ getInput(TNFa::getMolecule()) > 0) ? 0 : this->iterations2;

    this->inputs.clear();
    this->clearPhenotype();
    this->computePhenotype();
}

void PneumocyteStateModel::computePhenotype() {
    if (this->booleanNetwork[ACTIVE] > 0) {
        this->addPhenotype(TNFa::getMolecule()->getPhenotype(), this->booleanNetwork[ACTIVE]);
        //this->addPhenotype(IL6::getMolecule()->getPhenotype(), this->booleanNetwork[ACTIVE]);
        //this->addPhenotype(MCP1::getMolecule()->getPhenotype(), this->booleanNetwork[ACTIVE]);
        this->addPhenotype(MIP1B::getMolecule()->getPhenotype(), this->booleanNetwork[ACTIVE]);
        this->addPhenotype(MIP2::getMolecule()->getPhenotype(), this->booleanNetwork[ACTIVE]);
    }
    if (this->booleanNetwork[MIX_ACTIVE] > 0) {
        this->addPhenotype(TNFa::getMolecule()->getPhenotype(), this->booleanNetwork[MIX_ACTIVE]);
        //this->addPhenotype(IL6::getMolecule()->getPhenotype(), this->booleanNetwork[MIX_ACTIVE]);
    }
}

void PneumocyteStateModel::updateStatus(int id, int x, int y, int z) {
    // Disabled
}

} // namespace intracellularState
} // namespace uf
} // namespace edu
