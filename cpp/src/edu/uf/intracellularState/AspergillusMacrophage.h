/*
 * AspergillusMacrophage.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_ASPERGILLUSMACROPHAGE_H_
#define EDU_UF_INTRACELLULARSTATE_ASPERGILLUSMACROPHAGE_H_

#include "FMacrophageBooleanNetwork.h"

namespace edu {
namespace uf {
namespace intracellularState {

class AspergillusMacrophage : public FMacrophageBooleanNetwork {
public:
    static const int M1;
    static const int M2B;
    static const int M2C;
    static const int _FPN;
    static const string name;

    AspergillusMacrophage();
    virtual void updateStatus(int id, int x, int y, int z) override;

protected:
    virtual void computePhenotype() override;

};

} // namespace intracellularState
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTRACELLULARSTATE_ASPERGILLUSMACROPHAGE_H_ */
