/*
 * Interactable.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Interactable.h"

namespace edu {
namespace uf {
namespace interactable {

bool Interactable::interact(Interactable* interactable, int x, int y, int z) {
	if (negativeInteractList.find(interactable->getClassId()) != negativeInteractList.end()) {
		return false;
	}
	if (callCounter > 0) {
		negativeInteractList.insert(interactable->getClassId());
	    interactable->negativeInteractList.insert(this->getClassId());
	    return false;
	}

    callCounter++;
    //interactable->callCounter++;
    bool result = templateInteract(interactable, x, y, z);
    interactable->callCounter = 0;
    callCounter = 0;
    return result;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
