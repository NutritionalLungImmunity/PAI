/*
 * FMacrophageBooleanNetwork.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_FMACROPHAGEBOOLEANNETWORK_H_
#define EDU_UF_INTRACELLULARSTATE_FMACROPHAGEBOOLEANNETWORK_H_

#include "IntracellularModel.h"

namespace edu {
namespace uf {
namespace intracellularState {

class FMacrophageBooleanNetwork : public IntracellularModel {
public:
    static const int size = 29;

    static const int IFNGR = 0;
    static const int IFNB = 1;
    static const int CSF2Ra = 2;
    static const int IL1R = 3;
    static const int IL1B = 4;
    static const int TLR4 = 5;
    static const int FCGR = 6;
    static const int IL4Ra = 7;
    static const int IL10R = 8;
    static const int IL10_out = 9;
    static const int STAT1 = 10;
    static const int SOCS1 = 11;
    static const int STAT3 = 12;
    static const int STAT5 = 13;
    static const int IRF4 = 14;
    static const int NFkB = 15;
    static const int PPARG = 16;
    static const int KLF4 = 17;
    static const int STAT6 = 18;
    static const int JMJD3 = 19;
    static const int IRF3 = 20;
    static const int ERK = 21;
    static const int IL12_out = 22;
    static const int TNFR = 23;
    static const int Dectin = 24;
    static const int ALK5 = 25;
    static const int SMAD2 = 26;
    static const int PtSR = 27;
    static const int FPN = 28;

    FMacrophageBooleanNetwork() {
        booleanNetwork = new int[size]();
    }

    ~FMacrophageBooleanNetwork() {
        delete[] booleanNetwork;
    }

    virtual void processBooleanNetwork() override;

private:
    static const int N = 4;
};

} // namespace intracellularState
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTRACELLULARSTATE_FMACROPHAGEBOOLEANNETWORK_H_ */
