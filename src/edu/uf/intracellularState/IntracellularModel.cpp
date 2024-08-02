/*
 * IntracellularModel.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "IntracellularModel.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace intracellularState {

using namespace edu::uf::utils;

const int IntracellularModel::STATUS = Id::getId();
const int IntracellularModel::LIFE_STATUS = Id::getId();
const int IntracellularModel::LOCATION = Id::getId();
const int IntracellularModel::DEAD = Id::getId();
const int IntracellularModel::ALIVE = Id::getId();
const int IntracellularModel::APOPTOTIC = Id::getId();
const int IntracellularModel::NECROTIC = Id::getId();
const int IntracellularModel::DYING = Id::getId();
const int IntracellularModel::NETOTIC = Id::getId();

IntracellularModel::IntracellularModel() : bnIteration(0), booleanNetwork(nullptr) {
    states[LIFE_STATUS] = ALIVE;
}

int IntracellularModel::getBnIteration() const {
    return bnIteration;
}

int* IntracellularModel::getBooleanNetwork() const {
    return booleanNetwork;
}

void IntracellularModel::setBooleanNetwork(int* booleanNetwork) {
    this->booleanNetwork = booleanNetwork;
}

void IntracellularModel::setBnIteration(int bnIteration) {
    this->bnIteration = bnIteration;
}

void IntracellularModel::activateReceptor(int idx, int level) {
    inputs[idx] = level;
}

void IntracellularModel::clearPhenotype() {
    phenotypes.clear();
}

bool IntracellularModel::hasPhenotype(const interactable::Molecule& molecule) const {
    return phenotypes.find(molecule.getPhenotype()) != phenotypes.end();
}

bool IntracellularModel::hasPhenotype(int phenotype) const {
    return phenotypes.find(phenotype) != phenotypes.end();
}

bool IntracellularModel::hasPhenotype(const int* phenotype, int length) const {
    for (int i = 0; i < length; ++i) {
        if (phenotypes.find(phenotype[i]) != phenotypes.end()) {
            return true;
        }
    }
    return false;
}

void IntracellularModel::addPhenotype(int phenotype, int level) {
    phenotypes[phenotype] = level;
}

void IntracellularModel::addPhenotype(int phenotype) {
    addPhenotype(phenotype, -1);
}

std::unordered_map<int, int>& IntracellularModel::getState() {
    return states;
}

int IntracellularModel::getState(int state) const {
    auto it = states.find(state);
    if (it != states.end()) {
        return it->second;
    }
    return -1; // Or handle error appropriately
}

void IntracellularModel::setState(int stateName, int stateValue) {
    states[stateName] = stateValue;
}

bool IntracellularModel::isDead() const {
    int lifeStatus = getState(LIFE_STATUS);
    return lifeStatus == DEAD || lifeStatus == DYING || lifeStatus == APOPTOTIC || lifeStatus == NECROTIC;
}

int IntracellularModel::getInput(const interactable::Binder* i) const {
    auto it = inputs.find(i->getInteractionId());
    if (it != inputs.end()) {
        return it->second;
    }
    return 0;
}

int IntracellularModel::amax(const int* a, int length) const {
    int maxVal = a[0];
    for (int i = 1; i < length; ++i) {
        if (a[i] > maxVal) {
            maxVal = a[i];
        }
    }
    return maxVal;
}

int IntracellularModel::max(int i, int j) const {
    return (i > j) ? i : j;
}

int IntracellularModel::amin(const int* a, int length) const {
    int minVal = a[0];
    for (int i = 1; i < length; ++i) {
        if (a[i] < minVal) {
            minVal = a[i];
        }
    }
    return minVal;
}

int IntracellularModel::min(int i, int j) const {
    return (i < j) ? i : j;
}

int IntracellularModel::gnot(int i, int k) const {
    return -i + k;
}

std::unordered_map<int, int>& IntracellularModel::getPhenotype(){
		return phenotypes;
	}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
