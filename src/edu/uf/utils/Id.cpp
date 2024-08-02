/*
 * Id.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Id.h"

namespace edu {
namespace uf {
namespace utils {

int Id::idCounter = 0;

int Id::getId() {
    return idCounter++;
}

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */
