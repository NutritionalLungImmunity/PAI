/*
 * Main.cpp
 *
 *  Created on: Jul 17, 2024
 *      Author: henriquedeassis
 */

#include <iostream>
#include <vector>
#include <string>
#include <memory>

#include "initialize/InitializeBaseModel.h"
#include "run/RunSingleThread.h"
#include "print/PrintBaseModel.h"
#include "../diffusion/FADIPeriodic.h"
#include "../compartments/MacrophageRecruiter.h"
#include "../compartments/NeutrophilRecruiter.h"
#include "../utils/Constants.h"
#include "../intracellularState/AspergillusIntracellularModel.h"
#include "../control/Exec.h"
#include "../compartments/Voxel.h"

#include <chrono>
using namespace std::chrono;


using namespace edu::uf::compartments;
using namespace edu::uf::main::print;

void baseModel() {
    std::unique_ptr<edu::uf::main::initialize::Initialize> initialize(new edu::uf::main::initialize::InitializeBaseModel());
    std::unique_ptr<edu::uf::main::run::Run> run(new edu::uf::main::run::RunSingleThread());
    PrintBaseModel* stat = new PrintBaseModel();

    int xbin = 10;
    int ybin = 10;
    int zbin = 10;

    std::vector<std::string> input = {"0", "1920", "15", "640"};

    double f = 0.1;
    double pdeFactor = edu::uf::utils::constexprants::D / (30 / edu::uf::utils::constexprants::TIME_STEP_SIZE);
    double dt = 1.0;
    std::unique_ptr<edu::uf::diffusion::FADIPeriodic> diffusion(new edu::uf::diffusion::FADIPeriodic(f, pdeFactor, dt));

    initialize->createPeriodicGrid(xbin, ybin, zbin);
    initialize->initializeMolecules(diffusion.get(), false);
    initialize->initializePneumocytes(std::stoi(input[3]));
    initialize->initializeMacrophage(std::stoi(input[2]));
    initialize->initializeNeutrophils(0);
    initialize->infect(std::stoi(input[1]), edu::uf::intracellularState::AspergillusIntracellularModel::RESTING_CONIDIA, edu::uf::utils::constexprants::CONIDIA_INIT_IRON, -1, false);

    Recruiter** recruiters = new Recruiter*[]{new MacrophageRecruiter(), new NeutrophilRecruiter()};

    run->run(2160, xbin, ybin, zbin, recruiters, 2, false, "", -1, stat);

    stat->close();
}

int main() {
	auto start = high_resolution_clock::now();
    baseModel();
    auto stop = high_resolution_clock::now();
    auto duration = duration_cast<microseconds>(stop - start);
    printf("Total execution time: %d micro-seconds\n", duration.count());
    /*printf("Voxel.Interact: %d\n", edu::uf::control::Exec::interactTime);
    printf("Exec.gc: %d\n", edu::uf::control::Exec::gcTime);
    printf("Voxel.next: %d\n", edu::uf::control::Exec::nextTime);
    printf("Molecule.degrade: %d\n", edu::uf::control::Exec::degradeTime);
    printf("Exec.next: %d\n", edu::uf::main::run::RunSingleThread::eNextTime);
    printf("Exec.recruit: %d\n", edu::uf::main::run::RunSingleThread::recruitTime);
    printf("Exec.diffusion: %d\n", edu::uf::main::run::RunSingleThread::diffusionTime);*/
    return 0;
}








