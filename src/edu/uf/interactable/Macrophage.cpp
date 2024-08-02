/*
 * Macrophage.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Macrophage.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;

const int Macrophage::classId = Id::getId();
const std::string Macrophage::NAME = "Macrophage";
std::string Macrophage::chemokine = "";
int Macrophage::totalCells = 0;
double Macrophage::totalIron = 0;
int Macrophage::interactionId = utils::Id::getId();

Macrophage::Macrophage(double ironPool, intracellularState::IntracellularModel* network)
    : Leukocyte(ironPool, network), maxMoveStep(-1), engaged(false) {
    Macrophage::totalCells++;
    Macrophage::totalIron += ironPool;
}

int Macrophage::getClassId() const {
	return classId;
}

void Macrophage::setMaxMoveStep(int moveStep) {
    maxMoveStep = moveStep;
}

int Macrophage::getInteractionId() const {
    return interactionId;
}

bool Macrophage::isTime() const {
    return getClock()->toc();
}

std::string Macrophage::getChemokine() {
    return chemokine;
}

void Macrophage::setChemokine(const std::string& chemokine) {
    Macrophage::chemokine = chemokine;
}

int Macrophage::getTotalCells() {
    return totalCells;
}

void Macrophage::setTotalCells(int totalCells) {
    Macrophage::totalCells = totalCells;
}

double Macrophage::getTotalIron() {
    return totalIron;
}

void Macrophage::setTotalIron(double totalIron) {
    Macrophage::totalIron = totalIron;
}

bool Macrophage::isEngaged() const {
    return engaged;
}

void Macrophage::setEngaged(bool engaged) {
    this->engaged = engaged;
}

int Macrophage::getMaxMoveSteps() {
    double r = 1.0;
    if (maxMoveStep == -1) {
        maxMoveStep = utils::Rand::getRand().randpois(utils::constexprants::MA_MOVE_RATE_ACT * r);
    }
    return maxMoveStep;
}

bool Macrophage::templateInteract(Interactable* interactable, int x, int y, int z) {
    return interactable->interact(this, x, y, z);
}

void Macrophage::incIronPool(double qtty) {
    setIronPool(getIronPool() + qtty);
    Macrophage::totalIron += qtty;
}

void Macrophage::die() {
    if (getBooleanNetwork()->getState(intracellularState::IntracellularModel::LIFE_STATUS) != intracellularState::IntracellularModel::DEAD) {
        getBooleanNetwork()->setState(intracellularState::IntracellularModel::LIFE_STATUS, intracellularState::IntracellularModel::DEAD);
        Macrophage::totalCells--;
    }
}

std::string Macrophage::attractedBy() const {
    return chemokine;
}

int Macrophage::getMaxCell() {
    return utils::constexprants::MA_MAX_CONIDIA;
}

std::string Macrophage::getName() const {
    return NAME;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
