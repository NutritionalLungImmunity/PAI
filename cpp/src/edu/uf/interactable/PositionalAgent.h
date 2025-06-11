/*
 * PositionalAgent.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_POSITIONALAGENT_H_
#define EDU_UF_INTERACTABLE_POSITIONALAGENT_H_

namespace edu {
namespace uf {
namespace interactable {

class PositionalAgent {
public:

	virtual ~PositionalAgent() = default;

    virtual double getX() const = 0;
    virtual void setX(double x) = 0;
    virtual double getY() const = 0;
    virtual void setY(double y) = 0;
    virtual double getZ() const = 0;
    virtual void setZ(double z) = 0;
};

} // namespace interactable
} // namespace uf
} // namespace edu



#endif /* EDU_UF_INTERACTABLE_POSITIONALAGENT_H_ */
