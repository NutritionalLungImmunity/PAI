/*
 * IntracellularModel.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTRACELLULARSTATE_INTRACELLULARMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_INTRACELLULARMODEL_H_

#include <unordered_map>
#include <vector>
#include <algorithm>
#include "../interactable/Binder.h"
#include "../interactable/Molecule.h"

namespace edu {
namespace uf {
namespace intracellularState {

using namespace std;
using namespace edu::uf;
using namespace interactable;

class IntracellularModel {
public:
    static const int STATUS;
    static const int LIFE_STATUS;
    static const int LOCATION;
    static const int DEAD;
    static const int ALIVE;
    static const int APOPTOTIC;
    static const int NECROTIC;
    static const int DYING;
    static const int NETOTIC;

    IntracellularModel();
    virtual ~IntracellularModel() = default;

    virtual int getBnIteration() const;
    virtual int* getBooleanNetwork() const;
    virtual void setBooleanNetwork(int* booleanNetwork);
    virtual void setBnIteration(int bnIteration);
    virtual void activateReceptor(int idx, int level);
    virtual void processBooleanNetwork() = 0;
    virtual void updateStatus(int id, int x, int y, int z) = 0;
    virtual void clearPhenotype();
    virtual bool hasPhenotype(const Molecule& molecule) const;
    virtual bool hasPhenotype(int phenotype) const;
    virtual bool hasPhenotype(const int* phenotype, int length) const;
    virtual unordered_map<int, int>& getPhenotype();
    virtual void addPhenotype(int phenotype, int level);
    virtual void addPhenotype(int phenotype);
    virtual unordered_map<int, int>& getState();
    virtual int getState(int state) const;
    virtual void setState(int stateName, int stateValue);
    virtual bool isDead() const;
    virtual int getInput(const Binder* i) const;
    //virtual int amax(const int* a, int length) const;
    template<typename Container>
    auto amax(const Container& container) -> typename Container::value_type;
    template<typename T>
    T amax(const T* arr, int size);
    virtual int max(int i, int j) const;
    virtual int amin(const int* a, int length) const;
    virtual int min(int i, int j) const;
    virtual int gnot(int i, int k) const;

protected:
    virtual void computePhenotype() = 0;
    unordered_map<int, int> inputs;
    int* booleanNetwork;

private:
    int bnIteration;


    unordered_map<int, int> phenotypes;
    unordered_map<int, int> states;
};





} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTRACELLULARSTATE_INTRACELLULARMODEL_H_ */
