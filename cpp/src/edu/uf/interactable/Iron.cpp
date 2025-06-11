/*
 * Iron.cpp
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#include "Iron.h"
#include "../utils/Id.h"
#include "../utils/Constants.h"
#include "../utils/Util.h"
#include "../primitives/Interactions.h"
#include "Macrophage.h"
#include "Neutrophil.h"
#include "Transferrin.h"
#include "Lactoferrin.h"
#include "afumigatus/TAFC.h"
#include "afumigatus/Afumigatus.h"
#include "../intracellularState/IntracellularModel.h"
#include <memory>

namespace edu {
namespace uf {
namespace interactable {

using namespace primitives;
using namespace afumigatus;

const int Iron::classId = Id::getId();
const string Iron::NAME = "Iron";
const int Iron::NUM_STATES = 1;
Iron* Iron::molecule = nullptr;

Iron::Iron(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
    : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {
    setPhenotype(Id::getId());
}

Iron* Iron::getMolecule(Diffuse* diffuse, int xbin, int ybin, int zbin, char wbin) {
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
        molecule = new Iron(values, xbin, ybin, zbin, wbin, diffuse);
    }
    return molecule;
}

int Iron::getClassId() const {
	return classId;
}

double Iron::getKd() {
    return -1;
}

void Iron::degrade() {
    //degrade(utils::constexprants::TNF_HALF_LIFE, 0);
}

void Iron::turnOver(int x, int y, int z) {
	this->set(100*6.4e-18*0, 0, x, y, z);
}

int Iron::getIndex(const std::string& str) const {
    return 0;
}

void Iron::computeTotalMolecule(int x, int y, int z) {
    totalMoleculesAux[0] += get(0, x, y, z);
}

bool Iron::templateInteract(Interactable* interactable, int x, int y, int z) {
	if (dynamic_cast<Macrophage*>(interactable)) {
		Cell* cell  = static_cast<Cell*>(interactable);
	    return Interactions::releaseIron(cell, this, IntracellularModel::NECROTIC, x, y, z);
	}
	if(auto afumigatus = dynamic_cast<Afumigatus*>(interactable)){
		if(Interactions::releaseIron(afumigatus, this, IntracellularModel::DYING, x, y, z)) return true;
		return Interactions::releaseIron(afumigatus, this, IntracellularModel::DEAD, x, y, z);
	}
	if(auto tafc = dynamic_cast<TAFC*>(interactable))
		return Interactions::siderophoreIronChelation(this, tafc, x, y, z);
	if(auto lac = dynamic_cast<Lactoferrin*>(interactable))
		return Interactions::transferrinIronChelation(lac, this, x, y, z);
	if(auto tf = dynamic_cast<Transferrin*>(interactable))
		return Interactions::transferrinIronChelation(tf, this, x, y, z);
	if(auto neutrophil = dynamic_cast<Neutrophil*>(interactable))
		return Interactions::releaseIron(neutrophil, this, IntracellularModel::NETOTIC, x, y, z);


    return interactable->interact(this, x, y, z);
}

string Iron::getName() const {
    return NAME;
}

double Iron::getThreshold() const {
    return 0;
}

int Iron::getNumState() const {
    return NUM_STATES;
}

bool Iron::isTime() const {
    return true;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
