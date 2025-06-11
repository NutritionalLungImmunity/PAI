/*
 * TGFb.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "TGFb.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace primitives;

const int TGFb::classId = Id::getId();
const string TGFb::NAME = "TGFb";
const int TGFb::NUM_STATES = 1;
TGFb* TGFb::molecule = nullptr;

TGFb::TGFb(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

TGFb* TGFb::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
        molecule = new TGFb(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int TGFb::getClassId() const {
	return classId;
}

double TGFb::getKd() {
    return utils::constexprants::Kd_IL10;
}

void TGFb::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

int TGFb::getIndex(const std::string& str) const {
    return 0;
}

void TGFb::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool TGFb::templateInteract(Interactable* interactable, int x, int y, int z) {
    if (dynamic_cast<Macrophage*>(interactable)) {
    	Cell* cell  = static_cast<Cell*>(interactable);
    	if(!cell->isDead()){
    		Interactions::secrete(cell, this, utils::constexprants::MA_TGF_QTTY, x, y, z, 0);
    		Interactions::bind(cell, this, x, y, z, 0);
    	}
    	return true;
    }
    return interactable->interact(this, x, y, z);
}

string TGFb::getName() const {
    return NAME;
}

double TGFb::getThreshold() const {
    return 0;
}

int TGFb::getNumState() const {
    return NUM_STATES;
}

bool TGFb::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
