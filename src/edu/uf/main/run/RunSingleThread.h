/*
 * RunSingleThread.h
 *
 *  Created on: Jul 22, 2024
 *      Author: henriquedeassis
 */

#ifndef EDU_UF_MAIN_RUN_RUNSINGLETHREAD_H_
#define EDU_UF_MAIN_RUN_RUNSINGLETHREAD_H_

#include "Run.h"
#include "../../compartments/Recruiter.h"
#include "../print/PrintStat.h"

namespace edu {
namespace uf {
namespace main {
namespace run {

class RunSingleThread : public Run {
public:

	static long eNextTime;
	static long diffusionTime;
	static long recruitTime;

	virtual void run(
        int iterations,
        int xbin,
        int ybin,
        int zbin,
		Recruiter** recruiters,
        int recruiterCount,
        bool printLattice,
        const string& outputFile,
        int nthreads,
        PrintStat* printStat
    ) override;
};

} // namespace run
} // namespace main
} // namespace uf
} // namespace edu

#endif /* EDU_UF_MAIN_RUN_RUNSINGLETHREAD_H_ */
