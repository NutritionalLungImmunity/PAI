/*
 * NeutrophilRecruiter.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_COMPARTMENTS_NEUTROPHILRECRUITER_H_
#define EDU_UF_COMPARTMENTS_NEUTROPHILRECRUITER_H_

#include "Recruiter.h"
#include "../interactable/Cell.h"
#include "Voxel.h"



namespace edu {
namespace uf {
namespace compartments {

class NeutrophilRecruiter : public Recruiter {
public:
    edu::uf::interactable::Cell* createCell() override;

    virtual int getQtty() override;

protected:
    virtual int getQtty(double chemokine) override;

public:
    virtual bool leave() override;

    edu::uf::interactable::Cell* getCell(edu::uf::compartments::Voxel* voxel) override;
};

} // namespace compartments
} // namespace uf
} // namespace edu

#endif /* EDU_UF_COMPARTMENTS_NEUTROPHILRECRUITER_H_ */
