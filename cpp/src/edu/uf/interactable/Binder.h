/*
 * Binder.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_INTERACTABLE_BINDER_H_
#define EDU_UF_INTERACTABLE_BINDER_H_

namespace edu {
namespace uf {
namespace interactable {

class Binder{
public:
	virtual ~Binder() {}

	virtual int getInteractionId() const = 0;
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */


#endif /* EDU_UF_INTERACTABLE_BINDER_H_ */
