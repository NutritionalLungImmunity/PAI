# Use a base image with CMake and GCC
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
        locales \
        build-essential \
        cmake \
        openjdk-17-jdk \
        python3 python3-pip && \
    locale-gen en_US.UTF-8 && \
    rm -rf /var/lib/apt/lists/*

ENV LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8 \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF.8"

# Set working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Make a build directory
RUN mkdir ./cpp/build

# Configure and build the project
RUN cd ./cpp/build && cmake .. && make

RUN cp ./cpp/build/PAIpp ./PAIpp

# Create the bin/ directory
RUN mkdir -p ./java/bin

# Find and copy all .java files from src/ to bin/
RUN find ./java/src -name "*.java" -exec javac -d java/bin {} +

RUN jar cfe jPAI.jar edu.uf.main.Main -C java/bin .

RUN chmod +x /app/run.sh

ENV APP_MODE=cpp

ENTRYPOINT ["/app/run.sh"]

