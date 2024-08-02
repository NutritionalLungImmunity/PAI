/*
 * MIP1B.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "MIP1B.h"
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

const int MIP1B::classId = Id::getId();
const string MIP1B::NAME = "MIP1B";
const int MIP1B::NUM_STATES = 1;
MIP1B* MIP1B::molecule = nullptr;

MIP1B::MIP1B(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Chemokine(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

MIP1B* MIP1B::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
        molecule = new MIP1B(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int MIP1B::getClassId() const {
	return classId;
}

double MIP1B::getKd() {
    return utils::constexprants::Kd_IL10;
}

void MIP1B::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

int MIP1B::getIndex(const char* str) const {
    return 0;
}

void MIP1B::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool MIP1B::templateInteract(Interactable* interactable, int x, int y, int z) {
	if (dynamic_cast<PneumocyteII*>(interactable))
		return Interactions::secrete(static_cast<Cell*>(interactable), this, utils::constexprants::P_MIP2_QTTY, x, y, z, 0);
	if (dynamic_cast<Macrophage*>(interactable))
		return Interactions::secrete(static_cast<Cell*>(interactable), this, utils::constexprants::MA_MIP2_QTTY, x, y, z, 0);

    return interactable->interact(this, x, y, z);
}

string MIP1B::getName() const {
    return NAME;
}

double MIP1B::getThreshold() const {
    return 0;
}

int MIP1B::getNumState() const {
    return NUM_STATES;
}

bool MIP1B::isTime() const {
    return true;
}

double MIP1B::chemoatract(int x, int y, int z){
	return utils::Util::activationFunction(this->get(0, x, y, z), utils::constexprants::Kd_MIP1B, utils::constexprants::VOXEL_VOL, 1, utils::constexprants::STD_UNIT_T) + utils::constexprants::DRIFT_BIAS;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
