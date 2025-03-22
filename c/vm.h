#ifndef clox_vm_h
#define clox_vm_h

#include "chunk.h"

typedef struct {
    Chunk *chunk;
    uint8_t *ip;
} VM;

void initVM();
void freeVM();

#endif // clox_vm_h