# Specify the minimum version of CMake required
cmake_minimum_required(VERSION 3.10)

# Define the project name and version
project(ISSpp VERSION 1.0 LANGUAGES CXX)

# Set C++ standard to C++17
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

#Handle different platforms
if(WIN32)
    message(STATUS "Configuring for Windows")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DWIN32")
elseif(APPLE)
    message(STATUS "Configuring for macOS")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DAPPLE")
elseif(UNIX)
    message(STATUS "Configuring for Linux")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DLINUX")
endif()

# Set compiler flags for GCC
# Enable optimization, warnings, and set message length for GCC
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -Wall -fmessage-length=0")

# Set specific assembler flags for GCC
set(CMAKE_ASM_FLAGS "${CMAKE_ASM_FLAGS} -g -c")

# use file(GLOB ...) to automatically add all source files recursively
message(STATUS "Collected sources: ${SOURCES}")
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS ${CMAKE_SOURCE_DIR}/src/*.cpp)

# Add executable using the collected files
add_executable(ISSpp ${SOURCES})

# Add src/ to the include path
target_include_directories(ISSpp PRIVATE ${CMAKE_SOURCE_DIR}/src)




