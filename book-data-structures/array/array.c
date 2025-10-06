#include <stdio.h>

#define ARRAY_SIZE 5

void create(int *a, const int n) {
    for (int i = 0; i < n; i++) {
        a[i] = i;
    }
}

#define array_size 5
int retrieve(const int *a, const int i) {
    if (i >= 0 && i < array_size) return a[i];
    printf("Error\n"); return -1;
}

#define array_size 5
void store(int *a, const int i, const int e) {
    if (i >= 0 && i < array_size) a[i] = e;
    else printf("Error\n");
}

int main() {
    int arr[ARRAY_SIZE];

    create(arr, ARRAY_SIZE);

    for (int i = 0; i < ARRAY_SIZE; i++) {
        store(arr, i, i);
    }

    for (int i = 0; i < ARRAY_SIZE; i++) {
        int v = retrieve(arr, i);
        printf("arr[%d] = %d\n", i, v);
    }

    printf("retrieve(arr, -1) => %d\n", retrieve(arr, -1));
    printf("retrieve(arr, 10) => %d\n", retrieve(arr, 10));

    return 0;
}
