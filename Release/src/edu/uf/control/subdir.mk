################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/control/Exec.cpp 

CPP_DEPS += \
./src/edu/uf/control/Exec.d 

OBJS += \
./src/edu/uf/control/Exec.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/control/%.o: ../src/edu/uf/control/%.cpp src/edu/uf/control/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-control

clean-src-2f-edu-2f-uf-2f-control:
	-$(RM) ./src/edu/uf/control/Exec.d ./src/edu/uf/control/Exec.o

.PHONY: clean-src-2f-edu-2f-uf-2f-control

