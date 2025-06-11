/*
 * TAFC.h
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_AFUMIGATUS_TAFC_H_
#define EDU_UF_INTERACTABLE_AFUMIGATUS_TAFC_H_

#include <string>
#include "../../diffusion/Diffuse.h"
#include "../Siderophore.h"
//#include "Interactable.h"
//#include "Molecule.h"




namespace edu {
namespace uf {
namespace interactable {

class TAFC : public Siderophore {
public:
    static const string NAME;
    static const int NUM_STATES = 2;
    static const double THRESHOLD;

    static TAFC* getMolecule(Diffuse* diffuse = nullptr, int xbin = -1, int ybin = -1, int zbin  = -1, char wbin = -1);

    int getClassId() const override;
    virtual double getKd() override;
    virtual void degrade() override;
    virtual void computeTotalMolecule(int x, int y, int z) override;

    virtual string getName() const override;
    virtual double getThreshold() const override;
    virtual int getNumState() const override;
    virtual double getSiderophoreQtty() override;

protected:
    TAFC(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse);
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;

private:
    static TAFC* molecule;
    static const int classId;
};


} // namespace interactable
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTERACTABLE_TAFC_H_ */
