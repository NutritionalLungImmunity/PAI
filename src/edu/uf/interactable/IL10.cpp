/*
 * IL10.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "IL10.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;
using namespace primitives;

const int IL10::classId = Id::getId();
const string IL10::NAME = "IL10";
const int IL10::NUM_STATES = 1;
IL10* IL10::molecule = nullptr;

IL10::IL10(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

IL10* IL10::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
        molecule = new IL10(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int IL10::getClassId() const {
	return classId;
}

double IL10::getKd() {
    return utils::constexprants::Kd_IL10;
}

void IL10::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

int IL10::getIndex(const char* str) const {
    return 0;
}

void IL10::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool IL10::templateInteract(Interactable* interactable, int x, int y, int z) {
	if (dynamic_cast<Macrophage*>(interactable)) {
	    	Cell* cell  = static_cast<Cell*>(interactable);
	    	if(!cell->isDead()){
	    		Interactions::secrete(cell, this, utils::constexprants::MA_IL10_QTTY, x, y, z, 0);
	    		Interactions::bind(cell, this, x, y, z, 0);
	    	}
	    	return true;
	    }
    return interactable->interact(this, x, y, z);
}

string IL10::getName() const {
    return NAME;
}

double IL10::getThreshold() const {
    return 0;
}

int IL10::getNumState() const {
    return NUM_STATES;
}

bool IL10::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
