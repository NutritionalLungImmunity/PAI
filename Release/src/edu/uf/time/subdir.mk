################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/time/Clock.cpp 

CPP_DEPS += \
./src/edu/uf/time/Clock.d 

OBJS += \
./src/edu/uf/time/Clock.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/time/%.o: ../src/edu/uf/time/%.cpp src/edu/uf/time/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-time

clean-src-2f-edu-2f-uf-2f-time:
	-$(RM) ./src/edu/uf/time/Clock.d ./src/edu/uf/time/Clock.o

.PHONY: clean-src-2f-edu-2f-uf-2f-time

