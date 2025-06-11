/*
 * Molecule.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Molecule.h"
#include <cstring>
#include "../utils/Constants.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace utils;

Molecule::Molecule(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse) {
    this->id = Id::getId();
    this->moleculeId = Id::getId();
    this->values = qttys;
    this->wbin = wbin;
    this->xbin = xbin;
    this->ybin = ybin;
    this->zbin = zbin;
    this->totalMoleculesAux = new double[wbin];
    this->totalMolecules = new double[wbin];
    this->diff = diffuse;
    this->phenotype = -1;

    for (int l = 0; l < wbin; l++) {
        for (int i = 0; i < xbin; i++) {
            for (int j = 0; j < ybin; j++) {
                for (int k = 0; k < zbin; k++) {
                    this->incTotalMolecule(i, qttys[l][i][j][k]);
                }
            }
        }
    }
}

int Molecule::getInteractionId() const {
    return moleculeId;
}

void Molecule::setPhenotype(int phenotype) {
    this->phenotype = phenotype;
}

int Molecule::getPhenotype() const {
    return this->phenotype;
}

double Molecule::inc(double qtty, const char* index, int x, int y, int z) {
    return inc(qtty, this->getIndex(index), x, y, z);
}

double Molecule::inc(double qtty, int index, int x, int y, int z) {
    this->values[index][x][y][z] += qtty;
    return this->values[index][x][y][z];
}

double Molecule::dec(double qtty, const char* index, int x, int y, int z) {
    return dec(qtty, this->getIndex(index), x, y, z);
}

double Molecule::dec(double qtty, int index, int x, int y, int z) {
    qtty = this->values[index][x][y][z] - qtty >= 0 ? qtty : 0;
    this->values[index][x][y][z] -= qtty;
    return this->values[index][x][y][z];
}

double Molecule::pdec(double p, const char* index, int x, int y, int z) {
    return pdec(p, this->getIndex(index), x, y, z);
}

double Molecule::pdec(double p, int index, int x, int y, int z) {
    double dec = this->values[index][x][y][z] * p;
    this->values[index][x][y][z] -= dec;
    return this->values[index][x][y][z];
}

double Molecule::pinc(double p, const char* index, int x, int y, int z) {
    return pinc(p, this->getIndex(index), x, y, z);
}

double Molecule::pinc(double p, int index, int x, int y, int z) {
    double inc = this->values[index][x][y][z] * p;
    this->values[index][x][y][z] += inc;
    return this->values[index][x][y][z];
}

void Molecule::set(double qtty, const char* index, int x, int y, int z) {
    set(qtty, this->getIndex(index), x, y, z);
}

void Molecule::set(double qtty, int index, int x, int y, int z) {
    this->incTotalMolecule(index, -this->values[index][x][y][z]);
    this->incTotalMolecule(index, qtty);
    this->values[index][x][y][z] = qtty;
}

double Molecule::get(const char* index, int x, int y, int z) const {
    return get(this->getIndex(index), x, y, z);
}

double Molecule::get(int index, int x, int y, int z) const {
    return this->values[index][x][y][z];
}

void Molecule::turnOver(int x, int y, int z) {
    for (int i = 0; i < wbin; i++) {
        this->pdec(1 - utils::constexprants::MCP1_HALF_LIFE, i, x, y, z);
    }
}

void Molecule::diffuse() {
    if (diff == nullptr) return;
    for (int i = 0; i < wbin; i++) {
        diff->solver(values, i, xbin, ybin, zbin);
    }
}

void Molecule::resetCount() {
    for (int i = 0; i < wbin; i++) {
        this->totalMolecules[i] = this->totalMoleculesAux[i];
        this->totalMoleculesAux[i] = 0;
    }
}

void Molecule::incTotalMolecule(int index, double inc) {
    this->totalMolecules[index] += inc;
}

int Molecule::getId() const {
    return id;
}

double Molecule::getTotalMolecule(int index) const {
    return this->totalMolecules[index];
}

int Molecule::getXbin() const{
	return xbin;
}

int Molecule::getYbin() const{
	return ybin;
}

int Molecule::getZbin() const{
	return zbin;
}

char Molecule::getWbin() const{
	return wbin;
}

void Molecule::degrade(double p, int x){
	if(p < 0) {
		return;
	}
	for(int i = 0; i < wbin; i++) {
		//this.pdec(1-p, i, x);
	}
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
