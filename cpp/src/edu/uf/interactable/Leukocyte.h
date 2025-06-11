/*
 * Leukocyte.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_LEUKOCYTE_H_
#define EDU_UF_INTERACTABLE_LEUKOCYTE_H_

#include "Cell.h"
#include <memory>
#include <vector>

namespace edu {
namespace uf {
namespace interactable {

class Leukocyte : public Cell {
public:
    Leukocyte(IntracellularModel* network);
    Leukocyte(double ironPool, IntracellularModel* network);

    virtual int getMaxCell() = 0;

    virtual bool isEngaged() const;
    virtual void setEngaged(bool engaged);

    virtual void kill();
    virtual void move(int x, int y, int z, int steps) override;

    virtual void setPhagosome(vector<Cell*>& phagosome);
    virtual vector<Cell*>& getPhagosome();

    void addAspergillus(Cell* aspergillus);

    virtual int getMaxMoveSteps() override = 0;

private:
    bool engaged;
    vector<Cell*> phagosome;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_LEUKOCYTE_H_ */
