#include <stdio.h>
#include <stdlib.h>

#define MAX_SIZE 100

typedef struct {
    int heap[MAX_SIZE];
    int size;
} Heap;

void init(Heap* h) {
    h -> size = 0;
}

void insert(Heap *h, const int data) {
    int i = ++h -> size;

    while (i != 1 && data < h -> heap[i / 2]) {
        h -> heap[i] = h -> heap[i / 2];
        i /= 2;
    }
    h -> heap[i] = data;
}

int deleteHeap(Heap* h) {
    const int data = h -> heap[1];
    const int temp = h -> heap[h -> size--];
    int parent = 1;
    int child = 2;

    while (child <= h -> size) {
        if (child < h -> size && h -> heap[child] > h -> heap[child + 1])
            child++;
        if (temp <= h -> heap[child])
            break;
        h -> heap[parent] = h -> heap[child];
        parent = child;
        child *= 2;
    }

    h -> heap[parent] = temp;
    return data;
}

void printHeap(const Heap* h) {
    for (int i = 1; i <= h -> size; i++) printf("%d ", h -> heap[i]);
    printf("\n");
}

int main(void) {
    Heap* h = malloc(sizeof(Heap));
    init(h);

    insert(h, 20);
    insert(h, 15);
    insert(h, 30);
    insert(h, 10);

    printf("Current Heap: ");
    printHeap(h);

    printf("Deleted Root: %d\n", deleteHeap(h));

    printf("After Delete: ");
    printHeap(h);

    free(h);
    return 0;
}
