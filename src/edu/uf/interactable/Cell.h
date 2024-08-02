/*
 * Cell.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_CELL_H_
#define EDU_UF_INTERACTABLE_CELL_H_

#include <unordered_map>
#include <memory>
#include <string>
#include "Interactable.h"
#include "../intracellularState/IntracellularModel.h"
#include "../time/Clock.h"
#include "Binder.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf;
using namespace time;
using namespace intracellularState;

class Cell : public Interactable {
private:
    static unordered_map<int, Cell*> cells;
    int id;
    double ironPool;
    bool engulfed;
    Clock *clock;
    IntracellularModel *intracellularModel;
    int externalState;

public:
    Cell(IntracellularModel* intracellularModel);

    static void remove(int id);
    static Cell* get(int id);

    virtual void setExternalState(int state);
    virtual int getExternalState() const;

    virtual double getIronPool() const;
    virtual void setIronPool(double ironPool);

    virtual bool isEngulfed() const;
    virtual void setEngulfed(bool engulfed);

    virtual int getId() const;
    virtual void setId(int id);

    virtual void bind(Binder* iter, int level);

    virtual Clock* getClock() const;
    virtual IntracellularModel* getBooleanNetwork() const;

    virtual bool isDead() const;
    virtual void updateStatus(int x, int y, int z);

    virtual bool removeUponDeath() const;

    virtual void move(int x, int y, int z, int steps) = 0;
    virtual void die() = 0;
    virtual void incIronPool(double ironPool) = 0;
    virtual int getMaxMoveSteps() = 0;

    virtual string attractedBy() const;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_CELL_H_ */
