/*
 * Id.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */
#pragma once

#ifndef EDU_UF_UTILS_ID_H_
#define EDU_UF_UTILS_ID_H_

namespace edu {
namespace uf {
namespace utils {

class Id {
private:
    static int idCounter;

public:
    static int getId();
};

} /* namespace interactable */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_UTILS_ID_H_ */
