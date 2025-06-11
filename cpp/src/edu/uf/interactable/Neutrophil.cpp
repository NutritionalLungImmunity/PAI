/*
 * Neutrophil.cpp
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#include "Neutrophil.h"
#include "Molecule.h"
#include "Macrophage.h"
#include "TNFa.h"
#include "../utils/Constants.h"
#include "../primitives/Interactions.h"
#include "../utils/Rand.h"
#include "../utils/Id.h"
#include  "afumigatus/Afumigatus.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;
using namespace afumigatus;

const int Neutrophil::classId = Id::getId();
const std::string Neutrophil::NAME = "Neutrophils";
bool Neutrophil::netOnly = true;
std::string Neutrophil::chemokine;
int Neutrophil::totalCells = 0;
double Neutrophil::totalIron = 0;
int Neutrophil::interactionId = utils::Id::getId();

Neutrophil::Neutrophil(double ironPool, intracellularState::IntracellularModel* network)
    : Leukocyte(ironPool, network),
      maxMoveStep(-1),
      degranulated(false),
      netHalfLife(utils::constexprants::NET_HALF_LIFE)
      //depleted(false),
      /*control(true)*/ {
    Neutrophil::totalCells++;
    Neutrophil::totalIron += ironPool;
    setEngaged(false);
}

int Neutrophil::getClassId() const {
	return classId;
}

void Neutrophil::setMaxMoveStep(int moveStep) {
    this->maxMoveStep = moveStep;
}

double Neutrophil::getNetHalfLife() const {
    return this->netHalfLife;
}

void Neutrophil::setNetHalfLife(double halfLife) {
    this->netHalfLife = halfLife;
}

int Neutrophil::getInteractionId() const {
    return interactionId;
}

bool Neutrophil::isTime() const {
    return this->getClock()->toc();
}

bool Neutrophil::hasDegranulated() const {
    return this->degranulated;
}

void Neutrophil::degranulate() {
    this->degranulated = true;
}

std::string Neutrophil::getChemokine() {
    return chemokine;
}

void Neutrophil::setChemokine(const std::string& chemokine) {
    Neutrophil::chemokine = chemokine;
}

int Neutrophil::getTotalCells() {
    return totalCells;
}

void Neutrophil::setTotalCells(int totalCells) {
    Neutrophil::totalCells = totalCells;
}

double Neutrophil::getTotalIron() {
    return totalIron;
}

void Neutrophil::setTotalIron(double totalIron) {
    Neutrophil::totalIron = totalIron;
}

int Neutrophil::getMaxMoveSteps() {
    double r = 1.0;
    // if(this->getExternalState() == 1) r = Constants::NET_COUNTER_INHIBITION;
    if (this->maxMoveStep == -1) {
        this->maxMoveStep = utils::Rand::getRand().randpois(utils::constexprants::MA_MOVE_RATE_REST * r);
    }
    return this->maxMoveStep;
}

void Neutrophil::incIronPool(double qtty) {
    this->setIronPool(this->getIronPool() + qtty);
    Neutrophil::totalIron += qtty;
}

bool Neutrophil::templateInteract(Interactable* interactable, int x, int y, int z) {
    if (dynamic_cast<Macrophage*>(interactable)) {
        return primitives::Interactions::macrophagePhagApoptoticNeutrophilS(this, dynamic_cast<Leukocyte*>(interactable));
    }
    if (dynamic_cast<TNFa*>(interactable)) {
    	primitives::Interactions::secrete(this, dynamic_cast<TNFa*>(interactable), utils::constexprants::N_TNF_QTTY, x, y, z, 0);
    	return primitives::Interactions::bind(this, dynamic_cast<TNFa*>(interactable), x, y, z, 0);
    }
    if(dynamic_cast<Afumigatus*>(interactable)){
    	return primitives::Interactions::neutrophilAspergillus(this, dynamic_cast<Afumigatus*>(interactable), x, y, z);
    }

    return interactable->interact(this, x, y, z);
}

void Neutrophil::die() {
    if (this->getBooleanNetwork()->getState(intracellularState::IntracellularModel::LIFE_STATUS) != intracellularState::IntracellularModel::DEAD) {
        this->getBooleanNetwork()->setState(intracellularState::IntracellularModel::LIFE_STATUS, intracellularState::IntracellularModel::DEAD);
        Neutrophil::totalCells--;
    }
}

std::string Neutrophil::attractedBy() const {
    return Neutrophil::chemokine;
}

std::string Neutrophil::getName() const {
    return NAME;
}

int Neutrophil::getMaxCell() {
    return utils::constexprants::N_MAX_CONIDIA;
}

} // namespace interactable
} // namespace uf
} // namespace edu
