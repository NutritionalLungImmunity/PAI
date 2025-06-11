/*
 * AspergillusIntracellularModel.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTRACELLULARSTATE_ASPERGILLUSINTRACELLULARMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_ASPERGILLUSINTRACELLULARMODEL_H_

#include "IntracellularModel.h"






namespace edu {
namespace uf {
namespace intracellularState {

class AspergillusIntracellularModel : public IntracellularModel {
public:
    static const int RESTING_CONIDIA;
    static const int SWELLING_CONIDIA;
    static const int GERM_TUBE;
    static const int HYPHAE;
    static const int STERILE_CONIDIA;

    static const int FREE;
    static const int INTERNALIZING;
    static const int RELEASING;
    static const int ENGAGED;

    static const int SECRETING_TAFC;
    static const int UPTAKING_TAFC;

    static const int NUM_SPECIES;

    AspergillusIntracellularModel(int phenotype);
    virtual void updateStatus(int id, int x, int y, int z) override;
    virtual void processBooleanNetwork() override;
    static int hasLifeStage(int phenotype);

protected:
    virtual void computePhenotype() override;

private:
    int lipActivation;
    static const int hapX = 0;
    static const int sreA = 1;
    static const int HapX = 2;
    static const int SreA = 3;
    static const int RIA = 4;
    static const int EstB = 5;
    static const int MirB = 6;
    static const int SidA = 7;
    static const int Tafc = 8;
    static const int ICP = 9;
    static const int LIP = 10;
    static const int CccA = 11;
    static const int FC0fe = 12;
    static const int FC1fe = 13;
    static const int VAC = 14;
    static const int ROS = 15;
    static const int Yap1 = 16;
    static const int SOD2_3 = 17;
    static const int Cat1_2 = 18;
    static const int ThP = 19;
    static const int Fe = 20;
    static const int O = 21;

    static int INIT_AFUMIGATUS_BOOLEAN_STATE[];
};

} // namespace intracellularState
} // namespace uf
} // namespace edu

//static_assert(true, "AspergillusIntracellularModel included successfully");

#endif /* EDU_UF_INTRACELLULARSTATE_ASPERGILLUSINTRACELLULARMODEL_H_ */
