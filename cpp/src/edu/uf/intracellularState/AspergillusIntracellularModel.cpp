/*
 * AspergillusIntracellularModel.cpp
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#include "AspergillusIntracellularModel.h"
#include "../interactable/afumigatus/Afumigatus.h"
#include "../interactable/Cell.h"
#include "../utils/Constants.h"
#include "../utils/Rand.h"
#include "../utils/Util.h"
#include "../utils/Id.h"

namespace edu {
namespace uf {
namespace intracellularState {

using namespace edu::uf::utils;
using namespace afumigatus;

int AspergillusIntracellularModel::INIT_AFUMIGATUS_BOOLEAN_STATE[] = {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


const int AspergillusIntracellularModel::RESTING_CONIDIA = Id::getId();
const int AspergillusIntracellularModel::SWELLING_CONIDIA = Id::getId();
const int AspergillusIntracellularModel::GERM_TUBE = Id::getId();
const int AspergillusIntracellularModel::HYPHAE = Id::getId();
const int AspergillusIntracellularModel::STERILE_CONIDIA = Id::getId();

const int AspergillusIntracellularModel::FREE = Id::getId();
const int AspergillusIntracellularModel::INTERNALIZING = Id::getId();
const int AspergillusIntracellularModel::RELEASING = Id::getId();
const int AspergillusIntracellularModel::ENGAGED = Id::getId();

const int AspergillusIntracellularModel::SECRETING_TAFC = Id::getId();
const int AspergillusIntracellularModel::UPTAKING_TAFC = Id::getId();

const int AspergillusIntracellularModel::NUM_SPECIES = 22;

AspergillusIntracellularModel::AspergillusIntracellularModel(int phenotype) : IntracellularModel() {
    setBnIteration(0);
    setBooleanNetwork(&INIT_AFUMIGATUS_BOOLEAN_STATE[0]);
    setState(LOCATION, AspergillusIntracellularModel::FREE);
    setState(STATUS, phenotype);
    this->lipActivation = 0;
}

void AspergillusIntracellularModel::updateStatus(int id, int x, int y, int z) {
    Afumigatus* asp = dynamic_cast<Afumigatus*>(Cell::get(id));

    lipActivation = Rand::getRand().randunif() < Util::activationFunction(asp->getIronPool(), utils::constexprants::Kd_LIP, utils::constexprants::HYPHAE_VOL) ? 1 : 0;

    if (getState(STATUS) == AspergillusIntracellularModel::RESTING_CONIDIA && asp->getClock()->getCount() >= utils::constexprants::ITER_TO_SWELLING) {
        if (Rand::getRand().randunif() < utils::constexprants::PR_ASPERGILLUS_CHANGE) {
            setState(STATUS, AspergillusIntracellularModel::SWELLING_CONIDIA);
            Afumigatus::incTotalCells(2);
            Afumigatus::decTotalCells(1);
        }
    } else if (!asp->isEngaged() && getState(STATUS) == AspergillusIntracellularModel::SWELLING_CONIDIA && asp->getClock()->getCount() >= utils::constexprants::ITER_TO_GERMINATE) {
        setState(STATUS, AspergillusIntracellularModel::GERM_TUBE);
        Afumigatus::incTotalCells(3);
        Afumigatus::decTotalCells(2);
    } else if (getState(IntracellularModel::LIFE_STATUS) == IntracellularModel::DYING) {
        asp->die();
    }

    if (asp->getNextSepta() == nullptr)
        asp->setGrowable(true);

    if (getState(LOCATION) == AspergillusIntracellularModel::INTERNALIZING || getState(LOCATION) == AspergillusIntracellularModel::RELEASING)
        setState(LOCATION, AspergillusIntracellularModel::FREE);

    asp->diffuseIron();
    if (asp->getNextBranch() == nullptr)
        asp->setGrowable(true);
    asp->setEpithelialInhibition(2.0);
}

int AspergillusIntracellularModel::hasLifeStage(int phenotype) {
    if (phenotype == AspergillusIntracellularModel::RESTING_CONIDIA)
        return 1;
    if (phenotype == AspergillusIntracellularModel::SWELLING_CONIDIA)
        return 2;
    if (phenotype == AspergillusIntracellularModel::GERM_TUBE)
        return 3;
    if (phenotype == AspergillusIntracellularModel::HYPHAE)
        return 4;
    if (phenotype == AspergillusIntracellularModel::STERILE_CONIDIA)
        return 5;
    return -1;
}

void AspergillusIntracellularModel::processBooleanNetwork() {
    int temp[NUM_SPECIES] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    temp[hapX] = -booleanNetwork[sreA] + 1;
    temp[sreA] = -booleanNetwork[HapX] + 1;
    temp[HapX] = booleanNetwork[hapX] & (-booleanNetwork[LIP] + 1);
    temp[SreA] = booleanNetwork[sreA] & booleanNetwork[LIP];
    temp[RIA] = -booleanNetwork[SreA] + 1;
    temp[EstB] = -booleanNetwork[SreA] + 1;
    temp[MirB] = booleanNetwork[HapX] & (-booleanNetwork[SreA] + 1);
    temp[SidA] = booleanNetwork[HapX] & (-booleanNetwork[SreA] + 1);
    temp[Tafc] = booleanNetwork[SidA];
    temp[ICP] = (-booleanNetwork[HapX] + 1) & (booleanNetwork[VAC] | booleanNetwork[FC1fe]);
    temp[LIP] = (booleanNetwork[Fe] & booleanNetwork[RIA]) | lipActivation;
    temp[CccA] = -booleanNetwork[HapX] + 1;
    temp[FC0fe] = booleanNetwork[SidA];
    temp[FC1fe] = booleanNetwork[LIP] & booleanNetwork[FC0fe];
    temp[VAC] = booleanNetwork[LIP] & booleanNetwork[CccA];
    temp[ROS] = (booleanNetwork[O] & (-(booleanNetwork[SOD2_3] & booleanNetwork[ThP] & booleanNetwork[Cat1_2]) + 1))
                | (booleanNetwork[ROS] & (-(booleanNetwork[SOD2_3] & (booleanNetwork[ThP] | booleanNetwork[Cat1_2])) + 1));
    temp[Yap1] = booleanNetwork[ROS];
    temp[SOD2_3] = booleanNetwork[Yap1];
    temp[Cat1_2] = booleanNetwork[Yap1] & (-booleanNetwork[HapX] + 1);
    temp[ThP] = booleanNetwork[Yap1];

    temp[Fe] = 0;
    temp[O] = 0;

    for (int i = 0; i < NUM_SPECIES; ++i) {
        booleanNetwork[i] = temp[i];
    }
    setBnIteration(0);
    computePhenotype();
}

void AspergillusIntracellularModel::computePhenotype() {
    if (booleanNetwork[MirB] == 1 && booleanNetwork[EstB] == 1)
        addPhenotype(UPTAKING_TAFC);
    if (booleanNetwork[Tafc] == 1)
        addPhenotype(SECRETING_TAFC);
}

} // namespace intracellularState
} // namespace uf
} // namespace edu
