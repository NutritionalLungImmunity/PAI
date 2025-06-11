/*
 * NeutrophilStateModel.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */
#include "IntracellularModel.h"
#include "AspergillusIntracellularModel.h"
#include "../compartments/GridFactory.h"
#include "../interactable/Cell.h"
#include "../interactable/InfectiousAgent.h"
#include "../interactable/Lactoferrin.h"
#include "../interactable/Neutrophil.h"
#include "../compartments/Voxel.h"
#include "../interactable/Cell.h"
#include "../interactable/afumigatus/Afumigatus.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include <vector>
#include <algorithm>
#include <random>
#include "NeutrophilStateModel.h"
#include <iostream>

namespace edu {
namespace uf {
namespace intracellularState {

using namespace compartments;
using namespace utils;
using namespace afumigatus;

const string NeutrophilStateModel::name = "NeutrophilStateModel";

const int NeutrophilStateModel::ACTIVE = 0;
const int NeutrophilStateModel::DYING = 1;
const int NeutrophilStateModel::APOPTOTIC = 2;
const int NeutrophilStateModel::NETOTIC = 3;

NeutrophilStateModel::NeutrophilStateModel() {
    booleanNetwork = new int[size]();
}

void NeutrophilStateModel::processBooleanNetwork() {
    if (booleanNetwork[APOPTOTIC] > 0 || booleanNetwork[NETOTIC] == 1) {
        return;
    }

    int k = 0;
    vector<int> array(size);
    iota(array.begin(), array.end(), 0);

    while (true) {
        if (k++ > 3) break;
        shuffle(array.begin(), array.end(), Rand::getRand().getGenerator());
        for (int i : array) {
            switch (i) {
                case ACTIVE:
                    booleanNetwork[ACTIVE] = getInput(Afumigatus::DEF_OBJ) |
                                             //getInput(TLRBinder::getBinder()) |
                                             booleanNetwork[ACTIVE];
                    break;
                case DYING:
                    booleanNetwork[DYING] = booleanNetwork[ACTIVE] > 0 ?
                                            (Rand::getRand().randunif() < utils::constexprants::NEUTROPHIL_HALF_LIFE ? 1 : 0) :
                                            0;
                    break;
                case APOPTOTIC:
                    if (booleanNetwork[APOPTOTIC] == 0) {
                        int j = booleanNetwork[DYING] > 0 ?
                                (Rand::getRand().randunif() < utils::constexprants::PR_NEUT_APOPT ? 2 : 1) :
                                0;

                        booleanNetwork[APOPTOTIC] = j == 2 ? 1 : 0;
                        booleanNetwork[NETOTIC] = j == 1 ? 1 : 0;
                    }
                    break;
                case NETOTIC:
                    booleanNetwork[APOPTOTIC] = booleanNetwork[ACTIVE] == 0 ?
                                                (Rand::getRand().randunif() < utils::constexprants::NEUTROPHIL_HALF_LIFE ? 1 : 0) :
                                                0;
                    break;
                default:
                    cerr << "No such interaction " << i << "!" << endl;;
                    break;
            }
        }
    }

    inputs.clear();
    clearPhenotype();
    computePhenotype();
}

void NeutrophilStateModel::computePhenotype() {
    if (booleanNetwork[ACTIVE] > 0) {
        getPhenotype()[ACTIVE] = booleanNetwork[ACTIVE];
        getPhenotype()[Lactoferrin::getMolecule()->getPhenotype()] = booleanNetwork[ACTIVE];
    }
    if (booleanNetwork[APOPTOTIC] == 1) {
        getPhenotype()[APOPTOTIC] = booleanNetwork[APOPTOTIC];
    }
    if (booleanNetwork[NETOTIC] == 1) {
        getPhenotype()[NETOTIC] = booleanNetwork[NETOTIC];
    }
}

void NeutrophilStateModel::updateStatus(int id, int x, int y, int z) {
    Neutrophil* net = dynamic_cast<Neutrophil*>(Cell::get(id));
    if (this->getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DEAD) {
        return;
    }

    if (net->getBooleanNetwork()->hasPhenotype(APOPTOTIC)) {
        net->die();
        for (Cell* entry : net->getPhagosome()) {
            entry->getBooleanNetwork()->setState(AspergillusIntracellularModel::LOCATION, AspergillusIntracellularModel::RELEASING);
        }
    }
    if (net->getBooleanNetwork()->hasPhenotype(NETOTIC)) {
        GridFactory::getGrid()[x][y][z]->setExternalState(1);
        if (Rand::getRand().randunif() < net->getNetHalfLife()) {
            net->die();
            GridFactory::getGrid()[x][y][z]->setExternalState(0);
            for (Cell* entry : net->getPhagosome()) {
                entry->getBooleanNetwork()->setState(AspergillusIntracellularModel::LOCATION, AspergillusIntracellularModel::RELEASING);
            }
        }
    }

    net->setMaxMoveStep(-1);
    net->setEngaged(false);
}

} // namespace intracellularState
} // namespace uf
} // namespace edu
