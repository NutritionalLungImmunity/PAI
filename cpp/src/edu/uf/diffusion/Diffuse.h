/*
 * Diffuse.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_DIFFUSION_DIFFUSE_H_
#define EDU_UF_DIFFUSION_DIFFUSE_H_


namespace edu {
namespace uf {
namespace diffusion {

class Diffuse {
public:
    virtual ~Diffuse() = default;

    virtual void solver(double**** space, int index, int xbin, int ybin, int zbin) = 0;
};

}
}
}


#endif /* EDU_UF_DIFFUSION_DIFFUSE_H_ */
