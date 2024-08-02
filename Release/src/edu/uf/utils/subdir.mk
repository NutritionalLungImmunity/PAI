################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/utils/Id.cpp \
../src/edu/uf/utils/LinAlg.cpp \
../src/edu/uf/utils/Rand.cpp 

CPP_DEPS += \
./src/edu/uf/utils/Id.d \
./src/edu/uf/utils/LinAlg.d \
./src/edu/uf/utils/Rand.d 

OBJS += \
./src/edu/uf/utils/Id.o \
./src/edu/uf/utils/LinAlg.o \
./src/edu/uf/utils/Rand.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/utils/%.o: ../src/edu/uf/utils/%.cpp src/edu/uf/utils/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-utils

clean-src-2f-edu-2f-uf-2f-utils:
	-$(RM) ./src/edu/uf/utils/Id.d ./src/edu/uf/utils/Id.o ./src/edu/uf/utils/LinAlg.d ./src/edu/uf/utils/LinAlg.o ./src/edu/uf/utils/Rand.d ./src/edu/uf/utils/Rand.o

.PHONY: clean-src-2f-edu-2f-uf-2f-utils

