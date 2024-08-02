/*
 * Leukocyte.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#include "../intracellularState/IntracellularModel.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "Leukocyte.h"
#include "../compartments/GridFactory.h"
#include "../compartments/Voxel.h"
#include "../utils/Util.h"
#include "PositionalAgent.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::compartments;
using namespace edu::uf::utils;

Leukocyte::Leukocyte(intracellularState::IntracellularModel* network)
    : Leukocyte(0.0, network) {}

Leukocyte::Leukocyte(double ironPool, intracellularState::IntracellularModel* network)
    : Cell(network), engaged(false) {
    setIronPool(ironPool);
}

bool Leukocyte::isEngaged() const {
    return engaged;
}

void Leukocyte::setEngaged(bool engaged) {
    this->engaged = engaged;
}

void Leukocyte::kill() {
    if(Rand::getRand().randunif() < utils::constexprants::PR_KILL) {
        for(auto agent : phagosome) {
            incIronPool(agent->getIronPool());
            agent->incIronPool(-agent->getIronPool());
            agent->die();
        }
        phagosome.clear();
    }
}

void Leukocyte::setPhagosome(std::vector<Cell*>& phagosome) {
	this->phagosome = phagosome;
}

std::vector<Cell*>& Leukocyte::getPhagosome() {
	return this->phagosome;
}

void Leukocyte::addAspergillus(Cell* aspergillus) {
	phagosome.push_back(aspergillus);
}

void Leukocyte::move(int x, int y, int z, int steps) {
    if(steps < getMaxMoveSteps()) {
        auto grid = GridFactory::getGrid();
        Voxel* oldVoxel = grid[x][y][z];
        Util::calcDriftProbability(oldVoxel, this);
        Voxel* newVoxel = Util::getVoxel(oldVoxel, Rand::getRand().randunif());

        oldVoxel->removeCell(getId());
        newVoxel->setCell(this);
        steps += 1;

        for(auto agent : phagosome) {
            if(auto positionalAgent = dynamic_cast<PositionalAgent*>(agent)) {
                positionalAgent->setX(newVoxel->getX() + Rand::getRand().randunif());
                positionalAgent->setY(newVoxel->getY() + Rand::getRand().randunif());
                positionalAgent->setZ(newVoxel->getZ() + Rand::getRand().randunif());
            }
        }
        move(newVoxel->getX(), newVoxel->getY(), newVoxel->getZ(), steps);
    }
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
