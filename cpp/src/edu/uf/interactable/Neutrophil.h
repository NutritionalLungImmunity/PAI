/*
 * Neutrophil.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_NEUTROPHIL_H_
#define EDU_UF_INTERACTABLE_NEUTROPHIL_H_

#include "Leukocyte.h"
#include "../intracellularState/IntracellularModel.h"



namespace edu {
namespace uf {
namespace interactable {

class Neutrophil : public Leukocyte {
public:
    static const std::string NAME;
    static bool netOnly;

    Neutrophil(double ironPool, intracellularState::IntracellularModel* network);

    int getClassId() const override;
    virtual void setMaxMoveStep(int moveStep);
    virtual double getNetHalfLife() const;
    virtual void setNetHalfLife(double halfLife);
    virtual int getInteractionId() const override;
    virtual bool isTime() const override;
    virtual bool hasDegranulated() const;
    virtual void degranulate();

    static std::string getChemokine();
    static void setChemokine(const std::string& chemokine);

    static int getTotalCells();
    static void setTotalCells(int totalCells);

    static double getTotalIron();
    static void setTotalIron(double totalIron);

    virtual int getMaxMoveSteps() override;
    virtual void incIronPool(double qtty) override;
    virtual void die() override;
    virtual std::string attractedBy() const override;
    virtual std::string getName() const override;
    virtual int getMaxCell() override;

protected:
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) override;

private:
    static std::string chemokine;
    static int totalCells;
    static double totalIron;

    int maxMoveStep;
    bool degranulated;
    double netHalfLife;
    //bool depleted;
    //bool control;

    static int interactionId;
    static const int classId;
};

} // namespace interactable
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTERACTABLE_NEUTROPHIL_H_ */
