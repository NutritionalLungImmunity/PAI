/*
 * Lactoferrin.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "Lactoferrin.h"
#include "../utils/Util.h"
#include "../utils/Id.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include "Neutrophil.h"
#include "Transferrin.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace primitives;

const int Lactoferrin::classId = Id::getId();
const string Lactoferrin::NAME = "Lactoferrin";
const double Lactoferrin::THRESHOLD = utils::constexprants::K_M_TF_LAC * utils::constexprants::VOXEL_VOL / 1.0e6;
Lactoferrin* Lactoferrin::molecule = nullptr;

Lactoferrin::Lactoferrin(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {}

Lactoferrin* Lactoferrin::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
	        molecule = new Lactoferrin(values, xbin, ybin, zbin, wbin, diffuse);
	    }
	    return molecule;
}

int Lactoferrin::getClassId() const {
	return classId;
}

double Lactoferrin::getKd() {
    return -1;
}

int Lactoferrin::getIndex(const char* str) const{
	return INDEXES.at(str);
}

void Lactoferrin::degrade() {}

void Lactoferrin::computeTotalMolecule(int x, int y, int z) {
	totalMoleculesAux[0] += get(0, x, y, z);
	totalMoleculesAux[1] += get(1, x, y, z);
	totalMoleculesAux[2] += get(2, x, y, z);
}

bool Lactoferrin::templateInteract(Interactable* interactable, int x, int y, int z) {
    if (auto macrophage = dynamic_cast<Macrophage*>(interactable))
        return Interactions::lactoferrinMacrophageUpatake(macrophage, this, x, y, z);

    if (auto neutrophil = dynamic_cast<Neutrophil*>(interactable))
        return Interactions::lactoferrinDegranulation(neutrophil, this, x, y, z);

    if (auto transferrin = dynamic_cast<Transferrin*>(interactable))
    	return Interactions::lactoferrinTransferrinChelation(transferrin, this, x, y, z);

    return interactable->interact(this, x, y, z);
}

string Lactoferrin::getName() const {
    return NAME;
}

double Lactoferrin::getThreshold() const {
    return THRESHOLD;
}

int Lactoferrin::getNumState() const {
    return NUM_STATES;
}

bool Lactoferrin::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
