# Installation Instructions

This project uses CMake to configure and build on multiple platforms. Below are the platform-specific instructions.

## Windows (MinGW) 

### Prerequisites

- **MinGW**: Install MinGW to get a compatible GCC toolchain. You can download MinGW from [here](https://sourceforge.net/projects/mingw/).
- **CMake**: Download and install CMake from [here](https://cmake.org/download/).

### Building the Project

1. Open a Command Prompt (or PowerShell) and navigate to the project directory.
2. Create a build directory:
   
   ```bash/batch
   mkdir build
   cd build
   ```
3. Run CMake to configure the project:
    

    ```bash/batch
    cmake -G "MinGW Makefiles" ..
    ```

4. Build the project:
   
    ```bash/batch
    mingw32-make
    ```
5. Or combined codes: 

```bash/batch
    cmake -S -B build -G "MinGW Makefiles"
    cmake --build build
```
## Windows (Visual Studio)

### Prerequisites

- **Visual Studio 2019 or later**: Install Visual Studio and the necessary C++ build tools.

### Building the Project

1. Open a Developer Command Prompt for Visual Studio.
2. Navigate to the project directoty and create a build directory:

   ```bash/batch
   mkdir build
   cd build
   ```
3. Run CMake to configure the project:

    ```bash/batch
    cmake -G "Visual Studio 16 2019" ..
    ```
4. Open the solution in Visual Studio and build the project or 

5. Or combined codes: 

```bash/batch
    cmake -S -B build -G "Visual Studio 16 2019"
    cmake --build build
```

### Running the Executable

```bash 
./ISSpp
```
From Command Prompt or PowerShell:

```batch
ISSpp.exe
```

## Windows (Cygwin)

### Prerequisites

- **Cygwin**: Install MinGW to get a compatible GCC toolchain. You can download MinGW from [here](https://sourceforge.net/projects/mingw/).
- **CMake**: Download and install CMake from [here](https://cmake.org/download/).

### Building the Project

1. Open a Command Prompt (or PowerShell) and navigate to the project directory.
2. Create a build directory:
   
   ```bash/batch
   mkdir build
   cd build
   ```
3. Run CMake to configure the project:
    
    ```bash/batch
    cmake -G "Unix Makefiles" ..
    ```
4. Build the project:
    
    ```bash/batch
    make
    ```
5. Or combined codes: 

```bash/batch
    cmake -S -B build -G "Unix Makefiles"
    cmake --build build
```

### Running the Executable

```bash 
./ISSpp
```
From Command prompt or PowerShell:

```batch
ISSpp.exe
```

## Linux (Ubuntu/Debian)

### Prerequisites
- **GCC/G++ (version 8.5 and higher)**: install the GCC toolchain if it's not already installed:

```bash
sudo apt-get update
sudo apt-get install build-essential
```
- **CMake**: Install CMake;

```bash
sudo apt-get install cmake
```
### Building the Project

1. Open a terminal and navigate to the project directory.
2. Create a build directory:

    ```bash
    mkdir build 
    cd build
    ```
3. Run CMake to configure the project:
    
    ```bash
    cmake ..
    ```
4. Build the project:
    
    ```bash
    make
    ```
### Running the Executable

```bash 
./ISSpp
```
## macOS

### Prerequisites

- **Xcode Command Line Tools**: Install Xcode command line tools:

```bash 
xcode-select --install
```
- **CMake**: Install CMake from Homebrew or the official CMake website:

```bash
brew install cmake
```
### Buillding the project

1. Open a terminal and navigate to the project directory.
2. Create a build directory:

    ```bash 
    mkdir build
    cd build
    ```
3. Run CMake to configure the project
    
    ```bash
    cmake ..
    ```
4. Build the project:

    ```bash 
    make
    ```
### Running the Executable
Run the executable directly:

```bash
./ISSpp
```


