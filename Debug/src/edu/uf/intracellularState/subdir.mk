################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/intracellularState/AspergillusIntracellularModel.cpp \
../src/edu/uf/intracellularState/AspergillusMacrophage.cpp \
../src/edu/uf/intracellularState/EmptyModel.cpp \
../src/edu/uf/intracellularState/FMacrophageBooleanNetwork.cpp \
../src/edu/uf/intracellularState/IntracellularModel.cpp \
../src/edu/uf/intracellularState/MATestModel.cpp \
../src/edu/uf/intracellularState/NeutrophilStateModel.cpp \
../src/edu/uf/intracellularState/Phenotype.cpp \
../src/edu/uf/intracellularState/PneumocyteStateModel.cpp 

CPP_DEPS += \
./src/edu/uf/intracellularState/AspergillusIntracellularModel.d \
./src/edu/uf/intracellularState/AspergillusMacrophage.d \
./src/edu/uf/intracellularState/EmptyModel.d \
./src/edu/uf/intracellularState/FMacrophageBooleanNetwork.d \
./src/edu/uf/intracellularState/IntracellularModel.d \
./src/edu/uf/intracellularState/MATestModel.d \
./src/edu/uf/intracellularState/NeutrophilStateModel.d \
./src/edu/uf/intracellularState/Phenotype.d \
./src/edu/uf/intracellularState/PneumocyteStateModel.d 

OBJS += \
./src/edu/uf/intracellularState/AspergillusIntracellularModel.o \
./src/edu/uf/intracellularState/AspergillusMacrophage.o \
./src/edu/uf/intracellularState/EmptyModel.o \
./src/edu/uf/intracellularState/FMacrophageBooleanNetwork.o \
./src/edu/uf/intracellularState/IntracellularModel.o \
./src/edu/uf/intracellularState/MATestModel.o \
./src/edu/uf/intracellularState/NeutrophilStateModel.o \
./src/edu/uf/intracellularState/Phenotype.o \
./src/edu/uf/intracellularState/PneumocyteStateModel.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/intracellularState/%.o: ../src/edu/uf/intracellularState/%.cpp src/edu/uf/intracellularState/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O2 -g3 -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-intracellularState

clean-src-2f-edu-2f-uf-2f-intracellularState:
	-$(RM) ./src/edu/uf/intracellularState/AspergillusIntracellularModel.d ./src/edu/uf/intracellularState/AspergillusIntracellularModel.o ./src/edu/uf/intracellularState/AspergillusMacrophage.d ./src/edu/uf/intracellularState/AspergillusMacrophage.o ./src/edu/uf/intracellularState/EmptyModel.d ./src/edu/uf/intracellularState/EmptyModel.o ./src/edu/uf/intracellularState/FMacrophageBooleanNetwork.d ./src/edu/uf/intracellularState/FMacrophageBooleanNetwork.o ./src/edu/uf/intracellularState/IntracellularModel.d ./src/edu/uf/intracellularState/IntracellularModel.o ./src/edu/uf/intracellularState/MATestModel.d ./src/edu/uf/intracellularState/MATestModel.o ./src/edu/uf/intracellularState/NeutrophilStateModel.d ./src/edu/uf/intracellularState/NeutrophilStateModel.o ./src/edu/uf/intracellularState/Phenotype.d ./src/edu/uf/intracellularState/Phenotype.o ./src/edu/uf/intracellularState/PneumocyteStateModel.d ./src/edu/uf/intracellularState/PneumocyteStateModel.o

.PHONY: clean-src-2f-edu-2f-uf-2f-intracellularState

