/*
 * FMacrophageBooleanNetwork.cpp
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#include "FMacrophageBooleanNetwork.h"
#include "../interactable/IL10.h"
#include "../interactable/TGFb.h"
#include "../interactable/TNFa.h"
#include "../interactable/afumigatus/Afumigatus.h"
#include "../utils/Constants.h"
#include <vector>
#include <algorithm>
#include <random>
#include <iostream>

namespace edu {
namespace uf {
namespace intracellularState {

using namespace edu::uf::utils;
using namespace afumigatus;


void FMacrophageBooleanNetwork::processBooleanNetwork() {
    int k = 0;
    vector<int> array(size);
    iota(array.begin(), array.end(), 0);

    while (true) {
        if (k++ > utils::constexprants::MAX_BN_ITERATIONS) break;
        shuffle(array.begin(), array.end(), Rand::getRand().getGenerator());
        for (int i : array) {
            switch (i) {
                case IFNGR:
                    //booleanNetwork[IFNGR] = booleanNetwork[IFNB];
                    break;
                case CSF2Ra:
                    //booleanNetwork[CSF2Ra] = getInput(GM_CSF::getMolecule());
                    break;
                case IL1R:
                    //booleanNetwork[IL1R] = max(booleanNetwork[IL1B], getInput(IL1::getMolecule()));
                    break;
                case TLR4:
                    //booleanNetwork[TLR4] = min(getInput(TLRBinder::getBinder()), gnot(booleanNetwork[FCGR], N));
                	booleanNetwork[TLR4] = min(getInput(Afumigatus::DEF_OBJ), gnot(booleanNetwork[FCGR], N));
                    break;
                case FCGR:
                    booleanNetwork[FCGR] = 0;
                    break;
                case IL4Ra:
                    //booleanNetwork[IL4Ra] = getInput(IL4::getMolecule());
                    break;
                case IL10R:
                    booleanNetwork[IL10R] = max(getInput(IL10::getMolecule()), booleanNetwork[IL10_out]);
                    break;
                case STAT1:
                    booleanNetwork[STAT1] = min(booleanNetwork[IFNGR], gnot(max(booleanNetwork[SOCS1], booleanNetwork[STAT3]), N));
                    break;
                case STAT5:
                    booleanNetwork[STAT5] = min(booleanNetwork[CSF2Ra], gnot(max(booleanNetwork[STAT3], booleanNetwork[IRF4]), N));
                    break;
                case NFkB:{
                    int nfkbInputs[] = { booleanNetwork[IL1R], booleanNetwork[TLR4], booleanNetwork[Dectin], booleanNetwork[TNFR] };
                    int nfkbInhibitors[] = { booleanNetwork[STAT3], booleanNetwork[FCGR], booleanNetwork[PPARG], booleanNetwork[KLF4] };
                    booleanNetwork[NFkB] = min(amax(nfkbInputs, 4), gnot(amax(nfkbInhibitors, 4), N));
                    break;
                }
                case PPARG:
                    booleanNetwork[PPARG] = booleanNetwork[IL4Ra];
                    break;
                case STAT6:
                    booleanNetwork[STAT6] = booleanNetwork[IL4Ra];
                    break;
                case JMJD3:
                    booleanNetwork[JMJD3] = booleanNetwork[IL4Ra];
                    break;
                case STAT3:
                    booleanNetwork[STAT3] = min(max(booleanNetwork[IL10R], booleanNetwork[SMAD2]), gnot(max(booleanNetwork[FCGR], booleanNetwork[PPARG]), N));
                    break;
                case IRF3:
                    booleanNetwork[IRF3] = booleanNetwork[TLR4];
                    break;
                case ERK:
                    booleanNetwork[ERK] = booleanNetwork[FCGR];
                    break;
                case KLF4:
                    booleanNetwork[KLF4] = booleanNetwork[STAT6];
                    break;
                case IL1B:
                    booleanNetwork[IL1B] = booleanNetwork[NFkB];
                    break;
                case IFNB:
                    booleanNetwork[IFNB] = booleanNetwork[IRF3];
                    break;
                case IL12_out:{
                    int il12_outInputs[] = { booleanNetwork[STAT1], booleanNetwork[STAT5], booleanNetwork[NFkB] };
                    booleanNetwork[IL12_out] = amax(il12_outInputs, 3);
                    break;
                }
                case IL10_out:{
                    int il10_outInputs[] = { booleanNetwork[PPARG], booleanNetwork[STAT6], booleanNetwork[JMJD3], booleanNetwork[STAT3], booleanNetwork[ERK], booleanNetwork[PtSR] };
                    booleanNetwork[IL10_out] = amax(il10_outInputs, 6);
                    break;
                }
                case TNFR:
                    booleanNetwork[TNFR] = getInput(TNFa::getMolecule());
                    break;
                case Dectin:
                    booleanNetwork[Dectin] = getInput(Afumigatus::DEF_OBJ);
                    break;
                case SOCS1:
                    booleanNetwork[SOCS1] = booleanNetwork[STAT6];
                    break;
                case IRF4:
                    booleanNetwork[IRF4] = booleanNetwork[JMJD3];
                    break;
                case ALK5:
                    booleanNetwork[ALK5] = getInput(TGFb::getMolecule());
                    break;
                case SMAD2:
                    booleanNetwork[SMAD2] = booleanNetwork[ALK5];
                    break;
                case PtSR:
                    // booleanNetwork[PtSR] = getInput(SAMP::getMolecule());
                    break;
                case FPN:
                    //booleanNetwork[FPN] = gnot(getInput(Hepcidin::getMolecule()), N);
                    break;
                default:
                    cerr << "No such interaction " << i << "!" << endl;
                    break;
            }
        }
    }

    inputs.clear();
    clearPhenotype();
    computePhenotype();
}

} // namespace intracellularState
} // namespace uf
} // namespace edu
