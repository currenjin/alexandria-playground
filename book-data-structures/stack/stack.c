#include <stdio.h>

#define STACK_SIZE 3
int stack[STACK_SIZE];
int top = -1;

void push(const int item) {
    if (top >= STACK_SIZE - 1) {
        printf("Overflow\n");
        return;
    }
    stack[++top] = item;
}

int pop(void) {
    if (top < 0) {
        printf("Stack is empty\n");
        return -1;
    }
    return stack[top--];
}

int peek(void) {
    if (top < 0) {
        printf("Stack is empty\n");
        return -1;
    }
    return stack[top];
}

int main() {
    push(1);
    push(2);
    push(3);

    pop();

    for (int i = 0; i <= top; i++) {
        printf("item: %d\n", stack[i]);
    }

    push(4);

    const int topItem = peek();
    printf("topItem: %d\n", topItem);
}
