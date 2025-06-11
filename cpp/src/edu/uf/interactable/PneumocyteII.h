/*
 * PneumocyteII.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_PNEUMOCYTEII_H_
#define EDU_UF_INTERACTABLE_PNEUMOCYTEII_H_

#include <memory>
#include <string>
#include "Cell.h"
#include "../intracellularState/IntracellularModel.h"


namespace edu {
namespace uf {
namespace interactable {

class PneumocyteII : public Cell {
public:
    static const std::string NAME;

    PneumocyteII(intracellularState::IntracellularModel* network);

    int getClassId() const override;
    virtual int getInteractionId() const override;
    virtual bool isTime() const override;

    static int getTotalCells();
    static void setTotalCells(int totalCells);

    virtual int getIteration() const;

    virtual void die() override;

    virtual void incIronPool(double qtty) override;

    virtual void move(int x, int y, int z, int steps) override;

    virtual std::string getName() const override;

    virtual int getMaxMoveSteps() override;

protected:
    virtual bool templateInteract(interactable::Interactable* interactable, int x, int y, int z) override;

private:
    static int totalCells;
    static int interactionId;
    int iteration;
    static const int classId;
};

} // namespace interactable
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTERACTABLE_PNEUMOCYTEII_H_ */
