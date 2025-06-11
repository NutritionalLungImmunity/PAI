/*
 * MATestModel.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_INTRACELLULARSTATE_MATESTMODEL_H_
#define EDU_UF_INTRACELLULARSTATE_MATESTMODEL_H_

#include "IntracellularModel.h"


namespace edu {
namespace uf {
namespace intracellularState {

class MATestModel : public IntracellularModel{
public:

	static const int M1;
	static const int M2B;
	static const int M2C;
	static const int _FPN;

	MATestModel();

	virtual void processBooleanNetwork();
	virtual void updateStatus(int id, int x, int y, int z);

protected:
	virtual void computePhenotype();

private:
    int booleanNetwork;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTRACELLULARSTATE_MATESTMODEL_H_ */
