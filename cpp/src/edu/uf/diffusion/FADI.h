/*
 * FADI.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_DIFFUSION_FADI_H_
#define EDU_UF_DIFFUSION_FADI_H_

#include "Diffuse.h"

namespace edu {
namespace uf {
namespace diffusion {

class FADI : public diffusion::Diffuse {
public:
    FADI(double f, double pdeFactor, double deltaT);

protected:
    double f;
    double pdeFactor;
    double deltaT;
};

} // namespace Diffusion
} // namespace uf
} // namespace edu

#endif /* EDU_UF_DIFFUSION_FADI_H_ */
