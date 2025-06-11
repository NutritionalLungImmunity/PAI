/*
 * TAFC.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "TAFC.h"
#include "../../utils/Id.h"
#include "../PositionalInfectiousAgent.h"
#include "../../primitives/Interactions.h"
#include "../../utils/Constants.h"
#include "../Siderophore.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::utils;
using namespace edu::uf::primitives;
using namespace edu::uf::diffusion;

const int TAFC::classId = Id::getId();


const string TAFC::NAME = "TAFC";
const double TAFC::THRESHOLD = utils::constexprants::K_M_TF_TAFC * utils::constexprants::VOXEL_VOL / 1.0e6;
TAFC* TAFC::molecule = nullptr;

TAFC::TAFC(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Siderophore::Siderophore(qttys, xbin, ybin, zbin, wbin, diffuse) {}

TAFC* TAFC::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
	        molecule = new TAFC(values, xbin, ybin, zbin, wbin, diffuse);
	    }
	    return molecule;
}

int TAFC::getClassId() const {
	return classId;
}

double TAFC::getKd() {
    return -1;
}

void TAFC::degrade() {}

void TAFC::computeTotalMolecule(int x, int y, int z) {
	totalMoleculesAux[0] += get(0, x, y, z);
	totalMoleculesAux[1] += get(1, x, y, z);
}

bool TAFC::templateInteract(Interactable* interactable, int x, int y, int z) {
    /*if (auto transferrin = dynamic_cast<Transferrin*>(interactable)) {
        return Interactions::siderophoreTransferrinChelation(dynamic_cast<Molecule*>(transferrin), this, Constants::K_M_TF_TAFC, x, y, z);
    }*/
    if (auto afumigatus = dynamic_cast<PositionalInfectiousAgent*>(interactable)) {
        Interactions::uptakeSiderophore(afumigatus, this, utils::constexprants::TAFC_UP, x, y, z);
        return Interactions::secreteSiderophore(afumigatus, this, x, y, z);
    }
    /*if (auto iron = dynamic_cast<Iron*>(interactable)) {
        return Interactions::siderophoreIronChelation(dynamic_cast<Molecule*>(iron), this, x, y, z);
    }*/
    return interactable->interact(this, x, y, z);
}

string TAFC::getName() const {
    return NAME;
}

double TAFC::getThreshold() const {
    return THRESHOLD;
}

int TAFC::getNumState() const {
    return NUM_STATES;
}

double TAFC::getSiderophoreQtty() {
    return utils::constexprants::TAFC_QTTY;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
