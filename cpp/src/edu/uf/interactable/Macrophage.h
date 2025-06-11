/*
 * Macrophage.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_MACROPHAGE_H_
#define EDU_UF_INTERACTABLE_MACROPHAGE_H_

#include "Leukocyte.h"
#include "../intracellularState/IntracellularModel.h"
#include <string>

namespace edu {
namespace uf {
namespace interactable {

class Macrophage : public Leukocyte {
public:
    static const std::string NAME;

    Macrophage(double ironPool, intracellularState::IntracellularModel* network);

    int getClassId() const override;
    virtual void setMaxMoveStep(int moveStep);
    virtual int getInteractionId() const override;
    virtual bool isTime() const override;

    static std::string getChemokine();
    static void setChemokine(const std::string& chemokine);

    static int getTotalCells();
    static void setTotalCells(int totalCells);

    static double getTotalIron();
    static void setTotalIron(double totalIron);

    virtual bool isEngaged() const override;
    virtual void setEngaged(bool engaged) override;

    virtual int getMaxMoveSteps() override;

    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;

    virtual void incIronPool(double qtty) override;
    virtual void die() override;

    virtual std::string attractedBy() const override;
    virtual int getMaxCell() override;
    virtual std::string getName() const override;

private:
    static std::string chemokine;
    static int totalCells;
    static double totalIron;
    static int interactionId;

    int maxMoveStep;
    bool engaged;
    static const int classId;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_MACROPHAGE_H_ */
