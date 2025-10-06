#include <stdio.h>

#define QUEUE_SIZE 5
typedef int element;
element queue[QUEUE_SIZE];
int front = -1;
int rear = -1;

void add(const element item) {
    if (rear >= QUEUE_SIZE - 1) {
        printf("Queue is full\n");
        return;
    }
    queue[++rear] = item;
}

element delete() {
    if (front == rear) {
        printf("Queue is empty\n");
        return -1;
    }
    return queue[++front];
}

int main() {
    add(1);
    add(2);
    add(3);

    const element deleted = delete();


    for (int i = front + 1; i <= rear; i++) {
        printf("item: %d\n", queue[i]);
    }
    printf("deleted item: %d\n", deleted);
    printf("front: %d, rear: %d\n", front, rear);
}
