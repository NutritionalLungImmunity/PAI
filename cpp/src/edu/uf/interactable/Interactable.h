/*
 * Interactable.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_INTERACTABLE_H_
#define EDU_UF_INTERACTABLE_INTERACTABLE_H_

#include <set>
#include <vector>
#include <string>
#include "Binder.h"

namespace edu {
namespace uf {
namespace interactable {

using namespace std;

class Interactable : public Binder {
private:
    set<int> negativeInteractList;
    int callCounter = 0;
    vector<Interactable*> removeList;


public:

    virtual ~Interactable() = default;

    virtual bool isTime() const = 0;

    bool interact(Interactable* interactable, int x, int y, int z);

    virtual string getName() const = 0;
    virtual int getClassId() const = 0;
    virtual int getId() const = 0;

    bool isInNegativeList(Interactable* interactable);

protected:
    virtual bool templateInteract(Interactable* interactable, int x, int y, int z) = 0;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_INTERACTABLE_H_ */
