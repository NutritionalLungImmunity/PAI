/*
 * Chemokine.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_CHEMOKINE_H_
#define EDU_UF_INTERACTABLE_CHEMOKINE_H_

#include "Molecule.h"
#include "../diffusion/Diffuse.h"
#include <string>

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::diffusion;

class Chemokine : public Molecule {
public:
    Chemokine(double**** qttys, int xbin, int ybin, int zbin, char wbin, Diffuse* diffuse)
        : Molecule(qttys, xbin, ybin, zbin, wbin, diffuse) {}

    virtual double chemoatract(int x, int y, int z) = 0;
};

}
}
}


#endif /* EDU_UF_INTERACTABLE_CHEMOKINE_H_ */
