/*
 * Transferrin.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "Transferrin.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include "afumigatus/TAFC.h"
#include <memory>
#include "../intracellularState/MATestModel.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace primitives;
using namespace afumigatus;

const int Transferrin::classId = Id::getId();
const string Transferrin::NAME = "Transferrin";
const double Transferrin::THRESHOLD = utils::constexprants::K_M_TF_TAFC * utils::constexprants::VOXEL_VOL / 1.0e6;
Transferrin* Transferrin::molecule = nullptr;

Transferrin::Transferrin(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {}

Transferrin* Transferrin::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
	        molecule = new Transferrin(values, xbin, ybin, zbin, wbin, diffuse);
	    }
	    return molecule;
}

int Transferrin::getClassId() const {
	return classId;
}

double Transferrin::getKd() {
    return utils::constexprants::Kd_TfR2;
}

int Transferrin::getIndex(const char* str) const{
	return INDEXES.at(str);
}

void Transferrin::turnOver(int x, int y, int z) {}

void Transferrin::degrade() {}

void Transferrin::computeTotalMolecule(int x, int y, int z) {
	totalMoleculesAux[0] += get(0, x, y, z);
	totalMoleculesAux[1] += get(1, x, y, z);
	totalMoleculesAux[2] += get(2, x, y, z);
}

bool Transferrin::templateInteract(Interactable* interactable, int x, int y, int z) {
    /*if (auto transferrin = dynamic_cast<Transferrin*>(interactable)) {
        return Interactions::siderophoreTransferrinChelation(dynamic_cast<Molecule*>(transferrin), this, Constants::K_M_TF_TAFC, x, y, z);
    }*/
    if (auto macrophage = dynamic_cast<Macrophage*>(interactable))
        return Interactions::transferrinMacrophage(macrophage, this, MATestModel::_FPN,  MATestModel::M1, x, y, z);

    //if(auto iron = dynamic_cast<Iron*>(interactable))
    //	return Interactions::transferrinIronChelation(this, iron, x, y, z);

    if(auto tafc = dynamic_cast<TAFC*>(interactable))
    	return Interactions::siderophoreTransferrinChelation(this, tafc, utils::constexprants::K_M_TF_TAFC, x, y, z);
    /*if (auto iron = dynamic_cast<Iron*>(interactable)) {
        return Interactions::siderophoreIronChelation(dynamic_cast<Molecule*>(iron), this, x, y, z);
    }*/
    return interactable->interact(this, x, y, z);
}

string Transferrin::getName() const {
    return NAME;
}

double Transferrin::getThreshold() const {
    return THRESHOLD;
}

int Transferrin::getNumState() const {
    return NUM_STATES;
}

bool Transferrin::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
