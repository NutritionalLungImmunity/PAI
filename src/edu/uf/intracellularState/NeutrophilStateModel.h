/*
 * NeutrophilStateModel.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_NEUTROPHILSTATEMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_NEUTROPHILSTATEMODEL_H_

namespace edu {
namespace uf {
namespace intracellularState {

class NeutrophilStateModel : public IntracellularModel {
public:
    static const string name;
    static const int size = 4;
    static const int ACTIVE;
    static const int DYING;
    static const int APOPTOTIC;
    static const int NETOTIC;

    NeutrophilStateModel();

    virtual void processBooleanNetwork() override;
    virtual void updateStatus(int id, int x, int y, int z) override;

protected:
    virtual void computePhenotype() override;

private:
    int* booleanNetwork;
};

} // namespace intracellularState
} // namespace uf
} // namespace edu

#endif /* EDU_UF_INTRACELLULARSTATE_NEUTROPHILSTATEMODEL_H_ */
