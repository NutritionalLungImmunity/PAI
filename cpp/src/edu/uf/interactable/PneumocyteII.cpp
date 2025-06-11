/*
 * PneumocyteII.cpp
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#include "PneumocyteII.h"
#include "TNFa.h"
#include "../primitives/Interactions.h"
#include "../utils/Constants.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;
using namespace afumigatus;

const int PneumocyteII::classId = Id::getId();
const std::string PneumocyteII::NAME = "Pneumocyte";
int PneumocyteII::totalCells = 0;
int PneumocyteII::interactionId = utils::Id::getId();

PneumocyteII::PneumocyteII(intracellularState::IntracellularModel* network)
    : Cell(network), iteration(0) {
    totalCells++;
}

int PneumocyteII::getClassId() const {
	return classId;
}

int PneumocyteII::getInteractionId() const {
    return interactionId;
}

bool PneumocyteII::isTime() const {
    return this->getClock()->toc();
}

int PneumocyteII::getTotalCells() {
    return totalCells;
}

void PneumocyteII::setTotalCells(int totalCells) {
    PneumocyteII::totalCells = totalCells;
}

int PneumocyteII::getIteration() const {
    return iteration;
}

void PneumocyteII::die() {
    if (this->getBooleanNetwork()->getState(intracellularState::IntracellularModel::LIFE_STATUS) != intracellularState::IntracellularModel::DEAD) {
        this->getBooleanNetwork()->setState(intracellularState::IntracellularModel::LIFE_STATUS,    intracellularState::IntracellularModel::DEAD);
        totalCells--;
    }
}

bool PneumocyteII::templateInteract(interactable::Interactable* interactable, int x, int y, int z) {
    if (dynamic_cast<TNFa*>(interactable)) {
        primitives::Interactions::bind(this, static_cast<Molecule*>(interactable), x, y, z, 0);
        return primitives::Interactions::secrete(this, static_cast<Molecule*>(interactable), utils::constexprants::MA_TNF_QTTY, x, y, z, 0);
    }

    return interactable->interact(this, x, y, z);
}

void PneumocyteII::incIronPool(double qtty) {
    // No implementation needed as per original code
}

void PneumocyteII::move(int x, int y, int z, int steps) {
    // Disabled as per original code
}

std::string PneumocyteII::getName() const {
    return NAME;
}

int PneumocyteII::getMaxMoveSteps() {
    // TODO: Implement as needed
    return -1;
}

} // namespace interactable
} // namespace uf
} // namespace edu
