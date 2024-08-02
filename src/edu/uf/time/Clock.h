/*
 * Clock.h
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_TIME_CLOCK_H_
#define EDU_UF_TIME_CLOCK_H_

#include "../utils/Rand.h"

namespace edu {
namespace uf {
namespace time {

class Clock {

public:
    int iteration;
    int size;
    long count;

    Clock(int size, int iteration);
    virtual ~Clock() = default;
    //Clock(int size);

    virtual int getSize() const;
    virtual void tic();
    virtual bool toc() const;
    virtual long getCount() const;
};

} /* namespace time */
} /* namespace uf */
} /* namespace edu */

#endif /* EDU_UF_TIME_CLOCK_H_ */
