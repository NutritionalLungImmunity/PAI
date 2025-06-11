/*
 * MIP2.h
 *
 *  Created on: Jul 19, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTERACTABLE_MIP2_H_
#define EDU_UF_INTERACTABLE_MIP2_H_

#include "Molecule.h"
#include "../diffusion/Diffuse.h"
#include "Chemokine.h"


namespace edu {
namespace uf {
namespace interactable {

class MIP2 : public Chemokine {
public:
    static const string NAME;
    static const int NUM_STATES;

    static MIP2* getMolecule(Diffuse* diffuse = nullptr, int xbin = -1, int ybin = -1, int zbin  = -1, char wbin = -1);

    int getClassId() const override;
    virtual double getKd() override;
    virtual void degrade() override;
    virtual int getIndex(const std::string& str) const override;
    virtual void computeTotalMolecule(int x, int y, int z) override;


    virtual string getName() const override;
    virtual double getThreshold() const override;
    virtual int getNumState() const override;
    virtual bool isTime() const override;

    virtual double chemoatract(int x, int y, int z) override;

protected:
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;
//    virtual void degrade(double p, int x);

private:
    MIP2(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse);
    static MIP2* molecule;
    static const int classId;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_MIP2_H_ */
