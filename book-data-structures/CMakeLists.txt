cmake_minimum_required(VERSION 4.0)
project(template_project_clang C)

set(CMAKE_C_STANDARD 11)

add_executable(template_project_clang
        main.c
        array/array.c
        stack/stack.c
        queue/queue.c
        linked-list/linkedList.c
        tree/tree.c
)
