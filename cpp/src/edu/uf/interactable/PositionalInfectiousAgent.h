/*
 * PositionalInfectiousAgent.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_POSITIONALINFECTIOUSAGENT_H_
#define EDU_UF_INTERACTABLE_POSITIONALINFECTIOUSAGENT_H_

#include "InfectiousAgent.h"
#include "PositionalAgent.h"
#include "../intracellularState/IntracellularModel.h"



namespace edu {
namespace uf {
namespace interactable {

using namespace edu::uf::intracellularState;

class PositionalInfectiousAgent : public InfectiousAgent, public PositionalAgent {
public:
	PositionalInfectiousAgent(double x, double y, double z, IntracellularModel* network)
	    : InfectiousAgent(network), x_(x), y_(y), z_(z) {}

	virtual ~PositionalInfectiousAgent() override = default;

	double getX() const override{
	    return x_;
	}

	void setX(double x) override{
	    x_ = x;
	}

	double getY() const override{
	    return y_;
	}

	void setY(double y) override{
	    y_ = y;
	}

	double getZ() const override{
	    return z_;
	}

	void setZ(double z) override{
	    z_ = z;
	}

private:
    double x_;
    double y_;
    double z_;

};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_INTERACTABLE_POSITIONALINFECTIOUSAGENT_H_ */
