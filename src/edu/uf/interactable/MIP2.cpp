/*
 * MIP2.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "MIP2.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include "PneumocyteII.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace primitives;

const int MIP2::classId = Id::getId();
const string MIP2::NAME = "MIP2";
const int MIP2::NUM_STATES = 1;
MIP2* MIP2::molecule = nullptr;

MIP2::MIP2(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Chemokine(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

MIP2* MIP2::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
        molecule = new MIP2(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int MIP2::getClassId() const {
	return classId;
}

double MIP2::getKd() {
    return utils::constexprants::Kd_IL10;
}

void MIP2::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

int MIP2::getIndex(const std::string& str) const {
    return 0;
}

void MIP2::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool MIP2::templateInteract(Interactable* interactable, int x, int y, int z) {
	if (dynamic_cast<Neutrophil*>(interactable)) {
		Cell* cell  = static_cast<Cell*>(interactable);
	    if(!cell->isDead()){
	    	Interactions::secrete(cell, this, utils::constexprants::N_MIP2_QTTY, x, y, z, 0);
	    	Interactions::bind(cell, this, x, y, z, 0);
	    }
	    return true;
	}
	if (dynamic_cast<PneumocyteII*>(interactable))
		return Interactions::secrete(static_cast<Cell*>(interactable), this, utils::constexprants::P_MIP2_QTTY, x, y, z, 0);
	if (dynamic_cast<Macrophage*>(interactable))
		return Interactions::secrete(static_cast<Cell*>(interactable), this, utils::constexprants::MA_MIP2_QTTY, x, y, z, 0);

    return interactable->interact(this, x, y, z);
}

string MIP2::getName() const {
    return NAME;
}

double MIP2::getThreshold() const {
    return 0;
}

int MIP2::getNumState() const {
    return NUM_STATES;
}

bool MIP2::isTime() const {
    return true;
}

double MIP2::chemoatract(int x, int y, int z){
	return utils::Util::activationFunction(this->get(0, x, y, z), utils::constexprants::Kd_MIP2, utils::constexprants::VOXEL_VOL, 1, utils::constexprants::STD_UNIT_T) + utils::constexprants::DRIFT_BIAS;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
