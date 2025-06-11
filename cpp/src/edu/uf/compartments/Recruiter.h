/*
 * Recruiter.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_COMPARTMENTS_RECRUITER_H_
#define EDU_UF_COMPARTMENTS_RECRUITER_H_

#include "../interactable/Cell.h"
#include "Voxel.h"

namespace edu {
namespace uf {
namespace compartments {

using namespace edu::uf::compartments;
using namespace edu::uf::interactable;

class Recruiter {
public:
    virtual ~Recruiter() = default;

    virtual void recruit();

    virtual Cell* createCell() = 0;

    virtual int getQtty() = 0;

    virtual int getQtty(double c) = 0;

    virtual bool leave() = 0;

    virtual Cell* getCell(Voxel* voxel) = 0;
};

} // namespace compartments
} // namespace uf
} // namespace edu

#endif /* EDU_UF_COMPARTMENTS_RECRUITER_H_ */
