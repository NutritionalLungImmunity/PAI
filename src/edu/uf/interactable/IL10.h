/*
 * IL10.h
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTERACTABLE_IL10_H_
#define EDU_UF_INTERACTABLE_IL10_H_

#include "Molecule.h"
#include "../diffusion/Diffuse.h"



namespace edu {
namespace uf {
namespace interactable {

class IL10 : public Molecule {
public:
    static const string NAME;
    static const int NUM_STATES;

    static IL10* getMolecule(Diffuse* diffuse = nullptr, int xbin = -1, int ybin = -1, int zbin  = -1, char wbin = -1);

    int getClassId() const override;
    virtual double getKd() override;
    virtual void degrade() override;
    virtual int getIndex(const std::string& str) const override ;
    virtual void computeTotalMolecule(int x, int y, int z) override ;


    virtual string getName() const override;
    virtual double getThreshold() const override;
    virtual int getNumState() const override;
    virtual bool isTime() const override;

protected:
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;
//    virtual void degrade(double p, int x);

private:
    IL10(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse);
    static IL10* molecule;
    static const int classId;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_IL10_H_ */
