/*
 * Cell.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#include "Cell.h"
#include "Macrophage.h"
#include "TNFa.h"
#include "../utils/Constants.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace utils;

unordered_map<int, Cell*> Cell::cells;

Cell::Cell(IntracellularModel* intracellularModel){
	this->intracellularModel = intracellularModel;
	this->clock = new Clock(utils::constexprants::INV_UNIT_T, Rand::getRand().randunif(0, utils::constexprants::INV_UNIT_T));
	this->id = utils::Id::getId();
	this->externalState = 0;
	cells[id] = this;
	this->ironPool = 0;
	this->engulfed = false;
}

void Cell::remove(int id) {
    cells.erase(id);
}

Cell* Cell::get(int id) {
    return cells[id];
}

void Cell::setExternalState(int state) {
    externalState = state;
}

int Cell::getExternalState() const {
    return externalState;
}

double Cell::getIronPool() const {
    return ironPool;
}

void Cell::setIronPool(double ironPool) {
    this->ironPool = ironPool;
}

bool Cell::isEngulfed() const {
    return engulfed;
}

void Cell::setEngulfed(bool engulfed) {
    this->engulfed = engulfed;
}

int Cell::getId() const {
    return id;
}

void Cell::setId(int id) {
    this->id = id;
}

void Cell::bind(Binder* iter, int level) {
    intracellularModel->activateReceptor(iter->getInteractionId(), level);
}

Clock* Cell::getClock() const {
    return clock;
}

IntracellularModel* Cell::getBooleanNetwork() const {
    return intracellularModel;
}

bool Cell::isDead() const {
    return intracellularModel->isDead();
}

void Cell::updateStatus(int x, int y, int z) {
    clock->tic();
    if (!clock->toc()) return;
    intracellularModel->processBooleanNetwork();
    intracellularModel->updateStatus(id, x, y, z);
}

bool Cell::removeUponDeath() const {
    return true;
}

string Cell::attractedBy() const {
    return "";
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
