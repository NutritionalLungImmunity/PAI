################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/edu/uf/diffusion/FADI.cpp \
../src/edu/uf/diffusion/FADIPeriodic.cpp 

CPP_DEPS += \
./src/edu/uf/diffusion/FADI.d \
./src/edu/uf/diffusion/FADIPeriodic.d 

OBJS += \
./src/edu/uf/diffusion/FADI.o \
./src/edu/uf/diffusion/FADIPeriodic.o 


# Each subdirectory must supply rules for building sources it contributes
src/edu/uf/diffusion/%.o: ../src/edu/uf/diffusion/%.cpp src/edu/uf/diffusion/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -std=c++2a -O2 -g3 -Wall -c -fmessage-length=0 -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src-2f-edu-2f-uf-2f-diffusion

clean-src-2f-edu-2f-uf-2f-diffusion:
	-$(RM) ./src/edu/uf/diffusion/FADI.d ./src/edu/uf/diffusion/FADI.o ./src/edu/uf/diffusion/FADIPeriodic.d ./src/edu/uf/diffusion/FADIPeriodic.o

.PHONY: clean-src-2f-edu-2f-uf-2f-diffusion

