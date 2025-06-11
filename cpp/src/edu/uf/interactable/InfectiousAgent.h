/*
 * InfectiousAgent.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_INFECTIOUSAGENT_H_
#define EDU_UF_INTERACTABLE_INFECTIOUSAGENT_H_

#include "../intracellularState/IntracellularModel.h"
#include "Siderophore.h"
#include "Leukocyte.h"



namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::intracellularState;

class InfectiousAgent : public Cell {

	public:
	InfectiousAgent(IntracellularModel* booleanNetwork) :
		Cell(booleanNetwork) {}

	virtual ~InfectiousAgent() override = default;

	virtual void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* phagocyte = nullptr) = 0;

    //virtual void grow(int x, int y, int z, int xbin, int ybin, int zbin, Leukocyte* phagocyte) = 0;

    virtual bool hasSiderophore(Siderophore* siderophore) = 0;

    virtual bool isSecretingSiderophore(Siderophore* mol) = 0;

    virtual bool isUptakingSiderophore(Siderophore* mol) = 0;

};

}
}
}

#endif /* EDU_UF_INTERACTABLE_INFECTIOUSAGENT_H_ */
