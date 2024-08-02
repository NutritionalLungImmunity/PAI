/*
 * Lactoferrin.h
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_LACTOFERRIN_H_
#define EDU_UF_INTERACTABLE_LACTOFERRIN_H_

#include "Molecule.h"
#include "../diffusion/Diffuse.h"
#include "../utils/Constants.h"
#include <unordered_map>

namespace edu {
namespace uf {
namespace interactable {

class Lactoferrin : public Molecule {
public:
    static const string NAME;
    static const int NUM_STATES = 3;
    static const double THRESHOLD;

    static Lactoferrin* getMolecule(Diffuse* diffuse = nullptr, int xbin = -1, int ybin = -1, int zbin  = -1, char wbin = -1);

    int getClassId() const override;
    virtual double getKd() override;
    virtual void degrade() override;
    virtual int getIndex(const char* str) const override;
    virtual void computeTotalMolecule(int x, int y, int z) override;


    virtual string getName() const override;
    virtual double getThreshold() const override;
    virtual int getNumState() const override;
    virtual bool isTime() const override;

protected:
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;
    //virtual void degrade(double p, int x);

private:
    Lactoferrin(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse);
    static Lactoferrin* molecule;
    const unordered_map<const char*, int> INDEXES = {
    		    {"Lactoferrin", 0},
    		    {"LactoferrinFe", 1},
				{"LactoferrinFe2", 2}
    		};
    static const int classId;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_LACTOFERRIN_H_ */
