/*
 * Internalizable.h
 *
 *  Created on: Jul 18, 2024
 *      Author: henriquedeassis
 */

#pragma once

#ifndef EDU_UF_INTERACTABLE_INTERNALIZABLE_H_
#define EDU_UF_INTERACTABLE_INTERNALIZABLE_H_


namespace edu {
namespace uf {
namespace interactable {

class Internalizable{

	public:
	virtual ~Internalizable() = default;
	virtual bool isInternalizing() = 0;

};

}
}
}


#endif /* EDU_UF_INTERACTABLE_INTERNALIZABLE_H_ */
