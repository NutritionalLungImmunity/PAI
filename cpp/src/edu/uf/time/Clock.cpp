/*
 * Clock.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include "Clock.h"


namespace edu {
namespace uf {
namespace time {

Clock::Clock(int size, int iteration) {
	this->iteration = iteration;
	this->size = size;
	count = 0L;
}

int Clock::getSize() const {
    return size;
}

void Clock::tic() {
    iteration = (iteration + 1) % size;
    if (iteration == 0) {
        count++;
    }
}

bool Clock::toc() const {
    return iteration % size == 0;
}

long Clock::getCount() const {
    return count;
}

} /* namespace time */
} /* namespace uf */
} /* namespace edu */
