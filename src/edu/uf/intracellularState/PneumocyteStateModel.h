/*
 * PneumocyteStateModel.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_PNEUMOCYTESTATEMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_PNEUMOCYTESTATEMODEL_H_

#include "IntracellularModel.h"


namespace edu {
namespace uf {
namespace intracellularState {

class PneumocyteStateModel : public IntracellularModel {
public:
    static const string name;
    static const int size = 2;
    static const int MIX_ACTIVE = 0;
    static const int ACTIVE = 1;
    static const int _ITER_REST = 12;
    static const int N = 4;

    PneumocyteStateModel();

    virtual void processBooleanNetwork() override;
    virtual void updateStatus(int id, int x, int y, int z) override;

protected:
    virtual void computePhenotype() override;

private:
    int iterations1;
    int iterations2;
    int countMix();
    int countActive();
};

} // namespace intracellularState
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTRACELLULARSTATE_PNEUMOCYTESTATEMODEL_H_ */
