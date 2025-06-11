/*
 * EmptyModel.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_EMPTYMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_EMPTYMODEL_H_

#include "IntracellularModel.h"

namespace edu {
namespace uf {
namespace intracellularState {

class EmptyModel : public IntracellularModel {
public:
	EmptyModel();
	virtual void processBooleanNetwork();
	virtual void updateStatus(int id, int x, int y, int z);
protected:
	virtual void computePhenotype();
};

} /* namespace intracellularState */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTRACELLULARSTATE_EMPTYMODEL_H_ */
