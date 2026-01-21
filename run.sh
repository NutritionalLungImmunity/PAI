#!/bin/bash

MODE="$1"
shift

case "$MODE" in 
    cpp)
        exec ./cpp/build/PAIpp "$@"
        ;;
    java)
        exec java -jar jPAI.jar "$@"
        ;;
    ctest)
        exec python3 cpp/test_paipp.py
        ;;
    jtest)
        exec python3 java/test_jpai.py
        ;;
    *)
        echo "Usage: cpp|java|ctest|jtest"
        exit 1
esac