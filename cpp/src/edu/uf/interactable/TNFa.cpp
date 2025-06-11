/*
 * TNFa.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "TNFa.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "Macrophage.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;
//using namespace primitives;

const int TNFa::classId = Id::getId();
const string TNFa::NAME = "TNFa";
const int TNFa::NUM_STATES = 1;
TNFa* TNFa::molecule = nullptr;

TNFa::TNFa(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

TNFa* TNFa::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
    if (molecule == nullptr) {
        double**** values = new double***[wbin];
        for (int i = 0; i < wbin; ++i) {
            values[i] = new double**[xbin];
            for (int j = 0; j < xbin; ++j) {
                values[i][j] = new double*[ybin];
                for (int k = 0; k < ybin; ++k) {
                    values[i][j][k] = new double[zbin]();
                }
            }
        }
        molecule = new TNFa(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int TNFa::getClassId() const {
	return classId;
}

double TNFa::getKd() {
    return utils::constexprants::Kd_TNF;
}

void TNFa::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

int TNFa::getIndex(const std::string& str) const {
    return 0;
}

void TNFa::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool TNFa::templateInteract(Interactable* interactable, int x, int y, int z) {
    if (dynamic_cast<Macrophage*>(interactable)) {
    	Cell* cell = static_cast<Cell*>(interactable);
    	if(cell->getBooleanNetwork()->hasPhenotype(*this)){
    		double level = cell->getBooleanNetwork()->getPhenotype().at(this->getPhenotype())/4.0;
    		this->inc(utils::constexprants::MA_TNF_QTTY * level, 0, x, y, z);
    	}
    	cell->bind(molecule, utils::Util::activationFunction5(this->get(0, x, y, z), this->getKd()));
        return true;
    }
    return interactable->interact(this, x, y, z);
}

string TNFa::getName() const {
    return NAME;
}

double TNFa::getThreshold() const {
    return 0;
}

int TNFa::getNumState() const {
    return NUM_STATES;
}

bool TNFa::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
