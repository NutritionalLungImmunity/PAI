# ISSpp

This project is a C++ cross-platform project built with CMake. Buidable on Windows, Linux, and macOS via terminal.

## Build Instructions

See [INSTALL.md](INSTALL.md) for platform-specific instructions.

### Quick Start (Unix/macOS/Linux)

```bash
git clone https://github.com/...
cd ISS++
cmake -S . -B build
cmake --build build
./build/ISSpp
```

### macOS

#### Requirements

- Xcode CLI Tools (for clang++ and make)

```bash
xcode-select --install
brew install cmake
```
#### Build

```bash
cmake -S . -B build
cmake --build build
./build/ISSpp
```

### Windows (MinGW)

#### Requirements

- MinGW-w64
- Add mingw32-make to PATH
- CMake

#### Build

```bash
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
./build/ISSpp.exe
```

From Command prompt or PowerShell:

```batch
cmake -G "MinGW Makefiles" -S . -B build
cmake --build build
.\build\ISSpp.exe
```

### Windows (Cygwin)

#### Requirements

- Cygwin
- Add cygwin to PATH
- CMake

#### Build

```bash
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
./build/ISSpp.exe
```

From Command prompt or PowerShell:

```batch
cmake -G "Unix Makefiles" -S . -B build
cmake --build build
.\build\ISSpp.exe
```

### Linux/Unix/

#### Requirements

- g++
- cmake

```bash 
sudo apt get update
sudo apt install g++
sudo apt install cmake
```
#### Build

```bash
cmake -S . -B build
cmake --build build
./build/ISSpp
```

## Clean build

```bash
rm -rf build
```

From Command prompt or PowerShell:
```batch
rmdir /s /q build
```



