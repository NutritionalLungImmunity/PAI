/*
 * Molecule.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_MOLECULE_H_
#define EDU_UF_INTERACTABLE_MOLECULE_H_

#include "../diffusion/Diffuse.h"
#include "Interactable.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf;
using namespace diffusion;

class Molecule : public Interactable {
private:
    int id;
    int moleculeId;
    static const int NUM_COMPARTMENTS = 2;
    double**** values;
    double* totalMolecules;
    Diffuse* diff;
    int phenotype;
    int xbin;
    int ybin;
    int zbin;
    char wbin;

    void incTotalMolecule(int index, double inc);

protected:
    double* totalMoleculesAux;
    Molecule(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse);
    virtual void degrade(double p, int x);

public:
    //Molecule() = default; // @suppress("Class members should be properly initialized")
    virtual int getInteractionId() const;
    virtual void setPhenotype(int phenotype);
    virtual int getPhenotype() const;
    double inc(double qtty, const char* index, int x, int y, int z);
    double inc(double qtty, int index, int x, int y, int z);
    double dec(double qtty, const char* index, int x, int y, int z);
    double dec(double qtty, int index, int x, int y, int z);
    double pdec(double p, const char* index, int x, int y, int z);
    double pdec(double p, int index, int x, int y, int z);
    double pinc(double p, const char* index, int x, int y, int z);
    double pinc(double p, int index, int x, int y, int z);
    void set(double qtty, const char* index, int x, int y, int z);
    void set(double qtty, int index, int x, int y, int z);
    double get(const char* index, int x, int y, int z) const;
    double get(int index, int x, int y, int z) const;
    virtual void turnOver(int x, int y, int z);
    virtual void diffuse();
    virtual void resetCount();
    virtual int getId() const;
    virtual double getTotalMolecule(int index) const;
    int getXbin() const;
    int getYbin() const;
    int getZbin() const;
    char getWbin() const;

    virtual void degrade() = 0;
    virtual void computeTotalMolecule(int x, int y, int z) = 0;
    virtual int getIndex(const char* str) const = 0;
    virtual double getThreshold() const = 0;
    virtual int getNumState() const = 0;
    virtual double getKd() = 0;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_MOLECULE_H_ */
