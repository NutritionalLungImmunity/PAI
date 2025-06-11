/*
 * FADIPeriodic.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_DIFFUSION_FADIPERIODIC_H_
#define EDU_UF_DIFFUSION_FADIPERIODIC_H_

#include "FADI.h"

namespace edu {
namespace uf {
namespace diffusion {

class FADIPeriodic : public FADI {
public:
    FADIPeriodic(double f, double pdeFactor, double deltaT);

    void solver(double**** space, int index, int xbin, int ybin, int zbin) override;
};

} // namespace Diffusion
} // namespace uf
} // namespace edu

#endif /* EDU_UF_DIFFUSION_FADIPERIODIC_H_ */
