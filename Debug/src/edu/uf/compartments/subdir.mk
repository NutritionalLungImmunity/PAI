################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/compartments/GridFactory.cpp \
../src/edu/uf/compartments/MacrophageRecruiter.cpp \
../src/edu/uf/compartments/NeutrophilRecruiter.cpp \
../src/edu/uf/compartments/Recruiter.cpp \
../src/edu/uf/compartments/Voxel.cpp 

CPP_DEPS += \
./src/edu/uf/compartments/GridFactory.d \
./src/edu/uf/compartments/MacrophageRecruiter.d \
./src/edu/uf/compartments/NeutrophilRecruiter.d \
./src/edu/uf/compartments/Recruiter.d \
./src/edu/uf/compartments/Voxel.d 

OBJS += \
./src/edu/uf/compartments/GridFactory.o \
./src/edu/uf/compartments/MacrophageRecruiter.o \
./src/edu/uf/compartments/NeutrophilRecruiter.o \
./src/edu/uf/compartments/Recruiter.o \
./src/edu/uf/compartments/Voxel.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/compartments/%.o: ../src/edu/uf/compartments/%.cpp src/edu/uf/compartments/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O2 -g3 -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-compartments

clean-src-2f-edu-2f-uf-2f-compartments:
	-$(RM) ./src/edu/uf/compartments/GridFactory.d ./src/edu/uf/compartments/GridFactory.o ./src/edu/uf/compartments/MacrophageRecruiter.d ./src/edu/uf/compartments/MacrophageRecruiter.o ./src/edu/uf/compartments/NeutrophilRecruiter.d ./src/edu/uf/compartments/NeutrophilRecruiter.o ./src/edu/uf/compartments/Recruiter.d ./src/edu/uf/compartments/Recruiter.o ./src/edu/uf/compartments/Voxel.d ./src/edu/uf/compartments/Voxel.o

.PHONY: clean-src-2f-edu-2f-uf-2f-compartments

