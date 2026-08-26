cmake_minimum_required(VERSION 3.10)
project(GrievanceManagementSystem)

set(CMAKE_CXX_STANDARD 17)

file(GLOB_RECURSE SOURCES
    "main.cpp"
    "models/*.cpp"
    "utils/*.cpp"
    "services/*.cpp"
)

add_executable(GrievanceManagementSystem ${SOURCES})
