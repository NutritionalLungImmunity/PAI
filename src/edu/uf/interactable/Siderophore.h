/*
 * Siderophore.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_SIDEROPHORE_H_
#define EDU_UF_INTERACTABLE_SIDEROPHORE_H_

#include <unordered_map>
#include "../diffusion/Diffuse.h"
#include "Molecule.h"



namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::diffusion;

class Siderophore : public Molecule{

private:
	const unordered_map<const char*, int> INDEXES = {
		    {"SID", 0},
		    {"SIDBI", 1}
		};
    bool hasSid;

protected:

    Siderophore(double**** qttys, int xbin, int ybin, int zbin, int wbin, Diffuse* diffuse) :
    	Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {
    	this->hasSid = false;
    }

public:
    static constexpr int NUM_STATES = 2;

    virtual double getKd() override {
    	return -1;
    }

    virtual void degrade() override {} //REVIEW

    virtual int getIndex(const char* str) const override{
        return Siderophore::INDEXES.at(str);
    }

    virtual void computeTotalMolecule(int x, int y, int z) override{
    	this->totalMoleculesAux[0] = this->totalMoleculesAux[0] + this->get(0, x, y, z);
    	this->totalMoleculesAux[1] = this->totalMoleculesAux[1] + this->get(0, x, y, z);
    }

    virtual double getSiderophoreQtty() = 0;

    virtual void setHasSiderophore(bool siderophore) {
    	this->hasSid = siderophore;
    }

    virtual bool hasSiderophore() {
    	return this->hasSid;
    }

    virtual int getNumState() const override{
		return NUM_STATES;
	}

    virtual bool isTime() const override{
		return true;
	}

    virtual double getThreshold() const override{
		return 0;
	}
};

}
}
}


#endif /* EDU_UF_INTERACTABLE_SIDEROPHORE_H_ */
